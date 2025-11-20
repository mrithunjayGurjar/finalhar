
const API_BASE = '';
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    if (authToken && currentUser) {
        showAppSection();
        loadTickets();
    }
});

// Auth Tab Switching
function showTab(tab) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    document.getElementById('login-form').classList.toggle('hidden', tab !== 'login');
    document.getElementById('register-form').classList.toggle('hidden', tab !== 'register');
}

// App Tab Switching
function showAppTab(tab) {
    document.querySelectorAll('.app-tab-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    document.getElementById('create-ticket').classList.add('hidden');
    document.getElementById('tickets-list').classList.add('hidden');
    document.getElementById('active-tickets').classList.add('hidden');
    
    if (tab === 'create') {
        document.getElementById('create-ticket').classList.remove('hidden');
    } else if (tab === 'tickets') {
        document.getElementById('tickets-list').classList.remove('hidden');
        loadTickets();
    } else if (tab === 'active') {
        document.getElementById('active-tickets').classList.remove('hidden');
        loadActiveTickets();
    }
}

// Login
async function login() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const errorDiv = document.getElementById('login-error');
    
    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        if (!response.ok) {
            const error = await response.text();
            errorDiv.textContent = error || 'Login failed';
            return;
        }
        
        const data = await response.json();
        authToken = data.token;
        currentUser = data;
        localStorage.setItem('authToken', authToken);
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        
        showAppSection();
        loadTickets();
    } catch (error) {
        errorDiv.textContent = 'Network error. Please try again.';
    }
}

// Register
async function register() {
    const name = document.getElementById('register-name').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    const role = document.getElementById('register-role').value;
    const errorDiv = document.getElementById('register-error');
    
    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password, role })
        });
        
        if (!response.ok) {
            const error = await response.text();
            errorDiv.textContent = error || 'Registration failed';
            return;
        }
        
        const data = await response.json();
        authToken = data.token;
        currentUser = data;
        localStorage.setItem('authToken', authToken);
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        
        showAppSection();
        loadTickets();
    } catch (error) {
        errorDiv.textContent = 'Network error. Please try again.';
    }
}

// Logout
function logout() {
    authToken = null;
    currentUser = null;
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    
    document.getElementById('auth-section').classList.remove('hidden');
    document.getElementById('app-section').classList.add('hidden');
    document.getElementById('user-info').classList.add('hidden');
}

// Show App Section
function showAppSection() {
    document.getElementById('auth-section').classList.add('hidden');
    document.getElementById('app-section').classList.remove('hidden');
    document.getElementById('user-info').classList.remove('hidden');
    document.getElementById('user-name').textContent = `${currentUser.name} (${currentUser.role})`;
}

// Create Ticket
async function createTicket() {
    const title = document.getElementById('ticket-title').value;
    const description = document.getElementById('ticket-description').value;
    const priority = document.getElementById('ticket-priority').value;
    const errorDiv = document.getElementById('create-error');
    
    if (!title) {
        errorDiv.textContent = 'Title is required';
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/api/tickets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ title, description, priority })
        });
        
        if (!response.ok) {
            errorDiv.textContent = 'Failed to create ticket';
            return;
        }
        
        document.getElementById('ticket-title').value = '';
        document.getElementById('ticket-description').value = '';
        errorDiv.textContent = '';
        
        alert('Ticket created successfully!');
        showAppTab('tickets');
        loadTickets();
    } catch (error) {
        errorDiv.textContent = 'Network error. Please try again.';
    }
}

// Load Tickets
async function loadTickets() {
    try {
        const response = await fetch(`${API_BASE}/api/tickets`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        if (!response.ok) return;
        
        const tickets = await response.json();
        displayTickets(tickets, 'tickets-container');
    } catch (error) {
        console.error('Failed to load tickets', error);
    }
}

// Load Active Tickets
async function loadActiveTickets() {
    try {
        const response = await fetch(`${API_BASE}/api/tickets/active`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        if (!response.ok) return;
        
        const tickets = await response.json();
        displayTickets(tickets, 'active-tickets-container');
    } catch (error) {
        console.error('Failed to load active tickets', error);
    }
}

// Display Tickets
function displayTickets(tickets, containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = '';
    
    tickets.forEach(ticket => {
        const card = document.createElement('div');
        card.className = 'ticket-card';
        card.onclick = () => showTicketDetails(ticket.id);
        
        card.innerHTML = `
            <div class="ticket-header">
                <div class="ticket-title">${ticket.title}</div>
            </div>
            <div class="ticket-meta">
                <span class="badge status-${ticket.status}">${ticket.status}</span>
                <span class="badge priority-${ticket.priority}">${ticket.priority}</span>
                <span style="color: #666; font-size: 12px;">Created: ${new Date(ticket.createdAt).toLocaleDateString()}</span>
            </div>
        `;
        
        container.appendChild(card);
    });
}

// Show Ticket Details
async function showTicketDetails(ticketId) {
    try {
        const response = await fetch(`${API_BASE}/api/tickets/${ticketId}`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        if (!response.ok) return;
        
        const ticket = await response.json();
        const modal = document.getElementById('ticket-modal');
        const details = document.getElementById('ticket-details');
        
        details.innerHTML = `
            <h2>${ticket.title}</h2>
            <div class="ticket-meta" style="margin: 15px 0;">
                <span class="badge status-${ticket.status}">${ticket.status}</span>
                <span class="badge priority-${ticket.priority}">${ticket.priority}</span>
            </div>
            <p><strong>Description:</strong></p>
            <p>${ticket.description || 'No description'}</p>
            <p><strong>Created:</strong> ${new Date(ticket.createdAt).toLocaleString()}</p>
            <p><strong>Created By:</strong> ${ticket.createdByName}</p>
            ${ticket.assignedToName ? `<p><strong>Assigned To:</strong> ${ticket.assignedToName}</p>` : ''}
            
            <div style="margin-top: 20px; display: flex; gap: 10px;">
                ${ticket.status !== 'CLOSED' ? `
                    <select id="new-status-${ticketId}">
                        <option value="">Change Status</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="ESCALATED">Escalate</option>
                        <option value="RESOLVED">Resolved</option>
                        <option value="CLOSED">Close</option>
                    </select>
                    <button onclick="updateTicketStatus(${ticketId})">Update Status</button>
                ` : ''}
            </div>
            
            <div class="notes-section">
                <h3>Notes</h3>
                <div id="notes-list-${ticketId}"></div>
                <div class="add-note-form">
                    <textarea id="note-message-${ticketId}" placeholder="Add a note..." rows="3"></textarea>
                    <select id="note-type-${ticketId}">
                        <option value="CUSTOMER_REPLY">Customer Reply</option>
                        <option value="TEAM_REPLY">Team Reply</option>
                    </select>
                    <button onclick="addNote(${ticketId})">Add Note</button>
                </div>
            </div>
        `;
        
        modal.classList.remove('hidden');
        loadNotes(ticketId);
    } catch (error) {
        console.error('Failed to load ticket details', error);
    }
}

// Load Notes
async function loadNotes(ticketId) {
    try {
        const response = await fetch(`${API_BASE}/api/tickets/${ticketId}/notes`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        if (!response.ok) return;
        
        const notes = await response.json();
        const notesList = document.getElementById(`notes-list-${ticketId}`);
        notesList.innerHTML = '';
        
        notes.forEach(note => {
            const noteDiv = document.createElement('div');
            noteDiv.className = `note ${note.type}`;
            noteDiv.innerHTML = `
                <div style="display: flex; justify-content: space-between; margin-bottom: 5px;">
                    <strong>${note.authorName}</strong>
                    <span class="badge">${note.type}</span>
                </div>
                <p>${note.message}</p>
                <small style="color: #666;">${new Date(note.createdAt).toLocaleString()}</small>
            `;
            notesList.appendChild(noteDiv);
        });
    } catch (error) {
        console.error('Failed to load notes', error);
    }
}

// Add Note
async function addNote(ticketId) {
    const message = document.getElementById(`note-message-${ticketId}`).value;
    const type = document.getElementById(`note-type-${ticketId}`).value;
    
    if (!message) return;
    
    try {
        const response = await fetch(`${API_BASE}/api/tickets/${ticketId}/notes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ message, type })
        });
        
        if (response.ok) {
            document.getElementById(`note-message-${ticketId}`).value = '';
            loadNotes(ticketId);
        }
    } catch (error) {
        console.error('Failed to add note', error);
    }
}

// Update Ticket Status
async function updateTicketStatus(ticketId) {
    const newStatus = document.getElementById(`new-status-${ticketId}`).value;
    
    if (!newStatus) return;
    
    try {
        const response = await fetch(`${API_BASE}/api/tickets/${ticketId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ status: newStatus })
        });
        
        if (response.ok) {
            closeModal();
            loadTickets();
            loadActiveTickets();
            alert('Status updated successfully!');
        }
    } catch (error) {
        console.error('Failed to update status', error);
    }
}

// Close Modal
function closeModal() {
    document.getElementById('ticket-modal').classList.add('hidden');
}
