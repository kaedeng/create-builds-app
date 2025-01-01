import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'createbuildsmc.com/api',
  timeout: 5000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default axiosInstance;
