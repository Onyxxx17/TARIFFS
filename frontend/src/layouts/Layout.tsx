import { Outlet, useLocation } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import type { ReactNode } from "react";

type LayoutProps = {
  children?: ReactNode; // optional, if you use <Layout>…</Layout>
};

export default function Layout({ children }: LayoutProps) {
  const location = useLocation();
  const isAuthPage = ["/login", "/signup"].includes(location.pathname);

  return (
    <>
      {!isAuthPage && <Header />}
      <main className={isAuthPage ? "min-h-screen" : "pt-[88px] min-h-screen"}>
        {children ?? <Outlet />} {/* If children provided, show them; otherwise render routed page */}
      </main>
      {!isAuthPage && <Footer />}
    </>
  );
}