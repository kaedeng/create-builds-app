import { useState } from 'react';
import { postUpvote, deleteUpvote } from '../../api/upvoteApi';
import {
  getComments,
  postComment,
  putComment,
  deleteComment,
} from '../../api/commentApi';
import { getUser, putUser, deleteUser } from '../../api/userApi';
import {
  getTopBuilds,
  getBuild,
  getUsersBuilds,
  postBuild,
  putBuild,
  deleteBuild,
} from '../../api/buildApi';
import PingApi from '../../api/pingApi';
import LoginButton from '../LoginButton/LoginButton';
import { logoutUser } from '../../api/auth';

export const ApiTestButtons = () => {
  const [buildId, setBuildId] = useState('');
  const [commentId, setCommentId] = useState('');
  const [username, setUsername] = useState('');
  const [content, setContent] = useState('');
  const [buildTitle, setBuildTitle] = useState('');
  const [buildDescription, setBuildDescription] = useState('');
  const [buildImgLinks, setBuildImgLinks] = useState('');
  const [buildNBT, setBuildNBT] = useState('');

  const parseArray = (input: string) =>
    input.split(',').map((item) => item.trim());

  const testPostUpvote = async () => {
    try {
      const result = await postUpvote(Number(buildId));
      console.log('Post Upvote Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testDeleteUpvote = async () => {
    try {
      const result = await deleteUpvote(Number(buildId));
      console.log('Delete Upvote Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testGetComments = async () => {
    try {
      const result = await getComments(Number(buildId));
      console.log('Get Comments Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testPostComment = async () => {
    try {
      const comment = { content };
      const result = await postComment(Number(buildId), comment);
      console.log('Post Comment Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testPutComment = async () => {
    try {
      const comment = { content };
      const result = await putComment(
        Number(commentId),
        Number(buildId),
        comment
      );
      console.log('Put Comment Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testDeleteComment = async () => {
    try {
      const result = await deleteComment(Number(commentId), Number(buildId));
      console.log('Delete Comment Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testGetUser = async () => {
    try {
      const result = await getUser();
      console.log('Get User Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testPutUser = async () => {
    try {
      const User = { username };
      const result = await putUser(User);
      console.log('Put User Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testGetTopBuilds = async () => {
    try {
      const result = await getTopBuilds();
      console.log('Get Top Builds Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testGetBuild = async () => {
    try {
      const result = await getBuild(Number(buildId));
      console.log('Get Build Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testGetUsersBuilds = async () => {
    try {
      const result = await getUsersBuilds();
      console.log('Get User Builds Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testPostBuild = async () => {
    try {
      const newBuild = {
        title: buildTitle,
        description: buildDescription,
        img_links: parseArray(buildImgLinks),
        nbt: buildNBT,
      };
      const result = await postBuild(newBuild);
      console.log('Post Build Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testPutBuild = async () => {
    try {
      const updatedBuild = {
        title: buildTitle,
        description: buildDescription,
        img_links: parseArray(buildImgLinks),
        nbt: buildNBT,
      };
      const result = await putBuild(Number(buildId), updatedBuild);
      console.log('Put Build Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testDeleteBuild = async () => {
    try {
      const result = await deleteBuild(Number(buildId));
      console.log('Delete Build Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testPing = async () => {
    try {
      const result = await PingApi();
      console.log(result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testDeleteUser = async () => {
    try {
      const result = await deleteUser();
      console.log('Delete User Result:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const testLogout = async () => {
    try {
      await logoutUser();
      console.log('Logged Out');
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div>
      <LoginButton />
      <div>
        <input
          type="text"
          placeholder="Build ID"
          value={buildId}
          onChange={(e) => setBuildId(e.target.value)}
        />
        <input
          type="text"
          placeholder="Comment ID"
          value={commentId}
          onChange={(e) => setCommentId(e.target.value)}
        />
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <textarea
          placeholder="Comment Content"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        <input
          type="text"
          placeholder="Build Title"
          value={buildTitle}
          onChange={(e) => setBuildTitle(e.target.value)}
        />
        <textarea
          placeholder="Build Description"
          value={buildDescription}
          onChange={(e) => setBuildDescription(e.target.value)}
        />
        <textarea
          placeholder="Build Image Links (comma-separated)"
          value={buildImgLinks}
          onChange={(e) => setBuildImgLinks(e.target.value)}
        />
        <textarea
          placeholder="Build NBT"
          value={buildNBT}
          onChange={(e) => setBuildNBT(e.target.value)}
        />
      </div>
      <button onClick={testPostUpvote}>Test Post Upvote</button>
      <button onClick={testDeleteUpvote}>Test Delete Upvote</button>
      <button onClick={testGetComments}>Test Get Comments</button>
      <button onClick={testPostComment}>Test Post Comment</button>
      <button onClick={testPutComment}>Test Put Comment</button>
      <button onClick={testDeleteComment}>Test Delete Comment</button>
      <button onClick={testGetUser}>Test Get User</button>
      <button onClick={testPutUser}>Test Put User</button>
      <button onClick={testGetTopBuilds}>Test Get Top Builds</button>
      <button onClick={testGetBuild}>Test Get Build</button>
      <button onClick={testGetUsersBuilds}>Test Get User Builds</button>
      <button onClick={testPostBuild}>Test Post Build</button>
      <button onClick={testPutBuild}>Test Put Build</button>
      <button onClick={testDeleteBuild}>Test Delete Build</button>
      <button onClick={testPing}>Ping</button>
      <button onClick={testDeleteUser}>Delete User</button>
      <button onClick={testLogout}>Logout</button>
    </div>
  );
};
