import axiosInstance from './axiosInstance';

type BuildDetails = {
  id: number;
  username: string;
  title: string;
  description: string;
  img_links: string[];
  nbt: string;
  total_upvotes: number;
  hasUpvoted?: boolean;
};

type BuildCreationDetails = {
  title: string;
  description: string;
  img_links: string[];
  nbt: string;
};

export const getTopBuilds = async (): Promise<BuildDetails[]> => {
  try {
    const response = await axiosInstance.get('/homepage-builds');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const getBuild = async (id: number): Promise<BuildDetails> => {
  try {
    const response = await axiosInstance.get(`/builds/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const getUsersBuilds = async (): Promise<BuildDetails[]> => {
  try {
    const response = await axiosInstance.get('/profile/builds');
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const postBuild = async (
  newBuild: BuildCreationDetails
): Promise<BuildDetails> => {
  try {
    const response = await axiosInstance.post('/builds', newBuild);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const putBuild = async (
  id: number,
  updatedBuild: BuildCreationDetails
): Promise<BuildDetails> => {
  try {
    const response = await axiosInstance.put(`/builds/${id}`, updatedBuild);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const deleteBuild = async (id: number): Promise<string> => {
  try {
    const response = await axiosInstance.delete(`/builds/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
