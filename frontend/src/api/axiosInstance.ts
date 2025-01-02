import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'https://api.createbuildsmc.com/api',
  timeout: 5000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      window.location.href =
        'https://api.createbuildsmc.com/oauth2/authorization/google';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
