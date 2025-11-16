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
      <div className="mt-8 mx-auto max-w-3xl rounded-2xl border border-red-200 bg-red-50 text-red-800 shadow-sm overflow-hidden">
        <div className="px-4 sm:px-6 py-4 text-sm font-medium border-b border-red-200">{error}</div>
      </div>
    );
  }

  if (!tariffResult) return null;

  const beforeTariff =
    (tariffResult.totalCost ?? 0) - (tariffResult.calculatedTariff ?? 0);

  return (
    <section className="mt-8 mx-auto max-w-3xl rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden transition-colors">
      {/* Header */}
      <div className="px-4 sm:px-6 py-4 bg-slate-50/70 border-b border-slate-200 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-2">
        <h4 className="font-semibold text-lg text-slate-900">
          Tariff Calculation Result
        </h4>
        <span className="text-xs sm:text-sm text-slate-600 whitespace-nowrap">
          {tariffResult.fromCountry || "—"} → {tariffResult.toCountry || "—"}
        </span>
      </div>

      {/* Top row */}
      <div className="px-4 sm:px-6 pt-6">
        <div className="rounded-xl bg-blue-50/70 border border-blue-200 p-4">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div className="text-slate-700">
              <div className="text-xs uppercase tracking-wide text-blue-900/80">
                Tariff Rate
              </div>
              <div className="mt-1 text-xl sm:text-2xl font-semibold text-blue-900">
                {typeof tariffResult.tariffRate === "number"
                  ? `${tariffResult.tariffRate}%`
                  : "—"}
              </div>
            </div>
            <div className="text-slate-700">
              <div className="text-xs uppercase tracking-wide text-blue-900/80">
                Additional Fee Rate
              </div>
              <div className="mt-1 text-2xl font-semibold text-blue-900 dark:text-blue-200">
                {`${(tariffResult.additionalFees?.reduce((a, b) => a + b, 0) ?? 0).toFixed(2)}%`}
              </div>
            </div>
            <div className="text-slate-700">
              <div className="text-xs uppercase tracking-wide text-blue-900/80">
                Total Fee
              </div>
              <div className="mt-1 text-xl sm:text-2xl font-semibold text-blue-900">
                {usd(tariffResult.calculatedTariff ?? 0)}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Detail labels */}
      <div className="px-4 sm:px-6 py-6">
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

          {/* Tariff Breakdown */}
          <div className="rounded-lg border border-slate-200 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500 mb-3">
              Breakdown
              {tariffResult.calculationType && (
                <span className="ml-2 text-blue-600 font-medium">
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
                <span className="text-slate-600 dark:text-slate-400">Total additional fee:</span>
                <span className="font-medium text-slate-900 dark:text-white">{usd(tariffResult.totalAdditionalFees ?? 0)}</span>
              </div>
              {tariffResult.additionalFees && tariffResult.additionalFees.length > 0 && (
                <div className="pl-4 border-l-2 border-slate-300 space-y-1">
                  {tariffResult.additionalFees.map((fee, idx) => (
                    <div key={idx} className="flex justify-between text-xs text-slate-600 dark:text-slate-400">
                      <span>Additional Fee {idx + 1} ({Number(fee).toFixed(2)}%):</span>
                      <span></span>
                    </div>
                  ))}
                </div>
              )}
              <div className="border-t border-slate-200 dark:border-slate-700 pt-2 flex justify-between font-semibold text-slate-900 dark:text-white">
                <span>Total Fee:</span>
                <span>{usd(tariffResult.calculatedTariff ?? 0)}</span>
              </div>
            </div>
          </div>

          {/* labels */}
          <div className="rounded-lg border border-slate-200 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500">
              Original Cost {getUnitLabel()}
            </p>
            <p className="mt-1 text-lg font-semibold text-slate-900">
              {usd(beforeTariff)}
            </p>
          </div>

          <div className="rounded-lg border border-slate-200 p-4 sm:col-span-2">
            <p className="text-xs uppercase tracking-wide text-slate-500">
              Final Cost {getUnitLabel()}
            </p>
            <p className="mt-1 text-lg font-semibold text-slate-900">
              {usd(tariffResult.totalCost ?? 0)}
            </p>
          </div>
        </div>

        {/* Actions */}
        {(onEdit || onReset) && (
          <div className="mt-6 flex flex-col sm:flex-row gap-3">
            {onEdit && (
              <button
                onClick={onEdit}
                className="w-full sm:w-auto inline-flex items-center justify-center rounded-full border border-slate-300 px-4 py-3 sm:py-2 text-slate-700 hover:bg-slate-50 transition-colors text-sm sm:text-base"
              >
                Edit
              </button>
            )}
            {onReset && (
              <button
                onClick={onReset}
                className="w-full sm:w-auto inline-flex items-center justify-center rounded-full bg-blue-600 hover:bg-blue-700 px-4 py-3 sm:py-2 text-white transition-colors text-sm sm:text-base"
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