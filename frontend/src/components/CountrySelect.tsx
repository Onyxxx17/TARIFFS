import { useEffect, useMemo, useRef, useState } from "react";
import countriesLib from "i18n-iso-countries";
import en from "i18n-iso-countries/langs/en.json";

countriesLib.registerLocale(en);

export type CountryOption = { name: string; code: string };

type Props = {
  label: string;
  value: CountryOption | null;
  placeholder?: string;
  onPick: (opt: CountryOption | null) => void;
};

function buildAllCountries(): CountryOption[] {
  const names = countriesLib.getNames("en") as Record<string, string>; //  name

  return Object.entries(names)
    .map(([alpha2, name]) => {
      const a3 = countriesLib.alpha2ToAlpha3(alpha2) as string | undefined; 
      if (!a3) return null;                         // skip invalid entries
      return { name, code: name };                  // Use full country name as code
    })
    .filter(Boolean) as CountryOption[];
}

export default function CountrySelect({
  label,
  value,
  onPick,
  placeholder = "Type or click a Country",
}: Props) {
  const all = useMemo(() => buildAllCountries(), []);
  const [open, setOpen] = useState(false);
  const [q, setQ] = useState(value?.name ?? "");
  const boxRef = useRef<HTMLDivElement>(null);

  // close on outside click
  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (!boxRef.current?.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, []);

  useEffect(() => {
    setQ(value?.name ?? "");
  }, [value?.name]);

  const filtered = useMemo(() => {
    const term = q.trim().toLowerCase();
    if (!term) return all.slice(0, 50);
    return all
      .filter(
        (c) =>
          c.name.toLowerCase().includes(term) ||
          c.code.toLowerCase().includes(term)
      )
      .slice(0, 50);
  }, [q, all]);

  return (
    <div className="flex flex-col gap-1" ref={boxRef}>
      <label className="block text-xs font-medium text-slate-500">{label}</label>

      <div className="relative">
        <input
          value={q}
          onChange={(e) => {
            const newValue = e.target.value;
            setQ(newValue);
            setOpen(true);
            
            // If input is cleared, automatically clear the selection
            if (newValue.trim() === "") {
              onPick(null);
            }
          }}
          onFocus={() => setOpen(true)}
          placeholder={placeholder}
          className="w-full rounded-md border px-3 py-2 pr-8 text-sm focus:outline-none focus:ring focus:ring-blue-200"
        />

        {value && (
          <button
            type="button"
            onClick={() => {
              onPick(null);
              setQ("");
              setOpen(false);
            }}
            className="absolute right-2 top-1/2 transform -translate-y-1/2 text-slate-400 hover:text-slate-600 text-lg leading-none"
          >
            ×
          </button>
        )}

        {open && (
          <div className="absolute z-10 mt-1 w-full max-h-64 overflow-auto rounded-md border bg-white shadow">
            {filtered.map((opt) => (
                              <button
                key={opt.code}
                type="button"
                onClick={() => {
                  onPick(opt);     // returns { name, code: "United States" }
                  setQ(opt.name);  // show name in search bar
                  setOpen(false);
                }}
                className="w-full text-left px-3 py-2 text-sm hover:bg-slate-50"
              >
                {opt.name}
              </button>
            ))}
            {filtered.length === 0 && (
              <div className="px-3 py-2 text-sm text-slate-400">No matches</div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}