import axios from 'axios';
import axiosInstance from './axiosInstance';

export const postUpvote = async (build_id: number): Promise<string> => {
  const response = await axiosInstance.post(`/builds/${build_id}/upvote`);
  return response.data;
};

export const deleteUpvote = async (build_id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/builds/${build_id}/upvote`);
  return response.data;
};
