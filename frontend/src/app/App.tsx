import './App.css';
import { useEffect } from 'react';
import LoginButton from '../components/LoginButton/LoginButton';
import { ApiTestButtons } from '../components/ApiTestButton/ApiTestButtons';
import LogoutButton from '../components/LogoutButton/LogoutButton';

function App() {
  return (
    <div className="App">
      <LoginButton />
      <ApiTestButtons />
      <LogoutButton />
    </div>
  );
}

export default App;
