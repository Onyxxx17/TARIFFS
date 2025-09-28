import { posts } from "../data/posts";
import { Link } from "react-router-dom";

export default function BlogList() {
  return (
    <div className="mx-auto max-w-6xl px-4 py-10">
      <div className="text-center mb-9">
        <h1 className="text-4xl font-bold tracking-tight text-blue-900">
          Blog{" "}
          <span className="text-gray-500">- short updates that matters</span>
        </h1>
      </div>

      <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3">
        {posts.map((p) => (
          <article key={p.slug} className="overflow-hidden shadow-md bg-white">
            <Link to={`/blog/${p.slug}`}>
              <img
                src={p.cover}
                alt={p.title}
                className="h-76 w-full object-cover"
              />
            </Link>

            <div className="p-5">
              <div className="text-sm text-slate-500">
                {p.author} · {new Date(p.date).toLocaleDateString()}
              </div>
              <Link to={`/blog/${p.slug}`} className="mt-2 block">
                <h2 className="text-lg font-semibold leading-snug hover:underline">
                  {p.title}
                </h2>
                <p className="mt-2 text-slate-600 line-clamp-3">{p.excerpt}</p>
              </Link>
              <hr className="my-3 border-gray-300" />
              <div className="mt-4 text-xs text-slate-500">
                {p.readingMins} min read
              </div>
            </div>
          </article>
        ))}
      </div>
    </div>
  );
}
