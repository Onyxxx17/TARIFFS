import { ExternalLink, Calendar, Globe } from "lucide-react";
import type { NewsArticle } from "../services/newsService";

interface NewsCardProps {
  article: NewsArticle;
}

export default function NewsCard({ article }: NewsCardProps) {
  const formatDate = (dateString: string) => {
    try {
      // Handle backend date format: "2025-11-10 20:15:00"
      const date = new Date(dateString.replace(' ', 'T'));
      if (isNaN(date.getTime())) {
        return 'Recent';
      }
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch (error) {
      return 'Recent';
    }
  };

  const truncateText = (text: string, maxLength: number) => {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  };

  const hasValidImage = article.image_url && article.image_url.trim() !== '' && article.image_url !== 'null';

  return (
    <div className="bg-white border border-gray-200 rounded-lg overflow-hidden hover:shadow-lg transition-shadow h-full flex flex-col">
      {/* Image - only show if valid URL exists */}
      {hasValidImage && (
        <div className="w-full h-48 bg-gray-100 flex-shrink-0">
          <img
            src={article.image_url || ''}
            alt={article.title}
            className="w-full h-full object-cover"
            onError={(e) => {
              // Hide the entire image container when image fails to load
              const imageContainer = e.currentTarget.parentElement;
              if (imageContainer) {
                imageContainer.style.display = 'none';
              }
            }}
          />
        </div>
      )}

      {/* Content - flex-1 ensures consistent card heights */}
      <div className="p-6 flex-1 flex flex-col">
        {/* Main content area */}
        <div className="space-y-3 flex-1">
          {/* Title */}
          <h3 className="text-xl font-semibold text-gray-900 line-clamp-2 leading-tight">
            {article.title}
          </h3>

          {/* Description */}
          {article.description && (
            <p className="text-gray-600 text-sm leading-relaxed flex-1">
              {truncateText(article.description, 150)}
            </p>
          )}
        </div>

        {/* Bottom section with metadata and actions */}
        <div className="space-y-3 pt-4">
          {/* Metadata */}
          <div className="flex items-center gap-4 text-xs text-gray-500">
          {/* Source */}
          <div className="flex items-center gap-1">
            <Globe size={12} />
            <span className="font-medium">{article.source_name || 'Unknown Source'}</span>
          </div>

          {/* Date */}
          <div className="flex items-center gap-1">
            <Calendar size={12} />
            <span>{formatDate(article.pubDate)}</span>
          </div>
        </div>

        {/* Keywords & Category */}
        {((article.keywords && article.keywords.length > 0) || (article.category && article.category.length > 0)) && (
          <div className="flex flex-wrap gap-1">
            {/* Show categories first */}
            {article.category && article.category.slice(0, 2).map((cat, index) => (
              <span
                key={`cat-${index}`}
                className="inline-block bg-green-50 text-green-700 text-xs px-2 py-1 rounded-full capitalize"
              >
                {cat}
              </span>
            ))}
            {/* Then show keywords */}
            {article.keywords && article.keywords.slice(0, 2).map((keyword, index) => (
              <span
                key={`kw-${index}`}
                className="inline-block bg-blue-50 text-blue-700 text-xs px-2 py-1 rounded-full"
              >
                {keyword}
              </span>
            ))}
          </div>
        )}

          {/* Read More Button */}
          {article.link && (
            <a
              href={article.link}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 text-blue-600 hover:text-blue-800 font-medium text-sm transition-colors"
            >
              Read Full Article
              <ExternalLink size={14} />
            </a>
          )}
        </div>
      </div>
    </div>
  );
}