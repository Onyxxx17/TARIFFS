import { Linkedin, Instagram, Twitter, Mail, Phone } from 'lucide-react';

const contacts = [
	{
		name: 'Yi Nam',
		role: 'Project Lead & Backend Developer',
		avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=YiNam',
		email: 'yinam@tariff-calc.com',
		phone: '+65 8111 2222',
		socials: {
			linkedin: 'https://linkedin.com',
			instagram: 'https://instagram.com',
			x: 'https://x.com',
		},
	},
	{
		name: 'Aung',
		role: 'Backend & Machine Learning',
		avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Aung',
		email: 'aung@tariff-calc.com',
		phone: '+65 8222 3333',
		socials: {
			linkedin: 'https://linkedin.com',
			instagram: 'https://instagram.com',
			x: 'https://x.com',
		},
	},
	{
		name: 'Chue',
		role: 'Frontend Developer',
		avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Chue',
		email: 'chue@tariff-calc.com',
		phone: '+65 8333 4444',
		socials: {
			linkedin: 'https://linkedin.com',
			instagram: 'https://instagram.com',
			x: 'https://x.com',
		},
	},
	{
		name: 'Lin',
		role: 'UI/UX Designer & Frontend',
		avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Lin',
		email: 'lin@tariff-calc.com',
		phone: '+65 8444 5555',
		socials: {
			linkedin: 'https://linkedin.com',
			instagram: 'https://instagram.com',
			x: 'https://x.com',
		},
	},
	{
		name: 'Jonathan',
		role: 'Data Scientist & Frontend',
		avatar: 'https://api.dicebear.com/8.x/avataaars/svg?seed=Jonathan',
		email: 'jonathan@tariff-calc.com',
		phone: '+65 8555 6666',
		socials: {
			linkedin: 'https://linkedin.com',
			instagram: 'https://instagram.com',
			x: 'https://x.com',
		},
	},
];

export default function Contact() {
	return (
		<div className="bg-gray-50 dark:bg-slate-900 py-12 sm:py-16">
			<div className="mx-auto max-w-7xl px-6 lg:px-8">
				<div className="mx-auto max-w-2xl lg:mx-0">
					<h2 className="text-3xl font-bold tracking-tight text-gray-900 dark:text-white sm:text-4xl">
						Our Team
					</h2>
					<p className="mt-6 text-lg leading-8 text-gray-600 dark:text-slate-400">
						Meet the dedicated individuals behind the Tariff Calculation project.
						We&apos;re passionate about making global trade more transparent and
						accessible.
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

							<div className="mt-4 space-y-2 text-sm text-gray-600 dark:text-slate-400">
								<div className="flex items-center gap-2">
									<Mail size={16} />
									<a
										href={`mailto:${person.email}`}
										className="hover:text-blue-600 dark:hover:text-blue-400"
									>
										{person.email}
									</a>
								</div>
								<div className="flex items-center gap-2">
									<Phone size={16} />
									<span>{person.phone}</span>
								</div>
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
			</div>
		</div>
	);
}