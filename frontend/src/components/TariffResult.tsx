interface TariffResultData {
  fromCountry?: string;
  toCountry?: string;
  tariffRate?: number;
  calculatedTariff?: number;
  totalAdditionalFees?: number;
  totalCost?: number;
  calculationType?: string;
  additionalFees?: number[];
}

export default function TariffResult({
  tariffResult,
  onReset,
  onEdit,
  error,
}: {
  tariffResult: TariffResultData | null;
  onReset?: () => void;
  onEdit?: () => void;
  error?: string;
}) {
  const usd = (n: number) =>
    n?.toLocaleString(undefined, { style: "currency", currency: "USD" });

  const getUnitLabel = () => {
    if (tariffResult?.calculationType === "WEIGHT") {
      return "/kg";
    }
    return "/unit";
  };

  if (error) {
    return (
      <div className="mt-8 mx-auto max-w-3xl rounded-2xl border border-red-200 dark:border-red-800 bg-red-50 dark:bg-red-900/20 text-red-800 dark:text-red-300 shadow-sm overflow-hidden">
        <div className="px-6 py-4 text-sm font-medium border-b border-red-200 dark:border-red-800">{error}</div>
      </div>
    );
  }

  if (!tariffResult) return null;

  const beforeTariff =
    (tariffResult.totalCost ?? 0) - (tariffResult.calculatedTariff ?? 0);

  return (
    <section className="mt-8 mx-auto max-w-3xl rounded-2xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 shadow-sm overflow-hidden transition-colors">
      {/* Header */}
      <div className="px-6 py-4 bg-slate-50/70 dark:bg-slate-700/50 border-b border-slate-200 dark:border-slate-700 flex items-center justify-between">
        <h4 className="font-semibold text-lg text-slate-900 dark:text-white">
          Tariff Calculation Result
        </h4>
        <span className="text-sm text-slate-600 dark:text-slate-400">
          {tariffResult.fromCountry || "—"} → {tariffResult.toCountry || "—"}
        </span>
      </div>

      {/* Top row */}
      <div className="px-6 pt-6">
        <div className="rounded-xl bg-blue-50/70 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 p-4">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div className="text-slate-700 dark:text-slate-300">
              <div className="text-xs uppercase tracking-wide text-blue-900/80 dark:text-blue-300">
                Tariff Rate
              </div>
              <div className="mt-1 text-2xl font-semibold text-blue-900 dark:text-blue-200">
                {typeof tariffResult.tariffRate === "number"
                  ? `${tariffResult.tariffRate}%`
                  : "—"}
              </div>
            </div>
            <div className="text-slate-700 dark:text-slate-300">
              <div className="text-xs uppercase tracking-wide text-blue-900/80 dark:text-blue-300">
                Additional Fee
              </div>
              <div className="mt-1 text-2xl font-semibold text-blue-900 dark:text-blue-200">
                {usd(tariffResult.totalAdditionalFees ?? 0)}
              </div>
            </div>
            <div className="text-right text-slate-700 dark:text-slate-300">
              <div className="text-xs uppercase tracking-wide text-blue-900/80 dark:text-blue-300">
                Total Tariff
              </div>
              <div className="mt-1 text-2xl font-semibold text-blue-900 dark:text-blue-200">
                {usd(tariffResult.calculatedTariff ?? 0)}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Detail labels */}
      <div className="px-6 py-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="rounded-lg border border-slate-200 dark:border-slate-700 p-4">
            <p className="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">
              From
            </p>
            <p className="mt-1 text-base font-medium text-slate-900 dark:text-white">
              {tariffResult.fromCountry || "—"}
            </p>
          </div>

          <div className="rounded-lg border border-slate-200 dark:border-slate-700 p-4">
            <p className="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">To</p>
            <p className="mt-1 text-base font-medium text-slate-900 dark:text-white">
              {tariffResult.toCountry || "—"}
            </p>
          </div>

          {/* Tariff Breakdown */}
          <div className="rounded-lg border border-slate-200 dark:border-slate-700 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400 mb-3">
              Tariff Breakdown
              {tariffResult.calculationType && (
                <span className="ml-2 text-blue-600 dark:text-blue-400 font-medium">
                  ({tariffResult.calculationType === "WEIGHT" ? "per kg" : "per unit"})
                </span>
              )}
            </p>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-slate-600 dark:text-slate-400">Percentage-based tariff ({tariffResult.tariffRate}%):</span>
                <span className="font-medium text-slate-900 dark:text-white">{usd(((tariffResult.calculatedTariff ?? 0) - (tariffResult.totalAdditionalFees ?? 0)))}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-600 dark:text-slate-400">Total additional fees:</span>
                <span className="font-medium text-slate-900 dark:text-white">{usd(tariffResult.totalAdditionalFees ?? 0)}</span>
              </div>
              {(tariffResult.additionalFees ?? []).map((fee, index) => (
                <div key={index} className="flex justify-between pl-4">
                  <span className="text-slate-500 dark:text-slate-500">└ Additional Fee {index + 1} ({fee}%):</span>
                </div>
              ))}
              <div className="border-t border-slate-200 dark:border-slate-700 pt-2 flex justify-between font-semibold text-slate-900 dark:text-white">
                <span>Total tariff:</span>
                <span>{usd(tariffResult.calculatedTariff ?? 0)}</span>
              </div>
            </div>
          </div>

          {/* labels */}
          <div className="rounded-lg border border-slate-200 dark:border-slate-700 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">
              Total Cost Before Tariff {getUnitLabel()}
            </p>
            <p className="mt-1 text-lg font-semibold text-slate-900 dark:text-white">
              {usd(beforeTariff)}
            </p>
          </div>

          <div className="rounded-lg border border-slate-200 dark:border-slate-700 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500 dark:text-slate-400">
              Total Cost After Tariff {getUnitLabel()}
            </p>
            <p className="mt-1 text-lg font-semibold text-slate-900 dark:text-white">
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
                className="inline-flex items-center justify-center rounded-full border border-slate-300 dark:border-slate-600 px-4 py-2 text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors"
              >
                Edit
              </button>
            )}
            {onReset && (
              <button
                onClick={onReset}
                className="inline-flex items-center justify-center rounded-full bg-blue-600 hover:bg-blue-700 px-4 py-2 text-white transition-colors"
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