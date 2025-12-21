'use client'

import { useEffect, useState } from 'react'
import Link from 'next/link'
import { passengersApi } from '@/lib/api'
import { Passenger } from '@/lib/types'

export default function PassengersPage() {
    const [passengers, setPassengers] = useState<Passenger[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        loadPassengers()
    }, [])

    async function loadPassengers() {
        try {
            const response = await passengersApi.getAll()
            setPassengers(response.data || [])
        } catch (error) {
            console.error('Error loading passengers:', error)
        } finally {
            setLoading(false)
        }
    }

    async function handleDelete(id: number) {
        if (!confirm('Are you sure you want to delete this passenger?')) return

        try {
            await passengersApi.delete(id)
            loadPassengers()
        } catch (error) {
            console.error('Error deleting passenger:', error)
        }
    }

    if (loading) {
        return <div className="text-center py-12 text-text-secondary">Loading passengers...</div>
    }

    return (
        <div>
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-united-blue">Passengers</h1>
                <Link
                    href="/passengers/new"
                    className="bg-success-green text-white px-6 py-2 rounded-lg hover:bg-green-600 transition font-semibold"
                >
                    + Add Passenger
                </Link>
            </div>

            {passengers.length === 0 ? (
                <div className="text-center py-12 text-text-secondary bg-card-bg rounded-lg shadow-md">
                    No passengers found. Add your first passenger!
                </div>
            ) : (
                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {passengers.map((passenger) => (
                        <div key={passenger.id} className="bg-card-bg rounded-lg shadow-md p-6 hover:shadow-xl transition">
                            {passenger.profile_image && (
                                <img
                                    src={`data:image/jpeg;base64,${passenger.profile_image}`}
                                    alt={passenger.full_name}
                                    className="w-24 h-24 rounded-full mx-auto mb-4 object-cover border-4 border-united-light-blue"
                                />
                            )}
                            <h3 className="text-xl font-bold text-center mb-2 text-united-blue">{passenger.full_name}</h3>
                            <p className="text-text-secondary text-center text-sm mb-1">{passenger.email}</p>
                            <p className="text-text-secondary text-center text-sm mb-4">{passenger.phone}</p>
                            <div className="text-center mb-4">
                                <span className="inline-block bg-united-light-blue bg-opacity-20 text-united-blue px-3 py-1 rounded-full text-sm font-semibold">
                                    {passenger.membership_level}
                                </span>
                            </div>
                            <div className="flex gap-2 justify-center">
                                <Link
                                    href={`/passengers/${passenger.id}`}
                                    className="bg-united-blue text-white px-4 py-2 rounded hover:bg-united-dark-blue text-sm transition font-semibold"
                                >
                                    View
                                </Link>
                                <button
                                    onClick={() => handleDelete(passenger.id)}
                                    className="bg-error-red text-white px-4 py-2 rounded hover:bg-red-600 text-sm transition font-semibold"
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}
