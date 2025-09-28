import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

export default function Header() {
  const [scrolled, setScrolled] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 8);
    onScroll();
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  const handleLogout = () => {
    const confirmLogout = window.confirm("Are you sure you want to log out?");
    if (confirmLogout) {
      // Add any logout logic here (clear tokens, etc.)
      navigate("/login");
    }
    // If they click "No", nothing happens and they stay on the current page
  };

  return (
    <header
      className={[
        "fixed top-0 inset-x-0 z-50 h-[88px] border-b",
        scrolled ? "bg-white/95 shadow-sm" : "bg-white/90",
        "backdrop-blur",
      ].join(" ")}
    >
      <div className="max-w-8xl mx-auto h-full px-9 flex items-center justify-between">
        {/* clickable sq shape to homepage via logo */}
        <a
          href="/"
          className="flex items-center gap-3 font-extrabold tracking-wide text-slate-900"
        >
          <span className="w-5 h-5 rounded-md bg-gradient-to-b from-blue-700 to-blue-500 shadow-[0_0_18px_rgba(59,130,246,.6)]" />
          TARIFFIC
        </a>

        <nav className="hidden md:flex items-center gap-8 text-slate-600">
          <a className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors" href="/">
            Home
          </a>
          <a className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors" href="/dashboard">
            Dashboard
          </a>
          <a className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors" href="/features">
            Features
          </a>
          <a className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors" href="/blog">
            Blog
          </a>
          <a className="hover:text-slate-900 py-2 px-3 rounded-md transition-colors" href="/contact">
            Contact
          </a>
        </nav>

        <div className="flex items-center gap-3">
          <button
            onClick={handleLogout}
            className="px-5 py-2.5 rounded-lg bg-gradient-to-b from-red-600 to-red-700 text-white text-sm font-semibold shadow-[0_8px_18px_rgba(239,68,68,.35)] hover:from-red-700 hover:to-red-800 transition-colors"
          >
            Logout
          </button>
        </div>
      </div>
    </header>
  );
}