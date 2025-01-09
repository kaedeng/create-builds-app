import axiosInstance from './axiosInstance';

export const postUpvote = async (build_id: number): Promise<string> => {
  try {
    const response = await axiosInstance.post(`/builds/${build_id}/upvote`);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};


export const deleteUpvote = async (build_id: number): Promise<string> => {
  try {
    const response = await axiosInstance.delete(`/builds/${build_id}/upvote`);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
