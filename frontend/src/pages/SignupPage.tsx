import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function SignupPage() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const role = "USER"; // default role
  const navigate = useNavigate();

  async function handleSignup(e: React.FormEvent) {
    e.preventDefault();
    setError("");

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password, role }),
      });

      if (!response.ok) {
        if (response.status === 409) {
          throw new Error("Email or username already exists");
        } else {
          throw new Error("Signup failed. Please try again.");
        }
      }

      // Navigate to login page with user details
      navigate("/login", {
        state: {
          username,
          email,
          password,
          message: "Account created successfully! You can now log in."
        }
      });
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  const inputStyle = {
    display: "block",
    marginBottom: "1rem",
    width: "100%",
    padding: "0.75rem",
    border: "1px solid #ccc",
    borderRadius: "4px",
    fontSize: "1rem",
    boxSizing: "border-box",
  };

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
      <h2 style={{ textAlign: "center", marginBottom: "1.5rem" }}>Sign Up</h2>
      <form onSubmit={handleSignup}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          style={inputStyle}
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={inputStyle}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={inputStyle}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
          style={inputStyle}
        />
        <button
          type="submit"
          disabled={loading}
          style={{
            width: "100%",
            padding: "0.75rem",
            backgroundColor: "#28a745",
            color: "#fff",
            fontSize: "1.1rem",
            fontWeight: "bold",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
            transition: "background-color 0.3s",
          }}
          onMouseEnter={(e) =>
            (e.currentTarget.style.backgroundColor = "#218838")
          }
          onMouseLeave={(e) =>
            (e.currentTarget.style.backgroundColor = "#28a745")
          }
        >
          {loading ? "Signing up..." : "Sign Up"}
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
          Already have an account?{" "}
          <Link 
            to="/login" 
            style={{ 
              color: "#007bff", 
              textDecoration: "none", 
              fontWeight: "600" 
            }}
            onMouseEnter={(e) => e.currentTarget.style.textDecoration = "underline"}
            onMouseLeave={(e) => e.currentTarget.style.textDecoration = "none"}
          >
            Login here
          </Link>
        </p>
      </div>
    </div>
  );
}