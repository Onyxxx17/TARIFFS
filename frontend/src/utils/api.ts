export async function fetchWithAuth(url: string, options: RequestInit = {}) {
  // Add auth header if token exists
  const BASE_URL = import.meta.env.VITE_BASE_APP_URL;
  const token = localStorage.getItem("token");
  const headers = new Headers(options.headers as HeadersInit);
  headers.set("Content-Type", "application/json");

  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  // Make the request
  const response = await fetch(BASE_URL + url, {
    ...options,
    headers,
  });

  // If we get a 401, try to refresh the token
  if (response.status === 401) {
    const refreshToken = localStorage.getItem("refreshToken");
    
    // If no refresh token, we can't refresh
    if (!refreshToken) {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      window.dispatchEvent(new Event("storage"));
      return response;
    }

    // Try to get a new token
    try {
      const refreshResponse = await fetch(BASE_URL + "/api/auth/refresh", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token: refreshToken }),
      });

      if (!refreshResponse.ok) {
        // Refresh failed, clear tokens and require new login
        localStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
        window.dispatchEvent(new Event("storage"));
        return response;
      }

      // Get new tokens
      const data = await refreshResponse.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("refreshToken", data.refreshToken || refreshToken);
      window.dispatchEvent(new Event("storage"));

      // Retry the original request with new token
      return fetchWithAuth(url, options);
    } catch (error) {
      // Network error during refresh, clear tokens
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      window.dispatchEvent(new Event("storage"));
      return response;
    }
  }

  return response;
}