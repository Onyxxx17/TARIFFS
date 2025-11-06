import { useState, useRef, useEffect } from "react";
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
  const [yearOpen, setYearOpen] = useState(false);
  const [yearQ, setYearQ] = useState<string>(year);
  const yearBoxRef = useRef<HTMLDivElement | null>(null);

  // close year dropdown when clicking outside
  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (!yearBoxRef.current) return;
      if (!yearBoxRef.current.contains(e.target as Node)) {
        setYearOpen(false);
      }
    };
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, []);

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
    setYearQ("");
    setYearOpen(false);
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

           // ✅ Extract the first additional fee percentage or use 0 if none
          const additionalFeePercentage = result.additionalFees && result.additionalFees.length > 0 
            ? Number(result.additionalFees[0]) 
            : 0;

          console.log("Calculation Result:", result);
          console.log("Additional Fees from response:", result.additionalFees);
          console.log("Extracted Additional Fee Percentage:", additionalFeePercentage);

          const savePayload = {
            fromCountryId: fromCountryCode,
            toCountryId: toCountryCode,
            productId: product.id,
            value: Number(value),
            year: Number(year),
            tariffRate: result.tariffRate,
            calculatedTariff: result.calculatedTariff,
            additionalFee: additionalFeePercentage,
            totalCost: result.totalCost,
            calculationType: result.calculationType
          };

          console.log("Save Payload being sent to backend:", savePayload);

          const saveResponse = await fetchWithAuth("/api/import-records/save-calculation", {
            method: "POST",
            body: JSON.stringify(savePayload),
          });

          if (!saveResponse.ok) {
            console.error("Failed to save calculation to backend");
          } else {
            console.log("Calculation saved successfully!");
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
    <section id="tariff-calculation" className="pt-3 pb-6 sm:py-6 bg-white transition-colors">
      <div className="max-w-[1200px] mx-auto px-4 sm:px-6 md:px-4">
        {/* Map section header  */}
        <div className="text-center mb-8">
          {/*  badge */}
          <span className="inline-flex items-center gap-2 rounded-lg border border-slate-200/70 bg-white px-3 py-1 text-xs font-medium text-slate-600 shadow-sm">
            <span className="h-2 w-2 rounded-full bg-blue-600" />
            Calculate Tariffs
          </span>

          {/* headline */}
          <h2 className="mt-4 text-2xl sm:text-3xl md:text-5xl font-extrabold tracking-tight text-slate-900">
            Select your{" "}
            <span className="bg-gradient-to-r from-blue-900 via-blue-500 to-blue-700 bg-clip-text text-transparent">
              Trade Destination
            </span>
          </h2>

          {/* subhead */}
          <p className="mt-3 text-sm sm:text-base md:text-lg text-slate-600 max-w-3xl mx-auto">
            For hundreds of destinations{" "}
            <span className="font-semibold text-slate-800">worldwide </span>-
            clear, simple, and accurate.
          </p>
        </div>

        {/* Map - show compact map on mobile, full map on tablet+ */}
        <div className="block sm:hidden mb-4">
          <GeoChart
            height={260}
            baseColor="#b3c5db"
            highlightColor="#030e61"
            onPick={onCountryPickFromMap}
          />
        </div>

        <div className="hidden sm:block mb-8">
          <GeoChart
            height={560}
            baseColor="#b3c5db"
            highlightColor="#030e61"
            onPick={onCountryPickFromMap}
          />
        </div>

  {/* "Paper" card */}
  <div className="mt-6 sm:mt-8 rounded-xl sm:rounded-2xl border border-slate-200 bg-white shadow-sm mx-auto transition-colors w-full md:max-w-3xl">
          {/* Header */}
          <div className="px-3 sm:px-6 pt-3 sm:pt-6">
            {/* title for form  */}
            <div className="text-center mb-3 sm:mb-5">
              <h3 className="text-lg sm:text-2xl font-semibold text-slate-900">
                Calculate Tariffs
              </h3>
              <p className="mt-1 text-xs text-slate-500">
                Pick countries and enter product info
              </p>
            </div>

            <div className="flex items-center gap-3" />
            <div className="mt-3 sm:mt-4 grid grid-cols-1 md:grid-cols-2 gap-1 sm:gap-6">
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
                <label className="block text-sm font-medium text-gray-700 mb-2">
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
                    <span className="ml-2 text-sm text-gray-700">By Quantity</span>
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
                    <span className="ml-2 text-sm text-gray-700">By Weight</span>
                  </label>
                </div>
              </div>

              {calculationType === "QUANTITY" ? (
                <div>
                  <label
                    htmlFor="quantity"
                    className="text-sm font-medium text-gray-700"
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
                    className="w-full border border-slate-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
              ) : (
                <div>
                  <label
                    htmlFor="weight"
                    className="text-sm font-medium text-gray-700"
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
                    className="w-full border border-slate-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
              )}

              <div>
                <label
                  htmlFor="year"
                  className="w-full text-sm font-medium text-gray-700"
                  style={{ fontSize: "12px", marginRight: "10px" }}
                >
                  Year:
                </label>
                <div className="relative" ref={yearBoxRef}>
                  <input
                    id="year"
                    value={yearQ}
                    onChange={(e) => {
                      setYearQ(e.target.value);
                      setYearOpen(true);
                    }}
                    onFocus={() => setYearOpen(true)}
                    placeholder="-- Choose a year --"
                    className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring focus:ring-blue-200 bg-white"
                  />

                  {yearOpen && (
                    <div className="absolute z-10 mt-1 w-full max-h-48 overflow-auto rounded-md border border-slate-300 bg-white shadow-lg">
                      {years
                        .filter((y) =>
                          yearQ.trim() === "" ? true : String(y).includes(yearQ.trim())
                        )
                        .slice(0, 50)
                        .map((y) => (
                          <button
                            key={y}
                            type="button"
                            onClick={() => {
                              setYear(String(y));
                              setYearQ(String(y));
                              setYearOpen(false);
                            }}
                            className="w-full text-left px-3 py-2 text-sm text-slate-900 hover:bg-slate-50"
                          >
                            {y}
                          </button>
                        ))}
                      {years.filter((y) => (yearQ.trim() === "" ? true : String(y).includes(yearQ.trim()))).length === 0 && (
                        <div className="px-3 py-2 text-sm text-slate-400">No matches</div>
                      )}
                    </div>
                  )}
                </div>
              </div>
            </div>

            <div className="mt-4 flex flex-col sm:flex-row items-center gap-2 sm:gap-3">
              <button
                type="button"
                onClick={swap}
                disabled={!to && !from}
                className="w-full sm:w-auto inline-flex items-center justify-center px-3 sm:px-2 py-2 sm:py-1 rounded-md border border-slate-300 text-xs sm:text-sm text-slate-700 hover:bg-slate-50 disabled:opacity-50 transition-colors"
              >
                Swap
              </button>
              <button
                type="button"
                onClick={clearCountries}
                className="w-full sm:w-auto inline-flex items-center justify-center px-3 sm:px-2 py-2 sm:py-1 rounded-md border border-slate-300 text-xs sm:text-sm text-slate-700 hover:bg-slate-50 transition-colors"
              >
                Clear All
              </button>
            </div>
          </div>

          {/* Divider with label */}
          <div className="mt-5 px-3 sm:px-6">
            <div className="relative">
              <div className="border-t border-slate-200" />
              <span className="absolute left-1/2 -translate-x-1/2 -top-3 bg-white px-3 text-[11px] tracking-wide text-slate-500">
                PRODUCT DETAILS
              </span>
            </div>
          </div>

          {/* Details */}
          <div className="px-3 sm:px-6 pb-3">
            <div className="mt-3 space-y-2">
              {/* Product Select */}
              <ProductSelect
                label="Product"
                value={product}
                onPick={setProduct}
                placeholder="Search by HS code or product name..."
              />

              {/* Product value */}
              <div className="mt-4">
                <label className="block text-xs font-medium text-slate-600 mb-1">
                  Unit Price{" "}
                  <span className="text-slate-400">(in dollars)</span>
                </label>

                <input
                  type="number"
                  min={0}
                  step={0.01}
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  placeholder="0.00"
                  className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200"
                />
              </div>
            </div>

            {/* Footer */}
            <div className="mt-5 flex flex-col gap-2">
              <button
                onClick={submit}
                className="w-full inline-flex justify-center items-center rounded-lg bg-[#1450ef] hover:bg-blue-700 text-white text-sm px-3 py-2 sm:px-4 sm:py-3 shadow-sm transition-colors font-medium"
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
                  setYearQ("");
                  setYearOpen(false);
                  setShowResult(false);
                }}
                className="w-full inline-flex justify-center items-center rounded-lg border border-slate-300 text-slate-700 text-sm px-3 py-2 sm:px-4 sm:py-3 hover:bg-slate-50 transition-colors"
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
