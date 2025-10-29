import { useState, useEffect, useRef } from 'react';
import { fetchWithAuth } from '../utils/api';

interface Country {
  countryCode: string;
  name: string;
}

interface CountrySelectAPIProps {
  label: string;
  value: string;
  onChange: (countryCode: string) => void;
  placeholder?: string;
}

export default function CountrySelectAPI({ 
  label, 
  value, 
  onChange, 
  placeholder = "Search for a country..." 
}: CountrySelectAPIProps) {
  const [countries, setCountries] = useState<Country[]>([]);
  const [filteredCountries, setFilteredCountries] = useState<Country[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Fetch countries on mount
  useEffect(() => {
    const fetchCountries = async () => {
      setLoading(true);
      setError("");
      try {
        const response = await fetchWithAuth('/api/countries', {
          method: 'GET',
        });
        if (!response.ok) throw new Error("Failed to fetch countries");
        const data = await response.json();
        setCountries(data);
        setFilteredCountries(data);
      } catch (err) {
        setError("Failed to load countries");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchCountries();
  }, []);

  // Filter countries based on search term
  useEffect(() => {
    if (!searchTerm.trim()) {
      setFilteredCountries(countries);
      return;
    }

    const term = searchTerm.toLowerCase();
    const filtered = countries.filter(
      (country) =>
        country.countryCode.toLowerCase().includes(term) ||
        country.name.toLowerCase().includes(term)
    );
    setFilteredCountries(filtered);
  }, [searchTerm, countries]);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleSelect = (country: Country) => {
    onChange(country.countryCode);
    setSearchTerm("");
    setIsOpen(false);
  };

  const handleClear = () => {
    onChange("");
    setSearchTerm("");
  };

  const selectedCountry = countries.find(c => c.countryCode === value);

  return (
    <div className="relative" ref={dropdownRef}>
      <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
        {label}
      </label>

      {/* Input field */}
      <div className="relative">
        <input
          type="text"
          value={selectedCountry ? selectedCountry.name : searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            setIsOpen(true);
            if (selectedCountry) onChange("");
          }}
          onFocus={() => setIsOpen(true)}
          placeholder={placeholder}
          className="w-full rounded-lg border border-slate-300 dark:border-slate-600 dark:bg-slate-700 dark:text-white px-3 py-2 pr-8 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200 dark:focus:ring-blue-500"
          disabled={loading}
        />
        
        {/* Clear button */}
        {selectedCountry && (
          <button
            type="button"
            onClick={handleClear}
            className="absolute right-2 top-1/2 -translate-y-1/2 text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-300"
          >
            ✕
          </button>
        )}
      </div>

      {/* Error message */}
      {error && (
        <p className="mt-1 text-xs text-red-600 dark:text-red-400">{error}</p>
      )}

      {/* Dropdown */}
      {isOpen && !loading && (
        <div className="absolute z-50 mt-1 w-full rounded-lg border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700 shadow-lg max-h-64 overflow-y-auto">
          {filteredCountries.length === 0 ? (
            <div className="px-3 py-2 text-sm text-slate-500 dark:text-slate-400">
              No countries found
            </div>
          ) : (
            <ul className="py-1">
              {filteredCountries.map((country) => (
                <li key={country.countryCode}>
                  <button
                    type="button"
                    onClick={() => handleSelect(country)}
                    className="w-full text-left px-3 py-2 text-sm hover:bg-slate-50 dark:hover:bg-slate-600 focus:bg-slate-50 dark:focus:bg-slate-600 focus:outline-none"
                  >
                    <div className="font-medium text-slate-900 dark:text-white">
                      {country.name}
                    </div>
                    <div className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                      {country.countryCode}
                    </div>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      {/* Loading state */}
      {loading && (
        <p className="mt-1 text-xs text-slate-500 dark:text-slate-400">Loading countries...</p>
      )}
    </div>
  );
}
