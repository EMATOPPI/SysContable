import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, isLoading, user } = useAuth();

  console.log('🛡️ ProtectedRoute - Estado:', { 
    isAuthenticated, 
    isLoading, 
    hasUser: !!user,
    userName: user?.usuario 
  });

  // Mostrar loading mientras se verifica la autenticación
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="flex items-center space-x-3">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          <span className="text-gray-600 text-lg">Verificando autenticación...</span>
        </div>
      </div>
    );
  }

  // Si no está autenticado, redirigir al login
  if (!isAuthenticated) {
    console.log('❌ Usuario no autenticado, redirigiendo a login...');
    return <Navigate to="/login" replace />;
  }

  console.log('✅ Usuario autenticado, mostrando contenido protegido...');
  
  // Si está autenticado, mostrar el contenido protegido
  return children;
};

export default ProtectedRoute;