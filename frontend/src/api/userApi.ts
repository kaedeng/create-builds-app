import axios from 'axios';
import axiosInstance from './axiosInstance';

export const getUser = async (): Promise<string> => {
  const response = await axiosInstance.get('/profile');
  return response.data;
};

export const putUser = async (username: string): Promise<string> => {
  const response = await axiosInstance.put('/profile', username);
  return response.data;
};
