# 📘 INSTRUCCIONES DE INSTALACIÓN - TurneroPro

## Manual Completo para Ejecutar el Proyecto desde Cero

---

## ⚙️ REQUISITOS PREVIOS

Antes de empezar, necesitas tener instalado:

### 1. Git
- **Descargar:** https://git-scm.com/downloads
- **Verificar instalación:**
  ```powershell
  git --version
  ```
  Debe mostrar algo como: `git version 2.x.x`

### 2. Docker Desktop (OBLIGATORIO)
- **Descargar:** https://www.docker.com/products/docker-desktop
- **Windows:** Instalar Docker Desktop para Windows
- **Mac:** Instalar Docker Desktop para Mac
- **Linux:** Instalar Docker Engine + Docker Compose

**Verificar instalación:**
```powershell
docker --version
docker-compose --version
```

**IMPORTANTE:** Docker Desktop debe estar corriendo (icono en la barra de tareas)

---

## 📥 PASO 1: CLONAR EL REPOSITORIO

Abre PowerShell (Windows) o Terminal (Mac/Linux) y ejecuta:

```powershell
# Navegar a la carpeta donde quieres el proyecto
cd Desktop

# Clonar el repositorio
git clone https://github.com/Claudio-Oumar/innovatech-dcj.git

# Entrar a la carpeta del proyecto
cd innovatech-dcj
```

---

## 🚀 PASO 2: LEVANTAR LA APLICACIÓN

**Comando único para ejecutar todo:**

```powershell
docker-compose up -d --build
```

**¿Qué hace este comando?**
- `docker-compose`: Orquesta todos los servicios
- `up`: Levanta los contenedores
- `-d`: Ejecuta en segundo plano (detached)
- `--build`: Compila la aplicación desde cero

**Este proceso tomará entre 2-5 minutos la primera vez** porque:
1. Descarga las imágenes de Docker (Java, Maven, MailHog)
2. Compila el código fuente
3. Crea la base de datos SQLite
4. Carga los datos de prueba

---

## ⏳ PASO 3: ESPERAR A QUE INICIE

Verifica que los servicios estén corriendo:

```powershell
docker-compose ps
```

**Debes ver algo así:**
```
NAME                  STATUS
turnero-pro-app       Up X seconds (healthy)
turnero-pro-mailhog   Up X seconds
```

**IMPORTANTE:** Espera a que aparezca **(healthy)** al lado de `turnero-pro-app`

Si no aparece "healthy" después de 1 minuto, revisa los logs:

```powershell
docker-compose logs app
```

---

## 🌐 PASO 4: ACCEDER A LA APLICACIÓN

### Aplicación Principal
Abre tu navegador en:

```
http://localhost:8081
```

### Simulador de Correos (MailHog)
Para ver los correos de confirmación:

```
http://localhost:8025
```

---

## 👥 PASO 5: USUARIOS DE PRUEBA

El sistema viene con usuarios pre-cargados. **NO necesitas registrarte**, puedes usar estos:

### Para Clientes:
| Usuario | Contraseña | Nombre |
|---------|-----------|--------|
| `cliente1` | `password123` | Juan Pérez |
| `cliente2` | `password123` | María González |

### Para Barberos:
| Usuario | Contraseña | Nombre |
|---------|-----------|--------|
| `barbero1` | `password123` | Carlos Martínez |
| `barbero2` | `password123` | Miguel Sánchez |

### Para Administrador:
| Usuario | Contraseña |
|---------|-----------|
| `admin` | `password123` |

---

## 🧪 PASO 6: PROBAR LA APLICACIÓN

### Prueba 1: Login como Cliente
1. Ve a: http://localhost:8081/login.html
2. Usuario: `cliente1`
3. Contraseña: `password123`
4. Click en "Ingresar"
5. ✅ Debes ver el panel de cliente

### Prueba 2: Crear una Reserva
1. Ya logueado como cliente1:
2. Selecciona **Barbero:** Carlos Martínez
3. Selecciona **Servicio:** Corte Clásico
4. **Fecha/Hora:** Elige un día de semana (Lunes-Viernes) entre 9:00-18:00
5. Click en "Reservar Turno"
6. ✅ Debe aparecer la reserva en "Mis Reservas"

### Prueba 3: Ver Email de Confirmación
1. Ve a: http://localhost:8025
2. ✅ Debes ver el email de confirmación de la reserva

### Prueba 4: Login como Barbero
1. Cierra sesión (botón en navbar)
2. Login con: `barbero1` / `password123`
3. ✅ Debes ver el panel de barbero con tus horarios y reservas

---

## 🛑 COMANDOS ÚTILES

### Ver logs en tiempo real:
```powershell
docker-compose logs -f app
```
Presiona `Ctrl+C` para salir

### Detener la aplicación:
```powershell
docker-compose down
```

### Reiniciar la aplicación:
```powershell
docker-compose restart
```

### Ver estado de los servicios:
```powershell
docker-compose ps
```

### Limpiar y empezar de cero:
```powershell
# Detener todo
docker-compose down

# Eliminar la base de datos
Remove-Item ./data/turnero_pro.db

# Levantar de nuevo
docker-compose up -d --build
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Problema: "Puerto 8081 ya está en uso"

**Solución 1 - Cambiar el puerto:**
Edita `docker-compose.yml`, línea que dice:
```yaml
ports:
  - "8081:8081"
```
Cámbialo a:
```yaml
ports:
  - "9090:8081"
```
Luego accede en: http://localhost:9090

**Solución 2 - Liberar el puerto:**
```powershell
# Ver qué está usando el puerto 8081
netstat -ano | findstr :8081

# Matar el proceso (reemplaza PID con el número que viste)
taskkill /PID <PID> /F
```

### Problema: "Docker no está corriendo"

**Solución:**
1. Abre Docker Desktop
2. Espera a que el icono esté en verde
3. Vuelve a ejecutar: `docker-compose up -d --build`

### Problema: "Credenciales inválidas"

**Solución:**
La base de datos puede tener datos viejos. Límpiala:

```powershell
docker-compose down
Remove-Item ./data/turnero_pro.db -Force
docker-compose up -d
```

Espera 30 segundos y prueba de nuevo con: `cliente1` / `password123`

### Problema: Error al compilar

**Solución:**
```powershell
# Limpiar todo
docker-compose down
docker system prune -a

# Volver a construir
docker-compose up -d --build
```

### Problema: "No puedo registrar un usuario"

**Causa:** Los usuarios de prueba ya existen.

**Solución:**
- Usa un username diferente: `test_user`, `mi_usuario`, etc.
- NO uses: cliente1, cliente2, barbero1, barbero2, admin

---

## 📋 CHECKLIST DE INSTALACIÓN

Marca cada paso a medida que lo completes:

- [ ] Git instalado (`git --version` funciona)
- [ ] Docker Desktop instalado y corriendo
- [ ] Repositorio clonado (`cd innovatech-dcj` funciona)
- [ ] `docker-compose up -d --build` ejecutado sin errores
- [ ] `docker-compose ps` muestra "healthy"
- [ ] http://localhost:8081 carga la página de inicio
- [ ] Login con `cliente1` / `password123` funciona
- [ ] Puedo crear una reserva
- [ ] http://localhost:8025 muestra el email

---

## 📊 ARQUITECTURA DEL PROYECTO

```
innovatech-dcj/
├── src/                          # Código fuente Java
│   └── main/
│       ├── java/                 # Clases Java (Backend)
│       └── resources/
│           ├── application.properties
│           └── static/           # Frontend (HTML/CSS/JS)
├── data/                         # Base de datos SQLite (auto-creada)
├── Dockerfile                    # Imagen Docker de la app
├── docker-compose.yml            # Orquestación de servicios
└── pom.xml                       # Dependencias Maven
```

---

## 🎯 SERVICIOS DEL PROYECTO

| Servicio | Puerto | Descripción | URL |
|----------|--------|-------------|-----|
| **Aplicación** | 8081 | Backend + Frontend | http://localhost:8081 |
| **MailHog** | 8025 | Simulador de email | http://localhost:8025 |
| **SMTP** | 1025 | Servidor de correo | localhost:1025 |

---

## 💾 BASE DE DATOS

**Tipo:** SQLite (archivo embebido)
**Ubicación:** `./data/turnero_pro.db`
**Auto-creada:** Sí, al iniciar la aplicación
**Datos pre-cargados:** 5 usuarios, 6 servicios, horarios de barberos

**No necesitas instalar ninguna base de datos**, todo está incluido.

---

## 🔧 TECNOLOGÍAS USADAS

- **Backend:** Java 17 + Spring Boot 3.2.0
- **Frontend:** HTML5 + CSS3 + JavaScript
- **Base de Datos:** SQLite 3.40
- **Autenticación:** JWT (JSON Web Tokens)
- **Containerización:** Docker + Docker Compose
- **Email:** MailHog (simulador)

---

## 📞 SOPORTE

Si tienes problemas:

1. **Revisa los logs:**
   ```powershell
   docker-compose logs app
   ```

2. **Verifica Docker:**
   ```powershell
   docker-compose ps
   ```

3. **Reinicia todo:**
   ```powershell
   docker-compose down
   docker-compose up -d --build
   ```

4. **Limpia y vuelve a empezar:**
   ```powershell
   docker-compose down
   Remove-Item ./data/turnero_pro.db -Force
   docker system prune -f
   docker-compose up -d --build
   ```

---

## ✅ RESUMEN EJECUTIVO

### Para ejecutar el proyecto en 3 comandos:

```powershell
# 1. Clonar
git clone https://github.com/Claudio-Oumar/innovatech-dcj.git
cd innovatech-dcj

# 2. Levantar
docker-compose up -d --build

# 3. Esperar 2 minutos y abrir
# http://localhost:8081
```

### Para detener:
```powershell
docker-compose down
```

---

## 🎓 PARA EQUIPO DE QA / REVISORES

Este proyecto está **100% dockerizado**. No necesitas:
- ❌ Instalar Java
- ❌ Instalar Maven
- ❌ Instalar PostgreSQL/MySQL
- ❌ Configurar nada manualmente

Solo necesitas:
- ✅ Docker Desktop
- ✅ Ejecutar `docker-compose up -d --build`
- ✅ Abrir http://localhost:8081

**Todo funciona out-of-the-box** 📦

---

**Desarrollado por:** Innovatech DCJ (Dennis, Claudio, Jhonathan)  
**Proyecto:** TurneroPro - Barber Shop Edition  
**Sprint:** 1  
**Materia:** Calidad de Software - EPN 2025
