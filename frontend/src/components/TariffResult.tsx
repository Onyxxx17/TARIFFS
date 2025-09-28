import React from "react";
import TariffCalculatorSection from "./TariffCalculationSection";
// import TariffCalculatorSection from "./TariffCalculatorSection"

type Chip = { name: string; code?: string } | null;

export default function TariffResult({
  to,
  from,
  quantity,
  year,
  description,
  hsCode,
  productValue,
  tariffRate,
  totalPrice,
  onReset,
  onEdit,
}: {
  to?: Chip;
  from?: Chip;
  description?: string;
  hsCode?: string;
  onReset?: () => void;
  onEdit?: () => void;
}) {
  return (
    <div className="mt-8 rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden w-200 mx-auto">
      {/* Header */}
      <div className="px-6 py-4 bg-slate-50/60 border-b border-slate-200 flex items-center gap-3">
        <div className="inline-flex items-center justify-center w-7 h-7 rounded-full bg-blue-100 text-blue-700 text-xs font-semibold">✓</div>
        
        <h3 className="text-base sm:text-lg font-semibold text-slate-900">Tariff Result</h3>
        
        <div className="ml-auto flex items-center gap-2">
          
          {onReset && (
            <button onClick={onReset} className="px-3 py-2 text-sm rounded-md border border-slate-300 text-slate-700 hover:bg-slate-100">
              New Calculation
            </button>
          )}
        </div>
      </div>

      

      {/* Empty message */}
      <div className="px-6 py-10">
        
          
          <p className="mt-2 text-l text-slate-500">
          
          <p>From: {from?.name} ({from?.code})</p>
          <p>To: {to?.name} ({to?.code})</p>
          <p>Quantity: {quantity}</p>
          <p>Year: {year}</p>
          <h2>Description: {description}</h2>
          <p>HS Code: {hsCode}</p>
          <p>Product Value: ${productValue}/unit</p>
          <p>Tariff Rate: {tariffRate}%</p>
          <p>Total Price: ${totalPrice}</p>
          </p>

        </div>
      </div>
   
  );
}




