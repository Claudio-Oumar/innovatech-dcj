const API_URL = 'http://localhost:8081/api';
let token = localStorage.getItem('token');
let usuario = JSON.parse(localStorage.getItem('usuario'));

// Verificar autenticación
if (!token || !usuario || usuario.rol !== 'CLIENTE') {
    window.location.href = '/login.html';
}

// Mostrar nombre de usuario
document.getElementById('nombreUsuario').textContent = usuario.nombreCompleto;
document.getElementById('nombreCliente').textContent = usuario.nombreCompleto;

// Cerrar sesión
document.getElementById('btnCerrarSesion').addEventListener('click', (e) => {
    e.preventDefault();
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    window.location.href = '/login.html';
});

// Cargar barberos y servicios al iniciar
document.addEventListener('DOMContentLoaded', () => {
    cargarBarberos();
    cargarServicios();
    cargarMisReservas();
});

// Cargar barberos disponibles
async function cargarBarberos() {
    try {
        const response = await fetch(`${API_URL}/barberos/disponibles`);
        const barberos = await response.json();
        
        const select = document.getElementById('barberoId');
        select.innerHTML = '<option value="">Seleccione un barbero...</option>';
        
        barberos.forEach(barbero => {
            const option = document.createElement('option');
            option.value = barbero.id;
            option.textContent = barbero.nombreCompleto;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar barberos:', error);
    }
}

// Cargar servicios
async function cargarServicios() {
    try {
        const response = await fetch(`${API_URL}/servicios`);
        const servicios = await response.json();
        
        const select = document.getElementById('servicioId');
        select.innerHTML = '<option value="">Seleccione un servicio...</option>';
        
        servicios.forEach(servicio => {
            const option = document.createElement('option');
            option.value = servicio.id;
            option.textContent = `${servicio.nombre} - $${servicio.precio} (${servicio.duracionMinutos} min)`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar servicios:', error);
    }
}

// Crear nueva reserva
document.getElementById('formNuevaReserva').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        barberoId: parseInt(document.getElementById('barberoId').value),
        servicioId: parseInt(document.getElementById('servicioId').value),
        fechaHoraInicio: document.getElementById('fechaHora').value,
        notasCliente: document.getElementById('notas').value
    };
    
    try {
        const response = await fetch(`${API_URL}/reservas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            mostrarMensaje('¡Reserva creada exitosamente! Recibirás un correo de confirmación.', 'success');
            document.getElementById('formNuevaReserva').reset();
            cargarMisReservas();
        } else {
            mostrarMensaje(result.mensaje || 'Error al crear la reserva', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje('Error de conexión con el servidor', 'error');
    }
});

// Cargar mis reservas
async function cargarMisReservas() {
    try {
        const response = await fetch(`${API_URL}/reservas/mis-reservas`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const reservas = await response.json();
        
        const listaDiv = document.getElementById('listaReservas');
        
        if (reservas.length === 0) {
            listaDiv.innerHTML = '<p>No tienes reservas aún.</p>';
            return;
        }
        
        let html = '<table><thead><tr><th>Barbero</th><th>Servicio</th><th>Fecha/Hora</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>';
        
        reservas.forEach(reserva => {
            const fecha = new Date(reserva.fechaHoraInicio).toLocaleString('es-ES');
            const puedeCancel = reserva.estado !== 'CANCELADA' && reserva.estado !== 'COMPLETADA';
            
            html += `
                <tr>
                    <td>${reserva.barbero.nombreCompleto}</td>
                    <td>${reserva.servicio.nombre}</td>
                    <td>${fecha}</td>
                    <td><span style="color: ${getColorEstado(reserva.estado)}">${reserva.estado}</span></td>
                    <td>
                        ${puedeCancel ? `<button class="btn-small btn-danger" onclick="cancelarReserva(${reserva.id})">Cancelar</button>` : '-'}
                    </td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        listaDiv.innerHTML = html;
    } catch (error) {
        console.error('Error al cargar reservas:', error);
        document.getElementById('listaReservas').innerHTML = '<p>Error al cargar las reservas</p>';
    }
}

// Cancelar reserva
async function cancelarReserva(reservaId) {
    if (!confirm('¿Estás seguro de cancelar esta reserva?')) {
        return;
    }
    
    const motivo = prompt('Motivo de cancelación (opcional):') || 'Sin motivo especificado';
    
    try {
        const response = await fetch(`${API_URL}/reservas/${reservaId}/cancelar`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ motivo })
        });
        
        if (response.ok) {
            alert('Reserva cancelada exitosamente');
            cargarMisReservas();
        } else {
            const result = await response.json();
            alert(result.mensaje || 'Error al cancelar la reserva');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error de conexión con el servidor');
    }
}

function getColorEstado(estado) {
    const colores = {
        'PENDIENTE': '#f39c12',
        'CONFIRMADA': '#27ae60',
        'CANCELADA': '#e74c3c',
        'COMPLETADA': '#3498db',
        'NO_ASISTIO': '#95a5a6'
    };
    return colores[estado] || '#333';
}

function mostrarMensaje(texto, tipo) {
    const mensajeDiv = document.getElementById('mensajeReserva');
    mensajeDiv.innerHTML = `<div class="alert alert-${tipo === 'success' ? 'success' : 'error'}">${texto}</div>`;
    
    setTimeout(() => {
        mensajeDiv.innerHTML = '';
    }, 5000);
}
