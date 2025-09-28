import TariffCalculatorSection from "../components/TariffCalculationSection";

export default function HomePage() {
  return (
    <>
      {/* HERO */}
      <section className="relative overflow-hidden bg-gradient-to-br from-[#2d6ebb] to-[#1e4a8c]">
        <div className="mx-auto max-w-9xl h-[65vh] md:h-[90vh] flex items-center justify-center px-6">
          <div className="z-10 text-center max-w-4xl">
            <p className="text-sm font-semibold text-white/90 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full inline-block border border-white/20">
              Join 15,725+ other customers
            </p>
            <h1 className="mt-8 text-4xl md:text-6xl font-extrabold leading-tight text-white">
              Tariff calculation made simple
            </h1>
            <p className="mt-8 mx-auto max-w-lg text-xl text-white/90 leading-relaxed">
              Accurate tariff calculations for global trade—clear, fast, and reliable.
            </p>
            <div className="mt-12 flex gap-6 justify-center">
              <a className="px-8 py-4 rounded-full bg-white text-[#2d6ebb] font-semibold hover:bg-white/95 transition-colors shadow-lg text-lg">
                Get started
              </a>
              <a className="px-8 py-4 rounded-full border-2 border-white/30 text-white hover:bg-white/10 hover:border-white/50 transition-colors backdrop-blur-sm text-lg">
                Learn More
              </a>
            </div>
          </div>

          {/* <div className="col-span-12 md:col-span-6 relative h-[38vh] md:h-full">
            <div className="absolute inset-y-0 right-[-50%] w-[220%] h-full pointer-events-none translate-y-6">
              <Spline scene="https://prod.spline.design/wCbJcHKT1zpH4ZqL/scene.splinecode" />
            </div>
          </div> */}
        </div>
      </section>

      {/* the map and form  */}
      <TariffCalculatorSection />
  
    </>
  );
}