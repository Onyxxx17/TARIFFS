import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./layouts/Layout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import TariffLoggingDisplay from "./pages/TariffLogging";
import SignupPage from "./pages/SignupPage";
import AuthCallback from "./pages/AuthCallback";

import BlogList from "./pages/BlogList";
import BlogPost from "./pages/BlogPost";

import AnalyticsDashboard from "./pages/AgricultureTariffChart";
import TariffPredictionDisplay from "./pages/TariffPredictionPages";
import ContactPage from "./pages/ContactPage";

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
          <Route path="auth/callback" element={<AuthCallback />} /> {/* OAuth2 Callback */}
          <Route path="logging" element={<TariffLoggingDisplay />} />
          <Route path="/tariff-prediction" element={<TariffPredictionDisplay />} />
          <Route path="/dashboard/analytics" element={<AnalyticsDashboard />} />
          <Route path="/contact" element={<ContactPage />} />
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
