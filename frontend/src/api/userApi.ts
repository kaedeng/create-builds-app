import axiosInstance from './axiosInstance';

export const getUser = async (): Promise<string> => {
  try {
    const response = await axiosInstance.get('/profile');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const putUser = async (username: string): Promise<string> => {
  try {
    const response = await axiosInstance.put('/profile', username);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const deleteUser = async (): Promise<string> => {
  try {
    const response = await axiosInstance.delete('/profile');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
