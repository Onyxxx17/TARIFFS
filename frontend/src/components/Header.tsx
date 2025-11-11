import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState, useRef } from "react";
import { fetchWithAuth } from "../utils/api";
import { ChevronDown, Menu, X, BarChart2, FileText, TrendingUp } from "lucide-react";


export default function Header() {
  const [scrolled, setScrolled] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(
    !!localStorage.getItem("token")
  );
  const [openFeatures, setOpenFeatures] = useState(false);
  const [openUserMenu, setOpenUserMenu] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const [userName, setUserName] = useState<string>("");
 
  const userMenuRef = useRef<HTMLDivElement>(null);
  const mobileMenuRef = useRef<HTMLDivElement | null>(null);
  const mobileToggleRef = useRef<HTMLButtonElement | null>(null);
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


  // Close mobile menu when clicking outside the menu and the hamburger button
  useEffect(() => {
    if (!mobileOpen) return;
    const onDoc = (e: MouseEvent) => {
      const target = e.target as Node;
      if (
        mobileMenuRef.current &&
        !mobileMenuRef.current.contains(target) &&
        mobileToggleRef.current &&
        !mobileToggleRef.current.contains(target)
      ) {
        setMobileOpen(false);
      }
    };
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, [mobileOpen]);


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
        "fixed top-0 inset-x-0 z-50 h-[64px] sm:h-[72px] border-b transition-colors",
        scrolled
          ? "bg-white/95 shadow-sm"
          : "bg-white/90",
        "backdrop-blur",
      ].join(" ")}
    >
      <div className="max-w-8xl mx-auto h-16 sm:h-20 px-4 sm:px-6 md:px-9 flex items-center justify-between">
        {/* logo -> home */}
        <Link
          to="/"
          className="flex items-center gap-2 sm:gap-3 font-extrabold tracking-wide text-slate-700 transition-colors text-sm sm:text-base"
        >
          <span className="w-3 h-3 sm:w-4 sm:h-4 rounded-md bg-gradient-to-b from-blue-700 to-blue-500 shadow-[0_0_18px_rgba(59,130,246,.6)]" />
          <span className="ml-1">TARIFF</span>
        </Link>


  <nav className="hidden sm:flex items-center gap-1 lg:gap-8 text-slate-600">
          <Link
            className="hover:text-slate-700 py-2 px-2 lg:px-3 rounded-md transition-colors text-xs lg:text-sm"
            to="/"
          >
            Home
          </Link>



          {/* Features dropdown */}
          <div
            className="relative"
            onMouseEnter={() => setOpenFeatures(true)}
            onMouseLeave={() => setOpenFeatures(false)}
          >
            <button className="flex items-center gap-1 hover:text-slate-700 py-2 px-2 lg:px-3 rounded-md transition-colors text-xs lg:text-sm">
              Features
              <ChevronDown
                size={16}
                className={`transition-transform ${
                  openFeatures ? "rotate-180" : ""
                }`}
              />
            </button>

            {openFeatures && (
              <div className="absolute top-full left-0 w-60 bg-white border shadow-lg rounded-lg z-50 mt-0">
                <Link
                  to="/dashboard/analytics"
                  className="block px-4 py-2 text-slate-700 hover:bg-blue-50 hover:text-blue-600 rounded-md transition-colors"
                >
                  Analytics Dashboard
                </Link>
                <Link
                  to="/logging"
                  className="block px-4 py-2 text-slate-700 hover:bg-blue-50 hover:text-blue-600 rounded-md transition-colors"
                >
                  Calcultion History
                </Link>
                <Link
                  to="/tariff-prediction"
                  className="block px-4 py-2 text-slate-700 hover:bg-blue-50 hover:text-blue-600 rounded-md transition-colors"
                >
                  Tariff Prediction
                </Link>
                
              </div>
            )}
          </div>


          <Link className="hover:text-slate-700 py-2 px-2 lg:px-3 rounded-md transition-colors text-xs lg:text-sm" to="/news">
            News
          </Link>
          <Link className="hover:text-slate-700 py-2 px-2 lg:px-3 rounded-md transition-colors text-xs lg:text-sm" to="/blog">
            Blog
          </Link>
          <Link
            className="hover:text-slate-700 py-2 px-2 lg:px-3 rounded-md transition-colors text-xs lg:text-sm"
            to="/contact"
          >
            Contact
          </Link>


        </nav>


        {/* Mobile quick actions + hamburger - visible when nav is hidden */}
        <div className="sm:hidden flex items-center gap-2">
          {/* quick links: analytics, prediction & logging as icons for mobile */}
          <Link to="/dashboard/analytics" className="inline-flex items-center justify-center p-2 rounded-md text-slate-700 hover:bg-slate-100 transition" aria-label="Analytics">
            <BarChart2 size={18} />
          </Link>
          <Link to="/tariff-prediction" className="inline-flex items-center justify-center p-2 rounded-md text-slate-700 hover:bg-slate-100 transition" aria-label="Tariff Prediction">
            <TrendingUp size={16} />
          </Link>
          <Link to="/logging" className="inline-flex items-center justify-center p-2 rounded-md text-slate-700 hover:bg-slate-100 transition" aria-label="Tariff Logging">
            <FileText size={18} />
          </Link>
          {/* quick login button so user doesn't need to open menu */}
          {!isLoggedIn && (
            <Link to="/login" className="inline-flex items-center px-3 py-1 rounded-md text-sm border border-slate-300 text-slate-700 hover:bg-slate-50">
              Log in
            </Link>
          )}
          <div>
            <button
              ref={mobileToggleRef}
              onClick={() => setMobileOpen(!mobileOpen)}
              aria-label="Open menu"
              className="inline-flex items-center justify-center p-2 rounded-md text-slate-700 hover:bg-slate-100 transition"
            >
              {mobileOpen ? <X size={20} /> : <Menu size={20} />}
            </button>
          </div>
        </div>


  <div className="hidden sm:flex items-center gap-2 sm:gap-3">
          {!isLoggedIn ? (
            <>
              <Link
                to="/login"
                className="px-3 py-2 rounded-lg border border-slate-400 text-xs sm:text-sm text-slate-700 hover:bg-slate-100 transition-colors whitespace-nowrap"
              >
                Log in
              </Link>
              <Link
                to="/signup"
                className="px-3 sm:px-4 py-2 rounded-lg bg-gradient-to-b from-blue-600 to-blue-700 text-white text-xs sm:text-sm font-semibold shadow-[0_8px_18px_rgba(59,130,246,.35)] hover:from-blue-700 hover:to-blue-800 transition-all whitespace-nowrap"
              >
                Sign up
              </Link>
            </>
          ) : (
            <div className="relative" ref={userMenuRef}>
              <button
                onClick={() => setOpenUserMenu(!openUserMenu)}
                className="flex items-center gap-2 px-2 sm:px-3 py-2 rounded-lg hover:bg-slate-100 transition-colors"
              >
                {/* Avatar (initials only) */}
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-base font-bold shadow-lg ring-2 ring-blue-500/20">
                  {getInitials(userName)}
                </div>
                <ChevronDown
                  size={16}
                  className={`text-slate-600 transition-transform ${
                    openUserMenu ? "rotate-180" : ""
                  }`}
                />
              </button>


              {/* Dropdown Menu */}
              {openUserMenu && (
                <div className="absolute right-0 top-12 w-56 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg shadow-xl overflow-hidden z-50">
                  {/* User Info */}
                  <div className="px-4 py-3 border-b border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-900">
                    <p className="text-sm font-semibold text-slate-700 dark:text-slate-200 truncate">
                      {userName}
                    </p>
                    <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                      Account Settings
                    </p>
                  </div>

                  {/* Menu Items */}
                  <div className="py-1">
                    <Link
                      to="/dashboard"
                      onClick={() => setOpenUserMenu(false)}
                      className="w-full text-left px-4 py-2.5 text-sm text-slate-700 dark:text-slate-200 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors flex items-center gap-2"
                    >
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
                      </svg>
                      Profile & Settings
                    </Link>

                    <div className="border-t border-slate-200 dark:border-slate-700 mt-1 pt-1">
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
                </div>
              )}
            </div>
          )}
        </div>
        {/* Mobile menu panel */}
        {mobileOpen && (
          <div ref={mobileMenuRef} className="sm:hidden fixed inset-x-0 top-16 bg-white border-t border-slate-200 shadow-md z-50 max-h-[calc(100vh-4rem)] overflow-auto">
            <div className="px-3 py-3 space-y-2">
              {isLoggedIn && (
                <div className="flex items-center gap-3 px-2 py-2 border-b border-slate-100">
                  <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-base font-bold">
                    {getInitials(userName)}
                  </div>
                  <div className="flex-1">
                    <p className="text-sm font-semibold text-slate-800 truncate">{userName}</p>
                  </div>
                </div>
              )}
              <Link onClick={() => setMobileOpen(false)} className="block px-3 py-2 rounded-md text-slate-700 hover:bg-slate-50" to="/">Home</Link>
              <Link onClick={() => setMobileOpen(false)} className="block px-3 py-2 rounded-md text-slate-700 hover:bg-slate-50" to="/news">News</Link>
              <Link onClick={() => setMobileOpen(false)} className="block px-3 py-2 rounded-md text-slate-700 hover:bg-slate-50" to="/blog">Blog</Link>
              <Link onClick={() => setMobileOpen(false)} className="block px-3 py-2 rounded-md text-slate-700 hover:bg-slate-50" to="/contact">Contact</Link>
              {/* auth actions - only show login/signup when not logged in */}
              {!isLoggedIn ? (
                <div className="pt-2 border-t border-slate-100 flex flex-col sm:flex-row items-stretch gap-2">
                  <Link onClick={() => setMobileOpen(false)} to="/login" className="w-full text-center px-3 py-2 rounded-md border border-slate-300 text-sm">Log in</Link>
                  <Link onClick={() => setMobileOpen(false)} to="/signup" className="w-full text-center px-3 py-2 rounded-md bg-blue-600 text-white text-sm">Sign up</Link>
                </div>
              ) : (
                <div className="pt-2 border-t border-slate-100 flex flex-col items-stretch gap-2">
                  <button onClick={() => { setMobileOpen(false); handleLogout(); }} className="w-full text-left px-3 py-2 rounded-md text-red-600 hover:bg-red-50">Log out</button>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </header>
  );
}




