//Signup Page
import React, { useState } from "react";
import { useNavigate,Link } from "react-router-dom";
import { BASE_URL } from "../config";
import GoogleLoginButton from "../components/GoogleLoginButton";

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
    if (!/[A-Z]/.test(pw)) return "Password must contain at least one uppercase letter..";
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
      const response = await fetch(BASE_URL + "/api/auth/signup", {
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

  return (
    <div
      className="
          min-h-screen flex items-center justify-center px-4
          bg-[radial-gradient(90%_70%_at_50%_0%,#EBF2FF_0%,#F6F8FF_45%,#FFFFFF_100%)]
          dark:bg-[radial-gradient(90%_70%_at_50%_0%,#1e293b_0%,#0f172a_45%,#020617_100%)]
          transition-colors
        "
    >
      <div className="w-full max-w-md">
        <div
          className="
              rounded-3xl bg-white/95 dark:bg-slate-800/95 backdrop-blur
              border border-slate-100 dark:border-slate-700
              shadow-[0_20px_60px_rgba(30,58,138,0.10)]
              dark:shadow-[0_20px_60px_rgba(0,0,0,0.3)]
              px-8 py-9 sm:px-10 sm:py-10
              transition-colors
            "
        >
          {/* Title */}
          <h1 className="text-[26px] font-semibold tracking-tight text-slate-900 dark:text-white">
            Create account
          </h1>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Join us to get started</p>

        

          {/* Form */}
          <form onSubmit={handleSignup} className="space-y-5">
            <div>
              <label
                htmlFor="username"
                className="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-2"
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
                    border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700
                    px-4 py-3 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition transition-colors
                  "
              />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-2"
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
                    border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700
                    px-4 py-3 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition transition-colors
                  "
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-2"
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
                    border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700
                    px-4 py-3 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition transition-colors
                  "
              />
              <p className="mt-1 text-xs text-slate-500 dark:text-slate-400">
                Password must be at least 8 characters, include uppercase, lowercase, number, and special character.
              </p>
            </div>

            <div>
              <label
                htmlFor="confirmPassword"
                className="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-2"
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
                    border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700
                    px-4 py-3 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500
                    focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                    transition transition-colors
                  "
              />
            </div>

            {/* Error */}
            {error && (
              <div className="rounded-lg border border-red-200 dark:border-red-800 bg-red-50 dark:bg-red-900/20 px-4 py-3">
                <p className="text-red-600 dark:text-red-400 text-sm font-medium">{error}</p>
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

          {/* Divider */}
          <div className="my-6 flex items-center gap-3">
            <div className="h-px flex-1 bg-slate-200 dark:bg-slate-700" />
            <span className="text-xs text-slate-500 dark:text-slate-400">or</span>
            <div className="h-px flex-1 bg-slate-200 dark:bg-slate-700" />
          </div>

          {/* Google Login */}
          <GoogleLoginButton text="Sign up with Google" />

          {/* Footer */}
          <p className="mt-6 text-center text-sm text-slate-600 dark:text-slate-400">
            Already have an account?{" "}
            <Link
              to="/login"
              className="font-semibold text-[#1E40AF] dark:text-blue-400 hover:underline"
            >
              Log in
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
