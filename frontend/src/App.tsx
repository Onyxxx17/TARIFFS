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
         
          <p className="text-blue-100/80 font-semibold mb-4 mt-4 text-3xl" >
            Join 15,725+ other loving customers
          </p>

          <h1 className="mx-auto max-w-5xl text-4xl md:text-6xl font-extrabold leading-[1.1] text-white body-paragraph" style={{marginTop: "50px", marginBottom: "70px"}}>
            Simplify Import Duties and Trade Costs
          </h1>

          <p className="mx-auto text-2xl text-blue-100/85 mt-4 body-paragraph">
            Accurate tariff calculations for global trade, with a focus on
            agriculture and clarity in costs.
          </p>

          <div style={{margin:"100px"}}>
            <p className="mx-auto text-3xl text-blue-100/85 mt-4 font-semibold body-paragraph">
              Enter two countries to calculate tariffs and get back results in real time!
            </p>
            <div style={{marginTop:"70px"}}>
              <div style={{display:"inline-block", marginRight:"220px"}}>
                <p className="mx-auto text-2xl text-blue-100/85 mt-4 font-semibold body-paragraph">Import from:</p>
              </div>
              <div style={{display:"inline-block", marginLeft:"220px"}}>
                <p className="mx-auto text-2xl text-blue-100/85 mt-4 font-semibold body-paragraph">Import to:</p>
              </div>
            </div>
            <input name="country 1" placeholder="Enter country 1 (e.g. USA)" className="px-4 py-2 rounded-lg border border-gray-300 bg-transparent text-white placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500" style={{width: "460px", height:"60px", marginTop: "30px", marginRight:"50px" ,fontSize:"25px"}}/>
            <input name="country 2" placeholder="Enter country 2 (e.g. China)" className="px-4 py-2 rounded-lg border border-gray-300 bg-transparent text-white placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500" style={{width: "460px", height:"60px", marginTop: "30px", marginLeft:"50px", fontSize:"25px"}}/>
          </div>

          <button className="mt-6 px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500" style={{width: "300px", height:"70px", marginTop: "10px", marginRight:"30px", fontSize:"25px"}}>
              Search now!
          </button>
        
        </div>
      </section>

      {/* <section id="new section" className="min-h-screen bg-[#f3f7ff]"> */}
        {/* Add content here */}
       
         
      {/* </section> */}
    </Layout>
  );
}
