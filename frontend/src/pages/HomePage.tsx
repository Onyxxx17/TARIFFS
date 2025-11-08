import TariffCalculatorSection from "../components/TariffCalculationSection";

export default function HomePage() {
  return (
    <>
  {/* HERO */}
  {/* lift hero slightly to remove small gap under the fixed header */}
  <section className="relative overflow-hidden bg-[#0f2247] -mt-3 sm:-mt-4">
        {/* dotted grid  */}
        <svg aria-hidden className="absolute inset-0 h-full w-full opacity-25">
          <defs>
            <pattern
              id="dotGrid"
              width="24"
              height="24"
              patternUnits="userSpaceOnUse"
            >
              <circle cx="1.5" cy="1.5" r="1.5" fill="#2d6ebb" />
            </pattern>
          </defs>
          <rect width="100%" height="100%" fill="url(#dotGrid)" />
        </svg>

        {/* gradient  */}
        <div
          aria-hidden
          className="absolute inset-0 bg-gradient-to-br from-[#1b3773]/70 via-transparent to-[#2d6ebb]/30 mix-blend-screen"
        />

        {/* glow */}
        <div
          aria-hidden
          className="absolute right-[10%] top-[20%] h-72 w-72 rounded-full bg-[#2d6ebb] blur-3xl opacity-40"
        />

        {/* content */}
        <div className="relative mx-auto max-w-7xl h-[68vh] md:h-[84vh] flex items-center px-6">
          <div className="max-w-3xl">
            <p className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full border border-white/20 bg-white/10 backdrop-blur-sm text-sm text-white/80">
              ⚖️ Trade & Compliance Tool
            </p>
            <h1 className="mt-6 text-3xl md:text-6xl font-semibold tracking-tight text-white">
              Smarter <span className="text-[#4ea0ff]">Tariff</span> Calculator
            </h1>
            <p className="mt-4 text-base md:text-xl text-white/85">
              Duties, taxes, landed cost — visualized and calculated instantly.
            </p>
            <div className="mt-8">
              <a
                onClick={(e) => {
                  e.preventDefault(); 
                  const target = document.getElementById("tariff-calculation");
                  if (target) {
                    window.scrollTo({
                      top: target.offsetTop - 70, 
                      behavior: "smooth", 
                    });
                  }
                }}
                className="px-7 py-3 rounded-full bg-white text-[#1b4f9c] font-medium shadow-lg hover:bg-white/95 transition"
              >
                Get Started
              </a>
            </div>
          </div>


        </div>
      </section>

      {/* AI Assistant Section */}
      <section className="py-16 bg-gradient-to-br from-slate-50 to-blue-50">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-12">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-blue-100 text-blue-800 text-sm font-medium mb-4">
              🤖 AI-Powered Assistant
            </div>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
              Ask Our <span className="text-blue-600">AI Assistant</span>
            </h2>
            <p className="text-lg text-slate-600 max-w-2xl mx-auto">
              Get instant answers about tariff rates, countries, products, and trade data. 
              Our AI extracts real information from our database and provides expert analysis.
            </p>
          </div>

          <div className="grid md:grid-cols-2 gap-8 items-center">
            {/* Left side - Features */}
            <div className="space-y-6">
              <div className="flex items-start gap-4">
                <div className="flex-shrink-0 w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                  <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-slate-900 mb-2">Real Database Queries</h3>
                  <p className="text-slate-600">Ask questions like "What are the tariff rates for China?" and get actual data from our database.</p>
                </div>
              </div>

              <div className="flex items-start gap-4">
                <div className="flex-shrink-0 w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                  <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                  </svg>
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-slate-900 mb-2">Instant Analysis</h3>
                  <p className="text-slate-600">Get expert analysis and explanations about trade policies, customs procedures, and more.</p>
                </div>
              </div>

              <div className="flex items-start gap-4">
                <div className="flex-shrink-0 w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                  <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                  </svg>
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-slate-900 mb-2">Natural Language</h3>
                  <p className="text-slate-600">Ask questions in plain English - no need to learn complex query syntax.</p>
                </div>
              </div>
            </div>

            {/* Right side - CTA */}
            <div className="bg-white rounded-2xl shadow-xl p-8 border border-slate-200">
              <div className="text-center">
                <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center mx-auto mb-6">
                  <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                  </svg>
                </div>
                
                <h3 className="text-2xl font-bold text-slate-900 mb-4">Try Our AI Assistant</h3>
                <p className="text-slate-600 mb-6">
                  Start a conversation with our AI to get answers about tariffs, trade data, and more.
                </p>
                
                <div className="space-y-3 mb-6">
                  <div className="text-left bg-slate-50 rounded-lg p-3 text-sm text-slate-700">
                    💬 "What are the tariff rates for electronics from China?"
                  </div>
                  <div className="text-left bg-slate-50 rounded-lg p-3 text-sm text-slate-700">
                    💬 "Show me all agriculture products in the database"
                  </div>
                  <div className="text-left bg-slate-50 rounded-lg p-3 text-sm text-slate-700">
                    💬 "Explain how tariffs affect international trade"
                  </div>
                </div>
                
                <a
                  href="/ai-chat"
                  className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-semibold rounded-lg hover:from-blue-700 hover:to-purple-700 transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                >
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                  </svg>
                  Start AI Chat
                </a>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* the map and form  */}
      <TariffCalculatorSection />
    </>
  );
}
