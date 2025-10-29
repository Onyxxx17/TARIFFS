import { Outlet } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import type { ReactNode } from "react";

type LayoutProps = {
  children?: ReactNode; // optional, if you use <Layout>…</Layout>
};

export default function Layout({ children }: LayoutProps) {
  return (
    <div className="min-h-screen bg-white dark:bg-slate-900 transition-colors">
      <Header />
      <main className="pt-[72px] min-h-screen">
        {children ?? <Outlet />} {/* If children provided, show them; otherwise render routed page */}
      </main>
      <Footer />
    </div>
  );
}
