import { useState } from "react";
import GeoChart from "./GeoCharts";
import CountrySelect, { type CountryOption } from "./CountrySelect";
import ProductSelect from "./ProductSelect";
import TariffResult from "./TariffResult";
import { fetchWithAuth } from "../utils/api";
type Picked = { name: string; code?: string } | null;

//Set type according to api response
interface Product {
  id: number;
  name: string;
  category: {
    id: number;
    name: string;
  };
}

export default function TariffCalculatorSection() {
  // click logic:  To,  From, alternate
  const [phase, setPhase] = useState<"to" | "from">("to");
  const [to, setTo] = useState<Picked>(null);
  const [from, setFrom] = useState<Picked>(null);
  const [tariffResult, setTariffResult] = useState(null);
  // result sheet
  const [showResult, setShowResult] = useState(false);

  // form fields
  const [product, setProduct] = useState<Product | null>(null);
  const [value, setValue] = useState("");
  const [quantity, setQuantity] = useState("");
  const [weight, setWeight] = useState("");
  const [calculationType, setCalculationType] = useState<"QUANTITY" | "WEIGHT">("QUANTITY");

  const [year, setYear] = useState("");
  const years = Array.from({ length: 2025 - 1996 + 1 }, (_, i) => 1996 + i);
  const [error, setError] = useState("");

  // map inputs
  const onCountryPickFromMap = (p: { name: string; code?: string }) => {
    const picked = p.code ? { name: p.name, code: p.code } : { name: p.name };
    if (phase === "to") {
      setTo(picked);
      setPhase("from");
    } else {
      setFrom(picked);
      setPhase("to");
    }
  };

  // typing inputs
  const onPickTo = (c: CountryOption | null) =>
    setTo(c ? { name: c.name, code: c.code } : null);
  const onPickFrom = (c: CountryOption | null) =>
    setFrom(c ? { name: c.name, code: c.code } : null);

  const swap = () => {
    const t = to;
    setTo(from);
    setFrom(t);
  };

  // hide sheet + clear fields
  const clearCountries = () => {
    setTo(null);
    setFrom(null);
    setPhase("to");
    setProduct(null);
    setValue("");
    setQuantity("");
    setWeight("");
    setYear("");
    setShowResult(false);
  };

  const submit = async () => {
    setError(""); // clear previous error

    // Validation
    const requiredQuantityOrWeight = calculationType === "QUANTITY" ? quantity : weight;
    if (!from?.name || !to?.name || !product || !value || !requiredQuantityOrWeight || !year) {
      setError("All fields are required.");
      setShowResult(true);
      setTariffResult(null);
      return;
    }

    if (to.name === from.name) {
      setError("Countries cannot be the same.");
      setShowResult(true);
      setTariffResult(null);
      return;
    }

    const payload = {
      fromCountry: from.name,
      toCountry: to.name,
      productId: product.id,
      unitCost: Number(value),
      quantity: calculationType === "QUANTITY" ? Number(quantity) : null,
      weight: calculationType === "WEIGHT" ? Number(weight) : null,
      calculationType: calculationType,
      effectiveYear: Number(year),
    };

    try {
      const response = await fetchWithAuth("/api/tariffs/calculate", {
        method: "POST",
        body: JSON.stringify(payload),
      });
      
      if (!response.ok && response.status == 401) {
        setError("Tariff calculation failed. Please Login First.");
        setShowResult(true);
        setTariffResult(null);
        return;
      }

      const result = await response.json();
      setShowResult(true);
      setTariffResult(result);

      if(!response.ok){
        setError(result.message || result.error || "Tariff calculation failed");
        setShowResult(true);
        setTariffResult(null);
      } else {
        // ✅ Save calculation to backend (no localStorage)
        try {
          // Always fetch country codes from backend by country names
          let fromCountryCode = "";
          let toCountryCode = "";

          // Fetch fromCountry code
          const fromCodeRes = await fetchWithAuth(
            `/api/countries/search/by-name?name=${encodeURIComponent(from.name)}`
          );
          if (fromCodeRes.ok) {
            const data = await fromCodeRes.json();
            fromCountryCode = data.countryCode;
          } else {
            console.error("Failed to fetch fromCountry code");
            return;
          }

          // Fetch toCountry code
          const toCodeRes = await fetchWithAuth(
            `/api/countries/search/by-name?name=${encodeURIComponent(to.name)}`
          );
          if (toCodeRes.ok) {
            const data = await toCodeRes.json();
            toCountryCode = data.countryCode;
          } else {
            console.error("Failed to fetch toCountry code");
            return;
          }

          // ✅ Fix payload to match backend SaveCalculationRequest
          const savePayload = {
            fromCountryId: fromCountryCode,
            toCountryId: toCountryCode,
            productId: product.id,
            value: Number(value),
            year: Number(year),
            tariffRate: result.tariffRate,
            calculatedTariff: result.calculatedTariff,
            additionalFee: result.additionalFees[0],
            totalCost: result.totalCost,
            calculationType: calculationType
          };
          console.log("Save payload:", savePayload);
          const saveResponse = await fetchWithAuth("/api/import-records/save-calculation", {
            method: "POST",
            body: JSON.stringify(savePayload),
          });

          if (!saveResponse.ok) {
            console.error("Failed to save calculation to backend");
          }
        } catch (error) {
          console.error("Error saving calculation to backend:", error);
        }
      }
    } catch (error) {
      setError("Unable to calculate tariff");
      setShowResult(true);
      setTariffResult(null);
    }
  };

  const toValue: CountryOption | null = to
    ? { name: to.name, code: to.code ?? "" }
    : null;
  const fromValue: CountryOption | null = from
    ? { name: from.name, code: from.code ?? "" }
    : null;

  return (
    <section id="tariff-calculation" className="py-10 bg-white dark:bg-slate-900 transition-colors">
      <div className="max-w-[1200px] mx-auto px-4">
        {/* Map section header  */}
        <div className="text-center mb-8">
          {/*  badge */}
          <span className="inline-flex items-center gap-2 rounded-lg border border-slate-200/70 dark:border-slate-700 bg-white dark:bg-slate-800 px-3 py-1 text-xs font-medium text-slate-600 dark:text-slate-300 shadow-sm">
            <span className="h-2 w-2 rounded-full bg-blue-600" />
            Calculate Tariffs
          </span>

          {/* headline */}
          <h2 className="mt-4 text-4xl md:text-5xl font-extrabold tracking-tight text-slate-900 dark:text-white">
            Select your{" "}
            <span className="bg-gradient-to-r from-blue-900 via-blue-500 to-blue-700 dark:from-blue-400 dark:via-blue-300 dark:to-blue-500 bg-clip-text text-transparent">
              Trade Destination
            </span>
          </h2>

          {/* subhead */}
          <p className="mt-3 text-base md:text-lg text-slate-600 dark:text-slate-400 max-w-3xl mx-auto">
            For hundreds of destinations{" "}
            <span className="font-semibold text-slate-800 dark:text-slate-200">worldwide </span>-
            clear, simple, and accurate.
          </p>
        </div>

        {/* Map */}
        <GeoChart
          height={420}
          baseColor="#b3c5db"
          highlightColor="#030e61"
          onPick={onCountryPickFromMap}
        />

        {/* "Paper" card */}
        <div className="mt-8 rounded-2xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 shadow-sm w-200 mx-auto transition-colors">
          {/* Header */}
          <div className="px-6 pt-6">
            {/* title for form  */}
            <div className="text-center mb-6">
              <h3 className="mt-3 text-2xl font-semibold text-slate-900 dark:text-white">
                Calculate Tariffs
              </h3>
              <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">
                Pick countries and enter basic product info to estimate landed
                costs.
              </p>
            </div>

            <div className="flex items-center gap-3" />
            <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-7">
              <CountrySelect
                label="Tariffs to:"
                value={toValue}
                onPick={onPickTo}
                placeholder="Type or click a Country"
              />
              <CountrySelect
                label="Tariffs from: "
                value={fromValue}
                onPick={onPickFrom}
                placeholder="Type or click a Country"
              />

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                  Calculation Type:
                </label>
                <div className="flex gap-4">
                  <label className="inline-flex items-center">
                    <input
                      type="radio"
                      name="calculationType"
                      value="QUANTITY"
                      checked={calculationType === "QUANTITY"}
                      onChange={(e) => setCalculationType(e.target.value as "QUANTITY" | "WEIGHT")}
                      className="form-radio h-4 w-4 text-blue-600"
                    />
                    <span className="ml-2 text-sm text-gray-700 dark:text-slate-300">By Quantity</span>
                  </label>
                  <label className="inline-flex items-center">
                    <input
                      type="radio"
                      name="calculationType"
                      value="WEIGHT"
                      checked={calculationType === "WEIGHT"}
                      onChange={(e) => setCalculationType(e.target.value as "QUANTITY" | "WEIGHT")}
                      className="form-radio h-4 w-4 text-blue-600"
                    />
                    <span className="ml-2 text-sm text-gray-700 dark:text-slate-300">By Weight</span>
                  </label>
                </div>
              </div>

              {calculationType === "QUANTITY" ? (
                <div>
                  <label
                    htmlFor="quantity"
                    className="text-sm font-medium text-gray-700 dark:text-slate-300"
                    style={{ fontSize: "12px", marginRight: "10px" }}
                  >
                    Quantity:
                  </label>
                  <input
                    name="quantity"
                    id="quantity"
                    type="number"
                    min={1}
                    value={quantity}
                    onChange={(e) => setQuantity(e.target.value)}
                    placeholder="Enter the quantity"
                    className="border border-slate-300 dark:border-slate-600 dark:bg-slate-700 dark:text-white rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
              ) : (
                <div>
                  <label
                    htmlFor="weight"
                    className="text-sm font-medium text-gray-700 dark:text-slate-300"
                    style={{ fontSize: "12px", marginRight: "10px" }}
                  >
                    Weight (kg):
                  </label>
                  <input
                    name="weight"
                    id="weight"
                    type="number"
                    min={0}
                    step={0.01}
                    value={weight}
                    onChange={(e) => setWeight(e.target.value)}
                    placeholder="Enter weight in kg"
                    className="border border-slate-300 dark:border-slate-600 dark:bg-slate-700 dark:text-white rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
              )}

              <div>
                <label
                  htmlFor="year"
                  className="w-full text-sm font-medium text-gray-700 dark:text-slate-300"
                  style={{ fontSize: "12px", marginRight: "10px" }}
                >
                  Year:
                </label>
                <select
                  id="year"
                  value={year}
                  onChange={(e) => setYear(e.target.value)}
                  className="border border-slate-300 dark:border-slate-600 dark:bg-slate-700 dark:text-white rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="" disabled>
                    -- Choose a year --
                  </option>
                  {years.map((y) => (
                    <option key={y} value={y}>
                      {y}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="mt-3 flex items-center gap-3">
              <button
                type="button"
                onClick={swap}
                disabled={!to && !from}
                className="inline-flex items-center px-2 py-1 rounded-md border border-slate-300 dark:border-slate-600 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-50 transition-colors"
              >
                Swap
              </button>
              <button
                type="button"
                onClick={clearCountries}
                className="inline-flex items-center px-2 py-1 rounded-md border border-slate-300 dark:border-slate-600 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors"
              >
                Clear All
              </button>
            </div>
          </div>

          {/* Divider with label */}
          <div className="mt-6 px-6">
            <div className="relative">
              <div className="border-t border-slate-200 dark:border-slate-700" />
              <span className="absolute left-1/2 -translate-x-1/2 -top-3 bg-white dark:bg-slate-800 px-3 text-[11px] tracking-wide text-slate-500 dark:text-slate-400">
                PRODUCT DETAILS
              </span>
            </div>
          </div>

          {/* Details */}
          <div className="px-6 pb-6">
            <div className="mt-6 space-y-4">
              {/* Product Select */}
              <ProductSelect
                label="Product"
                value={product}
                onPick={setProduct}
                placeholder="Search by HS code or product name..."
              />

              {/* Product value */}
              <div className="mt-4">
                <label className="block text-xs font-medium text-slate-600 dark:text-slate-300 mb-1">
                  Unit Price{" "}
                  <span className="text-slate-400 dark:text-slate-500">(in dollars)</span>
                </label>

                <input
                  type="number"
                  min={0}
                  step={0.01}
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  placeholder="0.00"
                  className="w-full rounded-lg border border-slate-300 dark:border-slate-600 dark:bg-slate-700 dark:text-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200 dark:focus:ring-blue-500"
                />
              </div>
            </div>

            {/* Footer */}
            <div className="mt-8 flex flex-col sm:flex-row gap-3">
              <button
                onClick={submit}
                className="inline-flex justify-center items-center rounded-lg bg-[#1450ef] hover:bg-blue-700 text-white text-sm px-4 py-2.5 shadow-sm transition-colors"
              >
                Calculate Tariff
              </button>
              <button
                onClick={() => {
                  setProduct(null);
                  setValue("");
                  setQuantity("");
                  setWeight("");
                  setYear("");
                  setShowResult(false);
                }}
                className="inline-flex justify-center items-center rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 text-sm px-4 py-2.5 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors"
              >
                Reset Details
              </button>
            </div>
          </div>
        </div>
        {/* /card */}

        {/* Result sheet  */}
        {showResult && (
          <TariffResult tariffResult={tariffResult} error={error} />
        )}
      </div>
    </section>
  );
}
