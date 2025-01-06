import axiosInstance from './axiosInstance';

type User = {
  username: string;
};

export const getUser = async (): Promise<string> => {
  try {
    const response = await axiosInstance.get('/profile');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const putUser = async (user: User): Promise<User> => {
  try {
    const response = await axiosInstance.put('/profile', user);
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
