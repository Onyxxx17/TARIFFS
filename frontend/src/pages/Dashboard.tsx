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
      <div className="p-4 sm:p-6 lg:p-8 max-w-6xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-white">Profile & Settings</h1>
          <p className="mt-2 text-slate-600 dark:text-slate-400">
            Manage your account settings and view your profile information
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Profile Information */}
          <div className="lg:col-span-2">
            <div className="bg-white dark:bg-slate-800 shadow rounded-lg border border-slate-200 dark:border-slate-700">
              <div className="px-6 py-4 border-b border-slate-200 dark:border-slate-700">
                <h3 className="text-lg font-semibold text-slate-900 dark:text-white">Profile Information</h3>
                <p className="text-sm text-slate-600 dark:text-slate-400 mt-1">Your account details and preferences</p>
              </div>
              
              <div className="px-6 py-6">
                <div className="flex items-start space-x-6">
                  {/* Profile Avatar */}
                  <div className="w-20 h-20 rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-2xl font-bold shadow-lg">
                    {currentUserUsername ? currentUserUsername.charAt(0).toUpperCase() : 'U'}
                  </div>
                  
                  {/* Profile Details */}
                  <div className="flex-1 space-y-4">
                    <div>
                      <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Display Name</dt>
                      <dd className="mt-1 text-xl font-semibold text-slate-900 dark:text-white">{currentUserUsername || 'User'}</dd>
                    </div>
                    
                    <div>
                      <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Email Address</dt>
                      <dd className="mt-1 text-slate-900 dark:text-white">{currentUserEmail || 'Not available'}</dd>
                    </div>
                    
                    <div>
                      <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Account Type</dt>
                      <dd className="mt-1">
                        <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                          {userRole === 'ROLE_ADMIN' ? 'Administrator' : 'Standard User'}
                        </span>
                      </dd>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Quick Actions & Status */}
          <div className="lg:col-span-1 space-y-6">
            {/* Quick Actions */}
            <div className="bg-white dark:bg-slate-800 shadow rounded-lg border border-slate-200 dark:border-slate-700">
              <div className="px-6 py-4 border-b border-slate-200 dark:border-slate-700">
                <h3 className="text-lg font-semibold text-slate-900 dark:text-white">Quick Actions</h3>
              </div>
              
              <div className="px-6 py-4 space-y-3">
                <Link 
                  to="/logging" 
                  className="flex items-center space-x-3 p-3 rounded-lg border border-slate-200 dark:border-slate-600 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors group"
                >
                  <div className="flex-shrink-0">
                    <svg className="w-5 h-5 text-slate-600 dark:text-slate-400 group-hover:text-blue-600 dark:group-hover:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                    </svg>
                  </div>
                  <div>
                    <p className="text-sm font-medium text-slate-900 dark:text-white">Calculation History</p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">View past tariff calculations</p>
                  </div>
                </Link>


                <Link 
                  to="/" 
                  className="flex items-center space-x-3 p-3 rounded-lg border border-slate-200 dark:border-slate-600 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors group"
                >
                  <div className="flex-shrink-0">
                    <svg className="w-5 h-5 text-slate-600 dark:text-slate-400 group-hover:text-green-600 dark:group-hover:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                    </svg>
                  </div>
                  <div>
                    <p className="text-sm font-medium text-slate-900 dark:text-white">Calculate Tariffs</p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">Start a new calculation</p>
                  </div>
                </Link>
              </div>
            </div>

            {/* Account Status */}
            <div className="bg-white dark:bg-slate-800 shadow rounded-lg border border-slate-200 dark:border-slate-700">
              <div className="px-6 py-4 border-b border-slate-200 dark:border-slate-700">
                <h3 className="text-lg font-semibold text-slate-900 dark:text-white">Account Status</h3>
              </div>
              
              <div className="px-6 py-4 space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-slate-600 dark:text-slate-400">Status</span>
                  <div className="flex items-center space-x-2">
                    <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                    <span className="text-sm font-medium text-green-600 dark:text-green-400">Active</span>
                  </div>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-slate-600 dark:text-slate-400">Member Since</span>
                  <span className="text-sm font-medium text-slate-900 dark:text-white">Recently</span>
                </div>
              </div>
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