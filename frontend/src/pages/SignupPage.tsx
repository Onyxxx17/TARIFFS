import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { BASE_URL } from "../config";

export default function SignupPage() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const role = "USER"; // default role
  const navigate = useNavigate();

  // Password constraints
  function validatePassword(pw: string): string | null {
    if (pw.length < 8) return "Password must be at least 8 characters.";
    if (!/[A-Z]/.test(pw)) return "Password must contain at least one uppercase letter.";
    if (!/[a-z]/.test(pw)) return "Password must contain at least one lowercase letter.";
    if (!/[0-9]/.test(pw)) return "Password must contain at least one number.";
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(pw)) return "Password must contain at least one special character.";
    return null;
  }

  async function handleSignup(e: React.FormEvent) {
    e.preventDefault();
    setError("");

    const pwError = validatePassword(password);
    if (pwError) {
      setError(pwError);
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(BASE_URL + "/api/users/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password, role }),
      });

      const body = await response.json().catch(() => null);

      if (!response.ok) {
        setError(body?.message || body?.error || "Signup failed");
        return;
      }

      navigate("/");
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  const handleGoogleSignup = () => {
    // Implement Google OAuth here
    console.log("Google signup clicked");
  };

  return (
    <div
      className="
          min-h-screen flex items-center justify-center px-4
          bg-[radial-gradient(90%_70%_at_50%_0%,#EBF2FF_0%,#F6F8FF_45%,#FFFFFF_100%)]
        "
    >
      <div className="w-full max-w-md">
        <div
          className="
              rounded-3xl bg-white/95 backdrop-blur
              border border-slate-100
              shadow-[0_20px_60px_rgba(30,58,138,0.10)]
              px-8 py-9 sm:px-10 sm:py-10
            "
        >
          {/* Title */}
          <h1 className="text-[26px] font-semibold tracking-tight text-slate-900">
            Create account
          </h1>
          <p className="mt-1 text-sm text-slate-500">Join us to get started</p>

          <button
            type="button"
            onClick={handleGoogleSignup}
            className="
    mt-6 w-full rounded-xl border border-slate-200 bg-white
    px-4 py-3 text-slate-800 font-medium
    hover:bg-slate-50 hover:border-slate-300 transition
    flex items-center justify-center gap-3
  "
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
            Continue with Google
          </button>

          {/* Divider */}
          <div className="my-6 flex items-center gap-3">
            <div className="h-px flex-1 bg-slate-200" />
            <span className="text-xs text-slate-500">or</span>
            <div className="h-px flex-1 bg-slate-200" />
          </div>

          {/* Form */}
          <form onSubmit={handleSignup} className="space-y-5">
            <div>
              <label
                htmlFor="username"
                className="block text-xs font-medium text-slate-700 mb-2"
              >
                Username
              </label>
              <input
                id="username"
                type="text"
                placeholder="johndoe"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                className="
                    w-full rounded-xl
                    border border-slate-200 bg-white
                    px-4 py-3 text-slate-900 placeholder-slate-400
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition
                  "
              />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-xs font-medium text-slate-700 mb-2"
              >
                Email
              </label>
              <input
                id="email"
                type="email"
                placeholder="you@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="
                    w-full rounded-xl
                    border border-slate-200 bg-white
                    px-4 py-3 text-slate-900 placeholder-slate-400
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition
                  "
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-xs font-medium text-slate-700 mb-2"
              >
                Password
              </label>
              <input
                id="password"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="
                    w-full rounded-xl
                    border border-slate-200 bg-white
                    px-4 py-3 text-slate-900 placeholder-slate-400
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition
                  "
              />
              <p className="mt-1 text-xs text-slate-500">
                Password must be at least 8 characters, include uppercase, lowercase, number, and special character.
              </p>
            </div>

            <div>
              <label
                htmlFor="confirmPassword"
                className="block text-xs font-medium text-slate-700 mb-2"
              >
                Confirm password
              </label>
              <input
                id="confirmPassword"
                type="password"
                placeholder="••••••••"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                className="
                    w-full rounded-xl
                    border border-slate-200 bg-white
                    px-4 py-3 text-slate-900 placeholder-slate-400
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition
                  "
              />
            </div>

            {/* Error */}
            {error && (
              <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3">
                <p className="text-red-600 text-sm font-medium">{error}</p>
              </div>
            )}

            {/* Primary */}
            <button
              type="submit"
              disabled={loading}
              className={`
                  w-full rounded-xl px-4 py-3.5 text-white font-semibold
                  transition-all
                  ${
                    loading
                      ? "bg-slate-400 cursor-not-allowed"
                      : "bg-[#1E40AF] hover:bg-[#1C3A9E] shadow-[0_10px_30px_rgba(30,64,175,0.35)] hover:shadow-[0_14px_36px_rgba(30,64,175,0.45)]"
                  }
                `}
            >
              {loading ? "Creating account..." : "Sign up"}
            </button>
          </form>

          {/* Footer */}
          <p className="mt-6 text-center text-sm text-slate-600">
            Already have an account?{" "}
            <a
              href="/login"
              className="font-semibold text-[#1E40AF] hover:underline"
            >
              Sign in
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
