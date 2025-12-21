import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'
import Link from 'next/link'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
    title: 'MyAir - Passenger Management',
    description: 'Manage passengers and flight bookings',
}

export default function RootLayout({
    children,
}: {
    children: React.ReactNode
}) {
    return (
        <html lang="en">
            <body className={inter.className}>
                {/* United Airlines Header */}
                <nav className="bg-united-blue text-white shadow-lg">
                    <div className="container mx-auto px-4 h-14 flex items-center justify-between">
                        <div className="w-8"></div>
                        <Link href="/" className="text-xl font-bold tracking-widest">
                            UNITED
                        </Link>
                        <div className="flex space-x-6 text-sm">
                            <Link href="/" className="hover:text-gray-300 transition">Home</Link>
                            <Link href="/passengers" className="hover:text-gray-300 transition">Passengers</Link>
                        </div>
                    </div>
                </nav>
                <main className="min-h-screen bg-form-bg">
                    <div className="container mx-auto px-4 py-8">
                        {children}
                    </div>
                </main>
            </body>
        </html>
    )
}
