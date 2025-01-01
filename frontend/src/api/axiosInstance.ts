import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'cba-backend-lb-1990243648.us-west-2.elb.amazonaws.com/api',
  timeout: 5000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default axiosInstance;
