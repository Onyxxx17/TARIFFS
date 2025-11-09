export default function ContactPage() {
  return (
    <section className="min-h-screen bg-gradient-to-b from-blue-50 to-blue-100 flex items-center justify-center py-16 px-6">
      <div className="max-w-6xl w-full bg-white shadow-lg overflow-hidden grid md:grid-cols-2">
        {/* LEFT SIDE */}
        <div className="bg-gradient-to-br from-blue-600 to-blue-400 text-white p-10 flex flex-col justify-center">
          <h2 className="text-4xl font-bold mb-4">Get in Touch</h2>
          <p className="text-blue-100 text-sm mb-6">
            We'd love to hear from you. Whether you have a question, feedback,
            or just want to say hi, feel free to drop a message.
          </p>
          <div className="mt-4">
            <p className="text-sm mb-2 font-medium">Email us at</p>
            <a
              href="mailto:hello@tariffs.com"
              className="text-white font-semibold hover:underline"
            >
              hello@tariffs.com
            </a>
          </div>
        </div>

        {/* RIGHT SIDE */}
        <div className="p-10 md:p-12 bg-white">
          <h3 className="text-2xl font-semibold text-gray-800 mb-6">
            Send us a message
          </h3>
          <form className="space-y-5">
            <div className="flex flex-col md:flex-row md:space-x-4 space-y-4 md:space-y-0">
              <input
                type="text"
                placeholder="First Name"
                className="flex-1 border border-gray-300 rounded-md h-10 p-3 focus:outline-none focus:ring-2 focus:ring-blue-300"
              />
              <input
                type="text"
                placeholder="Last Name"
                className="flex-1 border border-gray-300 rounded-md h-10 p-3 focus:outline-none focus:ring-2 focus:ring-blue-300"
              />
            </div>
            <input
              type="email"
              placeholder="Email *"
              required
              className="w-full border border-gray-300 rounded-md h-10 p-3 focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
            <textarea
              placeholder="Your Message..."
              className="w-full h-32 border border-gray-300 rounded-md p-3 focus:outline-none focus:ring-2 focus:ring-blue-300"
            ></textarea>

            <div className="flex justify-end">
              <button
                type="submit"
                className="w-full md:w-auto h-10 px-6 bg-blue-600 text-white font-medium rounded-md hover:bg-blue-700 transition"
              >
                Send
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>
  );
}
