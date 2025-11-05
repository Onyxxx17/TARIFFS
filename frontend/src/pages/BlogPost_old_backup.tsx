import { useParams, Link } from "react-router-dom";
import { posts } from "../data/posts";
import ReactMarkdown from "react-markdown";
import rehypeRaw from "rehype-raw";

/* --- Article 1: BBC-style explainer --- */
function TrumpTariffArticle() {
  return (
    <article className="prose prose-lg max-w-none">
      <div className="not-prose mb-8">
        <h2 className="text-3xl md:text-4xl font-bold text-slate-900 leading-tight mb-6">
          What are tariffs and how do they work?
        </h2>
      </div>

      <p className="text-xl text-slate-700 font-medium leading-relaxed mb-6">
        Tariffs are taxes on imported goods.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        Typically, the charge is a percentage of a good's value.
      </p>

      <div className="bg-blue-50 border-l-4 border-blue-600 p-6 my-8 rounded-r-lg">
        <p className="text-lg text-slate-700 leading-relaxed font-medium">
          <strong>Example:</strong> A 10% tariff on a $10 product would mean a $1 tax on top — 
          taking the total cost to $11 (£8.13).
        </p>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        The tax is paid to the government by companies bringing in the foreign products.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        These firms may pass some or all of the extra cost on to their customers, 
        which in this case means ordinary Americans and other US businesses.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-8">
        They may also decide to import fewer goods.
      </p>

      <div className="not-prose my-12">
        <h2 className="text-3xl md:text-4xl font-bold text-slate-900 leading-tight mb-6">
          Why is Trump using tariffs?
        </h2>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        Trump says tariffs will increase the amount of tax raised by the government, 
        encourage consumers to buy more American-made goods and boost investment in the US.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        He wants to reduce the US trade deficit - the gap between the value of goods 
        it buys from other countries and those it sells to them.
      </p>

      <div className="bg-slate-100 p-6 rounded-lg my-8">
        <p className="text-lg text-slate-800 leading-relaxed italic">
          The president argues that the US has been exploited by "cheaters" and 
          "pillaged" by foreigners.
        </p>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        Trump has also used the taxes to make other demands.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        For example, when announcing tariffs against China, Mexico and Canada, he said 
        the countries must do more to stop migrants and illegal drugs reaching the US.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        He has also threatened tariffs against countries trading with Russia, unless a 
        deal to end the war in Ukraine is reached.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        Many tariffs have been amended or delayed after being announced.
      </p>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        They have also faced numerous legal challenges.
      </p>

      <div className="bg-amber-50 border-l-4 border-amber-500 p-6 my-8 rounded-r-lg">
        <p className="text-lg text-slate-800 leading-relaxed">
          <strong>Legal Update:</strong> In August, a US appeals court ruled that most 
          tariffs announced by Trump were illegal.
        </p>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        The White House asked the Supreme Court to overturn the decision.{" "}
        <span className="text-blue-600 font-semibold">
          It has confirmed it will hear arguments in the case in the first week of November.
        </span>
      </p>
    </article>
  );
}

/* --- Article 2: Reciprocal tariff updates --- */
function ReciprocalTariffUpdate() {
  return (
    <article className="prose prose-lg max-w-none">
      <div className="not-prose mb-8">
        <h2 className="text-3xl md:text-4xl font-bold text-slate-900 leading-tight mb-6">
          Other Recent Key Tariff Updates
        </h2>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-6">
        In addition to the reciprocal tariff Executive Order, notable developments 
        in the past week:
      </p>

      <div className="space-y-4 my-8">
        <div className="bg-white border border-slate-200 rounded-lg p-5 hover:shadow-md transition-shadow">
          <h3 className="font-bold text-lg text-slate-900 mb-2">🇧🇷 Brazil</h3>
          <p className="text-slate-700">
            10% reciprocal + <strong className="text-red-600">additional 40%</strong> (total 50%) 
            per a separate Executive Order on Jul 30, 2025; with exceptions in annexes.
          </p>
        </div>

        <div className="bg-white border border-slate-200 rounded-lg p-5 hover:shadow-md transition-shadow">
          <h3 className="font-bold text-lg text-slate-900 mb-2">🇪🇺 European Union</h3>
          <p className="text-slate-700">
            EU–U.S. deal sets a <strong className="text-blue-600">15% ceiling</strong> on 
            most EU imports. Section 232 (steel, aluminum, copper at 50%) appears unaffected. 
            Autos/parts, pharmaceuticals, semiconductors are under the 15% ceiling.
          </p>
        </div>

        <div className="bg-white border border-slate-200 rounded-lg p-5 hover:shadow-md transition-shadow">
          <h3 className="font-bold text-lg text-slate-900 mb-2">🇬🇧 United Kingdom</h3>
          <p className="text-slate-700">
            Secured a <strong className="text-green-600">10%</strong> rate; UK–U.S. Economic 
            Prosperity Deal calls for "significantly preferential treatment" post Section 232 investigations.
          </p>
        </div>

        <div className="bg-white border border-slate-200 rounded-lg p-5 hover:shadow-md transition-shadow">
          <h3 className="font-bold text-lg text-slate-900 mb-2">🇨🇳 China</h3>
          <p className="text-slate-700">
            Increase from <strong className="text-red-600">10% → 34%</strong> on Aug 12, 2025 
            as a temporary suspension expires.
          </p>
        </div>

        <div className="bg-white border border-slate-200 rounded-lg p-5 hover:shadow-md transition-shadow">
          <h3 className="font-bold text-lg text-slate-900 mb-2">🇨🇦 Canada</h3>
          <p className="text-slate-700">
            Effective Aug 1, 2025, rate raised <strong className="text-orange-600">25% → 35%</strong> via 
            Executive Order.
          </p>
        </div>

        <div className="bg-white border border-slate-200 rounded-lg p-5 hover:shadow-md transition-shadow">
          <h3 className="font-bold text-lg text-slate-900 mb-2">🇲🇽 Mexico</h3>
          <p className="text-slate-700">
            Planned move to <strong className="text-yellow-600">30%</strong> on Aug 1, 2025 delayed{" "}
            <strong>90 days</strong> for talks.
          </p>
        </div>
      </div>

      <div className="not-prose my-12">
        <h3 className="text-2xl font-bold text-slate-900 mb-4">
          When the Reciprocal Tariffs Took Effect
        </h3>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        On <strong>Aug 7, 2025</strong>, reciprocal tariffs fully took effect.{" "}
      </p>

      <div className="bg-red-50 border-l-4 border-red-600 p-6 my-8 rounded-r-lg">
        <p className="text-lg text-slate-800 leading-relaxed font-semibold">
          The overall U.S. effective tariff rate is estimated at 18.6% — the highest since 1934.
        </p>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-8">
        Several countries continue to negotiate their applied rates.
      </p>

      <div className="not-prose my-12">
        <h3 className="text-2xl font-bold text-slate-900 mb-4">
          The Current Reciprocal Tariffs
        </h3>
      </div>

      <p className="text-lg text-slate-600 leading-relaxed mb-5">
        Pursuant to the <em>July 31, 2025</em> Executive Order "Further Modifying 
        the Reciprocal Tariff Rates," which builds on the Liberation Day order. 
        Initial Aug 1 start was delayed to Aug 7, with country updates.
      </p>

      <div className="bg-blue-50 p-6 rounded-lg my-8">
        <p className="text-lg text-blue-900 font-semibold">
          ⚠️ Reminder: reciprocal tariffs generally <strong>stack</strong> on top of 
          other tariffs (except certain Section 232).
        </p>
      </div>

      <div className="mt-8 p-6 bg-gradient-to-br from-slate-50 to-slate-100 border border-slate-200 rounded-xl">
        <h4 className="font-bold text-lg text-slate-900 mb-4">
          💡 Impact on landed cost — quick math:
        </h4>
        <ol className="list-decimal ml-6 space-y-2 text-slate-700">
          <li className="pl-2">CIF value (cost + insurance + freight)</li>
          <li className="pl-2">+ Base duty (MFN/FTA)</li>
          <li className="pl-2">+ Reciprocal tariff (if applicable)</li>
          <li className="pl-2">+ Taxes (GST/VAT, excise)</li>
          <li className="pl-2">+ Clearance/handling fees</li>
        </ol>
      </div>
    </article>
  );
}



export default function BlogPost() {
  const { slug } = useParams();
  const post = posts.find((p) => p.slug === slug);

  if (!post) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-10">
        <p>
          Post not found.{" "}
          <Link to="/blog" className="text-blue-600 underline">
            Back to Blog
          </Link>
        </p>
      </div>
    );
  }

  return (
    <article className="mx-auto max-w-3xl px-4 py-10">
      <Link to="/blog" className="text-sm text-blue-600 underline">
        ← Back to Blog
      </Link>

      <h1 className="mt-3 text-3xl font-semibold tracking-tight">
        {post.title}
      </h1>
      <div className="mt-2 text-sm text-slate-500">
        {post.author} · {new Date(post.date).toLocaleDateString()} ·{" "}
        {post.readingMins} min read
      </div>

      <img
        src={post.cover}
        alt={post.title}
        className="mt-6 rounded-md w-full object-cover"
      />

      <div className="mt-8">
        {slug === "what-tariffs-has-trump-announced-and-why" ? (
          <TrumpTariffArticle />
        ) : slug === "reciprocal-tariffs-2025" ? (
          <ReciprocalTariffUpdate />
        ) : (
          <div className="prose prose-slate max-w-none">
            <ReactMarkdown rehypePlugins={[rehypeRaw]}>
              {post.body}
            </ReactMarkdown>
          </div>
        )}
      </div>
    </article>
  );
}
