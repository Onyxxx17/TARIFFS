import { useState, useEffect, useRef, useMemo } from 'react';
import { BASE_URL } from '../config';

interface Country {
  countryCode: string;
  countryName: string;
}

interface CountrySelectSimpleProps {
  label: string;
  value: Country | null;
  onPick: (country: Country | null) => void;
  placeholder?: string;
}

export default function CountrySelectSimple({ 
  label, 
  value, 
  onPick, 
  placeholder = "Type or click a Country" 
}: CountrySelectSimpleProps) {
  const [countries, setCountries] = useState<Country[]>([]);
  const [open, setOpen] = useState(false);
  const [q, setQ] = useState(value?.countryName ?? "");
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Fetch countries on mount
  useEffect(() => {
    const fetchCountries = async () => {
      try {
        console.log('Fetching from:', `${BASE_URL}/api/countries`);
        const response = await fetch(`${BASE_URL}/api/countries`);
        if (!response.ok) throw new Error("Failed to fetch countries");
        const data = await response.json();
        console.log('Countries loaded:', data.length, data.slice(0, 3));
        setCountries(data);
      } catch (err) {
        console.error('Error loading countries:', err);
      }
    };

    fetchCountries();
  }, []);

  // Close on outside click
  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (!dropdownRef.current?.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, []);

  // Update search query when value changes
  useEffect(() => {
    setQ(value?.countryName ?? "");
  }, [value?.countryName]);

  // Filter countries based on search term
  const filtered = useMemo(() => {
    if (!countries || countries.length === 0) return [];
    const term = q.trim().toLowerCase();
    if (!term) return countries.slice(0, 50);
    return countries
      .filter(
        (c) =>
          c.countryName.toLowerCase().includes(term) ||
          c.countryCode.toLowerCase().includes(term)
      )
      .slice(0, 50);
  }, [q, countries]);

  return (
    <div className="flex flex-col gap-1" ref={dropdownRef}>
      <label className="block text-xs font-medium text-slate-600 mb-1">
        {label}
      </label>

      <div className="relative">
        <input
          value={q}
          onChange={(e) => {
            setQ(e.target.value);
            setOpen(true);
          }}
          onFocus={() => setOpen(true)}
          placeholder={placeholder}
          className="w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring focus:ring-blue-200"
        />

        {open && (
          <div className="absolute z-10 mt-1 w-full max-h-64 overflow-auto rounded-md border bg-white shadow">
            {filtered.map((country) => (
              <button
                key={country.countryCode}
                type="button"
                onClick={() => {
                  onPick(country);
                  setQ(country.countryName);
                  setOpen(false);
                }}
                className="w-full text-left px-3 py-2 text-sm hover:bg-slate-50"
              >
                {country.countryName}
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
