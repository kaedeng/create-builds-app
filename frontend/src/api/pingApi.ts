import axios from 'axios';

const PingApi = async () => {
  try {
    const response = await axios.get(
      'https://api.createbuildsmc.com/api/health/ping',
      { withCredentials: true }
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export default PingApi;
