# 🚀 Guía Rápida de Pruebas - Sprint 1

## Inicio Rápido (5 minutos)

### 1️⃣ Levantar la Aplicación

```powershell
# En la carpeta del proyecto
cd innovatech-dcj
docker-compose up -d --build

# Verificar que esté corriendo
docker-compose ps
# Esperar a que el estado sea "healthy" (puede tomar 1-2 minutos)
```

### 2️⃣ Acceder a la Aplicación

Abre tu navegador y ve a: **http://localhost:8080**

---

## ✅ Prueba 1: Registro de Cliente (2 minutos)

1. Click en **"Registrarse"**
2. Completa el formulario:
   - Username: `test_cliente`
   - Email: `test@mail.com`
   - Password: `test123`
   - Nombre: `Cliente Test`
   - Teléfono: `0991234567`
   - Rol: **CLIENTE**
3. Click **"Registrarse"**
4. ✅ Deberías ser redirigido a `/cliente-panel.html`

---

## ✅ Prueba 2: Login como Barbero (1 minuto)

1. Cierra sesión (botón en navbar)
2. Click en **"Iniciar Sesión"**
3. Ingresa:
   - Usuario: `barbero1`
   - Contraseña: `password123`
4. Click **"Iniciar Sesión"**
5. ✅ Deberías ver el panel del barbero

---

## ✅ Prueba 3: Configurar Horario (2 minutos)

**Como barbero1:**

1. En "Configurar Horarios de Atención":
   - Día: **Sábado**
   - Hora Inicio: **09:00**
   - Hora Fin: **17:00**
2. Click **"Agregar Horario"**
3. ✅ El horario debe aparecer en la tabla "Mis Horarios Configurados"
4. **Extra:** Click en "Eliminar" para quitar el horario

---

## ✅ Prueba 4: Reservar un Turno (3 minutos)

**Como cliente:**

1. Cierra sesión y entra con: `cliente1` / `password123`
2. En "Nueva Reserva":
   - **Barbero:** Carlos Martínez
   - **Servicio:** Corte Clásico - $12 (30 min)
   - **Fecha/Hora:** Elige un lunes a las 10:00 (debe estar en horarios de Carlos)
   - **Notas:** "Corte corto por favor"
3. Click **"Reservar Turno"**
4. ✅ Debe aparecer mensaje de éxito
5. ✅ La reserva aparece en "Mis Reservas" con estado CONFIRMADA

### Verificar Email

6. Abre **http://localhost:8025** (MailHog)
7. ✅ Deberías ver un email de confirmación

---

## ✅ Prueba 5: Validar Solapamiento (2 minutos)

**Objetivo:** Comprobar que no se puedan crear dos reservas al mismo tiempo

1. Intenta crear otra reserva:
   - **Mismo barbero:** Carlos Martínez
   - **Servicio:** Corte + Barba (45 min)
   - **Fecha/Hora:** Lunes a las 10:15 (se solapa con la reserva anterior)
2. Click **"Reservar Turno"**
3. ✅ Debe aparecer error: **"El horario seleccionado no está disponible"**

### Prueba Reserva Sin Solapamiento

4. Crea reserva:
   - **Mismo barbero:** Carlos Martínez
   - **Servicio:** Corte Premium (60 min)
   - **Fecha/Hora:** Lunes a las 10:30 (justo después de las 10:00-10:30)
5. ✅ Esta SÍ debe crearse exitosamente

---

## ✅ Prueba 6: Cancelar Reserva (1 minuto)

1. En "Mis Reservas", encuentra una reserva CONFIRMADA
2. Click en **"Cancelar"**
3. Confirma la acción
4. Ingresa motivo: `Tengo un compromiso`
5. ✅ El estado debe cambiar a CANCELADA
6. ✅ El botón "Cancelar" desaparece
7. Verifica email de cancelación en **http://localhost:8025**

---

## ✅ Prueba 7: Ver Reservas como Barbero (1 minuto)

1. Cierra sesión
2. Login como: `barbero1` / `password123`
3. Ve a la sección **"Mis Reservas"**
4. ✅ Deberías ver todas las reservas asignadas a Carlos Martínez
5. ✅ Incluye las creadas por cualquier cliente

---

## 🎯 Resumen de Pruebas

| Prueba | Objetivo | Tiempo | Status |
|--------|----------|--------|--------|
| 1. Registro | Crear cuenta nueva | 2 min | ⬜ |
| 2. Login Barbero | Autenticación y redirección | 1 min | ⬜ |
| 3. Horarios | Configurar disponibilidad | 2 min | ⬜ |
| 4. Reservar Turno | Crear cita + notificación | 3 min | ⬜ |
| 5. Solapamiento | Validar conflictos | 2 min | ⬜ |
| 6. Cancelar | Cambiar estado + email | 1 min | ⬜ |
| 7. Ver Reservas | Listado de citas | 1 min | ⬜ |

**Tiempo Total:** ~12 minutos

---

## 🐛 Si Algo No Funciona

### La aplicación no carga

```powershell
# Ver logs
docker-compose logs -f app

# Si hay error, reiniciar
docker-compose restart app
```

### No aparecen los servicios o barberos

```powershell
# Verificar base de datos
Test-Path ./data/turnero_pro.db

# Si no existe o está corrupta, recrear
docker-compose down
Remove-Item ./data/turnero_pro.db -Force
docker-compose up -d --build
```

### Error "Unauthorized" al crear reserva

- Verifica que hayas iniciado sesión
- Abre DevTools (F12) → Application → Local Storage
- Debe existir un `token` con un valor largo

### Formulario no envía

- Verifica que todos los campos requeridos estén llenos
- Abre DevTools (F12) → Console para ver errores JavaScript

---

## 📋 Checklist de Historias de Usuario

Marca con ✅ cada historia completada:

- [ ] **O1H2:** Cliente se registra e inicia sesión correctamente
- [ ] **O1H5:** Barbero configura horarios de atención
- [ ] **O1H3:** Cliente reserva turno y recibe confirmación por email
- [ ] **O1H1:** Sistema valida y rechaza reservas solapadas

---

## 🔑 Usuarios de Prueba

Si prefieres usar usuarios existentes:

| Usuario | Password | Rol |
|---------|----------|-----|
| cliente1 | password123 | CLIENTE |
| cliente2 | password123 | CLIENTE |
| barbero1 | password123 | BARBERO |
| barbero2 | password123 | BARBERO |

---

## 📊 Servicios Disponibles

| Servicio | Precio | Duración |
|----------|--------|----------|
| Corte Clásico | $12 | 30 min |
| Corte + Barba | $18 | 45 min |
| Barba | $8 | 20 min |
| Rapado | $10 | 15 min |
| Corte Premium | $25 | 60 min |
| Tinte | $35 | 90 min |

---

## 🎉 ¡Listo!

Si completaste todas las pruebas, **el Sprint 1 está funcionando correctamente** ✅

---

**Equipo:** Innovatech DCJ  
**Proyecto:** TurneroPro - Barber Shop Edition  
**Sprint:** 1
