export default function TariffResult({
  tariffResult,
  onReset,
  onEdit,
  error,
}: {
  tariffResult: any;
  onReset?: () => void;
  onEdit?: () => void;
  error?: string;
}) {
  if (error) {
    return (
      <div className="mt-8 rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden w-200 mx-auto">
        <div className="px-6 py-10 text-red-600">{error}</div>
      </div>
    );
  }

  if (!tariffResult) return null;

  return (
    <div className="mt-8 rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden w-200 mx-auto">
      <div className="px-6 py-4 bg-slate-50/60 border-b border-slate-200 flex items-center gap-3">
        <h4 className="font-semibold text-lg">Tariff Calculation Result</h4>
      </div>
      <div className="px-6 py-10">
        <p>
          <strong>From:</strong> {tariffResult.fromCountry}
        </p>
        <p>
          <strong>To:</strong> {tariffResult.toCountry}
        </p>
        <p>
          <strong>Tariff Rate:</strong> {tariffResult.tariffRate}%
        </p>
        <p>
          <strong>Total Cost Before Tariff:</strong> ${tariffResult.totalCost - tariffResult.calculatedTariff}
        </p>
        <p>
          <strong>Calculated Tariff:</strong> ${tariffResult.calculatedTariff}
        </p>
        <p>
          <strong>Total Cost After Tariff:</strong> ${tariffResult.totalCost}
        </p>
        {onReset && (
          <button onClick={onReset} className="mt-4 px-4 py-2 bg-blue-500 text-white rounded">
            Reset
          </button>
        )}
        {onEdit && (
          <button onClick={onEdit} className="mt-4 ml-2 px-4 py-2 bg-gray-300 text-black rounded">
            Edit
          </button>
        )}
      </div>
    </div>
  );
}