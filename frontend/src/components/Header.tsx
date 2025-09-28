import { Link } from "react-router-dom";
import { useEffect, useState } from "react";

export default function Header() {
  const [scrolled, setScrolled] = useState(false);
  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 8);
    onScroll();
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
    <header
      className={[
        "fixed top-0 inset-x-0 z-50 h-[72px] border-b",
        scrolled ? "bg-white/95 shadow-sm" : "bg-white/90", //if scrolled is false, the bg is a bit transparent
        "backdrop-blur",
      ].join(" ")}
    >
      <div className="max-w-8xl mx-auto h-20 px-9 flex items-center justify-between" >
        {/* clickable sq shape to homepage via logo */}
        <a
          href="/"
          className="flex items-center gap-3 font-extrabold tracking-wide text-slate-900"
        >
          <span className="w-4 h-4 rounded-md bg-gradient-to-b from-blue-700 to-blue-500 shadow-[0_0_18px_rgba(59,130,246,.6)]" />
          TARIFF
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
          <Link
            to="/login"
            className="px-3 py-2 rounded-lg border border-slate-400 text-sm text-slate-700 hover:bg-slate-100"
        >
            Log in
          </Link>
          <Link
            to="signup"
            className="px-4 py-2 rounded-lg bg-gradient-to-b from-blue-600 to-blue-700 text-white text-sm font-semibold shadow-[0_8px_18px_rgba(59,130,246,.35)]"
          >
            Sign up
          </Link>
          {/* <div className="flex items-center gap-3">
          <button
            onClick={handleLogout}
            className="px-5 py-2.5 rounded-lg bg-gradient-to-b from-red-600 to-red-700 text-white text-sm font-semibold shadow-[0_8px_18px_rgba(239,68,68,.35)] hover:from-red-700 hover:to-red-800 transition-colors"
          >
            Logout
          </button>
        </div> */}
        </div>
      </div>
    </header>
  );
}
