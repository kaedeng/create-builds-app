import axiosInstance from './axiosInstance';

export const loginUser = async (idToken: string): Promise<string> => {
  try {
    const response = await axiosInstance.post('/login', idToken);
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
