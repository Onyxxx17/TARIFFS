import { useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";

export default function LoginPage() {
  const [usernameOrEmail, setUsernameOrEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  const navigate = useNavigate();
  const location = useLocation();

  // Pre-fill form if coming from signup
  useEffect(() => {
    if (location.state) {
      const { username, email, password: userPassword, message } = location.state;
      console.log(username)
      if (username) setUsernameOrEmail(username);
      if (email) setUsernameOrEmail(email);
      if (userPassword) setPassword(userPassword);
      if (message) setSuccessMessage(message);
      // console.log(location.state)
      
      // Clear the state to prevent issues on refresh
      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccessMessage("");

    try {
      // Determine if input is email or username
      const isEmail = usernameOrEmail.includes("@");
      console.log(usernameOrEmail)
     
      
      let response;
      
      if (isEmail) {
        // Try login with email
        response = await fetch("http://localhost:8080/api/users/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email: usernameOrEmail, password }),
        });
      } else {
        // Try login with username - send as both email and identifier
        response = await fetch("http://localhost:8080/api/users/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email: usernameOrEmail, password }),
        });
        
        // If backend doesn't support identifier field, try with email field
        if (!response.ok && response.status === 400) {
          response = await fetch("http://localhost:8080/api/users/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: usernameOrEmail, password }),
          });
        }
      }

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error("Invalid username/email or password");
        } else {
          throw new Error("Login failed. Please try again.");
        }
      }

      const data = await response.json();
      localStorage.setItem("token", data.token);

      navigate("/");
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div
      style={{
        maxWidth: "400px",
        margin: "2rem auto",
        padding: "2rem",
        border: "1px solid #ccc",
        borderRadius: "8px",
        boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
        backgroundColor: "#f9f9f9",
      }}
    >
      <h2 style={{ textAlign: "center", marginBottom: "1.5rem" }}>Login</h2>
      
      {successMessage && (
        <div style={{
          backgroundColor: "#d4edda",
          color: "#155724",
          padding: "0.75rem",
          borderRadius: "4px",
          marginBottom: "1rem",
          textAlign: "center",
          border: "1px solid #c3e6cb"
        }}>
          {successMessage}
        </div>
      )}

      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username or Email"
          value={usernameOrEmail}
          onChange={(e) => setUsernameOrEmail(e.target.value)}
          required
          style={{
            display: "block",
            marginBottom: "1rem",
            width: "100%",
            padding: "0.75rem",
            border: "1px solid #ccc",
            borderRadius: "4px",
            fontSize: "1rem",
            boxSizing: "border-box",
          }}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={{
            display: "block",
            marginBottom: "1.5rem",
            width: "100%",
            padding: "0.75rem",
            border: "1px solid #ccc",
            borderRadius: "4px",
            fontSize: "1rem",
            boxSizing: "border-box",
          }}
        />
        <button
          type="submit"
          disabled={loading}
          style={{
            width: "100%",
            padding: "0.75rem",
            backgroundColor: "#007bff",
            color: "#fff",
            fontSize: "1.1rem",
            fontWeight: "bold",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
            transition: "background-color 0.3s",
          }}
          onMouseEnter={(e) =>
            (e.currentTarget.style.backgroundColor = "#0056b3")
          }
          onMouseLeave={(e) =>
            (e.currentTarget.style.backgroundColor = "#007bff")
          }
        >
          {loading ? "Logging in..." : "Login"}
        </button>
      </form>
      {error && (
        <p style={{ color: "red", marginTop: "1rem", textAlign: "center" }}>
          {error}
        </p>
      )}
      
      <div style={{ 
        textAlign: "center", 
        marginTop: "2rem", 
        paddingTop: "1.5rem", 
        borderTop: "1px solid #ddd" 
      }}>
        <p style={{ 
          margin: "0", 
          color: "#666", 
          fontSize: "0.9rem" 
        }}>
          Don't have an account?{" "}
          <Link 
            to="/signup" 
            style={{ 
              color: "#007bff", 
              textDecoration: "none", 
              fontWeight: "600" 
            }}
            onMouseEnter={(e) => e.currentTarget.style.textDecoration = "underline"}
            onMouseLeave={(e) => e.currentTarget.style.textDecoration = "none"}
          >
            Sign up here
          </Link>
        </p>
      </div>
    </div>
  );
}