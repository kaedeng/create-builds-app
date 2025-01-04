import axios from 'axios';

const PingApi = async () => {
  try {
    const response = await axios.get(
      'cba-lb-1126998897.us-west-2.elb.amazonaws.com/api/health/ping',
      { withCredentials: true }
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export default PingApi;
