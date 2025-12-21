import Link from 'next/link'

export default function Home() {
    return (
        <div className="max-w-4xl mx-auto">
            <div className="text-center py-12">
                <h1 className="text-5xl font-bold text-united-blue mb-4">
                    Welcome to United ✈️
                </h1>
                <p className="text-xl text-text-secondary mb-8">
                    Manage your passengers and flight bookings with ease
                </p>

                <div className="flex gap-4 justify-center">
                    <Link
                        href="/passengers"
                        className="bg-united-blue text-white px-8 py-3 rounded-lg font-semibold hover:bg-united-dark-blue transition"
                    >
                        View Passengers
                    </Link>
                    <Link
                        href="/passengers/new"
                        className="bg-success-green text-white px-8 py-3 rounded-lg font-semibold hover:bg-green-600 transition"
                    >
                        Add Passenger
                    </Link>
                </div>
            </div>

            <div className="grid md:grid-cols-3 gap-6 mt-12">
                <div className="bg-card-bg p-6 rounded-lg shadow-md hover:shadow-lg transition">
                    <div className="text-3xl mb-2">👥</div>
                    <h3 className="text-xl font-bold mb-2 text-united-blue">Manage Passengers</h3>
                    <p className="text-text-secondary">
                        Add, edit, and view passenger information with profile images
                    </p>
                </div>

                <div className="bg-card-bg p-6 rounded-lg shadow-md hover:shadow-lg transition">
                    <div className="text-3xl mb-2">✈️</div>
                    <h3 className="text-xl font-bold mb-2 text-united-blue">Flight Bookings</h3>
                    <p className="text-text-secondary">
                        Create and manage flight bookings for your passengers
                    </p>
                </div>

                <div className="bg-card-bg p-6 rounded-lg shadow-md hover:shadow-lg transition">
                    <div className="text-3xl mb-2">🔄</div>
                    <h3 className="text-xl font-bold mb-2 text-united-blue">Real-time Sync</h3>
                    <p className="text-text-secondary">
                        Data synced with server and accessible from Android app
                    </p>
                </div>
            </div>
        </div>
    )
}
