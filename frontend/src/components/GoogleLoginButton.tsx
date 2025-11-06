interface GoogleLoginButtonProps {
  text?: string;
  className?: string;
}

export default function GoogleLoginButton({ 
  text = "Continue with Google",
  className = ""
}: GoogleLoginButtonProps) {
  
  const handleGoogleLogin = () => {
    // Use the backend URL for OAuth2 authorization
    window.location.href = `${import.meta.env.VITE_BACKEND_URL}/oauth2/authorization/google`;
  };

  return (
    <button
      type="button"
      onClick={handleGoogleLogin}
      className={`
        w-full rounded-xl border border-slate-200 bg-white
        px-4 py-3 text-slate-800 font-medium
        hover:bg-slate-50 hover:border-slate-300 transition
        flex items-center justify-center gap-3
        ${className}
      `}
    >
      {/* Google icon */}
      <svg
        width="18"
        height="18"
        viewBox="0 0 533.5 544.3"
        aria-hidden="true"
      >
        <path
          fill="#4285F4"
          d="M533.5 278.4c0-18.6-1.7-37-5.2-54.8H272v103.8h147.2c-6.3 34.6-25.3 64-54 83.8v69.4h87.1c51-47 81.2-116.2 81.2-202.2z"
        />
        <path
          fill="#34A853"
          d="M272 544.3c73.4 0 135-24.3 180-66.1l-87.1-69.4c-24.2 16.2-55.2 25.8-92.9 25.8-71.4 0-132-48.2-153.6-112.9H29.4v70.8c44.6 88.6 135.8 151.8 242.6 151.8z"
        />
        <path
          fill="#FBBC05"
          d="M118.4 321.7c-10.3-30.6-10.3-63.5 0-94.1V156.8H29.4c-38.8 77.3-38.8 169.5 0 246.8l89-81.9z"
        />
        <path
          fill="#EA4335"
          d="M272 107.7c39.9-.7 78.2 14 107.4 41.3l79.9-79.9C406.8 17.7 341.8-.2 272 0 165.2 0 74 63.1 29.4 151.8l89 70.8C140.1 158 200.6 109.8 272 109.8z"
        />
      </svg>
      {text}
    </button>
  );
}