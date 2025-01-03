import axiosInstance from './axiosInstance';

export const loginUser = async (): Promise<string> => {
  try {
    const response = await axiosInstance.get('/login');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const logoutUser = async (): Promise<string> => {
  try {
    const response = await axiosInstance.post('/logout');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
