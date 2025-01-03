import axiosInstance from './axiosInstance';

export const loginUser = (): void => {
  window.location.href =
    'https://api.createbuildsmc.com/oauth2/authorization/google';
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
