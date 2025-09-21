import Spline from "@splinetool/react-spline";

export default function HomePage() {
  return (
    <>
      <section className="relative overflow-hidden bg-[#2d6ebb]">
        <div className="mx-auto max-w-9xl h-[65vh] md:h-[90vh] grid grid-cols-12 items-center gap-6 px-6">
          {/* LEFT : text */}
          <div className="col-span-12 md:col-span-6 z-10 ml-4">
            <p className="text-sm font-semibold text-blue-700/80">
              Join 15,725+ other customers
            </p>
            <h1 className="mt-3 text-4xl md:text-5xl font-extrabold leading-tight text-slate-900">
              Tariff calculation made simple
            </h1>
            <p className="mt-3 max-w-md text-slate-600">
              Accurate tariff calculations for global trade—clear, fast, and reliable.
            </p>
            <div className="mt-6 flex gap-3">
              <button className="px-5 py-3 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700">
                Get started
              </button>
              <button className="px-5 py-3 rounded-full border border-slate-300 text-slate-800 hover:bg-slate-50">
                Learn More
              </button>
            </div>
          </div>

          {/* RIGHT: Spline */}
          <div className="col-span-12 md:col-span-6 relative h-[38vh] md:h-full">
            <div className="absolute inset-y-0 right-[-50%] w-[220%] h-full pointer-events-none translate-y-6">
              <Spline scene="https://prod.spline.design/wCbJcHKT1zpH4ZqL/scene.splinecode" />
            </div>
          </div>
        </div>
      </section>

      <section id="section2" className="min-h-screen bg-gradient-to-b from-[#2d6ebb] to-white flex items-center">
        <div style={{ margin: "100px" }}>
          <p className="mx-auto text-3xl text-blue-800/85 mt-4 font-semibold body-paragraph">
            Enter two countries to calculate tariffs and get back results in real time!
          </p>

          <div style={{ marginTop: "70px" }}>
            <div style={{ display: "inline-block", marginRight: "220px" }}>
              <p className="mx-auto text-2xl text-blue-800/85 mt-4 font-semibold body-paragraph">Import from:</p>
            </div>
            <div style={{ display: "inline-block", marginLeft: "220px" }}>
              <p className="mx-auto text-2xl text-blue-800/85 mt-4 font-semibold body-paragraph">Import to:</p>
            </div>
          </div>

          <div className="flex flex-col items-center">
            <div>
              <input
                name="country1"
                placeholder="Enter country 1 (e.g. USA)"
                className="px-4 py-2 rounded-lg border border-gray-800 bg-transparent text-grey placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
                style={{ width: "460px", height: "60px", marginTop: "30px", marginRight: "50px", fontSize: "25px" }}
              />
              <input
                name="country2"
                placeholder="Enter country 2 (e.g. China)"
                className="px-4 py-2 rounded-lg border border-gray-800 bg-transparent text-grey placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
                style={{ width: "460px", height: "60px", marginTop: "30px", marginLeft: "50px", fontSize: "25px" }}
              />
            </div>

            <button
              className="mt-6 px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
              style={{ width: "300px", height: "70px", marginTop: "30px", fontSize: "25px" }}
            >
              Search now!
            </button>
          </div>
        </div>
      </section>
    </>
  );
}