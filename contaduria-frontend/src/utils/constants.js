// Configuración de la API
export const API_CONFIG = {
  BASE_URL: 'http://localhost:8081',
  ENDPOINTS: {
    LOGIN: '/api/auth/login',
    PROFILE: '/api/auth/perfil',
    REFRESH: '/api/auth/renovar',
    LOGOUT: '/api/auth/logout',
    CLIENTES: '/api/clientes',
    EMPRESAS: '/api/empresas',
    DOCUMENTOS: '/api/documentos',
    REPORTES: '/api/reportes',
    FACTURAS: '/api/facturas',
    BALANCES: '/api/balances'
  }
};

// Configuración de tokens
export const TOKEN_CONFIG = {
  ACCESS_TOKEN_KEY: 'accessToken',
  REFRESH_TOKEN_KEY: 'refreshToken',
  USER_KEY: 'user'
};

// Roles del sistema
export const ROLES = {
  ADMIN: 'Administrador',
  EMPLEADO: 'Empleado',
  CONTADOR: 'Contador'
};

// Estados de la aplicación
export const APP_STATES = {
  LOADING: 'loading',
  AUTHENTICATED: 'authenticated',
  UNAUTHENTICATED: 'unauthenticated',
  ERROR: 'error'
};