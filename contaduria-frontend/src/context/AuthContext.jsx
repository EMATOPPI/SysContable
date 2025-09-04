import React, { createContext, useContext, useReducer, useEffect } from 'react';
import { authService } from '../services/api';
import { TOKEN_CONFIG, APP_STATES } from '../utils/constants';

// Estado inicial
const initialState = {
  status: APP_STATES.LOADING,
  user: null,
  accessToken: null,
  refreshToken: null,
  error: null
};

// Reducer para manejar el estado de autenticación
const authReducer = (state, action) => {
  switch (action.type) {
    case 'LOADING':
      return {
        ...state,
        status: APP_STATES.LOADING,
        error: null
      };

    case 'LOGIN_SUCCESS':
      return {
        ...state,
        status: APP_STATES.AUTHENTICATED,
        user: action.payload.user,
        accessToken: action.payload.accessToken,
        refreshToken: action.payload.refreshToken,
        error: null
      };

    case 'LOGIN_ERROR':
      return {
        ...state,
        status: APP_STATES.UNAUTHENTICATED,
        user: null,
        accessToken: null,
        refreshToken: null,
        error: action.payload
      };

    case 'LOGOUT':
      return {
        ...initialState,
        status: APP_STATES.UNAUTHENTICATED
      };

    case 'UPDATE_USER':
      return {
        ...state,
        user: { ...state.user, ...action.payload }
      };

    default:
      return state;
  }
};

// Crear contexto
const AuthContext = createContext();

// Provider del contexto
export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Verificar si hay una sesión guardada al iniciar
  useEffect(() => {
    const checkAuthState = async () => {
      const savedToken = localStorage.getItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY);
      const savedUser = localStorage.getItem(TOKEN_CONFIG.USER_KEY);
      const savedRefreshToken = localStorage.getItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY);

      if (savedToken && savedUser) {
        try {
          // Parsear el usuario guardado
          const parsedUser = JSON.parse(savedUser);
          
          // Intentar obtener el perfil actualizado para validar el token
          const profileResponse = await authService.getProfile();
          
          if (profileResponse.exito) {
            dispatch({
              type: 'LOGIN_SUCCESS',
              payload: {
                user: profileResponse.datos.usuario,
                accessToken: savedToken,
                refreshToken: savedRefreshToken
              }
            });
          } else {
            // Si el perfil falla, usar datos guardados si el token parece válido
            dispatch({
              type: 'LOGIN_SUCCESS',
              payload: {
                user: parsedUser,
                accessToken: savedToken,
                refreshToken: savedRefreshToken
              }
            });
          }
        } catch (error) {
          console.error('Error verificando sesión:', error);
          // Si hay error, intentar con datos locales primero
          try {
            const parsedUser = JSON.parse(savedUser);
            dispatch({
              type: 'LOGIN_SUCCESS',
              payload: {
                user: parsedUser,
                accessToken: savedToken,
                refreshToken: savedRefreshToken
              }
            });
          } catch (parseError) {
            // Si no se puede parsear, limpiar todo
            localStorage.removeItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY);
            localStorage.removeItem(TOKEN_CONFIG.USER_KEY);
            localStorage.removeItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY);
            dispatch({ type: 'LOGOUT' });
          }
        }
      } else {
        dispatch({ type: 'LOGOUT' });
      }
    };

    checkAuthState();
  }, []);

  // Función de login
  const login = async (credentials) => {
    dispatch({ type: 'LOADING' });

    try {
      console.log('🔐 Iniciando login...', credentials.usuario);
      const response = await authService.login(credentials);
      console.log('📡 Respuesta del servidor:', response);

      if (response.exito) {
        const { accessToken, refreshToken, usuario } = response.datos;

        // Guardar en localStorage
        localStorage.setItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY, accessToken);
        localStorage.setItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY, refreshToken);
        localStorage.setItem(TOKEN_CONFIG.USER_KEY, JSON.stringify(usuario));

        console.log('✅ Login exitoso, actualizando estado...', usuario);

        dispatch({
          type: 'LOGIN_SUCCESS',
          payload: {
            user: usuario,
            accessToken,
            refreshToken
          }
        });

        return { success: true };
      } else {
        console.error('❌ Error en respuesta del servidor:', response.error);
        dispatch({
          type: 'LOGIN_ERROR',
          payload: response.error || 'Error en el login'
        });
        return { success: false, error: response.error };
      }
    } catch (error) {
      console.error('❌ Error durante login:', error);
      const errorMessage = error.response?.data?.error || error.message || 'Error de conexión';
      dispatch({
        type: 'LOGIN_ERROR',
        payload: errorMessage
      });
      return { success: false, error: errorMessage };
    }
  };

  // Función de logout
  const logout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Error durante logout:', error);
    } finally {
      // Limpiar localStorage
      localStorage.removeItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY);
      localStorage.removeItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY);
      localStorage.removeItem(TOKEN_CONFIG.USER_KEY);
      dispatch({ type: 'LOGOUT' });
    }
  };

  // Actualizar información del usuario
  const updateUser = (userData) => {
    const updatedUser = { ...state.user, ...userData };
    localStorage.setItem(TOKEN_CONFIG.USER_KEY, JSON.stringify(updatedUser));
    dispatch({ type: 'UPDATE_USER', payload: userData });
  };

  // Verificar si el usuario tiene un rol específico
  const hasRole = (role) => {
    return state.user?.roles?.includes(role) || false;
  };

  // Verificar si el usuario puede ver todos los clientes
  const canViewAllClients = () => {
    return state.user?.puedeVerTodosClientes === 1;
  };

  const value = {
    // Estado
    ...state,
    
    // Funciones
    login,
    logout,
    updateUser,
    
    // Helpers
    hasRole,
    canViewAllClients,
    isAuthenticated: state.status === APP_STATES.AUTHENTICATED,
    isLoading: state.status === APP_STATES.LOADING
  };

  // Debug logs
  console.log('🔍 AuthContext Estado:', {
    status: state.status,
    isAuthenticated: state.status === APP_STATES.AUTHENTICATED,
    isLoading: state.status === APP_STATES.LOADING,
    hasUser: !!state.user,
    userName: state.user?.usuario
  });

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook personalizado para usar el contexto
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de AuthProvider');
  }
  return context;
};

export default AuthContext;