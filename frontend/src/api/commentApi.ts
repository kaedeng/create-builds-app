import axiosInstance from './axiosInstance';

type Comment = {
  id: number;
  build_id: number;
  user_id: number;
  content: string;
};

type CommentCreation = {
  content: string;
};

export const getComments = async (build_id: number): Promise<Comment[]> => {
  try {
    const response = await axiosInstance.get(`/builds/${build_id}/comments`);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const postComment = async (
  build_id: number,
  comment: CommentCreation
): Promise<Comment> => {
  try {
    console.log('Sending payload:', comment);
    const response = await axiosInstance.post(
      `/builds/${build_id}/comments`,
      comment
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const putComment = async (
  id: number,
  build_id: number,
  comment: CommentCreation
): Promise<Comment> => {
  try {
    const response = await axiosInstance.put(
      `/builds/${build_id}/comments/${id}`,
      comment
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const deleteComment = async (
  id: number,
  build_id: number
): Promise<string> => {
  try {
    const response = await axiosInstance.delete(
      `/builds/${build_id}/comments/${id}`
    );
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
