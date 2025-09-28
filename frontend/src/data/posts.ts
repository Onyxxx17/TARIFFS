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
      "On August 7, 2025, President Trump’s reciprocal tariffs fully came into effect.",
    readingMins: 5,
    body: ` `,
  },
];
