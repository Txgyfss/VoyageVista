import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端API基础URL
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  register(userData) {
    return apiClient.post('/register', userData);
  },
  // 其他API调用
};
