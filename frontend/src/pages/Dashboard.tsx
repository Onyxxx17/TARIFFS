import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { fetchWithAuth } from "../utils/api"; // Assuming you have this utility for authenticated requests

interface User {
  id: number;
  username: string;
  email: string;
  role: string;
}

export default function Dashboard() {
  const [users, setUsers] = useState<User[]>([]);
  const [currentUserEmail, setCurrentUserEmail] = useState<string | null>(null);
  const [currentUserUsername, setCurrentUserUsername] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [userRole, setUserRole] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    let role: string | null = null;
    let email: string | null = null;
    let username: string | null = null;

    if (token) {
      try {
        // Parse the JWT to get user details
        const payload = JSON.parse(atob(token.split('.')[1]));
        email = payload.sub; // The 'sub' (subject) claim holds the email
        role = payload.role;
        
        // Derive username from email as a fallback
        username = email ? email.split('@')[0] : null;

      } catch (e) {
        console.error("Failed to parse JWT:", e);
        // Fallback to localStorage if token parsing fails
        role = localStorage.getItem("role");
        email = localStorage.getItem("email");
        username = localStorage.getItem("username");
      }
    }

    setUserRole(role);
    setCurrentUserEmail(email);
    setCurrentUserUsername(username);

    if (role === "ROLE_ADMIN") {
      fetchUsers();
    } else {
      setLoading(false);
    }
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const response = await fetchWithAuth("/api/users");
      if (!response.ok) {
        throw new Error("Failed to fetch users.");
      }
      const data: User[] = await response.json();
      setUsers(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-center p-8">Loading dashboard...</div>;
  }

  if (userRole !== "ROLE_ADMIN") {
    return (
      <div className="p-4 sm:p-6 lg:p-8">
        <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-white">My Dashboard</h1>
        <p className="mt-2 text-lg text-slate-600 dark:text-slate-400">Welcome to your personal dashboard.</p>
        
        <div className="mt-8 max-w-2xl">
          <div className="bg-white dark:bg-slate-800 shadow sm:rounded-lg">
            <div className="px-4 py-5 sm:px-6">
              <h3 className="text-lg font-medium leading-6 text-gray-900 dark:text-white">Your Role & Actions</h3>
              <p className="mt-1 max-w-2xl text-sm text-gray-500 dark:text-slate-400">Here's what you can do based on your account permissions.</p>
            </div>
            <div className="border-t border-gray-200 dark:border-slate-700 px-4 py-5 sm:p-0">
              <dl className="sm:divide-y sm:divide-gray-200 dark:sm:divide-slate-700">
                <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                  <dt className="text-sm font-medium text-gray-500 dark:text-slate-400">Username</dt>
                  <dd className="mt-1 text-sm text-gray-900 dark:text-white sm:col-span-2 sm:mt-0">{currentUserUsername || 'Not available'}</dd>
                </div>
                <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                  <dt className="text-sm font-medium text-gray-500 dark:text-slate-400">Email</dt>
                  <dd className="mt-1 text-sm text-gray-900 dark:text-white sm:col-span-2 sm:mt-0">{currentUserEmail || 'Not available'}</dd>
                </div>
                <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                  <dt className="text-sm font-medium text-gray-500 dark:text-slate-400">Your Role</dt>
                  <dd className="mt-1 text-sm text-gray-900 dark:text-white sm:col-span-2 sm:mt-0">{userRole || 'User'}</dd>
                </div>
                <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                  <dt className="text-sm font-medium text-gray-500 dark:text-slate-400">Available Actions</dt>
                  <dd className="mt-1 text-sm text-gray-900 sm:col-span-2 sm:mt-0">
                    <ul className="list-disc list-inside space-y-1">
                      <li>Calculate new tariffs.</li>
                      <li>
                        <Link to="/logging" className="font-medium text-indigo-600 hover:text-indigo-500 dark:text-indigo-400 dark:hover:text-indigo-300">
                          View your calculation history.
                        </Link>
                      </li>
                    </ul>
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Admin Dashboard
  return (
    <div className="p-4 sm:p-6 lg:p-8">
      <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-white">Admin Dashboard</h1>
      <p className="mt-2 text-lg text-slate-600 dark:text-slate-400">As an <span className="font-semibold">{userRole}</span>, you can view all users in the system.</p>

      {error && <div className="mt-4 p-4 bg-red-100 text-red-700 rounded">{error}</div>}

      <div className="mt-8 flow-root">
        <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
          <div className="inline-block min-w-full py-2 align-middle sm:px-6 lg:px-8">
            <table className="min-w-full divide-y divide-gray-200 dark:divide-slate-700">
              <thead>
                <tr>
                  <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 dark:text-white sm:pl-0">ID</th>
                  <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 dark:text-white">Username</th>
                  <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 dark:text-white">Email</th>
                  <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 dark:text-white">Role</th>
                  <th scope="col" className="relative py-3.5 pl-3 pr-4 sm:pr-0"><span className="sr-only">Actions</span></th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200 dark:divide-slate-800">
                {users.map((user) => (
                  <tr key={user.id}>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 dark:text-white sm:pl-0">{user.id}</td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500 dark:text-slate-400">{user.username}</td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500 dark:text-slate-400">{user.email}</td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500 dark:text-slate-400">{user.role}</td>
                    <td className="relative whitespace-nowrap py-4 pl-3 pr-4 text-right text-sm font-medium sm:pr-0">
                      <button disabled className="text-indigo-400 cursor-not-allowed">Edit</button>
                      <button disabled className="ml-4 text-red-400 cursor-not-allowed">Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <p className="text-xs text-slate-500 mt-2">Note: User management actions (Edit, Delete) are disabled because the required API endpoints are not available.</p>
          </div>
        </div>
      </div>
    </div>
  );
}