import { logoutUser } from '../../api/auth';

const LogoutButton = () => {
  return (
    <div>
      <button onClick={logoutUser}>Logout</button>
    </div>
  );
};

export default LogoutButton;
