import api from '../api/axios';
import type { UserDTO, LoginRequest, LoginResponse } from '../types';

export const authService = {
  register: async (user: UserDTO) => {
    const response = await api.post<UserDTO>('/auth/register', user);
    return response.data;
  },
  login: async (loginData: LoginRequest) => {
    const response = await api.post<LoginResponse>('/auth/login', loginData);
    const { token, id, username } = response.data;
    localStorage.setItem('token', token);
    localStorage.setItem('userId', id.toString());
    localStorage.setItem('username', username);
    return response.data;
  },
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  },
  getCurrentUser: () => {
    const userId = localStorage.getItem('userId');
    const username = localStorage.getItem('username');
    return userId ? { id: parseInt(userId), username } : null;
  },
  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  }
};
