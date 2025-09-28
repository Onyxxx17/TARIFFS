import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./layouts/Layout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";

import BlogList from "./pages/BlogList";
import BlogPost from "./pages/BlogPost";

function NotFound() {
  return <div className="mx-auto max-w-3xl p-6">Page not found.</div>;
}

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage />} />       {/* Home */}
          <Route path="login" element={<LoginPage />} /> {/* Login */}
          <Route path="signup" element={<SignupPage />} /> {/* Signup */}

          {/* Blog list + detail */}
          <Route path="/blog" element={<BlogList />} />
          <Route path="/blog/:slug" element={<BlogPost />} />

          {/* 404 */}
          <Route path="*" element={<NotFound />} />
        </Route>
      </Routes>
    </Router>
  );
}
