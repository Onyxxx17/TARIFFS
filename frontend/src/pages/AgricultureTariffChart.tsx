import { useState, useEffect } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import ProductSelect from "../components/ProductSelect";
import CountrySelectAPI from "../components/CountrySelectAPI";

import { fetchWithAuth } from "../utils/api";

interface Product {
  id: number;
  name: string;
  category: {
    id: number;
    name: string;
  };
}

interface TariffRateData {
  year: number;
  rate: number;
}

// 💬 Tooltip
const CustomTooltip = ({ active, payload, label }: any) => {
  if (active && payload && payload.length) {
    return (
      <div
        style={{
          backgroundColor: "var(--tooltip-bg, white)",
          border: "1px solid #e5e7eb",
          padding: "8px 12px",
          borderRadius: "8px",
          boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
        }}
        className="dark:bg-slate-800"
      >
        <p style={{ margin: 0, fontWeight: 600, color: "#1a73e8" }} className="dark:text-blue-400">
          Year {label}
        </p>
        {payload.map((p: any, idx: number) => (
          <p key={idx} style={{ margin: 0, color: p.stroke }} className="dark:text-slate-200">
            {p.name}: {p.value}%
          </p>
        ))}
      </div>
    );
  }
  return null;
};

const AgricultureTariffChart = () => {
  const [fromCountryCode, setFromCountryCode] = useState("");
  const [toCountryCode, setToCountryCode] = useState("");
  const [product, setProduct] = useState<Product | null>(null);
  const [tariffData, setTariffData] = useState<TariffRateData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!fromCountryCode || !toCountryCode || !product) {
      setTariffData([]);
      return;
    }

    const fetchTariffRates = async () => {
      setLoading(true);
      setError("");
      try {
        const url = `/api/tariff-rules/rates-over-time?fromCountryCode=${fromCountryCode}&toCountryCode=${toCountryCode}&productId=${product.id}`;

        const response = await fetchWithAuth(url);

        if (!response.ok) throw new Error("Failed to fetch tariff rates");

        const data: TariffRateData[] = await response.json();

        setTariffData(data);
      } catch (err) {
        console.error("Error fetching tariff rates:", err);
        setError("Failed to load tariff data. Please try again.");
        setTariffData([]);
      } finally {
        setLoading(false);
      }
    };

    fetchTariffRates();
  }, [fromCountryCode, toCountryCode, product]);

  // Calculate summary statistics
  const getSummaryStats = () => {
    if (tariffData.length < 2) return null;

    const firstRate = tariffData[0].rate;
    const lastRate = tariffData[tariffData.length - 1].rate;
    const change = lastRate - firstRate;
    const percentChange = (change / firstRate) * 100;

    const rates = tariffData.map((d) => d.rate);
    const maxRate = Math.max(...rates);
    const minRate = Math.min(...rates);
    const avgRate = rates.reduce((a, b) => a + b, 0) / rates.length;

    let trendDirection = "";
    if (Math.abs(change) < 0.5) {
      trendDirection = "stable";
    } else if (change > 0) {
      trendDirection = "increase";
    } else {
      trendDirection = "decrease";
    }

    return {
      firstRate,
      lastRate,
      change,
      percentChange,
      maxRate,
      minRate,
      avgRate,
      trendDirection,
    };
  };

  const summaryStats = getSummaryStats();

  return (
    <div className="max-w-7xl mx-auto space-y-4 sm:space-y-6 mt-6 sm:mt-8 px-4 sm:px-6 lg:px-8">
      {/* Selection Form */}
      <div className="rounded-lg sm:rounded-2xl border border-slate-200 bg-white shadow-sm p-4 sm:p-6 relative overflow-visible transition-colors">
        <div className="text-center mb-4 sm:mb-6">
          <h3 className="text-xl sm:text-2xl font-semibold text-slate-900">
            Tariff Rates Over Time
          </h3>
          <p className="mt-1 sm:mt-2 text-xs sm:text-sm text-slate-500">
            Select two countries and a product to visualize tariff rates from
            1996 to 2025
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
          <CountrySelectAPI
            label="From Country"
            value={fromCountryCode}
            onChange={setFromCountryCode}
            placeholder="Search for a country..."
          />

          <CountrySelectAPI
            label="To Country"
            value={toCountryCode}
            onChange={setToCountryCode}
            placeholder="Search for a country..."
          />

          <div className="sm:col-span-2 lg:col-span-1 lg:self-end">
            <ProductSelect
              label="Product"
              value={product}
              onPick={setProduct}
              placeholder="Search by HS code or product name..."
            />
          </div>
        </div>

        {error && (
          <div className="mt-3 sm:mt-4 p-3 bg-red-50 border border-red-200 rounded-lg text-xs sm:text-sm text-red-600">
            {error}
          </div>
        )}
      </div>

      {/* Chart */}
      {loading ? (
        <div className="rounded-lg sm:rounded-2xl border border-slate-200 bg-white shadow-sm p-4 sm:p-6 text-center transition-colors">
          <p className="text-sm text-slate-500">Loading tariff data...</p>
        </div>
      ) : tariffData.length > 0 ? (
        <div className="rounded-lg sm:rounded-2xl border border-slate-200 bg-white shadow-sm p-4 sm:p-6 transition-colors">
          {/* Summary Statistics Banner */}
          {summaryStats && (
            <div className="mb-4 sm:mb-6 bg-slate-50 border border-slate-200 rounded-lg p-3 sm:p-4">
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 sm:gap-4">
                <div className="text-center">
                  <p className="text-xs font-medium text-slate-500 uppercase tracking-wide mb-1">
                    Change
                  </p>
                  <p
                    className={`text-lg sm:text-xl font-bold ${
                      summaryStats.trendDirection === "increase"
                        ? "text-red-600"
                        : summaryStats.trendDirection === "decrease"
                        ? "text-green-600"
                        : "text-slate-700"
                    }`}
                  >
                    {summaryStats.change >= 0 ? "+" : ""}
                    {summaryStats.change.toFixed(2)}%
                  </p>
                </div>

                <div className="text-center">
                  <p className="text-xs font-medium text-slate-500 uppercase tracking-wide mb-1">
                    Current
                  </p>
                  <p className="text-lg sm:text-xl font-bold text-slate-900">
                    {summaryStats.lastRate.toFixed(2)}%
                  </p>
                </div>

                <div className="text-center">
                  <p className="text-xs font-medium text-slate-500 uppercase tracking-wide mb-1">
                    Average
                  </p>
                  <p className="text-lg sm:text-xl font-bold text-slate-900">
                    {summaryStats.avgRate.toFixed(2)}%
                  </p>
                </div>

                <div className="text-center">
                  <p className="text-xs font-medium text-slate-500 uppercase tracking-wide mb-1">
                    Range
                  </p>
                  <p className="text-lg sm:text-xl font-bold text-slate-900">
                    {summaryStats.minRate.toFixed(2)}% –{" "}
                    {summaryStats.maxRate.toFixed(2)}%
                  </p>
                </div>
              </div>
            </div>
          )}

          <div className="mb-4 sm:mb-6">
            <h3 className="text-base sm:text-lg font-semibold text-slate-900">
              Tariff Rates Over Time (1996–2025)
            </h3>
            <p className="text-xs sm:text-sm text-slate-600 mt-1">
              {toCountryCode} tariff rates on imports from {fromCountryCode} •
              Product: {product?.name}
            </p>
          </div>

          <div className="overflow-x-auto">
            <ResponsiveContainer width="100%" height={300} minWidth={250}>
              <LineChart
                margin={{ top: 20, right: 20, left: 0, bottom: 10 }}
                data={
                  tariffData.length > 0 ? tariffData : [{ year: 1996, rate: 0 }]
                }
              >
                <CartesianGrid stroke="#f1f3f4" vertical={false} />
                <XAxis
                  dataKey="year"
                  stroke="#9aa0a6"
                  fontSize={12}
                  tickMargin={8}
                  domain={[1996, 2025]}
                />
                <YAxis
                  stroke="#9aa0a6"
                  fontSize={12}
                  axisLine={false}
                  tickLine={false}
                  tickMargin={8}
                  label={{
                    value: "Rate (%)",
                    angle: -90,
                    position: "insideLeft",
                    style: { fontSize: 12, fill: "#9aa0a6" },
                  }}
                />
                <Tooltip content={<CustomTooltip />} />
                <Line
                  type="linear"
                  dataKey="rate"
                  name={`${toCountryCode} tariff`}
                  stroke="#1a73e8"
                  strokeWidth={2.5}
                  dot={{ r: 2, fill: "#1a73e8" }}
                  activeDot={{ r: 5, fill: "#1a73e8" }}
                  data={tariffData}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>

          {/* Analysis Description */}
          {summaryStats && (
            <div className="mt-4 sm:mt-6 p-3 sm:p-5 bg-slate-50 border border-slate-200 rounded-lg">
              <h4 className="text-xs sm:text-sm font-semibold text-slate-900 mb-2 sm:mb-3 uppercase tracking-wide">
                Executive Summary
              </h4>
              <p className="text-xs sm:text-sm text-slate-700 leading-relaxed">
                Over the {tariffData.length}-year period from{" "}
                {tariffData[0].year} to {tariffData[tariffData.length - 1].year}
                , the tariff rate applied by {toCountryCode} on imports from{" "}
                {fromCountryCode}
                {summaryStats.trendDirection === "increase" &&
                  ` increased by ${Math.abs(summaryStats.change).toFixed(
                    2
                  )} percentage points, representing a ${summaryStats.percentChange.toFixed(
                    1
                  )}% rise from the baseline rate of ${summaryStats.firstRate.toFixed(
                    2
                  )}%.`}
                {summaryStats.trendDirection === "decrease" &&
                  ` decreased by ${Math.abs(summaryStats.change).toFixed(
                    2
                  )} percentage points, representing a ${Math.abs(
                    summaryStats.percentChange
                  ).toFixed(
                    1
                  )}% reduction from the baseline rate of ${summaryStats.firstRate.toFixed(
                    2
                  )}%.`}
                {summaryStats.trendDirection === "stable" &&
                  ` remained relatively stable, fluctuating within a narrow band around the average rate of ${summaryStats.avgRate.toFixed(
                    2
                  )}%.`}{" "}
                The rate peaked at {summaryStats.maxRate.toFixed(2)}% and
                reached its lowest point at {summaryStats.minRate.toFixed(2)}%,
                resulting in a total variation of{" "}
                {(summaryStats.maxRate - summaryStats.minRate).toFixed(2)}{" "}
                percentage points throughout the analyzed period.
              </p>
            </div>
          )}
        </div>
      ) : (
        <div className="rounded-lg sm:rounded-2xl border border-slate-200 bg-white shadow-sm p-4 sm:p-6 text-center transition-colors">
          <p className="text-xs sm:text-sm text-slate-500">
            Select two countries and a product to view tariff rates over time.
          </p>
        </div>
      )}
    </div>
  );
};

export default AgricultureTariffChart;
