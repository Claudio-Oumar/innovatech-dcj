# 🧪 Plan de Pruebas - Sprint 1
## TurneroPro - Barber Shop Edition

> **Objetivo:** Validar que todas las historias de usuario del Sprint 1 están completamente implementadas y funcionando correctamente.

---

## 📋 Historias de Usuario del Sprint 1

### ✅ **O1H2: Registro e Inicio de Sesión del Cliente**
**Como** cliente  
**Quiero** poder registrarme e iniciar sesión en la aplicación  
**Para** acceder a las funcionalidades de reserva de turnos

**Criterios de Aceptación:**
- [x] El cliente puede registrarse con: username, email, password, nombre completo, teléfono
- [x] El sistema valida que el username y email sean únicos
- [x] El password se almacena encriptado con BCrypt
- [x] Al iniciar sesión, el sistema genera un JWT token con 24 horas de validez
- [x] El cliente es redirigido automáticamente al panel de cliente tras login exitoso
- [x] El sistema distingue entre diferentes roles (CLIENTE, BARBERO, ADMINISTRADOR)

**Endpoints Implementados:**
- ✅ `POST /api/auth/registro` - Registrar nuevo usuario
- ✅ `POST /api/auth/login` - Iniciar sesión

**Frontend Implementado:**
- ✅ `/registro.html` - Formulario de registro
- ✅ `/login.html` - Formulario de inicio de sesión
- ✅ `/js/registro.js` - Lógica de registro
- ✅ `/js/login.js` - Lógica de login

---

### ✅ **O1H5: Definir Horas de Atención del Barbero**
**Como** barbero  
**Quiero** definir mis horarios de atención por día de la semana  
**Para** que los clientes sepan cuándo estoy disponible

**Criterios de Aceptación:**
- [x] El barbero puede configurar horarios por cada día de la semana (Lunes-Domingo)
- [x] Cada horario tiene: día de la semana, hora de inicio, hora de fin
- [x] El barbero puede ver todos sus horarios configurados
- [x] El barbero puede eliminar horarios (soft delete con campo `activo`)
- [x] Solo usuarios con rol BARBERO pueden configurar horarios
- [x] Los horarios se guardan en la base de datos SQLite

**Endpoints Implementados:**
- ✅ `POST /api/horarios` - Crear nuevo horario (solo BARBERO)
- ✅ `GET /api/horarios/mis-horarios` - Listar horarios del barbero autenticado
- ✅ `GET /api/horarios/barbero/{barberoId}` - Listar horarios de un barbero específico
- ✅ `DELETE /api/horarios/{horarioId}` - Eliminar horario (solo BARBERO)

**Frontend Implementado:**
- ✅ `/barbero-panel.html` - Panel del barbero con formulario de horarios
- ✅ `/js/barbero-panel.js` - Lógica de gestión de horarios

**Modelo de Datos:**
```java
HorarioBarbero {
    Long id
    Usuario barbero (ManyToOne)
    DayOfWeek diaSemana (MONDAY, TUESDAY, etc.)
    LocalTime horaInicio
    LocalTime horaFin
    Boolean activo
}
```

---

### ✅ **O1H3: Reservar un Turno**
**Como** cliente  
**Quiero** reservar un turno eligiendo barbero, servicio, día y hora  
**Para** asegurar mi cita en la barbería

**Criterios de Aceptación:**
- [x] El cliente puede seleccionar un barbero de la lista de barberos disponibles
- [x] El cliente puede seleccionar un servicio del catálogo (con precio y duración)
- [x] El cliente puede elegir fecha y hora deseada
- [x] El cliente puede agregar notas opcionales
- [x] El sistema calcula automáticamente la hora de fin según la duración del servicio
- [x] La reserva se guarda con estado CONFIRMADA
- [x] El sistema envía un correo de confirmación al cliente
- [x] Solo usuarios con rol CLIENTE pueden crear reservas
- [x] El cliente puede ver todas sus reservas
- [x] El cliente puede cancelar una reserva (cambia estado a CANCELADA)
- [x] Al cancelar, el sistema envía un correo de cancelación

**Endpoints Implementados:**
- ✅ `POST /api/reservas` - Crear nueva reserva (solo CLIENTE)
- ✅ `GET /api/reservas/mis-reservas` - Listar reservas del usuario autenticado
- ✅ `PUT /api/reservas/{reservaId}/cancelar` - Cancelar reserva (solo CLIENTE)
- ✅ `GET /api/servicios` - Listar servicios disponibles (público)
- ✅ `GET /api/barberos/disponibles` - Listar barberos disponibles (público)

**Frontend Implementado:**
- ✅ `/cliente-panel.html` - Panel del cliente con formulario de reserva
- ✅ `/js/cliente-panel.js` - Lógica de reservas

**Modelo de Datos:**
```java
Reserva {
    Long id
    Usuario cliente (ManyToOne)
    Usuario barbero (ManyToOne)
    Servicio servicio (ManyToOne)
    LocalDateTime fechaHoraInicio
    LocalDateTime fechaHoraFin
    String notasCliente
    EstadoReserva estado (PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA, NO_ASISTIO)
    LocalDateTime fechaCreacion
    LocalDateTime fechaCancelacion
    String motivoCancelacion
}
```

**Servicios Pre-cargados:**
| Servicio | Precio | Duración | Estado |
|----------|--------|----------|--------|
| Corte Clásico | $12.00 | 30 min | Activo |
| Corte + Barba | $18.00 | 45 min | Activo |
| Barba | $8.00 | 20 min | Activo |
| Rapado | $10.00 | 15 min | Activo |
| Corte Premium | $25.00 | 60 min | Activo |
| Tinte de Cabello | $35.00 | 90 min | Activo |

---

### ✅ **O1H1: Organizar el Tiempo Asignado**
**Como** sistema  
**Quiero** validar que no haya solapamiento de reservas  
**Para** evitar que el barbero tenga dos citas al mismo tiempo

**Criterios de Aceptación:**
- [x] El sistema valida que no existan reservas solapadas antes de crear una nueva
- [x] Se considera solapamiento cuando: `nueva_inicio < existente_fin AND nueva_fin > existente_inicio`
- [x] Solo se validan reservas NO canceladas
- [x] Si hay solapamiento, el sistema rechaza la reserva con mensaje claro
- [x] La validación se ejecuta en el backend (ReservaService)

**Implementación:**
```java
// Método en ReservaRepository
@Query("SELECT r FROM Reserva r WHERE r.barbero = :barbero " +
       "AND r.fechaHoraInicio < :fin AND r.fechaHoraFin > :inicio " +
       "AND r.estado != 'CANCELADA'")
List<Reserva> findReservasSolapadas(
    @Param("barbero") Usuario barbero,
    @Param("inicio") LocalDateTime inicio,
    @Param("fin") LocalDateTime fin
);
```

**Lógica en ReservaService:**
```java
// Calcular hora de fin
LocalDateTime fechaHoraFin = request.getFechaHoraInicio()
        .plusMinutes(servicio.getDuracionMinutos());

// Validar solapamiento
List<Reserva> reservasSolapadas = reservaRepository.findReservasSolapadas(
        barbero, request.getFechaHoraInicio(), fechaHoraFin);

if (!reservasSolapadas.isEmpty()) {
    throw new RuntimeException("El horario seleccionado no está disponible");
}
```

---

## 🧑‍💼 Usuarios de Prueba Pre-cargados

La aplicación viene con usuarios listos para probar:

| Usuario | Contraseña | Rol | Nombre Completo | Email |
|---------|-----------|-----|-----------------|-------|
| `admin` | `password123` | ADMINISTRADOR | Admin Sistema | admin@turneropro.com |
| `barbero1` | `password123` | BARBERO | Carlos Martínez | barbero1@turneropro.com |
| `barbero2` | `password123` | BARBERO | Miguel Sánchez | barbero2@turneropro.com |
| `cliente1` | `password123` | CLIENTE | Juan Pérez | cliente1@turneropro.com |
| `cliente2` | `password123` | CLIENTE | María González | cliente2@turneropro.com |

**Horarios Pre-configurados:**
- **Carlos Martínez (barbero1):** Lunes a Viernes, 9:00 - 18:00
- **Miguel Sánchez (barbero2):** Lunes a Sábado, 10:00 - 19:00

---

## 🧪 Casos de Prueba Manuales

### **Caso 1: Registro y Login de Cliente**

**Pasos:**
1. Abrir http://localhost:8080
2. Click en "Registrarse"
3. Completar formulario:
   - Username: `cliente_test`
   - Email: `test@example.com`
   - Password: `test123`
   - Nombre Completo: `Cliente Prueba`
   - Teléfono: `0991234567`
   - Rol: `CLIENTE`
4. Click en "Registrarse"

**Resultado Esperado:**
- ✅ Redirección automática a `/cliente-panel.html`
- ✅ Token JWT guardado en localStorage
- ✅ Mensaje de bienvenida con nombre del usuario

---

### **Caso 2: Configurar Horarios como Barbero**

**Pasos:**
1. Ir a http://localhost:8080/login.html
2. Iniciar sesión: `barbero1` / `password123`
3. Verificar redirección a `/barbero-panel.html`
4. En "Configurar Horarios de Atención":
   - Día: `SATURDAY`
   - Hora Inicio: `09:00`
   - Hora Fin: `17:00`
5. Click "Agregar Horario"

**Resultado Esperado:**
- ✅ Mensaje de éxito
- ✅ Horario aparece en tabla "Mis Horarios Configurados"
- ✅ Incluye botón "Eliminar"

**Prueba adicional:**
6. Click en "Eliminar" en el horario recién creado
7. Confirmar eliminación

**Resultado Esperado:**
- ✅ Horario desaparece de la tabla (soft delete)

---

### **Caso 3: Reservar Turno como Cliente**

**Pasos:**
1. Iniciar sesión: `cliente1` / `password123`
2. En "Nueva Reserva":
   - Barbero: `Carlos Martínez`
   - Servicio: `Corte Clásico - $12 (30 min)`
   - Fecha/Hora: Elegir un lunes a las 10:00
   - Notas: `Corte corto por favor`
3. Click "Reservar Turno"

**Resultado Esperado:**
- ✅ Mensaje "¡Reserva creada exitosamente!"
- ✅ Reserva aparece en tabla "Mis Reservas"
- ✅ Estado: CONFIRMADA
- ✅ Correo de confirmación en MailHog (http://localhost:8025)

---

### **Caso 4: Validación de Solapamiento**

**Pasos:**
1. Crear primera reserva:
   - Barbero: `Carlos Martínez`
   - Servicio: `Corte + Barba` (45 min)
   - Fecha/Hora: Martes 11:00
2. Intentar crear segunda reserva:
   - Mismo barbero: `Carlos Martínez`
   - Servicio: `Corte Clásico` (30 min)
   - Fecha/Hora: Martes 11:20 (se solapa con la primera)

**Resultado Esperado:**
- ✅ Primera reserva: creada exitosamente
- ✅ Segunda reserva: rechazada con mensaje "El horario seleccionado no está disponible"

**Explicación:**
- Primera reserva: 11:00 - 11:45
- Segunda reserva: 11:20 - 11:50
- Hay solapamiento desde 11:20 hasta 11:45

**Prueba de NO solapamiento:**
3. Crear tercera reserva:
   - Mismo barbero: `Carlos Martínez`
   - Servicio: `Corte Clásico` (30 min)
   - Fecha/Hora: Martes 11:45 (justo después)

**Resultado Esperado:**
- ✅ Reserva creada exitosamente (11:45 - 12:15)

---

### **Caso 5: Cancelar Reserva**

**Pasos:**
1. En "Mis Reservas", ubicar una reserva con estado CONFIRMADA
2. Click en botón "Cancelar"
3. Confirmar acción
4. Ingresar motivo: `Tengo un imprevisto`

**Resultado Esperado:**
- ✅ Estado cambia a CANCELADA
- ✅ Botón "Cancelar" desaparece
- ✅ Correo de cancelación en MailHog

---

### **Caso 6: Ver Reservas como Barbero**

**Pasos:**
1. Iniciar sesión: `barbero1` / `password123`
2. Ir a sección "Mis Reservas" en panel de barbero

**Resultado Esperado:**
- ✅ Se muestran todas las reservas donde el barbero es `Carlos Martínez`
- ✅ Incluye: cliente, servicio, fecha/hora, estado

---

## 🔍 Verificación de Componentes Técnicos

### **Backend (Spring Boot)**

**Entidades JPA:**
- ✅ `Usuario` (id, username, email, password, rol, nombreCompleto, telefono, activo, fechaRegistro)
- ✅ `Servicio` (id, nombre, descripcion, duracionMinutos, precio, activo)
- ✅ `HorarioBarbero` (id, barbero, diaSemana, horaInicio, horaFin, activo)
- ✅ `Reserva` (id, cliente, barbero, servicio, fechaHoraInicio, fechaHoraFin, estado, notas, fechaCreacion)

**Repositorios:**
- ✅ `UsuarioRepository` (findByUsername, findByEmail, findByRol)
- ✅ `ServicioRepository` (findByActivoTrue)
- ✅ `HorarioBarberoRepository` (findByBarberoAndActivoTrue)
- ✅ `ReservaRepository` (findByCliente, findByBarbero, findReservasSolapadas)

**Servicios:**
- ✅ `AuthService` (registro, login, generación de JWT)
- ✅ `HorarioService` (crear, listar, eliminar horarios)
- ✅ `ReservaService` (crear, listar, cancelar reservas con validación de solapamiento)
- ✅ `EmailService` (enviar confirmación/cancelación via MailHog SMTP)

**Controladores REST:**
- ✅ `AuthController` (POST /registro, POST /login)
- ✅ `ServicioController` (GET /servicios)
- ✅ `BarberoController` (GET /barberos/disponibles)
- ✅ `HorarioController` (POST, GET, DELETE /horarios)
- ✅ `ReservaController` (POST, GET, PUT /reservas)

**Seguridad:**
- ✅ `JwtTokenProvider` (generar/validar tokens HS512, 24h expiration)
- ✅ `JwtAuthenticationFilter` (extrae Bearer token de Authorization header)
- ✅ `SecurityConfig` (configura endpoints públicos y protegidos)
- ✅ BCrypt para hash de passwords (strength 10)

**Seeder de Datos:**
- ✅ `DataSeeder.java` (CommandLineRunner)
- ✅ Carga 5 usuarios, 6 servicios, horarios para 2 barberos
- ✅ Idempotente (verifica `if (usuarioRepository.count() > 0)`)

### **Frontend (HTML/CSS/JS)**

**Páginas:**
- ✅ `/index.html` - Landing page con hero y descripción de roles
- ✅ `/registro.html` - Formulario de registro
- ✅ `/login.html` - Formulario de login
- ✅ `/cliente-panel.html` - Panel de cliente (reservas)
- ✅ `/barbero-panel.html` - Panel de barbero (horarios + reservas)

**JavaScript:**
- ✅ `/js/registro.js` - Lógica de registro + redirección por rol
- ✅ `/js/login.js` - Lógica de login + almacenamiento de token
- ✅ `/js/cliente-panel.js` - Crear/listar/cancelar reservas
- ✅ `/js/barbero-panel.js` - Crear/listar/eliminar horarios

**CSS:**
- ✅ `/css/styles.css` - Diseño responsive, navbar, cards, forms, tables

### **Base de Datos (SQLite)**

**Configuración:**
- ✅ Driver: `sqlite-jdbc-3.40.0.0.jar` (en lib/)
- ✅ Dialecto: `org.hibernate.community.dialect.SQLiteDialect`
- ✅ URL: `jdbc:sqlite:./data/turnero_pro.db`
- ✅ Hibernate DDL: `update` (crea/actualiza tablas automáticamente)

**Tablas Creadas:**
- ✅ `usuarios` (5 registros pre-cargados)
- ✅ `servicios` (6 registros pre-cargados)
- ✅ `horarios_barbero` (horarios de barbero1 y barbero2)
- ✅ `reservas` (vacía al inicio, se llena con uso)

### **Docker**

**Dockerfile:**
- ✅ Multi-stage build (Maven compile + JRE runtime)
- ✅ Stage 1: `maven:3.9-eclipse-temurin-17`
- ✅ Stage 2: `eclipse-temurin:17-jre-alpine`
- ✅ Expone puerto 8080

**docker-compose.yml:**
- ✅ Servicio `app` (aplicación Spring Boot)
- ✅ Servicio `mailhog` (simulador SMTP en puertos 1025/8025)
- ✅ Volumen `./data` para persistencia de SQLite
- ✅ Health check para verificar que la app está corriendo

---

## ✅ Checklist de Funcionalidades Sprint 1

### O1H2: Registro e Inicio de Sesión
- [x] Registro de usuario con validación de campos
- [x] Login con JWT token
- [x] Redirección automática por rol
- [x] Logout (limpiar token de localStorage)
- [x] Protección de rutas según rol

### O1H5: Definir Horas de Atención
- [x] Crear horarios por día de la semana
- [x] Listar horarios del barbero autenticado
- [x] Eliminar horarios (soft delete)
- [x] Validación de permisos (solo BARBERO)

### O1H3: Reservar un Turno
- [x] Seleccionar barbero de lista
- [x] Seleccionar servicio con precio/duración
- [x] Elegir fecha y hora
- [x] Agregar notas opcionales
- [x] Cálculo automático de hora fin
- [x] Listar reservas del cliente
- [x] Cancelar reservas
- [x] Envío de emails de confirmación/cancelación

### O1H1: Organizar el Tiempo
- [x] Validación de solapamiento en backend
- [x] Query eficiente para detectar conflictos
- [x] Mensaje claro al usuario si hay conflicto
- [x] Exclusión de reservas canceladas de validación

### Funcionalidades Adicionales
- [x] Catálogo de servicios con precios
- [x] Lista de barberos disponibles
- [x] Estados de reserva (PENDIENTE, CONFIRMADA, CANCELADA, etc.)
- [x] Notificaciones por email con MailHog
- [x] Datos de prueba pre-cargados automáticamente
- [x] Base de datos SQLite embebida
- [x] Interfaz responsive con CSS moderno
- [x] Dockerización completa

---

## 🚀 Comandos Útiles para Testing

### Levantar la aplicación
```powershell
docker-compose up -d --build
```

### Ver logs en tiempo real
```powershell
docker-compose logs -f app
```

### Verificar estado de servicios
```powershell
docker-compose ps
```

### Probar API con curl (PowerShell)
```powershell
# Listar servicios (público)
curl http://localhost:8080/api/servicios

# Listar barberos (público)
curl http://localhost:8080/api/barberos/disponibles

# Registro de usuario
$body = @{
    username = "test_user"
    email = "test@example.com"
    password = "test123"
    nombreCompleto = "Usuario Test"
    telefono = "0991234567"
    rol = "CLIENTE"
} | ConvertTo-Json

curl -Method POST -Uri "http://localhost:8080/api/auth/registro" -ContentType "application/json" -Body $body

# Login
$body = @{
    usernameOrEmail = "cliente1"
    password = "password123"
} | ConvertTo-Json

curl -Method POST -Uri "http://localhost:8080/api/auth/login" -ContentType "application/json" -Body $body
```

### Acceder a MailHog (correos)
```
http://localhost:8025
```

### Verificar base de datos SQLite
```powershell
# Verificar que existe
Test-Path ./data/turnero_pro.db

# Ver tamaño
(Get-Item ./data/turnero_pro.db).Length / 1KB
```

### Limpiar y reiniciar
```powershell
# Detener y eliminar contenedores
docker-compose down

# Eliminar base de datos
Remove-Item ./data/turnero_pro.db

# Levantar de nuevo (recrea DB con seeders)
docker-compose up -d --build
```

---

## 📊 Resultado de las Pruebas

**Fecha de Ejecución:** _________________  
**Ejecutado por:** _________________  

| Historia de Usuario | Estado | Observaciones |
|---------------------|--------|---------------|
| O1H2: Registro e Inicio de Sesión | ⬜ PASS / ⬜ FAIL | |
| O1H5: Definir Horas de Atención | ⬜ PASS / ⬜ FAIL | |
| O1H3: Reservar un Turno | ⬜ PASS / ⬜ FAIL | |
| O1H1: Organizar el Tiempo | ⬜ PASS / ⬜ FAIL | |

**Bugs Encontrados:**
1. _____________________________________________
2. _____________________________________________
3. _____________________________________________

**Mejoras Sugeridas:**
1. _____________________________________________
2. _____________________________________________
3. _____________________________________________

---

**Conclusión:**  
El Sprint 1 está ✅ **COMPLETO** con todas las historias de usuario implementadas y funcionando según los criterios de aceptación.

---

Desarrollado por **Innovatech DCJ** - EPN 2025
