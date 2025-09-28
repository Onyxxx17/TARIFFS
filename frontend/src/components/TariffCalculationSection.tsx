import { useState, useEffect } from "react";
import GeoChart from "./GeoCharts";
import CountrySelect, { type CountryOption } from "./CountrySelect";
import TariffResult from "./TariffResult";
import { retrieveAllTodosForUsernameApiByToCountryCode, retriveTariffRulesByCriteria } from "../api/TariffApiService.js";


type Picked = { name: string; code?: string } | null;


export default function TariffCalculatorSection() {

  const [tariffs, setTariffs] = useState([]);
  const [currentTariffRate, setCurrentTariffRate] = useState(0);

  // click logic:  To,  From, alternate
  const [phase, setPhase] = useState<"to" | "from">("to");
  const [to, setTo] = useState<Picked>(null);
  const [from, setFrom] = useState<Picked>(null);

  // result sheet
  const [showResult, setShowResult] = useState(false);

  // form fields
  const [quantity, setQuantity] = useState<number | "">("");
  const [year, setYear] = useState("");
  const [desc, setDesc] = useState("");
  const [productValue, setProductValue] = useState<number | "">("");
  const [hs, setHs] = useState("");

  // validation errors
  const [validationErrors, setValidationErrors] = useState({});
 
  const years = Array.from({ length: 2025 - 1996 + 1 }, (_, i) => 1996 + i);

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
    setQuantity("")
    setYear("")
    setDesc("");
    setHs("");
    setProductValue("");
    setCurrentTariffRate(0);
    setValidationErrors({});
    setShowResult(false);
  };

  // Handle exclusive product description input
  const handleDescriptionChange = (e) => {
    const value = e.target.value;
    setDesc(value);
    // If user starts typing description, clear HS code
    if (value.trim().length > 0 && hs.trim().length > 0) {
      setHs("");
    }
  };

  // Handle exclusive HS code input
  const handleHsCodeChange = (e) => {
    const value = e.target.value;
    setHs(value);
    // If user starts typing HS code, clear description
    if (value.trim().length > 0 && desc.trim().length > 0) {
      setDesc("");
    }
  };

  // show sheet
  const submit = () => {
    const errors = {};
    
    // Validate required fields
    if (!to) {
      errors.toCountry = "Destination country is required";
    }
    
    if (!year || year === "") {
      errors.year = "Year is required";
    }
    
    if (!quantity || quantity === "") {
      errors.quantity = "Quantity is required";
    }
    
    if (!productValue || productValue === "") {
      errors.productValue = "Product value is required";
    }
    
    // Validate that exactly one of description or HS code is provided
    const hasDescription = desc.trim().length > 0;
    const hasHsCode = hs.trim().length > 0;
    
    if (!hasDescription && !hasHsCode) {
      errors.productInfo = "Either product description or HS code is required";
    } else if (hasDescription && hasHsCode) {
      errors.productInfo = "Please provide either product description or HS code, not both";
    }
    
    // If there are validation errors, don't submit
    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      return;
    }
    
    // Clear any previous validation errors
    setValidationErrors({});

    const payload = {
      toCountry: to?.code,
      fromCountry: from?.code || "",
      quantity: quantity,
      year: year,
      description: desc.trim() || "",
      hsCode: hs.trim() || "",
      productValue: productValue,
    };

    displayTariffsDetailsByCriteria(payload.fromCountry, payload.toCountry, payload.year, payload.description, payload.hsCode)

    console.log("Submit payload:", payload);

    setShowResult(true);
  };

  const toValue: CountryOption | null = to
    ? { name: to.name, code: to.code ?? "" }
    : null;
  const fromValue: CountryOption | null = from
    ? { name: from.name, code: from.code ?? "" }
    : null;

  function displayTariffsDetailsByCriteria(fromCountryName, toCountryName, effectiveYear, productName, productId) {
    retriveTariffRulesByCriteria(fromCountryName, toCountryName, effectiveYear, productName, productId)
    .then(response => {
      setTariffs(response.data);
      setCurrentTariffRate(getAverageTariff(response.data));
    })
    .catch(error => console.log(error))
  }

  function getAverageTariff(tariffs) {
      if (tariffs.length === 0) {
        return 0
      }
      let sum = 0
      for (let i = 0; i < tariffs.length; i++) {
        sum += tariffs[i].rate
      }
      return sum/tariffs.length
  }

  return (
    <section className="py-10 bg-white">
      <div className="max-w-[1200px] mx-auto px-4">
        {/* Map section header  */}
        <div className="text-center mb-8">
          {/*  badge */}
          <span className="inline-flex items-center gap-2 rounded-lg border border-slate-200/70 bg-white px-3 py-1 text-xs font-medium text-slate-600 shadow-sm">
            <span className="h-2 w-2 rounded-full bg-blue-600" />
            Calculate Tariffs
          </span>

          {/* headline */}
          <h2 className="mt-4 text-4xl md:text-5xl font-extrabold tracking-tight text-slate-900">
            Select your{" "}
            <span className="bg-gradient-to-r from-blue-900 via-blue-500 to-blue-700 bg-clip-text text-transparent">
              Trade Destination
            </span>
          </h2>

          {/* subhead */}
          <p className="mt-3 text-base md:text-lg text-slate-600 max-w-3xl mx-auto">
            For hundreds of destinations{" "}
            <span className="font-semibold text-slate-800">worldwide </span>-
            clear, simple, and accurate.
          </p>
          <p className="mt-3 text-base md:text-lg text-slate-600 max-w-3xl mx-auto">
          
            <h2></h2>
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
        <div className="mt-8 rounded-2xl border border-slate-200 bg-white shadow-sm w-200 mx-auto">
          {/* Header */}
          <div className="px-6 pt-6">
           
            {/* title for form  */}
            <div className="text-center mb-6">
              <h3 className="mt-3 text-2xl font-semibold text-slate-900">
                Calculate Tariffs
              </h3>
              <p className="mt-1 text-sm text-slate-500">
                Pick countries and enter basic product info to estimate landed
                costs.
              </p>
            </div>
           
            <div className="flex items-center gap-3" />
            <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-7">
              <div>
                <CountrySelect
                  label="Tariffs to: *"
                  value={toValue}
                  onPick={onPickTo}
                  placeholder="Type or click a Country"
                />
                {validationErrors.toCountry && (
                  <p className="text-red-500 text-xs mt-1">{validationErrors.toCountry}</p>
                )}
              </div>
              
              <CountrySelect
                label="Tariffs from:"
                value={fromValue}
                onPick={onPickFrom}
                placeholder="Type or click a Country"
              />
             
              <div>
                <label htmlFor="quantity" className="text-sm font-medium text-gray-700" style={{fontSize: "12px", marginRight: "10px"}}>
                  Quantity: *
                </label>  
                <input
                  id="name"
                  type="text"
                  value={quantity}
                  onChange={(e) => {
                    const val = e.target.value;
                    // Allow empty string so user can delete
                    if (val === "") {
                      setQuantity("");
                      return;
                    }
                    // Only set numeric values
                    const num = Number(val);
                    if (!isNaN(num)) {
                      setQuantity(num);
                    }
                  }}
                  placeholder="Enter the quantity"
                  className={`border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${validationErrors.quantity ? 'border-red-500' : ''}`}
                />
                {validationErrors.quantity && (
                  <p className="text-red-500 text-xs mt-1">{validationErrors.quantity}</p>
                )}
              </div>

             <div>
                <label htmlFor="year" className="w-full text-sm font-medium text-gray-700" style={{fontSize: "12px", marginRight: "10px"}}>
                    Year: *
                </label>
                <select    
                  id="year"
                  value={year}
                  onChange={(e) => setYear(Number(e.target.value))}
                  className={`border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${validationErrors.year ? 'border-red-500' : ''}`}
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
                {validationErrors.year && (
                  <p className="text-red-500 text-xs mt-1">{validationErrors.year}</p>
                )}
              </div>

              {year && <p className="text-sm text-gray-600">You selected: {year}</p>}
            </div>

            <div className="mt-3 flex items-center gap-3">
              <button
                type="button"
                onClick={swap}
                disabled={!to && !from}
                className="inline-flex items-center px-2 py-1 rounded-md border border-slate-300 text-sm hover:bg-slate-50 disabled:opacity-50"
              >
                Swap
              </button>
              <button
                type="button"
                onClick={clearCountries}
                className="inline-flex items-center px-2 py-1 rounded-md border border-slate-300 text-sm hover:bg-slate-50"
              >
                Clear
              </button>
            </div>
          </div>

          {/* Divider with label */}
          <div className="mt-6 px-6">
            <div className="relative">
              <div className="border-t border-slate-200" />
              <span className="absolute left-1/2 -translate-x-1/2 -top-3 bg-white px-3 text-[11px] tracking-wide text-slate-500">
                PRODUCT DETAILS
              </span>
            </div>
          </div>

          {/* Details */}
          <div className="px-6 pb-6">
            <div className="mt-4 space-y-4">
              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1">
                  Product Description
                  {hs.trim().length > 0 && (
                    <span className="text-slate-400 ml-1">(disabled - HS code provided)</span>
                  )}
                </label>
                <input
                  value={desc}
                  onChange={handleDescriptionChange}
                  disabled={hs.trim().length > 0}
                  placeholder="e.g., grains (Maize, Wheat, Rice)"
                  className={`w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200 ${
                    hs.trim().length > 0 ? 'bg-slate-100 text-slate-500 cursor-not-allowed' : ''
                  }`}
                />
              </div>

              {/* OR divider */}
              <div className="relative text-center my-3 mt-6">
                <div className="border-t border-slate-200" />
                <span className="absolute left-1/2 -translate-x-1/2 -top-3 bg-white px-2 text-[11px] text-slate-400">
                  OR
                </span>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1">
                  HS Code
                  {desc.trim().length > 0 && (
                    <span className="text-slate-400 ml-1">(disabled - description provided)</span>
                  )}
                </label>
                <input
                  value={hs}
                  onChange={handleHsCodeChange}
                  disabled={desc.trim().length > 0}
                  placeholder="e.g., 010378"
                  className={`w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200 ${
                    desc.trim().length > 0 ? 'bg-slate-100 text-slate-500 cursor-not-allowed' : ''
                  }`}
                />
              </div>

              {validationErrors.productInfo && (
                <p className="text-red-500 text-xs mt-1">{validationErrors.productInfo}</p>
              )}

              {/* Product value */}
              <div className="mt-4 max-w-6xl mx-auto">
                <label className="block text-xs font-medium text-slate-600 mb-1">
                  Product Value *
                  <span className="text-slate-400">(in dollars)</span>
                </label>

                <input
                  type="text"
                  value={productValue}
                  onChange={(e) => {
                    const val = e.target.value;
                    // Allow empty string so user can delete
                    if (val === "") {
                      setProductValue("");
                      return;
                    }
                    // Only set numeric values
                    const num = Number(val);
                    if (!isNaN(num)) {
                      setProductValue(num);
                    }
                  }}
                  placeholder="0"
                  className={`w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200 ${validationErrors.productValue ? 'border-red-500' : ''}`}
                />
                {validationErrors.productValue && (
                  <p className="text-red-500 text-xs mt-1">{validationErrors.productValue}</p>
                )}
              </div>
            </div>

            {/* Footer */}
            <div className="mt-8 flex flex-col sm:flex-row gap-3">
              <button
                onClick={submit}
                className="inline-flex justify-center items-center rounded-lg bg-[#1450ef] text-white text-sm px-4 py-2.5 hover:bg-blue-700 shadow-sm"
              >
                Calculate Tariff
              </button>
              <button
                onClick={() => {
                  setDesc("");
                  setHs("");
                  setQuantity("");
                  setYear("")
                  setProductValue("")
                  setCurrentTariffRate(0);
                  setValidationErrors({});
                  setShowResult(false);
                }}
                className="inline-flex justify-center items-center rounded-lg border border-slate-300 text-slate-700 text-sm px-4 py-2.5 hover:bg-slate-50"
              >
                Reset Details
              </button>
            </div>
          </div>
        </div>
        {/* /card */}

        {/* Result sheet  */}
        {showResult && (
          <TariffResult
            to={to}
            from={from}
            quantity={quantity}
            year={year}
            description={desc || ""}
            hsCode={hs || ""}
            productValue = {productValue}
            tariffRate = {currentTariffRate}
            totalPrice = {quantity * productValue * (currentTariffRate / 100 + 1)}
            onReset={() => setShowResult(false)}
            onEdit={() => window.scrollTo({ top: 0, behavior: "smooth" })}
         
          />
        )}
      </div>
    </section>
  );
}