import React, { useState, useEffect } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Login = () => {
  const [formData, setFormData] = useState({
    usuario: 'testuser',
    contrasena: 'password123'
  });
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login, isAuthenticated, isLoading, error } = useAuth();
  const navigate = useNavigate();

  // Efecto para redirigir cuando se autentica exitosamente
  useEffect(() => {
    console.log('🔍 Login - Estado de autenticación:', { isAuthenticated, isLoading });
    if (isAuthenticated && !isLoading) {
      console.log('✅ Usuario autenticado, redirigiendo al dashboard...');
      navigate('/dashboard', { replace: true });
    }
  }, [isAuthenticated, isLoading, navigate]);

  // Si ya está autenticado, mostrar loading mientras redirige
  if (isAuthenticated) {
    return (
      <div className="login-container">
        <div className="login-card">
          <div className="loading-text">Redirigiendo al dashboard...</div>
        </div>
      </div>
    );
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    
    console.log('🚀 Enviando login...', { usuario: formData.usuario });
    
    try {
      const result = await login(formData);
      console.log('📝 Resultado del login:', result);
      
      if (result.success) {
        console.log('✅ Login exitoso, esperando redirección...');
        // La redirección se maneja en useEffect
      } else {
        console.error('❌ Login falló:', result.error);
        setIsSubmitting(false);
      }
    } catch (error) {
      console.error('❌ Error durante login:', error);
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="login-container">
        <div className="login-card">
          <div className="loading-text">
            <div className="loading-spinner"></div>
            Verificando sesión...
          </div>
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

        {error && (
          <div className="error-message">
            ❌ {error}
          </div>
        )}

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
            <div className="input-container">
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
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="password-toggle"
                disabled={isSubmitting}
              >
                {showPassword ? '🙈' : '👁️'}
              </button>
            </div>
          </div>

          <button type="submit" disabled={isSubmitting} className="btn-primary">
            {isSubmitting ? (
              <>
                <span className="loading-spinner"></span>
                Iniciando sesión...
              </>
            ) : (
              'Iniciar Sesión'
            )}
          </button>
        </form>

        {/* Debug info - remover en producción */}
        <div className="debug-info">
          <small>
            Estado: {isLoading ? 'Cargando' : isAuthenticated ? 'Autenticado' : 'No autenticado'} |
            Enviando: {isSubmitting ? 'Sí' : 'No'}
          </small>
        </div>

        <div className="footer-text">Sistema de Contaduría Pública v1.0.0</div>
      </div>
    </div>
  );
};

export default Login;