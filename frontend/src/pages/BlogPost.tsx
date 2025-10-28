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

      <div className="space-y-3 my-8">
        <div className="bg-white border-l-4 border-slate-300 p-5 rounded">
          <h3 className="font-semibold text-base text-slate-900 mb-2">Brazil</h3>
          <p className="text-slate-700 text-sm leading-relaxed">
            10% reciprocal + <strong>additional 40%</strong> (total 50%) 
            per a separate Executive Order on Jul 30, 2025; with exceptions in annexes.
          </p>
        </div>

        <div className="bg-white border-l-4 border-slate-300 p-5 rounded">
          <h3 className="font-semibold text-base text-slate-900 mb-2">European Union</h3>
          <p className="text-slate-700 text-sm leading-relaxed">
            EU–U.S. deal sets a <strong>15% ceiling</strong> on 
            most EU imports. Section 232 (steel, aluminum, copper at 50%) appears unaffected. 
            Autos/parts, pharmaceuticals, semiconductors are under the 15% ceiling.
          </p>
        </div>

        <div className="bg-white border-l-4 border-slate-300 p-5 rounded">
          <h3 className="font-semibold text-base text-slate-900 mb-2">United Kingdom</h3>
          <p className="text-slate-700 text-sm leading-relaxed">
            Secured a <strong>10%</strong> rate; UK–U.S. Economic 
            Prosperity Deal calls for "significantly preferential treatment" post Section 232 investigations.
          </p>
        </div>

        <div className="bg-white border-l-4 border-slate-300 p-5 rounded">
          <h3 className="font-semibold text-base text-slate-900 mb-2">China</h3>
          <p className="text-slate-700 text-sm leading-relaxed">
            Increase from <strong>10% → 34%</strong> on Aug 12, 2025 
            as a temporary suspension expires.
          </p>
        </div>

        <div className="bg-white border-l-4 border-slate-300 p-5 rounded">
          <h3 className="font-semibold text-base text-slate-900 mb-2">Canada</h3>
          <p className="text-slate-700 text-sm leading-relaxed">
            Effective Aug 1, 2025, rate raised <strong>25% → 35%</strong> via 
            Executive Order.
          </p>
        </div>

        <div className="bg-white border-l-4 border-slate-300 p-5 rounded">
          <h3 className="font-semibold text-base text-slate-900 mb-2">Mexico</h3>
          <p className="text-slate-700 text-sm leading-relaxed">
            Planned move to <strong>30%</strong> on Aug 1, 2025 delayed{" "}
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

      <div className="bg-slate-100 border-l-4 border-slate-400 p-6 my-8 rounded">
        <p className="text-base text-slate-800 leading-relaxed font-semibold">
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

      <div className="bg-slate-50 border border-slate-200 p-6 rounded-lg my-8">
        <p className="text-base text-slate-700 leading-relaxed">
          <strong>Note:</strong> Reciprocal tariffs generally <strong>stack</strong> on top of 
          other tariffs (except certain Section 232).
        </p>
      </div>

      <div className="mt-8 p-6 bg-white border border-slate-200 rounded-lg">
        <h4 className="font-semibold text-base text-slate-900 mb-4">
          Impact on landed cost calculation:
        </h4>
        <ol className="list-decimal ml-6 space-y-2 text-slate-700 text-sm">
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

/* --- Article 3: US-China Trade War Chart --- */
function USChinaTradeWarArticle() {
  return (
    <article className="prose prose-lg max-w-none">
      <div className="not-prose mb-8">
        <p className="text-sm text-slate-500 mb-4">
          This chart was originally published on September 20, 2019. For more information related to this chart, see{" "}
          <a href="#" className="text-blue-600 hover:underline">The US-China trade war and phase one agreement</a>,{" "}
          <a href="#" className="text-blue-600 hover:underline">Trump's Trade War Timeline: An Up-to-Date Guide</a>, and{" "}
          <a href="#" className="text-blue-600 hover:underline">Trump's trade war timeline 2.0: An up-to-date guide</a>.
        </p>
        <p className="text-sm italic text-slate-600 mb-8">
          Special thanks to Hexuan Li, Yuan Liu, Christine Wan, Yilin Wang, Jing Yan, and Eva Zhang for contributions.
          Design and production by Sam Elbouez, Melina Kolb, William Melancon, Alex Martin, and Oliver Ward.
        </p>
      </div>

      <div className="not-prose mb-8">
        <h2 className="text-2xl md:text-3xl font-bold text-slate-900 leading-tight mb-6">
          US-China trade war tariffs: An up-to-date chart
        </h2>
        <div className="inline-block bg-blue-600 text-white px-4 py-2 rounded text-sm font-semibold mb-6">
          Last updated September 25, 2025
        </div>
      </div>

      <div className="not-prose mb-8">
        <h3 className="text-xl font-bold text-blue-700 mb-4">
          a. US-China tariff rates toward each other and rest of world (ROW)
        </h3>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-6">
        Average US tariffs on Chinese exports now stand at <strong>57.6 percent</strong> and cover 100 percent of all goods. 
        China's average tariffs on US exports are at <strong>32.6 percent</strong> and cover 100 percent of all goods. 
        US tariffs have risen by 36.8 percentage points since the second Trump administration began on January 20, 2025. 
        Chinese tariffs have risen by 11.4 percentage points over the same period.
      </p>

      <div className="not-prose my-10">
        <h3 className="text-xl font-semibold text-slate-900 mb-6">
          COMPARING US-CHINA TARIFFS SINCE 2018
        </h3>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        US tariffs are more than 18 times higher than before the US-China tariff war began in 2018. All told, the 36.8-percentage 
        point increase in the average US tariff on imports from China during the second Trump term is more than twice as much as 
        the 16.2 percentage point <em>total</em> average tariff increase on US imports from China during the first Trump 
        administration of January 20, 2017, to January 24, 2021.
      </p>

      <div className="bg-slate-50 border-l-4 border-slate-400 p-6 my-8 rounded">
        <p className="text-base text-slate-800 leading-relaxed">
          <strong>Biden Administration Period:</strong> Under the Biden administration, which spanned January 20, 2021, to January 24, 2025, 
          US-China tariffs remained fairly stable. The main tariff increases in tariffs from China in September 2024 and January 2025, 
          which raised the average US tariff on Chinese exports from 19.3 percent to 20.7 percent. The product coverage was little changed; 
          the US policy was primarily to increase tariff rates on already-covered products.
        </p>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        During the first Trump administration, the February 14, 2020 implementation of the so-called US-China phase one agreement 
        established a tariff exclusion process whereby US tariffs on imports from China averaged 19.3 percent, more than six times 
        higher than in January 2018. Those tariffs covered 66.6 percent of US imports from China, or roughly $335 billion of trade 
        (measured in terms of 2017 import levels).
      </p>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        Average Chinese tariffs on US exports starting in February 2020 were also at 19.3 percent, up from 8.0 percent prior to 
        the trade war. China's retaliatory tariffs at the time covered 58.3 percent of imports from the United States, worth roughly 
        $90 billion (measured in terms of 2017 import levels). On February 17, 2020, the Chinese government announced an exclusion 
        process whereby Chinese companies could apply for a temporary exemption from the retaliatory tariffs on more than 400 US products.
      </p>

      <div className="bg-white border border-slate-200 p-6 rounded-lg my-8">
        <p className="text-base text-slate-700 leading-relaxed">
          <strong>Trade Agreement:</strong> The retaliatory tariffs were also nonetheless subject to the US commitment of purchasing 
          an additional $200 billion of US goods and services over 2020 and 2021, as established by the legal agreement signed on 
          January 15, 2020.
        </p>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        Also noteworthy is that China's average tariffs on imports from the rest of the world declined from 8.0 percent in early 2018 
        to 6.5 percent by early 2022, where they mostly remain today. Again, the United States increased its average tariff on imports 
        from the rest of the world to 19.5 percent by the end of the second Trump administration.
      </p>

      <div className="bg-slate-100 border-l-4 border-blue-600 p-6 my-8 rounded">
        <p className="text-sm text-slate-700 leading-relaxed">
          <strong>Note:</strong> This chart was updated from the original data available in Chad P. Bown, 2021. The US-China Trade War 
          and Phase One Agreement (also published in the Journal of Policy Modeling 43, no. 4: 805-843). The chart does not include 
          data on antidumping or countervailing duties. For information on those, as well as the temporary product exclusions that 
          each side granted to the tariffs in 2018-20, see the paper. The chart has also been updated to fix an error in the original 
          paper regarding the size of the tariff increase in 2021.
        </p>
      </div>

      <div className="not-prose mt-12 pt-8 border-t border-slate-200">
        <h3 className="text-lg font-semibold text-slate-900 mb-4">
          Key Timeline Events
        </h3>
        <div className="space-y-3">
          <div className="flex gap-4">
            <div className="font-semibold text-blue-700 text-sm w-32 flex-shrink-0">January 1, 2018</div>
            <div className="text-sm text-slate-700">US Section 201 tariffs imposed on solar panels and washing machines</div>
          </div>
          <div className="flex gap-4">
            <div className="font-semibold text-blue-700 text-sm w-32 flex-shrink-0">March 12, 2018</div>
            <div className="text-sm text-slate-700">US Section 232 tariffs imposed on steel, aluminum, and derivative products</div>
          </div>
          <div className="flex gap-4">
            <div className="font-semibold text-blue-700 text-sm w-32 flex-shrink-0">April 5, 2025</div>
            <div className="text-sm text-slate-700">US tariffs of 10 percent imposed on nearly all countries, including China, under IEEPA</div>
          </div>
          <div className="flex gap-4">
            <div className="font-semibold text-blue-700 text-sm w-32 flex-shrink-0">August 7, 2025</div>
            <div className="text-sm text-slate-700">US tariffs adjusted on nearly all countries with a trade surplus with the US, under IEEPA</div>
          </div>
          <div className="flex gap-4">
            <div className="font-semibold text-blue-700 text-sm w-32 flex-shrink-0">September 25, 2025</div>
            <div className="text-sm text-slate-700">US tariffs adjusted on imports from EU after clarification of US-EU deal</div>
          </div>
        </div>
      </div>
    </article>
  );
}

/* --- Article 4: EU Tariff Duties on Russia and Belarus --- */
function EUTariffRussiaBelarusArticle() {
  return (
    <article className="prose prose-lg max-w-none">
      <div className="not-prose mb-8">
        <p className="text-sm text-slate-500 mb-6">23 July 2025</p>
      </div>

      <p className="text-lg text-slate-700 leading-relaxed mb-6">
        The EU has imposed new tariffs applicable on <strong>1 July 2025</strong> on the remaining agricultural products 
        and certain fertilisers (nitrogen-based products) from Russia and Belarus that were not yet subject to extra customs duties.
      </p>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        Products classified under codes of the Combined Nomenclature (CN) listed in{" "}
        <span className="text-blue-600 font-medium">Annex I to Regulation (EU) 2025/1227</span> (around those not affected) 
        that are imported into the Union and that originate in or have been exported directly or indirectly from Russia or Belarus, 
        shall be subject to an ad valorem customs duty of <strong>50%</strong>, to be applied in addition to the applicable 
        Common Customs Tariff rate.
      </p>

      <div className="not-prose my-10">
        <h3 className="text-xl font-semibold text-slate-900 mb-4">
          Products Affected by New Tariff Duties
        </h3>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        Some of the products affected by these new tariff duties are:
      </p>

      <div className="bg-white border border-slate-200 rounded-lg p-6 my-8">
        <ul className="space-y-2 text-slate-700 text-sm">
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Live animals, meat, dairy produce, birds' eggs, natural honey, edible products of animal origin</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Live trees and other plants; bulbs, roots and the like; cut flowers and ornamental foliage</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Edible vegetables and certain roots and tubers (except peas and chickpeas) and edible fruits and nuts, peel of citrus fruit or melons</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Coffee, tea, maté and spices</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Animal fats and oils</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Sugar and sugar confectionery</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Cocoa and cocoa preparations</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Preparations of cereals and vegetables, flour, starch or milk and pastry products</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Preparations for animal feeding</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Tobacco</span>
          </li>
          <li className="flex items-start gap-3">
            <span className="text-slate-400 mt-1">•</span>
            <span>Raw textiles</span>
          </li>
        </ul>
      </div>

      <div className="not-prose my-10">
        <h3 className="text-xl font-semibold text-slate-900 mb-4">
          Fertilizer Tariffs
        </h3>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        The Regulation also establishes a tariff of <strong>6.5%</strong> on fertilizers included in Annex II imported from 
        Russia and Belarus, in addition to a levy of between 40 and 45 euros per ton for the period 2025-2026 that will 
        increase gradually, over a transitional period of three years, rising to €430 per ton in 2028.
      </p>

      <div className="bg-slate-50 border-l-4 border-slate-400 p-6 my-8 rounded">
        <p className="text-base text-slate-800 leading-relaxed mb-3">
          <strong>Affected Fertilizer Products:</strong>
        </p>
        <ul className="space-y-2 text-slate-700 text-sm ml-4">
          <li>• Products under HS code 3102 – Mineral or chemical fertilizers, nitrogenous</li>
          <li>• Products under HS codes 3105 20, 3105 30, 3105 40, 3105 51, 3105 59 and 3105 90</li>
        </ul>
      </div>

      <div className="not-prose my-10">
        <h3 className="text-xl font-semibold text-slate-900 mb-4">
          Market Monitoring and Impact
        </h3>
      </div>

      <p className="text-base text-slate-700 leading-relaxed mb-5">
        The EU Commission will monitor price increases and any possible damage to the internal market or the EU agriculture 
        sector and will take action to mitigate the impact.
      </p>

      <div className="bg-white border border-slate-200 p-6 rounded-lg my-8">
        <p className="text-base text-slate-700 leading-relaxed">
          <strong>Policy Objectives:</strong> The aim of these new tariffs is not only to weaken Russia's war economy, 
          as revenues from the sale of Russian and Belarusian fertilisers and agricultural products are contributing to 
          the war against Ukraine, but also to help reduce the EU's dependence on Russia and Belarus, and boost diversification 
          and domestic production.
        </p>
      </div>

      <div className="bg-slate-100 border-l-4 border-blue-600 p-6 my-8 rounded">
        <p className="text-sm text-slate-700 leading-relaxed">
          <strong>Reference:</strong> Annex I to Regulation (EU) 2025/1227 provides the complete list of CN codes for 
          agricultural products affected by the 50% ad valorem customs duty. Annex II contains the specific fertilizer 
          products subject to the graduated tariff increase.
        </p>
      </div>
    </article>
  );
}

export default function BlogPost() {
  const { slug } = useParams();
  const post = posts.find((p) => p.slug === slug);

  if (!post) {
    return (
      <div className="bg-slate-50 min-h-screen">
        <div className="mx-auto max-w-4xl px-6 py-20 text-center">
          <h2 className="text-2xl font-bold text-slate-900 mb-4">Article Not Found</h2>
          <p className="text-slate-600 mb-6">
            The article you're looking for doesn't exist or has been removed.
          </p>
          <Link 
            to="/blog" 
            className="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
            Back to News
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-slate-50 min-h-screen">
      {/* Article Header */}
      <div className="bg-white border-b border-slate-200">
        <div className="mx-auto max-w-4xl px-6 py-8">
          <Link 
            to="/blog" 
            className="inline-flex items-center gap-2 text-sm font-medium text-blue-600 hover:text-blue-700 mb-6"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
            Back to News
          </Link>

          <div className="mb-4">
            <span className="inline-block px-3 py-1 text-xs font-semibold text-blue-700 bg-blue-50 rounded-full">
              Trade Policy
            </span>
          </div>

          <h1 className="text-4xl md:text-5xl font-bold text-slate-900 leading-tight mb-6">
            {post.title}
          </h1>

          <div className="flex items-center gap-4 text-sm text-slate-600 pb-6 border-b border-slate-200">
            <div className="flex items-center gap-2">
              <div className="w-10 h-10 rounded-full bg-blue-600 flex items-center justify-center text-white font-semibold">
                {post.author.charAt(0)}
              </div>
              <span className="font-medium text-slate-900">{post.author}</span>
            </div>
            <span>•</span>
            <time>{new Date(post.date).toLocaleDateString('en-US', { 
              month: 'long', 
              day: 'numeric', 
              year: 'numeric' 
            })}</time>
            <span>•</span>
            <span>{post.readingMins} min read</span>
          </div>
        </div>
      </div>

      {/* Featured Image */}
      <div className="bg-white">
        <div className="mx-auto max-w-4xl px-6 py-8">
          <img
            src={post.cover}
            alt={post.title}
            className="w-full h-[400px] object-cover rounded-xl shadow-lg"
          />
        </div>
      </div>

      {/* Article Content */}
      <div className="bg-white">
        <div className="mx-auto max-w-4xl px-6 py-12">
          {slug === "what-tariffs-has-trump-announced-and-why" ? (
            <TrumpTariffArticle />
          ) : slug === "reciprocal-tariffs-2025" ? (
            <ReciprocalTariffUpdate />
          ) : slug === "us-china-trade-war-tariffs-chart" ? (
            <USChinaTradeWarArticle />
          ) : slug === "eu-tariff-duties-russia-belarus-agricultural" ? (
            <EUTariffRussiaBelarusArticle />
          ) : (
            <div className="prose prose-lg max-w-none">
              <ReactMarkdown rehypePlugins={[rehypeRaw]}>
                {post.body}
              </ReactMarkdown>
            </div>
          )}
        </div>
      </div>

      {/* Share Section */}
      <div className="bg-slate-50 border-t border-slate-200">
        <div className="mx-auto max-w-4xl px-6 py-8">
          <div className="flex items-center justify-between">
            <Link
              to="/blog"
              className="inline-flex items-center gap-2 text-blue-600 font-semibold hover:gap-3 transition-all"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
              More Articles
            </Link>
            <div className="text-sm text-slate-500">
              Published {new Date(post.date).toLocaleDateString('en-US', { 
                month: 'long', 
                day: 'numeric', 
                year: 'numeric' 
              })}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
