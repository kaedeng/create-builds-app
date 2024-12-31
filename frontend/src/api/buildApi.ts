import axios from 'axios';
import axiosInstance from './axiosInstance';

type BuildPreview = {
  id: number;
  title: string;
  img_link: string;
  total_upvotes: number;
  hasUpvoted?: boolean;
};

type BuildDetails = {
  id: number;
  //For future profile functionality
  //user_id: number;
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

//Prop add other sorting methods at some point
//Pulls from builds and upvotes
export const getTopBuilds = async (): Promise<BuildPreview[]> => {
  const response = await axiosInstance.get('/homepage-builds');
  return response.data;
};

//Pulls from users, builds, and upvotes
export const getBuild = async (id: number): Promise<BuildDetails> => {
  const response = await axiosInstance.get(`/builds/${id}`);
  return response.data;
};

//Pulls from builds and upvotes
export const getUsersBuilds = async (): Promise<BuildPreview[]> => {
  const response = await axiosInstance.get('/profile/builds');
  return response.data;
};

//Posts to builds, and then returns with info from users, builds, and upvotes
export const postBuild = async (
  newBuild: BuildCreationDetails
): Promise<BuildDetails> => {
  const response = await axiosInstance.post('/builds', newBuild);
  return response.data;
};

//Posts to builds, and then returns with info from users, builds, and upvotes
export const putBuild = async (
  id: number,
  updatedBuild: BuildCreationDetails
): Promise<BuildDetails> => {
  const response = await axiosInstance.put(`/builds/${id}`, updatedBuild);
  return response.data;
};

//Posts to builds and then returns result
export const deleteBuild = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/builds/${id}`);
  return response.data;
};
