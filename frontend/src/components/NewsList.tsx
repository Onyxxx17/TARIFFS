import { useState, useEffect } from "react";
import { Loader2, AlertCircle } from "lucide-react";
import NewsCard from "./NewsCard";
import type { NewsArticle, NewsApiResponse } from "../services/newsService";
import { NewsService } from "../services/newsService";

interface NewsListProps {
  type?: 'latest' | 'tariff' | 'trade';
  searchQuery?: string;
  initialSize?: number;
}

export default function NewsList({ type = 'latest', searchQuery, initialSize = 10 }: NewsListProps) {
  const [articles, setArticles] = useState<NewsArticle[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchNews();
  }, [type, searchQuery]);

  const fetchNews = async () => {
    setLoading(true);
    setError(null);
    
    try {
      let response: NewsApiResponse;
      
      if (searchQuery) {
        response = await NewsService.searchNews(searchQuery, 1, initialSize);
      } else {
        switch (type) {
          case 'tariff':
            response = await NewsService.getTariffNews(1, initialSize);
            break;
          case 'trade':
            response = await NewsService.getTradeNews(1, initialSize);
            break;
          default:
            response = await NewsService.getLatestNews(initialSize);
            break;
        }
      }

      if (response.status === 'success') {
        setArticles(response.results);
      } else {
        setError('Failed to load news articles');
      }
    } catch (err) {
      setError('An error occurred while fetching news');
      console.error('News fetch error:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
        <span className="ml-2 text-gray-600">Loading news...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="text-center">
          <AlertCircle className="w-12 h-12 text-red-500 mx-auto mb-4" />
          <p className="text-gray-600 mb-4">{error}</p>
          <button
            onClick={fetchNews}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  if (articles.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-600">No news articles found.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {articles.map((article, index) => (
        <NewsCard key={index} article={article} />
      ))}
    </div>
  );
}