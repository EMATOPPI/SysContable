import axios from 'axios';
import { API_CONFIG, TOKEN_CONFIG } from '../utils/constants';

// Crear instancia de axios
const api = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para requests - agregar token automáticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    console.log('🚀 Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error('❌ Request Error:', error);
    return Promise.reject(error);
  }
);

// Interceptor para responses - manejar errores y tokens
api.interceptors.response.use(
  (response) => {
    console.log('✅ Response:', response.status, response.config.url);
    return response;
  },
  async (error) => {
    console.error('❌ Response Error:', error.response?.status, error.config?.url);
    
    // Si el token expiró (401), intentar renovar
    if (error.response?.status === 401) {
      const refreshToken = localStorage.getItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY);
      
      if (refreshToken) {
        try {
          const response = await axios.post(
            `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.REFRESH}`,
            { refreshToken }
          );
          
          if (response.data.exito) {
            const newToken = response.data.datos.accessToken;
            localStorage.setItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY, newToken);
            
            // Reintentar request original con nuevo token
            error.config.headers.Authorization = `Bearer ${newToken}`;
            return api.request(error.config);
          }
        } catch (refreshError) {
          console.error('❌ Error renovando token:', refreshError);
          // Si no se puede renovar, limpiar localStorage y redirigir
          localStorage.removeItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY);
          localStorage.removeItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY);
          localStorage.removeItem(TOKEN_CONFIG.USER_KEY);
          window.location.href = '/login';
        }
      }
    }
    
    return Promise.reject(error);
  }
);

// Servicios de autenticación
export const authService = {
  login: async (credentials) => {
    const response = await api.post(API_CONFIG.ENDPOINTS.LOGIN, credentials);
    return response.data;
  },
  
  getProfile: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.PROFILE);
    return response.data;
  },
  
  logout: async () => {
    try {
      await api.post(API_CONFIG.ENDPOINTS.LOGOUT);
    } catch (error) {
      console.warn('Error en logout:', error);
    } finally {
      // Limpiar localStorage siempre
      localStorage.removeItem(TOKEN_CONFIG.ACCESS_TOKEN_KEY);
      localStorage.removeItem(TOKEN_CONFIG.REFRESH_TOKEN_KEY);
      localStorage.removeItem(TOKEN_CONFIG.USER_KEY);
    }
  }
};

// Servicios de contaduría
export const contaduriaService = {
  getClientes: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.CLIENTES);
    return response.data;
  },
  
  getEmpresas: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.EMPRESAS);
    return response.data;
  },
  
  getDocumentos: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.DOCUMENTOS);
    return response.data;
  },
  
  getReportes: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.REPORTES);
    return response.data;
  },
  
  getFacturas: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.FACTURAS);
    return response.data;
  },
  
  getBalances: async () => {
    const response = await api.get(API_CONFIG.ENDPOINTS.BALANCES);
    return response.data;
  },
  
  // Crear nueva factura
  createFactura: async (facturaData) => {
    const response = await api.post(API_CONFIG.ENDPOINTS.FACTURAS, facturaData);
    return response.data;
  }
};

export default api;