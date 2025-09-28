import { useRef, useEffect, useState } from "react";
import { select, geoPath, geoMercator } from "d3";
import useResizeObserver from "../useResizeObserver";

type FC = any;

type Props = {
  height?: number;
  onPick?: (info: { name: string; code?: string }) => void;
  baseColor?: string;
  highlightColor?: string;
};

type Callout = {
  sx: number; sy: number;
  lx: number; ly: number;
  side: "tr" | "tl" | "br" | "bl";
  text: string;
};

function computeCalloutPosition(
  sx: number, sy: number, w: number, h: number,
  opts = { offset: 60, pad: 12 }
) {
  const { offset, pad } = opts;
  const sideH = sx < w / 2 ? "r" : "l";
  const sideV = sy < h / 2 ? "b" : "t";
  const side = (sideV + sideH) as Callout["side"];
  const dx = side.includes("r") ? offset : -offset;
  const dy = side.includes("t") ? -offset * 0.6 : offset * 0.6;
  let lx = sx + dx;
  let ly = sy + dy;
  lx = Math.max(pad, Math.min(w - pad, lx));
  ly = Math.max(pad, Math.min(h - pad, ly));
  return { lx, ly, side };
}

/** English from Natural Earth */
const getName = (p: any): string =>
  p?.NAME_EN ?? p?.ADMIN ?? p?.NAME ?? p?.name ?? "Unknown";

/** Return ISO alpha-3 (SGP, USA, CHN) */
const getAlpha3 = (p: any): string | undefined => {
  if (!p) return undefined;
  const candidates = [
    p.ISO_A3_EH,  
    p.ADM0_A3,
    p.ISO_A3,
    p.SOV_A3,
    p.iso_a3,
    p.adm0_a3,
  ];
  for (const c of candidates) {
    if (c && c !== "-99" && c !== -99) return String(c).toUpperCase();
  }
  
  for (const k of Object.keys(p)) {
    if (k.toLowerCase().startsWith("adm0_a3_")) {
      const v = p[k];
      if (v && v !== "-99" && v !== -99) return String(v).toUpperCase();
    }
  }
  return undefined;
};

export default function GeoChart({
  height = 480,
  onPick,
  baseColor = "#b3c5db", //bg one
  highlightColor = "#030e61", //when click 
}: Props) {
  const svgRef = useRef<SVGSVGElement | null>(null);
  const wrapperRef = useRef<HTMLDivElement | null>(null);
  const rect = useResizeObserver(wrapperRef as React.RefObject<HTMLElement>);

  const [geojson, setGeojson] = useState<FC | null>(null);
  const [selectedCode, setSelectedCode] = useState<string | null>(null);
  const [label, setLabel] = useState<Callout | null>(null);

  // load from /public - custom.geo.json
  useEffect(() => {
    let active = true;
    (async () => {
      const res = await fetch("/custom.geo.json");
      const fc = await res.json();
      if (active) setGeojson(fc);
    })();
    return () => { active = false; };
  }, []);

  useEffect(() => {
    if (!svgRef.current || !wrapperRef.current || !geojson) return;

    const svg = select(svgRef.current);
    const width = rect?.width ?? wrapperRef.current.getBoundingClientRect().width;
    const h = rect?.height ?? height;

    svg.attr("viewBox", `-100 -100 ${width + 200} ${h + 0}`)
       .attr("width", width)
       .attr("height", h);

    const projection = geoMercator().scale(120).translate([width / 2, h / 2]);
    const path = geoPath().projection(projection);

    const fillFor = (d: any) => {
      const code = getAlpha3(d.properties);               //use alpha-3
      return code && code === selectedCode ? highlightColor : baseColor;
    };

    svg
      .selectAll<SVGPathElement, any>(".country")
      .data((geojson as any).features)
      .join("path")
      .attr("class", "country")
      .attr("d", (d: any) => path(d)!)
      .attr("fill", fillFor as any)
      .attr("stroke", "#ffffff")
      .attr("stroke-width", 0.5)
      .style("cursor", "pointer")
      .on("click", (evt: { currentTarget: Element; clientX: number; clientY: number }, d: any) => {
        const name = getName(d.properties);
        const code = getAlpha3(d.properties) ?? null;     // SGP, USA
        setSelectedCode(code);

        // recolor
        svg.selectAll<SVGPathElement, any>(".country")
           .attr("fill", (x: any) => (fillFor as any)(x));

        // callout position
        const box = wrapperRef.current!.getBoundingClientRect();
        const sx = evt.clientX - box.left;
        const sy = evt.clientY - box.top;
        const { lx, ly, side } = computeCalloutPosition(sx, sy, box.width, box.height);

        // tooltip text — keep code in display
        const text = code ? `${name} (${code})` : name;
        setLabel({ sx, sy, lx, ly, side, text });

        // notify parent with unique identifier 
        onPick?.({ name, code: code ?? undefined });
      })
      .selectAll("title")
      .data((d: any) => [d])
      .join("title")
      .text((d: any) => getName(d.properties));
  }, [geojson, rect, height, baseColor, highlightColor, selectedCode]);

  return (
    <div ref={wrapperRef} style={{ position: "relative", width: "100%", height }}>
      <svg ref={svgRef} />
      {label && (
        <>
          <svg
            style={{ position: "absolute", left: 0, top: 0, width: "100%", height: "100%", pointerEvents: "none" }}
          >
            <defs>
              <filter id="calloutShadow" x="-20%" y="-20%" width="140%" height="140%">
                <feDropShadow dx="0" dy="1" stdDeviation="2" floodOpacity="0.25" />
              </filter>
            </defs>
            {(() => {
              const { sx, sy, lx, ly } = label;
              const cx = (sx + lx) / 2;
              const cy = (sy + ly) / 2 + (ly > sy ? 24 : -24);
              const d = `M ${sx} ${sy} Q ${cx} ${cy} ${lx} ${ly}`;
              return <path d={d} stroke="#2563eb" strokeWidth={2} fill="none" filter="url(#calloutShadow)" />;
            })()}
            <circle cx={label.sx} cy={label.sy} r={3} fill="#2563eb" />
          </svg>

          <div
            style={{
              position: "absolute",
              left: label.lx,
              top: label.ly,
              transform: "translate(-50%, -100%)",
              background: "rgba(255,255,255,0.9)",
              color: "#0f172a",
              fontSize: 12,
              padding: "8px 10px",
              borderRadius: 8,
              boxShadow: "0 4px 16px rgba(2,6,23,0.18)",
              border: "1px solid rgba(2,6,23,0.06)",
              pointerEvents: "none",
              whiteSpace: "nowrap",
            }}
          >
            {label.text}
            <span
              style={{
                position: "absolute",
                width: 0, height: 0,
                borderLeft: "6px solid transparent",
                borderRight: "6px solid transparent",
                borderTop: "6px solid white",
                left: label.side === "tl" || label.side === "bl" ? "14px" : "calc(100% - 14px)",
                bottom: "-6px",
                transform: label.side === "tl" || label.side === "tr" ? "rotate(0deg)" : "rotate(180deg)",
                filter: "drop-shadow(0 -1px 1px rgba(2,6,23,.08))",
              }}
            />
          </div>
        </>
      )}
    </div>
  );
}
