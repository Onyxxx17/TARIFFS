import { useParams, Link } from "react-router-dom";
import { posts } from "../data/posts";
import ReactMarkdown from "react-markdown";
import rehypeRaw from "rehype-raw";

/* --- Article 1: BBC-style explainer --- */
function TrumpTariffArticle() {
  return (
    <div className="prose prose-slate max-w-none">
      <h2 className="mb-4 text-2xl md:text-3xl font-semibold tracking-tight">
        What are tariffs and how do they work?
      </h2>
      <p className="mb-4 text-lg">Tariffs are taxes on imported goods.</p>
      <p className="mb-4 text-lg">
        Typically, the charge is a percentage of a good&apos;s value.
      </p>
      <p className="mb-4 text-lg">
        For example, a 10% tariff on a $10 product would mean a $1 tax on top —
        taking the total cost to $11 (£8.13).
      </p>
      <p className="mb-4 text-lg">
        The tax is paid to the government by companies bringing in the foreign
        products.
      </p>
      <p className="mb-4 text-lg">
        {" "}
        These firms may pass some or all of the extra cost on to their
        customers, which in this case means ordinary Americans and other US
        businesses.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        They may also decide to import fewer goods.
      </p>{" "}
      <h2 className="text-2xl md:text-3xl font-semibold tracking-tight mt-10 mb-4">
        {" "}
        Why is Trump using tariffs?{" "}
      </h2>{" "}
      <p className="mb-4 text-lg">
        {" "}
        Trump says tariffs will increase the amount of tax raised by the
        government, encourage consumers to buy more American-made goods and
        boost investment in the US.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        {" "}
        He wants to reduce the US trade deficit - the gap between the value of
        goods it buys from other countries and those it sells to them.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        {" "}
        The president argues that the US has been exploited by
        &quot;cheaters&quot; and &quot;pillaged&quot; by foreigners.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        Trump has also used the taxes to make other demands.
      </p>{" "}
      <p className="mb-4 text-lg">
        {" "}
        For example, when announcing tariffs against China, Mexico and Canada,
        he said the countries must do more to stop migrants and illegal drugs
        reaching the US.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        {" "}
        He has also threatened tariffs against countries trading with Russia,
        unless a deal to end the war in Ukraine is reached.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        Many tariffs have been amended or delayed after being announced.
      </p>{" "}
      <p className="mb-4 text-lg">
        They have also faced numerous legal challenges.
      </p>{" "}
      <p className="underline font-medium mb-4 text-lg">
        {" "}
        In August, a US appeals court ruled that most tariffs announced by Trump
        were illegal.{" "}
      </p>{" "}
      <p className="mb-4 text-lg">
        {" "}
        The White House asked the Supreme Court to overturn the decision.{" "}
        <span className=" text-blue-600 text-lgunderline">
          {" "}
          It has confirmed it will hear arguments in the case in the first week
          of November.{" "}
        </span>
      </p>
    </div>
  );
}

/* --- Article 2: Reciprocal tariff updates --- */
function ReciprocalTariffUpdate() {
  return (
    <div className="prose prose-slate max-w-none">
      <h2 className="mb-4 text-2xl md:text-3xl font-semibold tracking-tight">
        Other Recent Key Tariff Updates
      </h2>
      <p className = "mb-4 text-lg">
        In addition to the reciprocal tariff Executive Order, notable
        developments in the past week:
      </p>
      <ul>
        <li >
          <strong>Brazil:</strong> 10% reciprocal +{" "}
          <strong>additional 40%</strong> (total 50%) per a separate Executive
          Order on Jul 30, 2025; with exceptions in annexes.
        </li>
        <li>
          <strong>European Union:</strong> EU–U.S. deal sets a{" "}
          <strong>15% ceiling</strong> on most EU imports. Section 232 (steel,
          aluminum, copper at 50%) appears unaffected. Autos/parts,
          pharmaceuticals, semiconductors are under the 15% ceiling.
        </li>
        <li>
          <strong>United Kingdom:</strong> Secured a <strong>10%</strong> rate;
          UK–U.S. Economic Prosperity Deal calls for “significantly preferential
          treatment” post Section 232 investigations.
        </li>
        <li>
          <strong>China:</strong> Increase from <strong>10% → 34%</strong> on
          Aug 12, 2025 as a temporary suspension expires.
        </li>
        <li>
          <strong>Canada:</strong> Effective Aug 1, 2025, rate raised{" "}
          <strong>25% → 35%</strong> via Executive Order.
        </li>
        <li>
          <strong>Mexico:</strong> Planned move to <strong>30%</strong> on Aug
          1, 2025 delayed <strong>90 days</strong> for talks.
        </li>
      </ul>

      <h3 className="text-xl md:text-2xl font-semibold tracking-tight mt-8">
        When the Reciprocal Tariffs Took Effect
      </h3>
      <p>
        On <strong>Aug 7, 2025</strong>, reciprocal tariffs fully took effect.{" "}
        <span className="underline font-medium">
          The overall U.S. effective tariff rate is estimated at 18.6% — the
          highest since 1934.
        </span>{" "}
        Several countries continue to negotiate their applied rates.
      </p>

      <h3 className="text-xl md:text-2xl font-semibold tracking-tight mt-8">
        The Current Reciprocal Tariffs
      </h3>
      <p>
        Pursuant to the <em>July 31, 2025</em> Executive Order “Further
        Modifying the Reciprocal Tariff Rates,” which builds on the Liberation
        Day order. Initial Aug 1 start was delayed to Aug 7, with country
        updates.
      </p>
      <p className="underline">
        Reminder: reciprocal tariffs generally <strong>stack</strong> on top of
        other tariffs (except certain Section 232).
      </p>

      <div className="mt-6 p-4 border rounded-xl bg-slate-50 text-sm">
        <strong>Impact on landed cost — quick math:</strong>
        <ol className="list-decimal ml-5 mt-2">
          <li>CIF value (cost + insurance + freight)</li>
          <li>+ Base duty (MFN/FTA)</li>
          <li>+ Reciprocal tariff (if applicable)</li>
          <li>+ Taxes (GST/VAT, excise)</li>
          <li>+ Clearance/handling fees</li>
        </ol>
      </div>
    </div>
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
