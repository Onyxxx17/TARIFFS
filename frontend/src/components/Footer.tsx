type FooterProps = {
  brandName?: string;
  tagline?: string;
};

export default function Footer({
  brandName = "TARIFF",
  tagline = "Empowering Smarter Decisions with AI-Driven Tariff Insights and Business Trends.",
}: FooterProps) {
  return (
    <footer className="bg-white text-slate-700">
      <div className="max-w-7xl mx-auto px-6 py-14 grid grid-cols-1 md:grid-cols-12 gap-43">
        {/* Left: brand + socials */}
        <div className="md:col-span-5">
          <div className="flex items-center gap-3">
            <span className="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-gradient-to-br from-blue-600 to-blue-700 shadow-sm">
              {/*  bolt logo */}
              <svg
                width="20"
                height="20"
                viewBox="0 0 24 24"
                className="text-white"
              >
                <path
                  d="M14.9 3.5c-3.6 2.1-6.1 3.2-7.3 3.4-.6.1-.8.7-.3 1.1l2.9 2.1c.3.2.3.6 0 .8l-4.8 3.4c-.5.3-.3 1 .3 1 3.4-.2 6.2-.9 8.4-2 .6-.3 1.3.3 1.1.9l-1.2 4c-.2.7.6 1.2 1.1.7 3.5-3.5 5.3-7.1 5.3-10.7 0-2.1-.7-3.6-2.2-4.4-.4-.2-.9-.2-1.3 0Z"
                  fill="currentColor"
                />
              </svg>
            </span>
            <span className="text-xl font-semibold text-slate-900">
              {brandName}
            </span>
          </div>

          <p className="mt-4 max-w-md text-slate-500 leading-relaxed">
            {tagline}
          </p>

          <div className="mt-6 flex items-center gap-3">
            {/* Socials Icons */}

            <SocialIcon label="Facebook" href="https://facebook.com" kind="facebook" />
            <SocialIcon label="Instagram" href="https://instagram.com" kind="instagram" />
            <SocialIcon label="LinkedIn" href="https://linkedin.com" kind="linkedin" />
            <SocialIcon label="X" href="https://x.com" kind="x" />
          </div>
        </div>

        {/* Right: 2 columns of links */}
        <div className="md:col-span-5 grid grid-cols-2 md:grid-cols-3 gap-12">
          <LinkColumn
            title="About Us"
            links={[
              { label: "Homepage", href: "#" },
              { label: "Solution", href: "#" },
              { label: "About", href: "#" },
              { label: "Contact", href: "#" },
            ]}
          />
          <LinkColumn
            title="Features"
            links={[
              { label: "Blog", href: "#" },
              { label: "Blog Details", href: "#" },
              { label: "Review", href: "#" },
              { label: "FAQ", href: "#" },
            ]}
          />
        </div>
      </div>

      {/* bottom bar */}
      <div className="border-t border-slate-200">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <span className="h-2.5 w-2.5 rounded-full bg-blue-600" />
            <span className="text-sm text-slate-600">
              {brandName} © {new Date().getFullYear()}
            </span>
          </div>
          {/* add legal links here */}
          <div className="hidden md:flex items-center gap-4 text-sm text-slate-500">
            <a href="#" className="hover:text-slate-900">
              Privacy
            </a>
            <a href="#" className="hover:text-slate-900">
              Terms
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}

//components

function LinkColumn({
  title,
  links,
}: {
  title: string;
  links: { label: string; href: string }[];
}) {
  return (
    <div>
      <h3 className="text-slate-900 font-semibold"> {title} </h3>
      <ul className="mt-4 space-y-3">
        {links.map((l) => (
          <li key={l.label}>
            <a
              href={l.href}
              className="text-slate-500 hover:text-slate-900 transition-colors"
            >
              {l.label}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}

function SocialIcon({
  label,
  href,
  kind,
}: {
  label: string;
  href: string;
  kind: "facebook" | "instagram" | "linkedin" | "x";
}) {
  return (
    <a
      href={href}
      target="_blank"  //newtab
      rel = "noopener noreferrer"
      aria-label={label}
      className="inline-flex h-10 w-10 items-center justify-center rounded-full border border-slate-300 text-slate-600 hover:bg-slate-50 hover:text-slate-900 transition"
    >
      {kind === "facebook" && (
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="currentColor"
          aria-hidden
        >
          <path d="M22 12.06A10 10 0 1 0 10.5 22v-7H8v-3h2.5V9.5c0-2.1 1.3-3.5 3.4-3.5 1 0 2 .1 2 .1v2.3h-1.2c-1.2 0-1.6.74-1.6 1.5V12H16l-.5 3h-2.3v7A10 10 0 0 0 22 12.06Z" />
        </svg>
      )}
      {kind === "instagram" && (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
          <rect
            x="3"
            y="3"
            width="18"
            height="18"
            rx="5"
            stroke="currentColor"
            strokeWidth="2"
          />
          <circle cx="12" cy="12" r="4" stroke="currentColor" strokeWidth="2" />
          <circle cx="17.5" cy="6.5" r="1.2" fill="currentColor" />
        </svg>
      )}
      {kind === "linkedin" && (
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="currentColor"
          aria-hidden
        >
          <path d="M6.94 7.5a1.94 1.94 0 1 1 0-3.88 1.94 1.94 0 0 1 0 3.88ZM4.8 20.5h4.3V9.3H4.8v11.2ZM13 20.5h4.3v-6.1c0-1.63.97-2.68 2.27-2.68 1.23 0 1.93.84 1.93 2.5v6.28h4.3v-6.98c0-3.46-1.85-5.07-4.32-5.07-2.02 0-3.02 1.13-3.54 1.92h-.06V9.3H13v11.2Z" />
        </svg>
      )}
      {kind === "x" && (
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="currentColor"
          aria-hidden
        >
          <path d="M3 3h3.6L14.1 14l6.3-11h-3.6L9.9 14 3.6 3H3Zm0 18 6.9-6.9 2.1 2.9L8 21H3Zm18 0h-3.6l-5-6.9 2.1-2.9L21 21Z" />
        </svg>
      )}

      <span className="sr-only">{label}</span>
    </a>
  );
}
