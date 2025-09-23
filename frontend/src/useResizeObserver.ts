import { useEffect, useState } from "react";

export default function useResizeObserver<T extends HTMLElement>(ref: React.RefObject<T>) {
  const [rect, setRect] = useState<DOMRect | null>(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const ro = new ResizeObserver(() => setRect(el.getBoundingClientRect()));
    ro.observe(el);
    setRect(el.getBoundingClientRect());
    return () => ro.disconnect();
  }, [ref]);

  return rect;
}
