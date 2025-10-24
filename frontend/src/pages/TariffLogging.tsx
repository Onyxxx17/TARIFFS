import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

interface TariffLog {
  timestamp: string;
  fromCountry: string;
  toCountry: string;
  product: string;
  quantity: number;
  unitCost: number;
  year: number;
  tariffRate: number;
  calculatedTariff: number;
  totalCost: number;
}

export default function TariffLoggingDisplay() {
  const [logs, setLogs] = useState<TariffLog[]>([]);
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [showConfirmSingleDelete, setShowConfirmSingleDelete] = useState(false);
  const [deleteIndex, setDeleteIndex] = useState<number | null>(null);

  useEffect(() => {
    const storedLogs = JSON.parse(localStorage.getItem('tariffLogs') || '[]');
    setLogs(storedLogs);
  }, []);

  const handleDeleteClick = (index: number) => {
    setDeleteIndex(index);
    setShowConfirmSingleDelete(true);
  };

  const deleteEntry = () => {
    if (deleteIndex === null) return;
    const newLogs = logs.filter((_, idx) => idx !== deleteIndex);
    setLogs(newLogs);
    localStorage.setItem('tariffLogs', JSON.stringify(newLogs));
    setShowConfirmSingleDelete(false);
    setDeleteIndex(null);
  };

  const deleteAllEntries = () => {
    setLogs([]);
    localStorage.removeItem('tariffLogs');
    setShowConfirmDelete(false);
  };

  if (logs.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="text-center">
            <h2 className="text-3xl font-extrabold text-gray-900">Tariff Calculation History</h2>
            <p className="mt-4 text-lg text-gray-500">No calculations have been logged yet.</p>
            <Link
              to="/"
              className="mt-6 inline-block bg-blue-600 px-4 py-2 rounded-md text-white hover:bg-blue-700"
            >
              Calculate a Tariff
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-extrabold text-gray-900">Tariff Calculation History</h2>
          <p className="mt-4 text-lg text-gray-500">View your previous tariff calculations</p>
          
          {/* Clear All Button */}
          {logs.length > 0 && (
            <button
              onClick={() => setShowConfirmDelete(true)}
              className="mt-4 inline-flex items-center px-4 py-2 border border-red-300 text-sm font-medium rounded-md text-red-700 bg-white hover:bg-red-50"
            >
              Clear History
            </button>
          )}
        </div>

        {/* Confirmation Modal */}
        {showConfirmDelete && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg max-w-sm mx-4">
              <h3 className="text-lg font-medium text-gray-900">Confirm Delete</h3>
              <p className="mt-2 text-sm text-gray-500">
                Are you sure you want to clear all tariff calculation history? This action cannot be undone.
              </p>
              <div className="mt-4 flex justify-end space-x-3">
                <button
                  onClick={() => setShowConfirmDelete(false)}
                  className="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  onClick={deleteAllEntries}
                  className="inline-flex justify-center px-4 py-2 text-sm font-medium text-white bg-red-600 border border-transparent rounded-md hover:bg-red-700"
                >
                  Delete All
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Single Entry Delete Confirmation Modal */}
        {showConfirmSingleDelete && deleteIndex !== null && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg max-w-sm mx-4">
              <h3 className="text-lg font-medium text-gray-900">Confirm Delete Entry</h3>
              <p className="mt-2 text-sm text-gray-500">
                Are you sure you want to delete this tariff calculation? This action cannot be undone.
              </p>
              <div className="mt-4 flex justify-end space-x-3">
                <button
                  onClick={() => {
                    setShowConfirmSingleDelete(false);
                    setDeleteIndex(null);
                  }}
                  className="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  onClick={deleteEntry}
                  className="inline-flex justify-center px-4 py-2 text-sm font-medium text-white bg-red-600 border border-transparent rounded-md hover:bg-red-700"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        )}

        <div className="mt-8 flex flex-col">
          <div className="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
            <div className="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
              <div className="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Date
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        From → To
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Product
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Details
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Results
                      </th>
                      <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {logs.map((log, idx) => (
                      <tr key={idx}>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {new Date(log.timestamp).toLocaleDateString()}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {log.fromCountry} → {log.toCountry}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {log.product}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          <div>Quantity: {log.quantity}</div>
                          <div>Unit Cost: ${log.unitCost}</div>
                          <div>Year: {log.year}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          <div>Rate: {log.tariffRate}%</div>
                          <div>Tariff: ${log.calculatedTariff.toLocaleString()}</div>
                          <div>Total: ${log.totalCost.toLocaleString()}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                          <button
                            onClick={() => handleDeleteClick(idx)}
                            className="text-red-600 hover:text-red-900"
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
          </div>
        </div>
      </div>
    </div>
  );
}