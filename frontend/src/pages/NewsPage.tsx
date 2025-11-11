import { useState } from "react";
import { Search } from "lucide-react";
import NewsList from "../components/NewsList";

export default function NewsPage() {
  const [activeTab, setActiveTab] = useState<'latest' | 'tariff' | 'trade'>('latest');
  const [searchQuery, setSearchQuery] = useState('');
  const [isSearchMode, setIsSearchMode] = useState(false);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      setIsSearchMode(true);
    } else {
      setIsSearchMode(false);
    }
  };

  const clearSearch = () => {
    setSearchQuery('');
    setIsSearchMode(false);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">Trade & Tariff News</h1>
          <p className="text-lg text-gray-600">
            Stay updated with the latest news on international trade, tariffs, and economic policies.
          </p>
        </div>

        {/* Search Bar */}
        <div className="mb-8">
          <form onSubmit={handleSearch} className="relative max-w-md">
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search news articles..."
              className="w-full px-4 py-3 pl-12 pr-4 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
            <Search className="absolute left-4 top-3.5 w-5 h-5 text-gray-400" />
            {isSearchMode && (
              <button
                type="button"
                onClick={clearSearch}
                className="absolute right-4 top-3.5 text-gray-400 hover:text-gray-600"
              >
                ✕
              </button>
            )}
          </form>
          {isSearchMode && (
            <p className="mt-2 text-sm text-gray-600">
              Showing results for "{searchQuery}"
            </p>
          )}
        </div>

        {/* Category Tabs */}
        {!isSearchMode && (
          <div className="mb-8">
            <div className="border-b border-gray-200">
              <nav className="-mb-px flex space-x-8">
                <button
                  onClick={() => setActiveTab('latest')}
                  className={`py-2 px-1 border-b-2 font-medium text-sm ${
                    activeTab === 'latest'
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  Latest News
                </button>
                <button
                  onClick={() => setActiveTab('tariff')}
                  className={`py-2 px-1 border-b-2 font-medium text-sm ${
                    activeTab === 'tariff'
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  Tariff News
                </button>
                <button
                  onClick={() => setActiveTab('trade')}
                  className={`py-2 px-1 border-b-2 font-medium text-sm ${
                    activeTab === 'trade'
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  International Trade
                </button>
              </nav>
            </div>
          </div>
        )}

        {/* News Content */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          {isSearchMode ? (
            <NewsList searchQuery={searchQuery} />
          ) : (
            <NewsList type={activeTab} />
          )}
        </div>
      </div>
    </div>
  );
}