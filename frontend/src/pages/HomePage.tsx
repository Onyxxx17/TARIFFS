//import Layout from "../layouts/Layout";
import TariffCalculatorSection from "../components/TariffCalculationSection";

export default function HomePage() {
  return (
    <>
      {/* HERO */}
      <section className="relative overflow-hidden bg-[#2d6ebb]">
        <div className="mx-auto max-w-9xl h-[65vh] md:h-[90vh] grid grid-cols-12 items-center gap-6 px-6">
          <div className="col-span-12 md:col-span-6 z-10 ml-4">
            <p className="text-sm font-semibold text-blue-700/80">Join 15,725+ other customers</p>
            <h1 className="mt-3 text-4xl md:text-5xl font-extrabold leading-tight text-slate-900">
              Tariff calculation made simple
            </h1>
            <p className="mt-3 max-w-md text-slate-100/80">
              Accurate tariff calculations for global trade—clear, fast, and reliable.
            </p>
            <div className="mt-6 flex gap-3">
              <a className="px-5 py-3 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700">
                Get started
              </a>
              <a className="px-5 py-3 rounded-full border border-slate-300 text-slate-800 hover:bg-slate-50">
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