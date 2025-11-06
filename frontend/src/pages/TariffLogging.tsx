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
  additionalFee: number;           // stores the additional fee rate (%)
  totalAdditionalFees: number;     // stores the total additional fees amount
  totalCost: number;
  calculationType?: string;        // WEIGHT or QUANTITY
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
        setError(
          response.status === 403
            ? "Email not verified. Please verify your email first."
            : "Failed to load calculation history"
        );
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
      if (logs.length === 1 && page > 0) setPage(page - 1);
    } catch (err) {
      setDeleteError("Error deleting calculation");
    }
  };

  const handlePreviousPage = () => page > 0 && setPage(page - 1);
  const handleNextPage = () => page < totalPages - 1 && setPage(page + 1);

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (logs.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-6 text-center">
        <h2 className="text-2xl font-bold text-gray-900">Tariff Calculation History</h2>
        <p className="mt-3 text-gray-500">
          {error || "No calculations have been logged yet."}
        </p>
        <Link
          to="/"
          className="mt-6 inline-block bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition-colors"
        >
          Calculate a Tariff
        </Link>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-extrabold text-gray-900">Tariff Calculation History</h2>
          <p className="mt-2 text-gray-500">View your previous tariff calculations</p>
          <p className="mt-1 text-sm text-gray-400">
            Showing {logs.length} of {totalElements} records
          </p>
        </div>

        {/* Delete Modal */}
        {showConfirmSingleDelete && (
          <div className="fixed inset-0 backdrop-blur-sm bg-white/20 flex items-center justify-center z-50 p-4">
            <div className="bg-white p-6 rounded-xl shadow-2xl max-w-sm w-full">
              <h3 className="text-lg font-semibold text-gray-900">Delete Calculation?</h3>
              <p className="mt-2 text-sm text-gray-500">This action cannot be undone.</p>
              <div className="mt-4 flex justify-end gap-3">
                <button
                  onClick={() => setShowConfirmSingleDelete(false)}
                  className="px-4 py-2 text-sm rounded-md border border-gray-300 hover:bg-gray-100 transition"
                >
                  Cancel
                </button>
                <button
                  onClick={deleteEntry}
                  className="px-4 py-2 text-sm rounded-md bg-red-600 text-white hover:bg-red-700 transition"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Table */}
        <div className="overflow-x-auto shadow-lg rounded-lg border border-gray-200 bg-white">
          <table className="min-w-full text-sm text-left text-gray-700">
            <thead className="bg-gray-100 text-gray-600 uppercase text-xs">
              <tr>
                <th className="px-6 py-3">From → To</th>
                <th className="px-6 py-3">Product</th>
                <th className="px-6 py-3">Value ($)</th>
                <th className="px-6 py-3">Year</th>
                <th className="px-6 py-3">Base Rate (%)</th>
                <th className="px-6 py-3">Base Tariff ($)</th>
                <th className="px-6 py-3">Additional Fee Rate (%)</th>
                <th className="px-6 py-3">Additional Fee ($)</th>
                <th className="px-6 py-3">Calculation Type</th>
                <th className="px-6 py-3">Total Cost ($)</th>
                <th className="px-6 py-3 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {logs.map((log) => (
                <tr key={log.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-6 py-4">{log.fromCountry?.name} → {log.toCountry?.name}</td>
                  <td className="px-6 py-4">{log.product?.name}</td>
                  <td className="px-6 py-4">${log.value.toLocaleString()}</td>
                  <td className="px-6 py-4">{log.year}</td>
                  <td className="px-6 py-4">{log.tariffRate.toFixed(2)}%</td>
                  <td className="px-6 py-4">${log.calculatedTariff.toLocaleString()}</td>
                  <td className="px-6 py-4">{log.additionalFee.toFixed(2)}%</td>
                  <td className="px-6 py-4">${log.totalAdditionalFees.toLocaleString()}</td>
                  <td className="px-6 py-4">
                    <span
                      className={`px-3 py-1 rounded-full text-xs font-semibold ${
                        log.calculationType === "WEIGHT"
                          ? "bg-amber-100 text-amber-700"
                          : "bg-blue-100 text-blue-700"
                      }`}
                    >
                      {log.calculationType || "—"}
                    </span>
                  </td>
                  <td className="px-6 py-4 font-semibold text-green-600">
                    ${log.totalCost.toLocaleString()}
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button
                      onClick={() => handleDeleteClick(log.id)}
                      className="text-red-600 hover:text-red-800 font-medium"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        <div className="flex justify-between items-center mt-6 text-sm text-gray-600">
          <span>
            Page <b>{page + 1}</b> of <b>{totalPages}</b>
          </span>
          <div className="space-x-2">
            <button
              onClick={handlePreviousPage}
              disabled={page === 0}
              className={`px-3 py-2 rounded-md border ${
                page === 0
                  ? "text-gray-400 border-gray-200 cursor-not-allowed"
                  : "text-gray-700 hover:bg-gray-100 border-gray-300"
              }`}
            >
              Previous
            </button>
            <button
              onClick={handleNextPage}
              disabled={page >= totalPages - 1}
              className={`px-3 py-2 rounded-md border ${
                page >= totalPages - 1
                  ? "text-gray-400 border-gray-200 cursor-not-allowed"
                  : "bg-blue-600 text-white hover:bg-blue-700 border-blue-600"
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
