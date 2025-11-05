import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

export default function AuthCallback() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState('Processing...');

  useEffect(() => {
    console.log('AuthCallback: Component mounted');
    console.log('AuthCallback: Current URL:', window.location.href);
    
    const token = searchParams.get('token');
    const refreshToken = searchParams.get('refreshToken');
    const email = searchParams.get('email');

    console.log('AuthCallback: Extracted params:', { 
      token: token ? `${token.substring(0, 20)}...` : null, 
      refreshToken: refreshToken ? `${refreshToken.substring(0, 20)}...` : null, 
      email 
    });

    if (token && refreshToken) {
      console.log('AuthCallback: Storing tokens and redirecting...');
      setStatus('Success! Storing tokens...');
      
      // Store tokens in localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('refreshToken', refreshToken);
      if (email) localStorage.setItem('email', email);

      // Notify other tabs/components about the login
      window.dispatchEvent(new Event('storage'));
      
      // Also dispatch a custom event for same-tab updates
      window.dispatchEvent(new CustomEvent('authStateChanged', { 
        detail: { isLoggedIn: true, email } 
      }));

      setStatus('Redirecting to home page...');
      
      // Small delay to ensure storage event is processed by Header component
      setTimeout(() => {
        console.log('AuthCallback: Navigating to home page');
        navigate('/', { replace: true });
      }, 200);
    } else {
      // Handle login failure
      console.error('Google login failed: No tokens received');
      console.error('Available search params:', Array.from(searchParams.entries()));
      setStatus('Login failed - redirecting...');
      setTimeout(() => {
        navigate('/login?error=oauth_failed', { replace: true });
      }, 2000);
    }
  }, [navigate, searchParams]);

  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center max-w-md">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
        <p className="mt-4 text-gray-600">{status}</p>
        <p className="mt-2 text-sm text-gray-400">
          Current URL: {window.location.href}
        </p>
        
        {/* Manual navigation button as fallback */}
        <button 
          onClick={() => {
            console.log('Manual navigation button clicked');
            navigate('/', { replace: true });
          }}
          className="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
        >
          Go to Home Page
        </button>
        
        <div className="mt-4 text-xs text-gray-500">
          <p>Debug info:</p>
          <p>Has token: {localStorage.getItem('token') ? 'Yes' : 'No'}</p>
          <p>Has refresh token: {localStorage.getItem('refreshToken') ? 'Yes' : 'No'}</p>
        </div>
      </div>
    </div>
  );
}