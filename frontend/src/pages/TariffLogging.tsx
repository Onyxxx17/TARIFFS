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
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (logs.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto text-center">
          <h2 className="text-3xl font-extrabold text-gray-900">Tariff Calculation History</h2>
          {error ? (
            <p className="mt-4 text-lg text-red-600">{error}</p>
          ) : (
            <p className="mt-4 text-lg text-gray-500">No calculations found.</p>
          )}
          <Link
            to="/"
            className="mt-6 inline-block bg-blue-600 px-4 py-2 rounded-md text-white hover:bg-blue-700"
          >
            Calculate a Tariff
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-extrabold text-gray-900">Tariff Calculation History</h2>
          <p className="mt-2 text-sm text-gray-400">
            Showing {logs.length} of {totalElements} calculations
          </p>
        </div>

        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg">
            {error}
          </div>
        )}
        {deleteError && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg">
            {deleteError}
          </div>
        )}

        {/* Delete Confirmation Modal */}
        {showConfirmSingleDelete && deleteId !== null && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg max-w-sm mx-4">
              <h3 className="text-lg font-medium text-gray-900">Confirm Delete</h3>
              <p className="mt-2 text-sm text-gray-500">
                Are you sure you want to delete this tariff record? This cannot be undone.
              </p>
              <div className="mt-4 flex justify-end space-x-3">
                <button
                  onClick={() => {
                    setShowConfirmSingleDelete(false);
                    setDeleteId(null);
                  }}
                  className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  onClick={deleteEntry}
                  className="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-md hover:bg-red-700"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Table */}
        <div className="mt-8 shadow border border-gray-200 sm:rounded-lg overflow-hidden">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200 text-sm">
              <thead className="bg-gray-100 text-gray-700 uppercase text-xs">
                <tr>
                  <th className="px-6 py-3 text-left font-medium">From → To</th>
                  <th className="px-6 py-3 text-left font-medium">Product</th>
                  <th className="px-6 py-3 text-left font-medium">Value ($)</th>
                  <th className="px-6 py-3 text-left font-medium">Year</th>
                  <th className="px-6 py-3 text-left font-medium">Rate (%)</th>
                  <th className="px-6 py-3 text-left font-medium">Base Tariff ($)</th>
                  <th className="px-6 py-3 text-left font-medium">Additional Fees (%)</th>
                  <th className="px-6 py-3 text-left font-medium">Total Cost ($)</th>
                  <th className="px-6 py-3 text-right font-medium">Actions</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {logs.map((log, idx) => (
                  <tr key={log.id} className={idx % 2 === 0 ? "bg-white" : "bg-gray-50"}>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      {log.fromCountry?.name || "N/A"} → {log.toCountry?.name || "N/A"}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      {log.product?.name || "N/A"}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      ${log.value?.toLocaleString() || "0"}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      {log.year}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      {log.tariffRate?.toFixed(2) || "0"}%
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      ${log.calculatedTariff?.toLocaleString(undefined, { minimumFractionDigits: 2 })}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-900">
                      {log.totalAdditionalFees?.toFixed(2) || "0"}%
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap font-semibold text-green-700">
                      ${log.totalCost?.toLocaleString(undefined, { minimumFractionDigits: 2 })}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right">
                      <button
                        onClick={() => handleDeleteClick(log.id)}
                        className="text-red-600 hover:text-red-900 font-medium"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Pagination Controls */}
        <div className="flex items-center justify-between mt-6 pt-4 border-t border-gray-200">
          <div className="text-sm text-gray-600">
            Page <span className="font-semibold">{page + 1}</span> of{" "}
            <span className="font-semibold">{totalPages}</span>
          </div>

          <div className="flex gap-2">
            <button
              onClick={handlePreviousPage}
              disabled={page === 0}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                page === 0
                  ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                  : "bg-gray-200 text-gray-700 hover:bg-gray-300"
              }`}
            >
              Previous
            </button>

            <button
              onClick={handleNextPage}
              disabled={page >= totalPages - 1}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                page >= totalPages - 1
                  ? "bg-gray-100 text-gray-400 cursor-not-allowed"
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