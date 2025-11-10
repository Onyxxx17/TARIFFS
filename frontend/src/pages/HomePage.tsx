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
              Duties, taxes, landed cost - visualized and calculated instantly.
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

      {/* the map and form  */}
      <TariffCalculatorSection />
    </>
  );
}
