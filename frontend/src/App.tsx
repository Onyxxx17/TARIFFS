import Layout from "./layouts/Layout";

export default function App() {
  return (
    <Layout>
      <section className="relative isolate min-h-[calc(100vh-72px)] overflow-hidden">
        <img
          src="/hero-gradient.jpg"
          alt="Gradient background"
          className="absolute inset-0 h-full w-full object-cover object-left-top"
        />

        {/* content */}
        <div className="relative z-10 max-w-7xl mx-auto px-6 py-24 md:py-32 text-center">
          <p className="text-blue-100/80 font-semibold mb-4">
            Join 15,725+ other loving customers
          </p>

          <h1 className="mx-auto max-w-5xl text-4xl md:text-6xl font-extrabold leading-[1.1] text-white">
            Simplify Import Duties and Trade Costs
          </h1>

          <p className="mx-auto max-w-2xl text-blue-100/85 mt-4">
            Accurate tariff calculations for global trade, with a focus on
            agriculture and clarity in costs.
          </p>
        </div>
      </section>

      <section id="new section" className="min-h-screen bg-[#f3f7ff]">
        {/* Add content here */}

        
      </section>
    </Layout>
  );
}
