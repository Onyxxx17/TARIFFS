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
  const usd = (n: number) =>
    n?.toLocaleString(undefined, { style: "currency", currency: "USD" });

  if (error) {
    return (
      <div className="mt-8 mx-auto max-w-3xl rounded-2xl border border-red-200 bg-red-50 text-red-800 shadow-sm overflow-hidden">
        <div className="px-6 py-4 text-sm font-medium border-b border-red-200">{error}</div>
      </div>
    );
  }

  if (!tariffResult) return null;

  const beforeTariff =
    (tariffResult.totalCost ?? 0) - (tariffResult.calculatedTariff ?? 0);

  return (
    <section className="mt-8 mx-auto max-w-3xl rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden">
      {/* Header */}
      <div className="px-6 py-4 bg-slate-50/70 border-b border-slate-200 flex items-center justify-between">
        <h4 className="font-semibold text-lg text-slate-900">
          Tariff Calculation Result
        </h4>
        <span className="text-sm text-slate-600">
          {tariffResult.fromCountry || "—"} → {tariffResult.toCountry || "—"}
        </span>
      </div>

      {/* Top row */}
      <div className="px-6 pt-6">
        <div className="rounded-xl bg-blue-50/70 border border-blue-200 p-4 flex items-center justify-between">
          <div className="text-slate-700">
            <div className="text-xs uppercase tracking-wide text-blue-900/80">
              Tariff Rate
            </div>
            <div className="mt-1 text-3xl font-semibold text-blue-900">
              {typeof tariffResult.tariffRate === "number"
                ? `${tariffResult.tariffRate}%`
                : "—"}
            </div>
          </div>
          <div className="text-right text-slate-700">
            <div className="text-xs uppercase tracking-wide text-blue-900/80">
              Calculated Tariff
            </div>
            <div className="mt-1 text-2xl font-semibold text-blue-900">
              {usd(tariffResult.calculatedTariff ?? 0)}
            </div>
          </div>
        </div>
      </div>

      {/* Detail labels */}
      <div className="px-6 py-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="rounded-lg border border-slate-200 p-4">
            <p className="text-xs uppercase tracking-wide text-slate-500">
              From
            </p>
            <p className="mt-1 text-base font-medium text-slate-900">
              {tariffResult.fromCountry || "—"}
            </p>
          </div>

          <div className="rounded-lg border border-slate-200 p-4">
            <p className="text-xs uppercase tracking-wide text-slate-500">To</p>
            <p className="mt-1 text-base font-medium text-slate-900">
              {tariffResult.toCountry || "—"}
            </p>
          </div>

          {/* labels */}
          <div className="rounded-lg border border-slate-200 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500">
              Total Cost Before Tariff
            </p>
            <p className="mt-1 text-lg font-semibold text-slate-900">
              {usd(beforeTariff)}
            </p>
          </div>


          <div className="rounded-lg border border-slate-200 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500">
              Total Cost After Tariff
            </p>
            <p className="mt-1 text-lg font-semibold text-slate-900">
              {usd(tariffResult.totalCost ?? 0)}
            </p>
          </div>
        </div>

        {/* Actions */}
        {(onEdit || onReset) && (
          <div className="mt-6 flex flex-wrap gap-3">
            {onEdit && (
              <button
                onClick={onEdit}
                className="inline-flex items-center justify-center rounded-full border border-slate-300 px-4 py-2 text-slate-700 hover:bg-slate-50"
              >
                Edit
              </button>
            )}
            {onReset && (
              <button
                onClick={onReset}
                className="inline-flex items-center justify-center rounded-full bg-blue-600 px-4 py-2 text-white hover:bg-blue-600/90"
              >
                Reset
              </button>
            )}
          </div>
        )}
      </div>
    </section>
  );
}
