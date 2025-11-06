import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState, useRef } from "react";
import { fetchWithAuth } from "../utils/api";
import { ChevronDown, Moon, Sun } from "lucide-react"; 

export default function Header() {
  const [scrolled, setScrolled] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(
    !!localStorage.getItem("token")
  );
  const [openFeatures, setOpenFeatures] = useState(false);
  const [openUserMenu, setOpenUserMenu] = useState(false);
  const [userName, setUserName] = useState<string>("");
  const [darkMode, setDarkMode] = useState<boolean>(false);
  const userMenuRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();

  // Initialize dark mode from localStorage on mount
  useEffect(() => {
    const saved = localStorage.getItem("darkMode");
    if (saved === "true") {
      setDarkMode(true);
      document.documentElement.classList.add("dark");
    } else {
      // Default to light mode
      setDarkMode(false);
      document.documentElement.classList.remove("dark");
      localStorage.setItem("darkMode", "false");
    }
  }, []);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 8);
    onScroll();
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  // Dark mode effect - runs when darkMode changes
  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add("dark");
      localStorage.setItem("darkMode", "true");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("darkMode", "false");
    }
  }, [darkMode]);

  const toggleDarkMode = () => {
    const html = document.documentElement;
    const newMode = !darkMode;
    
    console.log("Current darkMode state:", darkMode);
    console.log("New darkMode state:", newMode);
    console.log("HTML classes before:", html.className);
    
    if (newMode) {
      html.classList.add("dark");
      localStorage.setItem("darkMode", "true");
    } else {
      html.classList.remove("dark");
      localStorage.setItem("darkMode", "false");
    }
    
    console.log("HTML classes after:", html.className);
    console.log("LocalStorage darkMode:", localStorage.getItem("darkMode"));
    
    setDarkMode(newMode);
  };

  // keep header state in sync when token changes in other tabs/components
  useEffect(() => {
    const onStorage = () => setIsLoggedIn(!!localStorage.getItem("token"));
    const onAuthStateChanged = () => setIsLoggedIn(!!localStorage.getItem("token"));
    
    window.addEventListener("storage", onStorage);
    window.addEventListener("authStateChanged", onAuthStateChanged);
    
    return () => {
      window.removeEventListener("storage", onStorage);
      window.removeEventListener("authStateChanged", onAuthStateChanged);
    };
  }, []);

  // Fetch user info when logged in
  useEffect(() => {
    const fetchUserInfo = async () => {
      if (!isLoggedIn) {
        setUserName("");
        return;
      }

      try {
        // Decode JWT to get user email/name or fetch from API
        const token = localStorage.getItem("token");
        if (token) {
          // Decode JWT payload (base64)
          const payload = JSON.parse(atob(token.split('.')[1]));
          // Use email or sub (subject) from JWT
          const email = payload.sub || payload.email || "";
          // Extract name before @ or use first part of email
          const name = email.split('@')[0] || "User";
          setUserName(name);
        }
      } catch (error) {
        console.error("Failed to decode user info:", error);
        setUserName("User");
      }
    };

    fetchUserInfo();
  }, [isLoggedIn]);

  // Close user menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target as Node)) {
        setOpenUserMenu(false);
      }
    };

    if (openUserMenu) {
      document.addEventListener("mousedown", handleClickOutside);
    }
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [openUserMenu]);

  const handleLogout = async () => {
    // Close it
    setOpenUserMenu(false);
    
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
    setUserName("");

    // notify other tabs/components
    window.dispatchEvent(new Event("storage"));
    navigate("/");
  };

  // Function to get user initials for avatar
  const getInitials = (name: string) => {
    if (!name) return "U";
    const parts = name.split(' ');
    if (parts.length >= 2) {
      return (parts[0][0] + parts[1][0]).toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
  };

  return (
    <header
      className={[
        "fixed top-0 inset-x-0 z-50 h-[72px] border-b transition-colors",
        scrolled 
          ? "bg-white/95 dark:bg-slate-900/95 shadow-sm" 
          : "bg-white/90 dark:bg-slate-900/90",
        "backdrop-blur dark:border-slate-700",
      ].join(" ")}
    >
      <div className="max-w-8xl mx-auto h-20 px-9 flex items-center justify-between">
        {/* logo -> home */}
        <Link
          to="/"
          className="flex items-center gap-3 font-extrabold tracking-wide text-slate-900 dark:text-white transition-colors"
        >
          <span className="w-4 h-4 rounded-md bg-gradient-to-b from-blue-700 to-blue-500 shadow-[0_0_18px_rgba(59,130,246,.6)]" />
          TARIFF
        </Link>

        <nav className="hidden md:flex items-center gap-8 text-slate-600 dark:text-slate-300">
          <Link
            className="hover:text-slate-900 dark:hover:text-white py-2 px-3 rounded-md transition-colors"
            to="/"
          >
            Home
          </Link>
          <Link
            className="hover:text-slate-900 dark:hover:text-white py-2 px-3 rounded-md transition-colors"
            to="/dashboard"
          >
            Dashboard
          </Link>

          {/* Features dropdown */}
          <div
            className="relative"
            onMouseEnter={() => setOpenFeatures(true)}
            onMouseLeave={() => setOpenFeatures(false)}
          >
            <button className="flex items-center gap-1 hover:text-slate-900 dark:hover:text-white py-2 px-3 rounded-md transition-colors">
              Features
              <ChevronDown
                size={16}
                className={`transition-transform ${
                  openFeatures ? "rotate-180" : ""
                }`}
              />
            </button>

            {openFeatures && (
              <div className="absolute top-10 left-0 w-60 bg-white dark:bg-slate-800 border dark:border-slate-700 shadow-lg rounded-lg z-50">
                <Link
                  to="/dashboard/analytics"
                  className="block px-4 py-2 text-slate-700 dark:text-slate-300 hover:bg-blue-50 dark:hover:bg-slate-700 hover:text-blue-600 dark:hover:text-blue-400 rounded-md transition-colors"
                >
                  Analytics Dashboard
                </Link>
                <Link
                  to="/logging"
                  className="block px-4 py-2 text-slate-700 dark:text-slate-300 hover:bg-blue-50 dark:hover:bg-slate-700 hover:text-blue-600 dark:hover:text-blue-400 rounded-md transition-colors"
                >
                  Tariff Logging
                </Link>
                <Link
                  to="/tariff-prediction"
                  className="block px-4 py-2 text-slate-700 dark:text-slate-300 hover:bg-blue-50 dark:hover:bg-slate-700 hover:text-blue-600 dark:hover:text-blue-400 rounded-md transition-colors"
                >
                  Tariff Prediction
                </Link>
              </div>
              
            )}
          </div>

          <Link className="hover:text-slate-900 dark:hover:text-white py-2 px-3 rounded-md transition-colors" to="/blog">
            Blog
          </Link>
          <Link
            className="hover:text-slate-900 dark:hover:text-white py-2 px-3 rounded-md transition-colors"
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
                className="px-3 py-2 rounded-lg border border-slate-400 dark:border-slate-600 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
              >
                Log in
              </Link>
              <Link
                to="/signup"
                className="px-4 py-2 rounded-lg bg-gradient-to-b from-blue-600 to-blue-700 text-white text-sm font-semibold shadow-[0_8px_18px_rgba(59,130,246,.35)] hover:from-blue-700 hover:to-blue-800 transition-all"
              >
                Sign up
              </Link>
            </>
          ) : (
            <div className="relative" ref={userMenuRef}>
              <button
                onClick={() => setOpenUserMenu(!openUserMenu)}
                className="flex items-center gap-2 px-3 py-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
              >
                {/* Avatar - Bigger size */}
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 dark:from-blue-500 dark:to-indigo-500 flex items-center justify-center text-white text-base font-bold shadow-lg ring-2 ring-blue-500/20 dark:ring-blue-400/30">
                  {getInitials(userName)}
                </div>
                <ChevronDown
                  size={16}
                  className={`text-slate-600 dark:text-slate-400 transition-transform ${
                    openUserMenu ? "rotate-180" : ""
                  }`}
                />
              </button>

              {/* Dropdown Menu */}
              {openUserMenu && (
                <div className="absolute right-0 top-12 w-56 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg shadow-xl overflow-hidden z-50">
                  {/* User Info */}
                  <div className="px-4 py-3 border-b border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50">
                    <p className="text-sm font-semibold text-slate-900 dark:text-white truncate">
                      {userName}
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                      Signed in
                    </p>
                  </div>

                  {/* Menu Items */}
                  <div className="py-1">
                    {/* Dark Mode Toggle */}
                    <button
                      onClick={toggleDarkMode}
                      className="w-full text-left px-4 py-2.5 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700/50 transition-colors flex items-center justify-between"
                    >
                      <span className="flex items-center gap-2">
                        {darkMode ? (
                          <>
                            <Moon size={16} className="text-blue-500" />
                            Dark Mode
                          </>
                        ) : (
                          <>
                            <Sun size={16} className="text-amber-500" />
                            Light Mode
                          </>
                        )}
                      </span>
                      <span className="text-xs text-slate-500 dark:text-slate-400">
                        {darkMode ? "On" : "Off"}
                      </span>
                    </button>

                    <div className="border-t border-slate-200 dark:border-slate-700 my-1"></div>

                    <button
                      onClick={handleLogout}
                      className="w-full text-left px-4 py-2.5 text-sm font-medium text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors flex items-center gap-2"
                    >
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                      </svg>
                      Log out
                    </button>
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
