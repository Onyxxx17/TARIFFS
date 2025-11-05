import { useState, useEffect, useRef } from 'react';
import { BASE_URL } from '../config';

interface Product {
  id: number;
  name: string;
  category: {
    id: number;
    name: string;
  };
}

interface ProductSelectProps {
  label: string;
  value: Product | null;
  onPick: (product: Product | null) => void;
  placeholder?: string;
}

export default function ProductSelect({ 
  label, 
  value, 
  onPick, 
  placeholder = "Search for a product..." 
}: ProductSelectProps) {
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Fetch products on mount
  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      setError("");
      try {
        const response = await fetch(`${BASE_URL}/api/products`);
        if (!response.ok) throw new Error("Failed to fetch products");
        const data = await response.json();
        setProducts(data);
        setFilteredProducts(data);
      } catch (err) {
        setError("Failed to load products");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Filter products based on search term
  useEffect(() => {
    if (!searchTerm.trim()) {
      setFilteredProducts(products);
      return;
    }

    const term = searchTerm.toLowerCase();
    const filtered = products.filter(
      (product) =>
        product.id.toString().includes(term) ||
        product.name.toLowerCase().includes(term) ||
        product.category.name.toLowerCase().includes(term)
    );
    setFilteredProducts(filtered);
  }, [searchTerm, products]);

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

  const handleSelect = (product: Product) => {
    onPick(product);
    setSearchTerm("");
    setIsOpen(false);
  };

  const handleClear = () => {
    onPick(null);
    setSearchTerm("");
  };

  return (
    <div className="relative w-full" ref={dropdownRef}>
      <label className="block text-xs sm:text-sm font-medium text-slate-700 mb-2">
        {label}
      </label>

      {/* Input field */}
      <div className="relative">
        <input
          type="text"
          value={value ? `${value.id} - ${value.name}` : searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            setIsOpen(true);
            if (value) onPick(null);
          }}
          onFocus={() => setIsOpen(true)}
          placeholder={placeholder}
          className="w-full rounded-lg border border-slate-300 px-3 py-2 sm:py-2 pr-8 text-sm focus:outline-none focus:ring-2 focus:ring-blue-200"
          disabled={loading}
        />
        
        {/* Clear button */}
        {value && (
          <button
            type="button"
            onClick={handleClear}
            className="absolute right-2 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
          >
            ✕
          </button>
        )}
      </div>

      {/* Error message */}
      {error && (
        <p className="mt-1 text-xs text-red-600">{error}</p>
      )}

      {/* Dropdown */}
      {isOpen && !loading && (
        <div className="absolute z-50 mt-1 w-full rounded-lg border border-slate-200 bg-white shadow-lg max-h-48 overflow-y-auto">
          {filteredProducts.length === 0 ? (
            <div className="px-3 py-3 sm:py-2 text-sm text-slate-500">
              No products found
            </div>
          ) : (
            <ul className="py-1">
              {filteredProducts.map((product) => (
                <li key={product.id}>
                  <button
                    type="button"
                    onClick={() => handleSelect(product)}
                    className="w-full text-left px-3 py-2 text-sm hover:bg-slate-50 focus:bg-slate-50 focus:outline-none"
                  >
                    <div className="font-medium text-slate-900">
                      {product.id} - {product.name}
                    </div>
                    <div className="text-xs text-slate-500 mt-0.5">
                      {product.category.name}
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
        <p className="mt-1 text-xs text-slate-500">Loading products...</p>
      )}
    </div>
  );
}