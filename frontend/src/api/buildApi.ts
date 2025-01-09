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
  newBuild: BuildCreationDetails,
  images: File[],
  nbtFile: File
): Promise<BuildDetails> => {
  try {
    const formData = new FormData();

    formData.append(
      'build',
      new Blob([JSON.stringify(newBuild)], { type: 'application/json' })
    );

    images.forEach((image) => {
      formData.append('images', image);
    });

    formData.append('nbtFile', nbtFile);

    const response = await axiosInstance.post('/builds', formData, {
      headers: {},
    });
    return response.data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

export const putBuild = async (
  id: number,
  updatedBuild: BuildCreationDetails,
  images: File[],
  nbtFile: File
): Promise<BuildDetails> => {
  try {
    const formData = new FormData();

    formData.append(
      'build',
      new Blob([JSON.stringify(updatedBuild)], { type: 'application/json' })
    );

    images.forEach((image) => {
      formData.append('images', image);
    });

    formData.append('nbtFile', nbtFile);

    const response = await axiosInstance.put(`/builds/${id}`, formData, {
      headers: {},
    });
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
