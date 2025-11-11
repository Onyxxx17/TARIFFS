import { Linkedin, Instagram, Twitter } from 'lucide-react';

const contacts = [
  {
    name: 'Yi Nam',
    role: 'Project Lead & Backend Developer',
    avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=YiNam',
    socials: {
      linkedin: 'https://linkedin.com/in/yinam',
      instagram: 'https://instagram.com/yinam',
      x: 'https://x.com/yinam',
    },
  },
  {
    name: 'Aung',
    role: 'Backend & Machine Learning',
    avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Aung',
    socials: {
      linkedin: 'www.linkedin.com/in/aungyethanthein',
      instagram: 'https://instagram.com/aung',
      x: 'https://x.com/aung',
    },
  },
  {
    name: 'Chue',
    role: 'Frontend Developer',
    avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Chue',
    socials: {
      linkedin: 'https://linkedin.com/in/chue',
      instagram: 'https://instagram.com/chue',
      x: 'https://x.com/chue',
    },
  },
  {
    name: 'Lin',
    role: 'UI/UX Designer & Frontend',
    avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Lin',
    socials: {
      linkedin: 'https://linkedin.com/in/lin',
      instagram: 'https://instagram.com/lin',
      x: 'https://x.com/lin',
    },
  },
  {
    name: 'Jonathan',
    role: 'Data Scientist & Frontend',
    avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Jonathan',
    socials: {
      linkedin: 'https://linkedin.com/in/jonathan',
      instagram: 'https://instagram.com/jonathan',
      x: 'https://x.com/jonathan',
    },
  },
];

export default function Contact() {
  return (
    <div className="bg-gray-50 dark:bg-slate-900 py-12 sm:py-16">
      <div className="mx-auto max-w-7xl px-6 lg:px-8">
        <div className="mx-auto max-w-2xl lg:mx-0 mb-16">
          <h1 className="text-4xl font-bold tracking-tight text-gray-900 dark:text-white sm:text-5xl">
            Tariffic Team
          </h1>
          <p className="mt-6 text-lg leading-8 text-gray-600 dark:text-slate-400">
            Meet the dedicated individuals behind the Tariffic platform.
            We're passionate about making global trade more transparent and accessible.
          </p>
        </div>
        
        <ul
          role="list"
          className="mx-auto mt-20 grid max-w-2xl grid-cols-1 gap-x-8 gap-y-16 sm:grid-cols-2 lg:mx-0 lg:max-w-none lg:grid-cols-3"
        >
          {contacts.map((person) => (
            <li
              key={person.name}
              className="rounded-2xl bg-white dark:bg-slate-800 p-8 shadow-lg border border-gray-200 dark:border-slate-700"
            >
              <img
                className="mx-auto h-24 w-24 rounded-full"
                src={person.avatar}
                alt={`${person.name} avatar`}
              />
              <h3 className="mt-6 text-base font-semibold leading-7 tracking-tight text-gray-900 dark:text-white">
                {person.name}
              </h3>
              <p className="text-sm leading-6 text-gray-600 dark:text-slate-400">
                {person.role}
              </p>

              <div className="mt-4 text-center">
                <p className="text-sm text-gray-500 dark:text-slate-400">
                  Connect with me on social media
                </p>
              </div>

              <ul
                role="list"
                className="mt-6 flex justify-center gap-x-6"
              >
                <li>
                  <a
                    href={person.socials.linkedin}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-gray-400 hover:text-gray-500 dark:hover:text-slate-300"
                  >
                    <span className="sr-only">LinkedIn</span>
                    <Linkedin />
                  </a>
                </li>
                <li>
                  <a
                    href={person.socials.instagram}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-gray-400 hover:text-gray-500 dark:hover:text-slate-300"
                  >
                    <span className="sr-only">Instagram</span>
                    <Instagram />
                  </a>
                </li>
                <li>
                  <a
                    href={person.socials.x}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-gray-400 hover:text-gray-500 dark:hover:text-slate-300"
                  >
                    <span className="sr-only">X</span>
                    <Twitter />
                  </a>
                </li>
              </ul>
            </li>
          ))}
        </ul>

        {/* Contact Section */}
        <div className="mt-20">
          <div className="bg-white dark:bg-slate-800 rounded-lg shadow-md p-8 text-center max-w-2xl mx-auto">
            <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-4">Contact Us</h2>
            
            <p className="text-gray-600 dark:text-slate-400 mb-6">
              Have questions or need support? We'd love to help!
            </p>

            <a 
              href="mailto:support@tariffic.com?subject=Contact%20Inquiry"
              className="inline-flex items-center justify-center bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition duration-200 font-medium"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              Email Us at support@tariffic.com
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
