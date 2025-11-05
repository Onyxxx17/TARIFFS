import { posts } from "../data/posts";
import { Link } from "react-router-dom";

export default function BlogList() {
  return (
    <div className="bg-slate-50 min-h-screen transition-colors">
      {/* Header Section */}
      <div className="bg-gradient-to-r from-slate-900 to-slate-800 border-b-4 border-blue-600">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 py-8 sm:py-16">
          <div className="text-center">
            <span className="inline-block px-3 sm:px-4 py-1 sm:py-1.5 mb-4 text-xs font-semibold tracking-wider text-blue-400 uppercase bg-blue-900/30 rounded-full border border-blue-400/30">
              News & Analysis
            </span>
            <h1 className="text-3xl sm:text-4xl md:text-5xl font-bold tracking-tight text-white mb-3 sm:mb-4">
              Trade Policy News
            </h1>
            <p className="text-base sm:text-lg text-slate-300 max-w-2xl mx-auto px-2">
              Latest updates on tariffs, trade regulations, and international commerce
            </p>
          </div>
        </div>
      </div>

      {/* Featured Article */}
      {posts.length > 0 && (
        <div className="bg-white border-b border-slate-200 transition-colors">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 py-8 sm:py-12">
            <div className="grid md:grid-cols-2 gap-6 sm:gap-8 items-center">
              <Link to={`/blog/${posts[0].slug}`} className="group">
                <img
                  src={posts[0].cover}
                  alt={posts[0].title}
                  className="w-full h-48 sm:h-64 md:h-[400px] object-cover rounded-lg shadow-lg group-hover:shadow-xl transition-shadow"
                />
              </Link>
              <div>
                <span className="inline-block px-3 py-1 text-xs font-semibold text-blue-700 bg-blue-50 rounded-full mb-3">
                  Featured Story
                </span>
                <Link to={`/blog/${posts[0].slug}`}>
                  <h2 className="text-2xl sm:text-3xl font-bold text-slate-900 mb-3 sm:mb-4 hover:text-blue-600 transition-colors">
                    {posts[0].title}
                  </h2>
                </Link>
                <p className="text-slate-600 text-sm sm:text-lg mb-3 sm:mb-4 leading-relaxed">
                  {posts[0].excerpt}
                </p>
                <div className="flex flex-col xs:flex-row xs:items-center gap-2 xs:gap-4 text-xs sm:text-sm text-slate-500 mb-4">
                  <span className="font-medium text-slate-700">{posts[0].author}</span>
                  <span className="hidden xs:inline">•</span>
                  <time>{new Date(posts[0].date).toLocaleDateString('en-US', { 
                    month: 'long', 
                    day: 'numeric', 
                    year: 'numeric' 
                  })}</time>
                  <span className="hidden xs:inline">•</span>
                  <span>{posts[0].readingMins} min read</span>
                </div>
                <Link
                  to={`/blog/${posts[0].slug}`}
                  className="inline-flex items-center gap-2 text-blue-600 font-semibold hover:gap-3 transition-all text-sm sm:text-base"
                >
                  Read Full Article
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </Link>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* More Stories */}
      <div className="mx-auto max-w-7xl px-4 sm:px-6 py-8 sm:py-12">
        <h2 className="text-2xl sm:text-3xl font-bold text-slate-900 mb-6 sm:mb-8 border-l-4 border-blue-600 pl-4">
          More Stories
        </h2>

        <div className="grid gap-4 sm:gap-6 md:grid-cols-2 lg:grid-cols-3">
          {posts.slice(1).map((p) => (
            <article 
              key={p.slug} 
              className="bg-white rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow border border-slate-200"
            >
              <Link to={`/blog/${p.slug}`} className="group">
                <div className="relative overflow-hidden">
                  <img
                    src={p.cover}
                    alt={p.title}
                    className="h-40 sm:h-48 w-full object-cover group-hover:scale-105 transition-transform duration-300"
                  />
                  <div className="absolute top-2 sm:top-3 left-2 sm:left-3">
                    <span className="px-2 py-1 text-xs font-semibold text-white bg-blue-600 rounded">
                      News
                    </span>
                  </div>
                </div>
              </Link>

              <div className="p-4 sm:p-5">
                <div className="flex flex-col xs:flex-row xs:items-center gap-1 xs:gap-2 text-xs text-slate-500 mb-3">
                  <time>{new Date(p.date).toLocaleDateString('en-US', { 
                    month: 'short', 
                    day: 'numeric', 
                    year: 'numeric' 
                  })}</time>
                  <span className="hidden xs:inline">•</span>
                  <span>{p.readingMins} min read</span>
                </div>

                <Link to={`/blog/${p.slug}`}>
                  <h3 className="text-base sm:text-lg font-bold text-slate-900 mb-2 leading-tight hover:text-blue-600 transition-colors line-clamp-2">
                    {p.title}
                  </h3>
                </Link>

                <p className="text-slate-600 text-xs sm:text-sm mb-4 line-clamp-3 leading-relaxed">
                  {p.excerpt}
                </p>

                <div className="flex flex-col xs:flex-row xs:items-center xs:justify-between gap-3 pt-3 border-t border-slate-100">
                  <span className="text-xs sm:text-sm font-medium text-slate-700">
                    {p.author}
                  </span>
                  <Link
                    to={`/blog/${p.slug}`}
                    className="text-xs sm:text-sm text-blue-600 font-medium hover:underline"
                  >
                    Read more →
                  </Link>
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>
    </div>
  );
}
