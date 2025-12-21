'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { passengersApi, bookingsApi } from '@/lib/api'
import { Passenger, Booking } from '@/lib/types'

export default function PassengerDetailsPage({ params }: { params: { id: string } }) {
    const router = useRouter()
    const [passenger, setPassenger] = useState<Passenger | null>(null)
    const [bookings, setBookings] = useState<Booking[]>([])
    const [loading, setLoading] = useState(true)
    const [showBookingForm, setShowBookingForm] = useState(false)
    const [bookingForm, setBookingForm] = useState({
        flight_number: '',
        booking_date: '',
        seat_number: '',
        status: 'Confirmed' as Booking['status']
    })

    useEffect(() => {
        loadPassengerData()
    }, [params.id])

    async function loadPassengerData() {
        try {
            const [passengerRes, bookingsRes] = await Promise.all([
                passengersApi.getOne(parseInt(params.id)),
                bookingsApi.getByPassenger(parseInt(params.id))
            ])
            setPassenger(passengerRes.data)
            setBookings(bookingsRes.data || [])
        } catch (error) {
            console.error('Error loading passenger data:', error)
        } finally {
            setLoading(false)
        }
    }

    async function handleCreateBooking(e: React.FormEvent) {
        e.preventDefault()
        try {
            await bookingsApi.create({
                passenger_id: parseInt(params.id),
                ...bookingForm
            })
            setShowBookingForm(false)
            setBookingForm({
                flight_number: '',
                booking_date: '',
                seat_number: '',
                status: 'Confirmed'
            })
            loadPassengerData()
        } catch (error) {
            console.error('Error creating booking:', error)
            alert('Failed to create booking')
        }
    }

    async function handleDeleteBooking(bookingId: number) {
        if (!confirm('Are you sure you want to delete this booking?')) return

        try {
            await bookingsApi.delete(bookingId)
            loadPassengerData()
        } catch (error) {
            console.error('Error deleting booking:', error)
        }
    }

    if (loading) {
        return <div className="text-center py-12 text-text-secondary">Loading passenger details...</div>
    }

    if (!passenger) {
        return <div className="text-center py-12 text-error-red">Passenger not found</div>
    }

    return (
        <div className="max-w-4xl mx-auto">
            {/* Passenger Details Card */}
            <div className="bg-card-bg rounded-lg shadow-md p-8 mb-8">
                <div className="flex items-start gap-6">
                    {passenger.profile_image && (
                        <img
                            src={`data:image/jpeg;base64,${passenger.profile_image}`}
                            alt={passenger.full_name}
                            className="w-32 h-32 rounded-full object-cover"
                        />
                    )}
                    <div className="flex-1">
                        <h1 className="text-3xl font-bold mb-2 text-united-blue">{passenger.full_name}</h1>
                        <div className="space-y-2 text-text-secondary">
                            <p><strong>Email:</strong> {passenger.email}</p>
                            <p><strong>Phone:</strong> {passenger.phone}</p>
                            <p><strong>Date of Birth:</strong> {passenger.date_of_birth}</p>
                            <p>
                                <span className="inline-block bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm">
                                    {passenger.membership_level}
                                </span>
                            </p>
                            <p>
                                <span className={`inline-block px-3 py-1 rounded-full text-sm ${passenger.is_active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                                    {passenger.is_active ? 'Active' : 'Inactive'}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>
                <div className="mt-6 flex gap-4">
                    <button
                        onClick={() => router.push('/passengers')}
                        className="bg-united-blue text-white px-6 py-2 rounded-lg hover:bg-united-dark-blue transition font-semibold"
                    >
                        ← Back to List
                    </button>
                </div>
            </div>

            {/* Bookings Section */}
            <div className="bg-card-bg rounded-lg shadow-md p-8">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-united-blue">Flight Bookings</h2>
                    <button
                        onClick={() => setShowBookingForm(!showBookingForm)}
                        className="bg-success-green text-white px-6 py-2 rounded-lg hover:bg-green-600 transition font-semibold"
                    >
                        {showBookingForm ? 'Cancel' : '+ Add Booking'}
                    </button>
                </div>

                {/* Booking Form */}
                {showBookingForm && (
                    <form onSubmit={handleCreateBooking} className="mb-8 p-6 bg-gray-50 rounded-lg">
                        <div className="grid md:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label className="block text-gray-700 font-semibold mb-2">
                                    Flight Number *
                                </label>
                                <input
                                    type="text"
                                    required
                                    value={bookingForm.flight_number}
                                    onChange={(e) => setBookingForm({ ...bookingForm, flight_number: e.target.value })}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-united-blue focus:border-united-blue"
                                    placeholder="UA123"
                                />
                            </div>
                            <div>
                                <label className="block text-gray-700 font-semibold mb-2">
                                    Booking Date *
                                </label>
                                <input
                                    type="date"
                                    required
                                    value={bookingForm.booking_date}
                                    onChange={(e) => setBookingForm({ ...bookingForm, booking_date: e.target.value })}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-united-blue focus:border-united-blue"
                                />
                            </div>
                            <div>
                                <label className="block text-gray-700 font-semibold mb-2">
                                    Seat Number
                                </label>
                                <input
                                    type="text"
                                    value={bookingForm.seat_number}
                                    onChange={(e) => setBookingForm({ ...bookingForm, seat_number: e.target.value })}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-united-blue focus:border-united-blue"
                                    placeholder="12A"
                                />
                            </div>
                            <div>
                                <label className="block text-gray-700 font-semibold mb-2">
                                    Status
                                </label>
                                <select
                                    value={bookingForm.status}
                                    onChange={(e) => setBookingForm({ ...bookingForm, status: e.target.value as Booking['status'] })}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-united-blue focus:border-united-blue"
                                >
                                    <option value="Confirmed">Confirmed</option>
                                    <option value="Pending">Pending</option>
                                    <option value="Cancelled">Cancelled</option>
                                </select>
                            </div>
                        </div>
                        <button
                            type="submit"
                            className="bg-success-green text-white px-6 py-2 rounded-lg hover:bg-green-600 transition font-semibold"
                        >
                            Create Booking
                        </button>
                    </form>
                )}

                {/* Bookings List */}
                {bookings.length === 0 ? (
                    <p className="text-text-secondary text-center py-8">No bookings yet</p>
                ) : (
                    <div className="space-y-4">
                        {bookings.map((booking) => (
                            <div key={booking.id} className="border-2 border-united-light-blue rounded-lg p-4 hover:shadow-md transition bg-white">
                                <div className="flex justify-between items-start">
                                    <div>
                                        <h3 className="text-lg font-bold mb-2 text-united-blue">Flight {booking.flight_number}</h3>
                                        <p className="text-text-secondary text-sm mb-1">
                                            <strong>Date:</strong> {booking.booking_date}
                                        </p>
                                        {booking.seat_number && (
                                            <p className="text-text-secondary text-sm mb-1">
                                                <strong>Seat:</strong> {booking.seat_number}
                                            </p>
                                        )}
                                        <p>
                                            <span className={`inline-block px-3 py-1 rounded-full text-sm ${booking.status === 'Confirmed' ? 'bg-green-100 text-green-800' :
                                                booking.status === 'Pending' ? 'bg-yellow-100 text-yellow-800' :
                                                    'bg-red-100 text-red-800'
                                                }`}>
                                                {booking.status}
                                            </span>
                                        </p>
                                    </div>
                                    <button
                                        onClick={() => handleDeleteBooking(booking.id)}
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
        </div>
    )
}
