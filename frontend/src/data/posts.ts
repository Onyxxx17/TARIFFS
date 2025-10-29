export type Post = {
  slug: string;
  title: string;
  author: string;
  date: string;
  cover: string;
  excerpt: string;
  readingMins: number;
  body: string;
};

export const posts: Post[] = [
  {
    slug: "what-tariffs-has-trump-announced-and-why",
    title: "What tariffs has Trump announced and why?",
    author: "Jennifer Clarke BBC News",
    date: "2025-09-26",
    cover:
      "https://ichef.bbci.co.uk/news/1536/cpsprodpb/ed0c/live/0f72d480-9a6a-11f0-b39f-8faa1a82cdd5.jpg.webp",
    excerpt:
      "President Donald Trump has introduced tariffs on goods reaching the US from countries around the world. Trump argues that the move will boost American manufacturing and create jobs, but critics warn of higher prices and damage to the global economy.",
    readingMins: 3,
    body: ` `
  },
  {
    slug: "reciprocal-tariffs-2025",
    title: "Tariff Update: Reciprocal Tariffs and Other Recent Changes",
    author: "Lisa Mays, Jonathan Wang & Jordan Mallory ",
    date: "2025-08-08",
    cover:
      "https://ichef.bbci.co.uk/news/1536/cpsprodpb/88b6/live/3cb41b00-9a21-11f0-89b7-f92e020b4774.jpg.webp",
    excerpt:
      "On August 7, 2025, President Trump's reciprocal tariffs fully came into effect.",
    readingMins: 5,
    body: ` `,
  },
  {
    slug: "us-china-trade-war-tariffs-chart",
    title: "US-China Trade War Tariffs: An Up-to-Date Chart",
    author: "Chad P. Bown (PIIE)",
    date: "2025-09-25",
    cover:
      "https://images.unsplash.com/photo-1526304640581-d334cdbbf45e?w=1200&h=600&fit=crop",
    excerpt:
      "Average US tariffs on Chinese exports now stand at 57.6 percent and cover 100 percent of all goods. China's average tariffs on US exports are at 32.6 percent and cover 100 percent of all goods.",
    readingMins: 8,
    body: ` `,
  },
  {
    slug: "eu-tariff-duties-russia-belarus-agricultural",
    title: "EU increases tariff duties on Russian and Belarusian imports of agricultural products and fertilizers",
    author: "European Commission",
    date: "2025-07-23",
    cover:
      "https://images.unsplash.com/photo-1464226184884-fa280b87c399?w=1200&h=600&fit=crop",
    excerpt:
      "The EU has imposed new tariffs applicable on 1 July 2025 on the remaining agricultural products and certain fertilisers from Russia and Belarus that were not yet subject to extra customs duties.",
    readingMins: 6,
    body: ` `,
  },
];
