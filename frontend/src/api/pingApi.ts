import axios from 'axios';

const PingApi = async () => {
  try {
    const response = await axios.get(
      'https://ec2-35-163-207-8.us-west-2.compute.amazonaws.com/api/health/ping',
      { withCredentials: true }
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export default PingApi;
