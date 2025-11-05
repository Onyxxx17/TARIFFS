import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import GoogleLoginButton from "../components/GoogleLoginButton";
export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string>("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      // For login, you can still use regular fetch since the user isn't authenticated yet
      const response = await fetch(
        import.meta.env.VITE_BASE_APP_URL + "/api/auth/login",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, password }),
        }
      );

      const data = await response.json().catch(() => ({}));

      if (!response.ok) {
        setError(data.message || data.error || "Login failed");
        return;
      }

      if (data.token) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("refreshToken", data.refreshToken);
        if (data.role) localStorage.setItem("role", data.role);

        // notify other tabs/components
        window.dispatchEvent(new Event("storage"));
        navigate("/");
      } else {
        setError("Invalid server response");
      }
    } catch (err) {
      setError("Network error");
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
      {/* CENTERED CARD WRAPPER */}
      <div className="w-full max-w-md">
        {/* CARD */}
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
          <h1 className="text-[26px] flex flex-col items-center justify-center font-semibold tracking-tight text-slate-900 dark:text-white">
            Sign in
          </h1>
          <p className="mt-3 text-sm mb-1 text-slate-500 dark:text-slate-400">Welcome back</p>

          {/* Form Fields */}
          <form onSubmit={handleLogin} className="space-y-5">
            <div>
              <label
                htmlFor="email"
                className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-2"
              >
                Email Address
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
                transition-colors
                focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                transition
              "
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-semibold mb-2 text-slate-700 dark:text-slate-300"
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
                transition-colors
                focus:outline-none focus:ring-4 focus:ring-[#2563EB]/15 focus:border-[#2563EB]
                transition
              "
              />
            </div>

            {/* Error Message */}
            {error && (
              <div className="rounded-xl border border-red-200 dark:border-red-800 bg-red-50 dark:bg-red-900/20 px-4 py-3">
                <p className="text-red-600 dark:text-red-400 text-sm font-medium">{error}</p>
              </div>
            )}

            {/* Submit Button */}
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
              {loading ? "Signing in..." : "Sign In"}
            </button>
          </form>

          {/* Divider */}
          <div className="my-6 flex items-center gap-3">
            <div className="h-px flex-1 bg-slate-200 dark:bg-slate-700" />
            <span className="text-xs text-slate-500 dark:text-slate-400">or</span>
            <div className="h-px flex-1 bg-slate-200 dark:bg-slate-700" />
          </div>

          {/* Google Login */}
          <GoogleLoginButton />

          {/* Sign Up Link */}
          <p className="text-center mt-6 text-sm text-slate-600 dark:text-slate-400">
            Don't have an account?{" "}
            <Link
              to="/signup"
              className="font-semibold text-[#1E40AF] dark:text-blue-400 hover:underline"
            >
              Sign up
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
