# 🔐 GUÍA RÁPIDA DE LOGIN - TurneroPro

## ✅ USUARIOS CREADOS EXITOSAMENTE

La base de datos está limpia y los usuarios fueron creados correctamente.

---

## 🎯 PASOS PARA ENTRAR A LA APLICACIÓN

### 1️⃣ Abre tu navegador en:
```
http://localhost:8081/login.html
```

### 2️⃣ Usa estos usuarios (COPIA Y PEGA):

## 👤 USUARIOS DISPONIBLES

### Para probar como CLIENTE:

**Opción 1:**
- **Usuario:** `cliente1`
- **Contraseña:** `password123`

**Opción 2:**
- **Usuario:** `cliente2`
- **Contraseña:** `password123`

### Para probar como BARBERO:

**Opción 1:**
- **Usuario:** `barbero1`
- **Contraseña:** `password123`

**Opción 2:**
- **Usuario:** `barbero2`
- **Contraseña:** `password123`

### Para probar como ADMIN:

- **Usuario:** `admin`
- **Contraseña:** `password123`

---

## 📝 IMPORTANTE - REGISTRO DE NUEVOS USUARIOS

Si quieres **REGISTRAR un usuario nuevo**, NO uses estos nombres:
- ❌ admin
- ❌ cliente1
- ❌ cliente2
- ❌ barbero1
- ❌ barbero2

**Estos nombres ya existen** y te dará error "username already exists".

### ✅ Para registrarte usa:

1. Ve a: `http://localhost:8081/registro.html`

2. Usa un **username diferente**, por ejemplo:
   - `test_user`
   - `juan123`
   - `maria_cliente`
   - `nuevo_cliente`
   - `tu_nombre`

3. Llena el formulario:
   ```
   Username: tu_nombre          (único, sin espacios)
   Email: tumail@gmail.com      (cualquier email válido)
   Contraseña: tu_password      (mínimo 6 caracteres)
   Nombre Completo: Tu Nombre Apellido
   ```

4. Click en "Registrarse"

5. ✅ Si todo sale bien, serás redirigido al login

---

## 🚨 SOLUCIÓN DE PROBLEMAS

### Problema: "Credenciales inválidas"

**Causas posibles:**

1. **Escribiste mal la contraseña**
   - La contraseña es: `password123` (todo minúsculas, sin espacios)
   - Cópiala y pégala directamente

2. **Escribiste mal el usuario**
   - Es: `cliente1` (todo minúsculas, sin espacios)
   - Cópialo y pégalo directamente

3. **Hay espacios extra**
   - Asegúrate de NO tener espacios al inicio o final

### Problema: "Username already exists" al registrarte

**Solución:**
- Estás intentando usar un username que ya existe (admin, cliente1, cliente2, barbero1, barbero2)
- Usa un nombre diferente como: `test_cliente`, `mi_usuario`, `prueba123`

### Problema: No carga la página

**Solución:**
```powershell
# Verifica que Docker esté corriendo
docker-compose ps

# Si no está corriendo, levanta los servicios
docker-compose up -d
```

---

## 🧪 FLUJO DE PRUEBA COMPLETO

### Test 1: Login como Cliente

1. Abre: http://localhost:8081/login.html
2. **Usuario:** `cliente1`
3. **Contraseña:** `password123`
4. Click "Ingresar"
5. ✅ Debes ver: Panel de Cliente con opciones para reservar

### Test 2: Crear una Reserva

1. Ya logueado como cliente1
2. Selecciona **Barbero:** Carlos Martínez (barbero1)
3. Selecciona **Servicio:** Corte Clásico
4. Elige **Fecha/Hora:** Un día entre Lunes-Viernes, entre 9:00-18:00
5. Click "Reservar Turno"
6. ✅ Debes ver: Confirmación de reserva creada

### Test 3: Ver Email

1. Abre: http://localhost:8025 (MailHog)
2. ✅ Debes ver: Email de confirmación de tu reserva

### Test 4: Cerrar Sesión

1. Click en el botón "Cerrar Sesión" en el navbar
2. ✅ Debes volver a: Página de login

### Test 5: Login como Barbero

1. En login: http://localhost:8081/login.html
2. **Usuario:** `barbero1`
3. **Contraseña:** `password123`
4. ✅ Debes ver: Panel de Barbero con tus horarios y reservas pendientes

---

## 💡 TIPS

### ✅ Para copiar las credenciales:

**Cliente 1:**
```
cliente1
password123
```

**Cliente 2:**
```
cliente2
password123
```

**Barbero 1:**
```
barbero1
password123
```

**Admin:**
```
admin
password123
```

### ✅ URLs Importantes:

| Función | URL |
|---------|-----|
| Login | http://localhost:8081/login.html |
| Registro | http://localhost:8081/registro.html |
| Home | http://localhost:8081/ |
| Emails | http://localhost:8025 |

---

## 🔄 Si nada funciona: RESETEAR TODO

```powershell
# 1. Detener todo
docker-compose down

# 2. Borrar base de datos
Remove-Item ./data/turnero_pro.db -Force

# 3. Limpiar Docker
docker system prune -f

# 4. Levantar de nuevo
docker-compose up -d --build

# 5. Esperar 30 segundos
Start-Sleep -Seconds 30

# 6. Verificar logs
docker-compose logs app | Select-String -Pattern "Datos de prueba"

# 7. Abrir navegador en http://localhost:8081/login.html
```

---

## 📊 ESTADO ACTUAL

✅ Base de datos: Limpia y creada  
✅ Usuarios: 5 usuarios cargados (admin, barbero1, barbero2, cliente1, cliente2)  
✅ Servicios: 6 servicios cargados  
✅ Horarios: Barberos con disponibilidad Lunes-Viernes 9:00-18:00  
✅ Aplicación: Corriendo en http://localhost:8081  
✅ MailHog: Corriendo en http://localhost:8025  

**TODO ESTÁ FUNCIONANDO CORRECTAMENTE** ✨

---

**Última actualización:** Base de datos recreada exitosamente  
**Usuarios verificados:** ✅ Todos creados con contraseña `password123`
