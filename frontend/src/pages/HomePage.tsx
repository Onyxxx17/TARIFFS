import TariffCalculatorSection from "../components/TariffCalculationSection";
import {
  LineChart,
  Line,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  BarChart,
  Bar,
} from "recharts";

const sampleData = [
  { month: "Jan-24", goods: 102, electronics: 106 },
  { month: "May-24", goods: 100, electronics: 104 },
  { month: "Sep-24", goods: 99, electronics: 101 },
  { month: "Jan-25", goods: 98, electronics: 97 },
  { month: "May-25", goods: 99, electronics: 95 },
];

const barData = [
  { month: "Apr", receipts: 14 },
  { month: "May", receipts: 20 },
  { month: "Jun", receipts: 26.6 },
];

export default function HomePage() {
  return (
    <>
      {/* HERO */}
      <section className="relative overflow-hidden bg-[#0f2247]">
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
        <div className="relative mx-auto max-w-7xl h-[58vh] md:h-[84vh] flex items-center px-6">
          <div className="max-w-3xl">
            <p className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full border border-white/20 bg-white/10 backdrop-blur-sm text-sm text-white/80">
              ⚖️ Trade & Compliance Tool
            </p>
            <h1 className="mt-6 text-4xl md:text-6xl font-semibold tracking-tight text-white">
              Smarter <span className="text-[#4ea0ff]">Tariff</span> Calculator
            </h1>
            <p className="mt-4 text-lg md:text-xl text-white/85">
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

          {/* chart card */}
          <div className="hidden md:block absolute right-1 top-1/3 bg-white/10 rotate-3 backdrop-blur-md p-3 rounded-xl border border-white/20 shadow-lg w-[250px]">
            <h3 className="text-white text-sm font-medium mb-3">
              Tariff-Sensitive Index
            </h3>
            <ResponsiveContainer width="100%" height={100}>
              <LineChart data={sampleData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#ffffff20" />
                <XAxis dataKey="month" stroke="#fff" fontSize={11} />
                <YAxis stroke="#fff" fontSize={11} domain={[90, 110]} />
                <Tooltip
                  contentStyle={{
                    background: "#0f2247",
                    border: "none",
                    borderRadius: "8px",
                    color: "#fff",
                  }}
                />
                <Line
                  type="monotone"
                  dataKey="goods"
                  stroke="#8b5cf6"
                  strokeWidth={2}
                  dot={false}
                />
                <Line
                  type="monotone"
                  dataKey="electronics"
                  stroke="#38bdf8"
                  strokeWidth={2}
                  dot={false}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>

          {/* Bar chart card */}
          <div className="bg-white/10 backdrop-blur-md p-5 rounded-xl border border-white/20 shadow-lg w-[240px] -rotate-4 right-70 absolute top-1/3">
            <h3 className="text-white text-sm font-medium mb-3">
              U.S. Customs Receipts ($B)
            </h3>
            <ResponsiveContainer width="100%" height={130}>
              <BarChart data={barData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#ffffff20" />
                <XAxis dataKey="month" stroke="#fff" fontSize={11} />
                <YAxis stroke="#fff" fontSize={11} />
                <Tooltip
                  contentStyle={{
                    background: "#0f2247",
                    border: "none",
                    borderRadius: "8px",
                    color: "#fff",
                  }}
                />
                <Bar dataKey="receipts" fill="#0050d8" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </section>

      {/* the map and form  */}
      <TariffCalculatorSection />
    </>
  );
}
