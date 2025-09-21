import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./layouts/Layout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage />} />       {/* Home */}
          <Route path="login" element={<LoginPage />} /> {/* Login */}
          <Route path="signup" element={<SignupPage />} /> {/* Signup */}
        </Route>
      </Routes>
    </Router>
  );
}
