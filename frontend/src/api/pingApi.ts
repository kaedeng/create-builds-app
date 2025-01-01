import axios from 'axios';

const PingApi = async () => {
  try {
    const response = await axios.get(
      'https://cba-backend-lb-1990243648.us-west-2.elb.amazonaws.com/api/profile/ping'
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export default PingApi;
