const API_URL = 'http://localhost:8081/api';
let token = localStorage.getItem('token');
let usuario = JSON.parse(localStorage.getItem('usuario'));

// Verificar autenticación
if (!token || !usuario || usuario.rol !== 'BARBERO') {
    window.location.href = '/login.html';
}

// Mostrar nombre de usuario
document.getElementById('nombreUsuario').textContent = usuario.nombreCompleto;
document.getElementById('nombreBarbero').textContent = usuario.nombreCompleto;

// Cerrar sesión
document.getElementById('btnCerrarSesion').addEventListener('click', (e) => {
    e.preventDefault();
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    window.location.href = '/login.html';
});

// Cargar datos al iniciar
document.addEventListener('DOMContentLoaded', () => {
    cargarMisHorarios();
    cargarMisReservas();
});

// Agregar nuevo horario
document.getElementById('formNuevoHorario').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        diaSemana: document.getElementById('diaSemana').value,
        horaInicio: document.getElementById('horaInicio').value,
        horaFin: document.getElementById('horaFin').value
    };
    
    try {
        const response = await fetch(`${API_URL}/horarios`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            mostrarMensaje('Horario agregado exitosamente', 'success');
            document.getElementById('formNuevoHorario').reset();
            cargarMisHorarios();
        } else {
            mostrarMensaje(result.mensaje || 'Error al agregar el horario', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje('Error de conexión con el servidor', 'error');
    }
});

// Cargar mis horarios
async function cargarMisHorarios() {
    try {
        const response = await fetch(`${API_URL}/horarios/mis-horarios`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const horarios = await response.json();
        
        const listaDiv = document.getElementById('listaHorarios');
        
        if (horarios.length === 0) {
            listaDiv.innerHTML = '<p>No tienes horarios configurados aún.</p>';
            return;
        }
        
        let html = '<table><thead><tr><th>Día</th><th>Hora Inicio</th><th>Hora Fin</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>';
        
        horarios.forEach(horario => {
            html += `
                <tr>
                    <td>${traducirDia(horario.diaSemana)}</td>
                    <td>${horario.horaInicio}</td>
                    <td>${horario.horaFin}</td>
                    <td><span style="color: ${horario.activo ? '#27ae60' : '#e74c3c'}">${horario.activo ? 'Activo' : 'Inactivo'}</span></td>
                    <td>
                        ${horario.activo ? `<button class="btn-small btn-danger" onclick="eliminarHorario(${horario.id})">Eliminar</button>` : '-'}
                    </td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        listaDiv.innerHTML = html;
    } catch (error) {
        console.error('Error al cargar horarios:', error);
        document.getElementById('listaHorarios').innerHTML = '<p>Error al cargar los horarios</p>';
    }
}

// Eliminar horario
async function eliminarHorario(horarioId) {
    if (!confirm('¿Estás seguro de eliminar este horario?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/horarios/${horarioId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            alert('Horario eliminado exitosamente');
            cargarMisHorarios();
        } else {
            const result = await response.json();
            alert(result.mensaje || 'Error al eliminar el horario');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error de conexión con el servidor');
    }
}

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
        
        let html = '<table><thead><tr><th>Cliente</th><th>Servicio</th><th>Fecha/Hora</th><th>Estado</th><th>Notas</th></tr></thead><tbody>';
        
        reservas.forEach(reserva => {
            const fecha = new Date(reserva.fechaHoraInicio).toLocaleString('es-ES');
            
            html += `
                <tr>
                    <td>${reserva.cliente.nombreCompleto}</td>
                    <td>${reserva.servicio.nombre}</td>
                    <td>${fecha}</td>
                    <td><span style="color: ${getColorEstado(reserva.estado)}">${reserva.estado}</span></td>
                    <td>${reserva.notasCliente || '-'}</td>
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

function traducirDia(dia) {
    const dias = {
        'MONDAY': 'Lunes',
        'TUESDAY': 'Martes',
        'WEDNESDAY': 'Miércoles',
        'THURSDAY': 'Jueves',
        'FRIDAY': 'Viernes',
        'SATURDAY': 'Sábado',
        'SUNDAY': 'Domingo'
    };
    return dias[dia] || dia;
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
    const mensajeDiv = document.getElementById('mensajeHorario');
    mensajeDiv.innerHTML = `<div class="alert alert-${tipo === 'success' ? 'success' : 'error'}">${texto}</div>`;
    
    setTimeout(() => {
        mensajeDiv.innerHTML = '';
    }, 5000);
}
