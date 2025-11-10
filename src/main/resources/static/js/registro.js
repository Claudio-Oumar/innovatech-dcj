const API_URL = 'http://localhost:8081/api';

document.getElementById('formRegistro').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        nombreCompleto: document.getElementById('nombreCompleto').value,
        telefono: document.getElementById('telefono').value,
        rol: document.getElementById('rol').value
    };
    
    try {
        const response = await fetch(`${API_URL}/auth/registro`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // Guardar token y datos de usuario
            localStorage.setItem('token', result.token);
            localStorage.setItem('usuario', JSON.stringify({
                id: result.id,
                username: result.username,
                email: result.email,
                nombreCompleto: result.nombreCompleto,
                rol: result.rol
            }));
            
            mostrarMensaje('Registro exitoso! Redirigiendo...', 'success');
            
            // Redirigir según el rol
            setTimeout(() => {
                if (result.rol === 'CLIENTE') {
                    window.location.href = '/cliente-panel.html';
                } else if (result.rol === 'BARBERO') {
                    window.location.href = '/barbero-panel.html';
                }
            }, 1500);
        } else {
            mostrarMensaje(result.mensaje || 'Error al registrar usuario', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje('Error de conexión con el servidor', 'error');
    }
});

function mostrarMensaje(texto, tipo) {
    const mensajeDiv = document.getElementById('mensaje');
    mensajeDiv.innerHTML = `<div class="alert alert-${tipo === 'success' ? 'success' : 'error'}">${texto}</div>`;
    
    setTimeout(() => {
        mensajeDiv.innerHTML = '';
    }, 5000);
}
