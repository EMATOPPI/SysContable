import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Login = () => {
  const [formData, setFormData] = useState({
    usuario: 'testuser',
    contrasena: 'password123'
  });
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login, isAuthenticated, isLoading, error } = useAuth();

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    const result = await login(formData);
    if (!result.success) {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="login-container">
        <div className="login-card">
          <div className="loading-text">Verificando sesión...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title">Sistema Contaduría</h1>
          <p className="login-subtitle">Ingresa tus credenciales para acceder</p>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="usuario" className="form-label">Usuario</label>
            <input
              id="usuario"
              name="usuario"
              type="text"
              required
              value={formData.usuario}
              onChange={handleInputChange}
              className="form-input"
              placeholder="Ingresa tu usuario"
              disabled={isSubmitting}
            />
          </div>

          <div className="form-group">
            <label htmlFor="contrasena" className="form-label">Contraseña</label>
            <input
              id="contrasena"
              name="contrasena"
              type={showPassword ? 'text' : 'password'}
              required
              value={formData.contrasena}
              onChange={handleInputChange}
              className="form-input"
              placeholder="Ingresa tu contraseña"
              disabled={isSubmitting}
            />
          </div>

          <button type="submit" disabled={isSubmitting} className="btn-primary">
            {isSubmitting ? 'Iniciando sesión...' : 'Iniciar Sesión'}
          </button>
        </form>

        <div className="footer-text">Sistema de Contaduría Pública v1.0.0</div>
      </div>
    </div>
  );
};

export default Login;