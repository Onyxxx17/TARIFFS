export interface NewsArticle {
  title: string;
  description: string;
  content: string;
  link: string;  // backend uses 'link' not 'url'
  image_url: string | null;  // backend uses 'image_url'
  source_name: string;  // backend uses 'source_name' not 'source'
  pubDate: string;  // backend uses 'pubDate' not 'publishedAt'
  keywords: string[];
  country: string[];  // backend returns array of countries
  language: string;
  article_id: string;
  source_id: string;
  source_url: string;
  source_icon: string | null;
  creator: string[] | null;
  category: string[];
  duplicate: boolean;
}

export interface NewsApiResponse {
  status: string;
  totalResults: number;
  results: NewsArticle[];
  nextPage: string | null;
}

const BASE_URL = import.meta.env.VITE_BASE_APP_URL || "http://localhost:8080";

export class NewsService {
  
  static async getLatestNews(size: number = 5): Promise<NewsApiResponse> {
    try {
      const response = await fetch(`${BASE_URL}/api/news/latest?size=${size}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching latest news:', error);
      return {
        status: 'error',
        totalResults: 0,
        results: [],
        nextPage: null
      };
    }
  }

  static async getTariffNews(page: number = 1, size: number = 10): Promise<NewsApiResponse> {
    try {
      const response = await fetch(`${BASE_URL}/api/news/tariff?page=${page}&size=${size}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching tariff news:', error);
      return {
        status: 'error',
        totalResults: 0,
        results: [],
        nextPage: null
      };
    }
  }

  static async getTradeNews(page: number = 1, size: number = 10): Promise<NewsApiResponse> {
    try {
      const response = await fetch(`${BASE_URL}/api/news/trade?page=${page}&size=${size}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching trade news:', error);
      return {
        status: 'error',
        totalResults: 0,
        results: [],
        nextPage: null
      };
    }
  }

  static async searchNews(query: string, page: number = 1, size: number = 10): Promise<NewsApiResponse> {
    try {
      const response = await fetch(`${BASE_URL}/api/news/search?q=${encodeURIComponent(query)}&page=${page}&size=${size}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error searching news:', error);
      return {
        status: 'error',
        totalResults: 0,
        results: [],
        nextPage: null
      };
    }
  }
}