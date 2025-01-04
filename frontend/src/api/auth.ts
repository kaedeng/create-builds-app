import axiosInstance from './axiosInstance';

export const loginUser = async (id: string): Promise<string> => {
  try {
    const response = await axiosInstance.post('/api/login', id);
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
