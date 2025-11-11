
import { useState } from "react";
import { fetchWithAuth } from "../utils/api";
import CountrySelect, { type CountryOption } from "../components/CountrySelect";
import ProductSelect from "../components/ProductSelect";

interface Product {
  id: number;
  name: string;
  category: {
    id: number;
    name: string;
  };
}

interface TariffPredictionResponse {
  fromCountryCode: string;
  toCountryCode: string;
  productId: number;
  predictedYear: number;
  predictedRate: number;
  modelFit: number;
  historicalRates?: { year: number; rate: number }[];
}

// Helper function to fetch country name from the country table
const getCountryName = async (countryCode: string): Promise<string> => {
  try {
    const res = await fetchWithAuth(`/api/countries/${countryCode}`);
    if (res.ok) {
      const data = await res.json();
      return data.name || countryCode;
    }
  } catch (err) {
    console.error(`Error fetching country name for ${countryCode}:`, err);
  }
  return countryCode;
};

export default function TariffPredictionPage() {
  const [fromCountry, setFromCountry] = useState<CountryOption | null>(null);
  const [toCountry, setToCountry] = useState<CountryOption | null>(null);
  const [product, setProduct] = useState<Product | null>(null);
  const [year, setYear] = useState<string>("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [result, setResult] = useState<TariffPredictionResponse | null>(null);
  const [fromCountryName, setFromCountryName] = useState<string>("");
  const [toCountryName, setToCountryName] = useState<string>("");

  // Generate future years dropdown
  const years = Array.from({ length: 2050 - 2026 + 1 }, (_, i) => 2026 + i);

  const getCountryCodeByName = async (name: string): Promise<string | null> => {
    try {
      const res = await fetchWithAuth(`/api/countries/search/by-name?name=${encodeURIComponent(name)}`);
      if (res.ok) {
        const data = await res.json();
        return data.countryCode;
      }
      return null;
    } catch (err) {
      console.error("Error fetching country code:", err);
      return null;
    }
  };

  const handlePredict = async () => {
    setError("");
    setResult(null);

    if (!fromCountry || !toCountry || !product || !year) {
      setError("All fields are required.");
      return;
    }

    setLoading(true);

    try {
      const fromCode = await getCountryCodeByName(fromCountry.name);
      const toCode = await getCountryCodeByName(toCountry.name);

      if (!fromCode || !toCode) {
        setError("Could not find valid country codes.");
        setLoading(false);
        return;
      }

      const payload = {
        fromCountryCode: fromCode,
        toCountryCode: toCode,
        productId: product.id,
        predictedYear: Number(year),
      };

      console.log("Sending payload:", payload);

      const res = await fetchWithAuth("/api/tariffs/predict", {
        method: "POST",
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const errData = await res.json();
        throw new Error(errData.message || "Prediction failed.");
      }

      const data: TariffPredictionResponse = await res.json();
      setResult(data);

      // Fetch country names from the country table
      const fromName = await getCountryName(data.fromCountryCode);
      const toName = await getCountryName(data.toCountryCode);
      setFromCountryName(fromName);
      setToCountryName(toName);
    } catch (err: any) {
      console.error("Prediction error:", err);
      setError(err.message || "An unexpected error occurred.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-6 px-2 sm:py-12 sm:px-6 lg:px-8 transition-colors">
      <div className="max-w-2xl sm:max-w-5xl mx-auto">
        <div className="text-center mb-4 sm:mb-8">
          <h1 className="text-2xl sm:text-4xl font-extrabold text-slate-900 mb-1">
            Tariff Rate Prediction
          </h1>
          <p className="text-slate-600 text-xs sm:text-base">
            Predict future tariff rates based on historical trade data.
          </p>
        </div>

        <div className="bg-white rounded-xl shadow-md p-4 sm:p-8 border border-slate-200">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3 md:gap-4 mb-3 md:mb-6">
            <CountrySelect label="Exporting From" value={fromCountry} onPick={setFromCountry} />
            <CountrySelect label="Importing To" value={toCountry} onPick={setToCountry} />
            <ProductSelect label="Product" value={product} onPick={setProduct} placeholder="Search product..." />

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">
                Predict Year
              </label>
              <select
                value={year}
                onChange={(e) => setYear(e.target.value)}
                className="w-full border border-slate-300 bg-white text-slate-900 rounded-md px-2 py-1.5 text-sm focus:ring-2 focus:ring-blue-500"
              >
                <option value="">-- Select Year --</option>
                {years.map((y) => (
                  <option key={y} value={y}>
                    {y}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {error && (
            <div className="mb-2 p-1 text-xs bg-red-100 border border-red-300 text-red-700 rounded-md">
              {error}
            </div>
          )}

          <button
            onClick={handlePredict}
            disabled={loading}
            className="w-full sm:w-auto px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-medium shadow-md transition-colors disabled:opacity-60 text-sm"
          >
            {loading ? "Predicting..." : "Predict Tariff Rate"}
          </button>
        </div>

        {result && (
          <div className="mt-6 sm:mt-10 bg-white rounded-xl shadow-md border border-slate-200 p-4 sm:p-8">
            <h2 className="text-lg sm:text-2xl font-bold text-slate-900 mb-2">
              Prediction Results
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-2 md:gap-3 text-slate-800 text-sm">
              <p>
                <span className="font-semibold">From:</span> {fromCountryName}
              </p>
              <p>
                <span className="font-semibold">To:</span> {toCountryName}
              </p>
              <p>
                <span className="font-semibold">Product ID:</span> {result.productId}
              </p>
              <p>
                <span className="font-semibold">Predicted Year:</span> {result.predictedYear}
              </p>
              <p>
                <span className="font-semibold">Predicted Tariff Rate:</span>{" "}
                {result.predictedRate.toFixed(2)}%
              </p>
              <p>
                <span className="font-semibold">Model Fit (R²):</span>{" "}
                {result.modelFit.toFixed(4)}
              </p>

            <div className="mt-2 text-sm text-slate-500 italic">
                *Note: Tariff Prediction rates are calculated using a simple linear regression model, so some predictions may be inaccurate.
            </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}