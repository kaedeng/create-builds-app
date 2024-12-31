import axios from 'axios';
import axiosInstance from './axiosInstance';

type Comment = {
  id: number;
  build_id: number;
  user_id: number;
  content: string;
};

export const getComments = async (build_id: number): Promise<Comment[]> => {
  const response = await axiosInstance.get(`/builds/${build_id}/comments`);
  return response.data;
};

export const postComment = async (
  build_id: number,
  content: string
): Promise<Comment> => {
  const response = await axiosInstance.post(
    `/builds/${build_id}/comments`,
    content
  );
  return response.data;
};

export const putComment = async (
  id: number,
  build_id: number,
  content: string
): Promise<Comment> => {
  const response = await axiosInstance.put(
    `/builds/${build_id}/comments/${id}`,
    content
  );
  return response.data;
};

export const deleteComment = async (
  id: number,
  build_id: number
): Promise<string> => {
  const response = await axiosInstance.delete(
    `/builds/${build_id}/comments/${id}`
  );
  return response.data;
};
