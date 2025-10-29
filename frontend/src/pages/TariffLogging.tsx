import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { fetchWithAuth } from "../utils/api";

interface CalculationRecord {
  id: number;
  fromCountry?: { countryCode: string; name: string };
  toCountry?: { countryCode: string; name: string };
  product?: { id: number; name: string };
  value: number;
  year: number;
  tariffRate: number;
  calculatedTariff: number;
  totalAdditionalFees?: number;      // combined value
  additionalFees?: number[];         // array of individual fees
  totalCost: number;
  timestamp?: string;
}

interface PaginatedResponse {
  content: CalculationRecord[];
  totalPages: number;
  totalElements: number;
  currentPage: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export default function TariffLoggingDisplay() {
  const [logs, setLogs] = useState<CalculationRecord[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [deleteError, setDeleteError] = useState("");
  const [showConfirmSingleDelete, setShowConfirmSingleDelete] = useState(false);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  useEffect(() => {
    fetchCalculationHistory();
  }, [page]);

  const fetchCalculationHistory = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await fetchWithAuth(`/api/import-records/history?page=${page}`);

      if (!response.ok) {
        if (response.status === 403) {
          setError("Email not verified. Please verify your email first.");
        } else {
          setError("Failed to load calculation history");
        }
        setLoading(false);
        return;
      }

      const data: PaginatedResponse = await response.json();
      setLogs(data.content || []);
      setTotalPages(data.totalPages || 1);
      setTotalElements(data.totalElements || 0);
      setLoading(false);
    } catch (err) {
      console.error(err);
      setError("Error loading calculation history");
      setLoading(false);
    }
  };

  const handleDeleteClick = (id: number) => {
    setDeleteId(id);
    setShowConfirmSingleDelete(true);
  };

  const deleteEntry = async () => {
    if (deleteId === null) return;
    setDeleteError("");

    try {
      const response = await fetchWithAuth(`/api/import-records/history/${deleteId}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        setDeleteError("Failed to delete calculation");
        return;
      }

      setLogs(logs.filter((log) => log.id !== deleteId));
      setShowConfirmSingleDelete(false);
      setDeleteId(null);

      if (logs.length === 1 && page > 0) {
        setPage(page - 1);
      }
    } catch (err) {
      setDeleteError("Error deleting calculation");
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) setPage(page - 1);
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) setPage(page + 1);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 dark:bg-slate-900 py-12 px-4 sm:px-6 lg:px-8 flex items-center justify-center transition-colors">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (logs.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 dark:bg-slate-900 py-12 px-4 sm:px-6 lg:px-8 transition-colors">
        <div className="max-w-7xl mx-auto">
          <div className="text-center">
            <h2 className="text-3xl font-extrabold text-gray-900 dark:text-white">Tariff Calculation History</h2>
            {error ? (
              <p className="mt-4 text-lg text-red-600 dark:text-red-400">{error}</p>
            ) : (
              <p className="mt-4 text-lg text-gray-500 dark:text-slate-400">No calculations have been logged yet.</p>
            )}
            <Link
              to="/"
              className="mt-6 inline-block bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-md text-white transition-colors"
            >
              Calculate a Tariff
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-slate-900 py-12 px-4 sm:px-6 lg:px-8 transition-colors">
      <div className="max-w-7xl mx-auto">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-extrabold text-gray-900 dark:text-white">Tariff Calculation History</h2>
          <p className="mt-4 text-lg text-gray-500 dark:text-slate-400">View your previous tariff calculations</p>
          <p className="mt-2 text-sm text-gray-400 dark:text-slate-500">
            Showing {logs.length} of {totalElements} calculations
          </p>
        </div>

        {error && (
          <div className="mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 rounded-lg">
            {error}
          </div>
        )}
        {deleteError && (
          <div className="mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 rounded-lg">
            {deleteError}
          </div>
        )}

        {/* Delete Confirmation Modal */}
        {showConfirmSingleDelete && deleteId !== null && (
          <div className="fixed inset-0 bg-black bg-opacity-50 dark:bg-opacity-70 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-slate-800 p-6 rounded-lg max-w-sm mx-4 shadow-xl">
              <h3 className="text-lg font-medium text-gray-900 dark:text-white">Confirm Delete Entry</h3>
              <p className="mt-2 text-sm text-gray-500 dark:text-slate-400">
                Are you sure you want to delete this tariff calculation? This action cannot be undone.
              </p>
              <div className="mt-4 flex justify-end space-x-3">
                <button
                  onClick={() => {
                    setShowConfirmSingleDelete(false);
                    setDeleteId(null);
                  }}
                  className="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-700 dark:text-slate-300 bg-white dark:bg-slate-700 border border-gray-300 dark:border-slate-600 rounded-md hover:bg-gray-50 dark:hover:bg-slate-600 transition-colors"
                >
                  Cancel
                </button>
                <button
                  onClick={deleteEntry}
                  className="inline-flex justify-center px-4 py-2 text-sm font-medium text-white bg-red-600 border border-transparent rounded-md hover:bg-red-700 transition-colors"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Table with horizontal scrolling */}
        <div className="mt-8 flex flex-col">
          <div className="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
            <div className="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
              <div className="shadow overflow-hidden border-b border-gray-200 dark:border-slate-700 sm:rounded-lg">
                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200 dark:divide-slate-700">
                    <thead className="bg-gray-50 dark:bg-slate-800">
                      <tr>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          From → To
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Product
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Value ($)
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Year
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Rate (%)
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Base Tariff ($)
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Additional Fees
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Total Cost ($)
                        </th>
                        <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-500 dark:text-slate-400 uppercase tracking-wider">
                          Actions
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white dark:bg-slate-800 divide-y divide-gray-200 dark:divide-slate-700">
                      {logs.map((log, idx) => {
                        // Calculate additional fees in dollars
                        const additionalFeesDollar = (log.totalAdditionalFees || 0) * log.value / 100;
                        
                        return (
                          <tr key={log.id} className={idx % 2 === 0 ? "bg-white dark:bg-slate-800" : "bg-gray-50 dark:bg-slate-700/50"}>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              {log.fromCountry?.name || "N/A"} → {log.toCountry?.name || "N/A"}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              {log.product?.name || "N/A"}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              ${log.value?.toLocaleString() || "0"}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              {log.year}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              {log.tariffRate?.toFixed(2) || "0"}%
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              ${log.calculatedTariff?.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) || "0"}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                              <div className="flex flex-col">
                                <span>{log.totalAdditionalFees?.toFixed(2) || "0"}%</span>
                                <span className="text-xs text-gray-500 dark:text-slate-400">
                                  (${additionalFeesDollar.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })})
                                </span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-green-700 dark:text-green-400">
                              ${log.totalCost?.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) || "0"}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                              <button
                                onClick={() => handleDeleteClick(log.id)}
                                className="text-red-600 dark:text-red-400 hover:text-red-900 dark:hover:text-red-300 font-medium transition-colors"
                              >
                                Delete
                              </button>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Pagination Controls */}
        <div className="flex items-center justify-between mt-6 pt-4 border-t border-gray-200 dark:border-slate-700">
          <div className="text-sm text-gray-600 dark:text-slate-400">
            Page <span className="font-semibold">{page + 1}</span> of{" "}
            <span className="font-semibold">{totalPages}</span>
          </div>

          <div className="flex gap-2">
            <button
              onClick={handlePreviousPage}
              disabled={page === 0}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                page === 0
                  ? "bg-gray-100 dark:bg-slate-700 text-gray-400 dark:text-slate-500 cursor-not-allowed"
                  : "bg-gray-200 dark:bg-slate-700 text-gray-700 dark:text-slate-200 hover:bg-gray-300 dark:hover:bg-slate-600"
              }`}
            >
              Previous
            </button>

            <button
              onClick={handleNextPage}
              disabled={page >= totalPages - 1}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                page >= totalPages - 1
                  ? "bg-gray-100 dark:bg-slate-700 text-gray-400 dark:text-slate-500 cursor-not-allowed"
                  : "bg-blue-600 text-white hover:bg-blue-700"
              }`}
            >
              Next
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}