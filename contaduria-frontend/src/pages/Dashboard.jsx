import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [activeSection, setActiveSection] = useState('perfil');

  const menuItems = [
    { id: 'perfil', name: 'Mi Perfil', icon: '👤' },
    { id: 'clientes', name: 'Clientes', icon: '👥' },
    { id: 'empresas', name: 'Empresas', icon: '🏢' },
    { id: 'documentos', name: 'Documentos', icon: '📄' },
    { id: 'reportes', name: 'Reportes', icon: '📊' },
    { id: 'facturas', name: 'Facturas', icon: '🧾' },
    { id: 'balances', name: 'Balances', icon: '⚖️' }
  ];

  const renderUserProfile = () => (
    <div>
      <h2 className="section-title">Mi Perfil</h2>
      
      <div className="profile-grid">
        {/* Información personal */}
        <div className="info-card">
          <h3 className="card-title">
            <span className="card-icon">👤</span>
            Información Personal
          </h3>
          <div>
            <div className="info-item">
              <span className="info-label">Usuario:</span>
              <p className="info-value">{user?.usuario}</p>
            </div>
            <div className="info-item">
              <span className="info-label">Nombre Completo:</span>
              <p className="info-value">{user?.nombreCompleto || 'No disponible'}</p>
            </div>
            <div className="info-item">
              <span className="info-label">Email:</span>
              <p className="info-value">{user?.email || 'No disponible'}</p>
            </div>
            <div className="info-item">
              <span className="info-label">Estado:</span>
              <span className={`status-badge ${user?.activo === 1 ? 'status-active' : 'status-inactive'}`}>
                {user?.activo === 1 ? 'Activo' : 'Inactivo'}
              </span>
            </div>
          </div>
        </div>

        {/* Permisos y roles */}
        <div className="info-card">
          <h3 className="card-title">Permisos y Roles</h3>
          <div>
            <div className="info-item">
              <span className="info-label">Roles:</span>
              <div className="role-badges">
                {user?.roles?.map((role, index) => (
                  <span key={index} className="role-badge">
                    {role}
                  </span>
                ))}
              </div>
            </div>
            <div className="info-item">
              <span className="info-label">Ver Todos los Clientes:</span>
              <span className={`status-badge ${user?.puedeVerTodosClientes === 1 ? 'status-active' : 'status-inactive'}`}>
                {user?.puedeVerTodosClientes === 1 ? 'Sí' : 'No'}
              </span>
            </div>
            <div className="info-item">
              <span className="info-label">Empleado ID:</span>
              <p className="info-value">{user?.empleado?.idEmpleados || 'No disponible'}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );

  const renderSection = () => (
    <div>
      <h2 className="section-title">{activeSection}</h2>
      <div className="info-card">
        <p>Sección <strong>{activeSection}</strong> - En desarrollo</p>
        <p>Aquí irá el contenido específico de {activeSection}.</p>
      </div>
    </div>
  );

  return (
    <div className="dashboard-container">
      {/* Header */}
      <header className="dashboard-header">
        <div className="header-content">
          <h1 className="header-title">Sistema Contaduría Pública</h1>
          
          <div className="header-actions">
            <div className="user-info">
              Hola, <strong>{user?.nombreCompleto || user?.usuario}</strong>
            </div>
            <button onClick={logout} className="btn-secondary">
              Cerrar Sesión
            </button>
          </div>
        </div>
      </header>

      <div className="main-content">
        <div className="dashboard-grid">
          {/* Sidebar */}
          <div className="sidebar">
            <h2 className="sidebar-title">Navegación</h2>
            <ul className="nav-list">
              {menuItems.map((item) => (
                <li key={item.id} className="nav-item">
                  <button
                    onClick={() => setActiveSection(item.id)}
                    className={`nav-button ${activeSection === item.id ? 'active' : ''}`}
                  >
                    <span className="nav-icon">{item.icon}</span>
                    {item.name}
                  </button>
                </li>
              ))}
            </ul>
          </div>

          {/* Main Content */}
          <div className="content-area">
            {activeSection === 'perfil' ? renderUserProfile() : renderSection()}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;