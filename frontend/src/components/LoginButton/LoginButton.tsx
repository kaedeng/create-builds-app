import { useEffect } from 'react';
import { loginUser } from '../../api/auth';

declare const google: any;

const clientId =
  '918801476528-js45th9lis27pqa9hu34v3trqp9i50sr.apps.googleusercontent.com';

const LoginButton = () => {
  const handleCallbackResponse = async (response: any) => {
    const idToken: string = response.credential;
    console.log(idToken);
    loginUser(idToken);
  };

  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.id = 'google-script';
    script.async = true;
    script.defer = true;
    script.onload = () => {
      if (google) {
        google.accounts.id.initialize({
          client_id: clientId,
          callback: handleCallbackResponse,
        });

        google.accounts.id.renderButton(document.getElementById('signInDiv'), {
          theme: 'outline',
          size: 'large',
        });
      }
    };
    document.body.appendChild(script);
  }, []);

  return <div id="signInDiv"></div>;
};

export default LoginButton;
