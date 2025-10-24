import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { fetchWithAuth } from "../utils/api";
import { ChevronDown } from "lucide-react"; 

export default function Header() {
  const [scrolled, setScrolled] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(
    !!localStorage.getItem("token")
  );
  const [openFeatures, setOpenFeatures] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 8);
    onScroll();
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  // keep header state in sync when token changes in other tabs/components
  useEffect(() => {
    const onStorage = () => setIsLoggedIn(!!localStorage.getItem("token"));
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
  }, []);

  const handleLogout = async () => {
    // Try to call logout API using fetchWithAuth
    try {
      await fetchWithAuth("/api/auth/logout", {
        method: "POST",
      });
    } catch (error) {
      console.error("Logout failed:", error);
    }

    // Clear local storage
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("role");
    setIsLoggedIn(false);

    // notify other tabs/components
    window.dispatchEvent(new Event("storage"));
    navigate("/");
  };

  return (
    <header
      className={[
        "fixed top-0 inset-x-0 z-50 h-[72px] border-b",
        scrolled ? "bg-white/95 shadow-sm" : "bg-white/90",
        "backdrop-blur",
      ].join(" ")}
    >
      <div className="max-w-8xl mx-auto h-20 px-9 flex items-center justify-between">
        {/* logo -> home */}
        <Link
          to="/"
          className="flex items-center gap-3 font-extrabold tracking-wide text-slate-900"
        >
          <span className="w-4 h-4 rounded-md bg-gradient-to-b from-blue-700 to-blue-500 shadow-[0_0_18px_rgba(59,130,246,.6)]" />
          TARIFF
        </Link>

        <nav className="hidden md:flex items-center gap-8 text-slate-600">
          <Link
            className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors"
            to="/"
          >
            Home
          </Link>
          <Link
            className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors"
            to="/dashboard"
          >
            Dashboard
          </Link>

          <Link
            className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors"
            to="/logging"
          >
            Tariff Logging
          </Link>

          {/* Features dropdown (only one for now) */}
          <div
            className="relative"
            onMouseEnter={() => setOpenFeatures(true)}
            onMouseLeave={() => setOpenFeatures(false)}
          >
            <button className="flex items-center gap-1 hover:text-slate-900 py-2 px-3 rounded-md transition-colors">
              Features
              <ChevronDown
                size={16}
                className={`transition-transform ${
                  openFeatures ? "rotate-180" : ""
                }`}
              />
            </button>

            {openFeatures && (
              <div className="absolute top-10 left-0 w-60 bg-white border shadow-lg rounded-lg z-50">
                <Link
                  to="/dashboard/analytics"
                  className="block px-4 py-2 text-slate-700 hover:bg-blue-50 hover:text-blue-600 rounded-md transition-colors"
                >
                  Analytics Dashboard
                </Link>
              </div>
            )}
          </div>

          <Link className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors" to="/blog">
            Blog
          </Link>
          <Link
            className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors"
            to="/contact"
          >
            Contact
          </Link>
        </nav>

        <div className="flex items-center gap-3">
          {!isLoggedIn ? (
            <>
              <Link
                to="/login"
                className="px-3 py-2 rounded-lg border border-slate-400 text-sm text-slate-700 hover:bg-slate-100"
              >
                Log in
              </Link>
              <Link
                to="/signup"
                className="px-4 py-2 rounded-lg bg-gradient-to-b from-blue-600 to-blue-700 text-white text-sm font-semibold shadow-[0_8px_18px_rgba(59,130,246,.35)]"
              >
                Sign up
              </Link>
            </>
          ) : (
            <button
              onClick={handleLogout}
              className="px-5 py-2.5 rounded-lg bg-gradient-to-b from-red-600 to-red-700 text-white text-sm font-semibold shadow-[0_8px_18px_rgba(239,68,68,.35)] hover:from-red-700 hover:to-red-800 transition-colors"
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </header>
  );
}
