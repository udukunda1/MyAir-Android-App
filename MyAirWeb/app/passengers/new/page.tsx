'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { passengersApi } from '@/lib/api'
import { Passenger } from '@/lib/types'

export default function NewPassengerPage() {
    const router = useRouter()
    const [loading, setLoading] = useState(false)
    const [formData, setFormData] = useState({
        full_name: '',
        email: '',
        phone: '',
        date_of_birth: '',
        membership_level: 'Economy' as Passenger['membership_level'],
        is_active: true,
        profile_image: ''
    })

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            const reader = new FileReader()
            reader.onloadend = () => {
                const base64 = reader.result as string
                // Remove the data:image/...;base64, prefix
                const base64Data = base64.split(',')[1]
                setFormData({ ...formData, profile_image: base64Data })
            }
            reader.readAsDataURL(file)
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setLoading(true)

        try {
            await passengersApi.create(formData)
            router.push('/passengers')
        } catch (error) {
            console.error('Error creating passenger:', error)
            alert('Failed to create passenger. Please try again.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="min-h-screen bg-form-bg py-6">
            <div className="max-w-2xl mx-auto px-4">
                <form onSubmit={handleSubmit} className="space-y-6">
                    {/* Profile Image Section */}
                    <div className="flex flex-col items-center py-6">
                        <div className="w-32 h-32 rounded-full bg-gray-200 border-4 border-gray-300 overflow-hidden mb-4 flex items-center justify-center">
                            {formData.profile_image ? (
                                <img
                                    src={`data:image/jpeg;base64,${formData.profile_image}`}
                                    alt="Preview"
                                    className="w-full h-full object-cover"
                                />
                            ) : (
                                <svg className="w-16 h-16 text-gray-400" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                                </svg>
                            )}
                        </div>
                        <label className="bg-united-blue text-white px-6 py-2 rounded-lg font-semibold cursor-pointer hover:bg-united-dark-blue transition">
                            Select Image
                            <input
                                type="file"
                                accept="image/*"
                                onChange={handleImageChange}
                                className="hidden"
                            />
                        </label>
                    </div>

                    {/* Full Name */}
                    <div className="relative">
                        <input
                            type="text"
                            id="full_name"
                            required
                            value={formData.full_name}
                            onChange={(e) => setFormData({ ...formData, full_name: e.target.value })}
                            placeholder=" "
                            className="peer w-full px-4 pt-6 pb-2 bg-white border-2 border-gray-300 rounded-lg focus:border-united-blue focus:outline-none transition"
                        />
                        <label
                            htmlFor="full_name"
                            className="absolute left-4 top-2 text-xs text-text-secondary peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-focus:top-2 peer-focus:text-xs peer-focus:text-united-blue transition-all"
                        >
                            Full Name *
                        </label>
                    </div>

                    {/* Email */}
                    <div className="relative">
                        <input
                            type="email"
                            id="email"
                            required
                            value={formData.email}
                            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                            placeholder=" "
                            className="peer w-full px-4 pt-6 pb-2 bg-white border-2 border-gray-300 rounded-lg focus:border-united-blue focus:outline-none transition"
                        />
                        <label
                            htmlFor="email"
                            className="absolute left-4 top-2 text-xs text-text-secondary peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-focus:top-2 peer-focus:text-xs peer-focus:text-united-blue transition-all"
                        >
                            Email *
                        </label>
                    </div>

                    {/* Phone */}
                    <div className="relative">
                        <input
                            type="tel"
                            id="phone"
                            required
                            value={formData.phone}
                            onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                            placeholder=" "
                            className="peer w-full px-4 pt-6 pb-2 bg-white border-2 border-gray-300 rounded-lg focus:border-united-blue focus:outline-none transition"
                        />
                        <label
                            htmlFor="phone"
                            className="absolute left-4 top-2 text-xs text-text-secondary peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-focus:top-2 peer-focus:text-xs peer-focus:text-united-blue transition-all"
                        >
                            Phone *
                        </label>
                    </div>

                    {/* Date of Birth */}
                    <div>
                        <label className="block text-sm text-text-secondary mb-2">
                            Date of Birth
                        </label>
                        <div className="flex gap-2">
                            <input
                                type="text"
                                value={formData.date_of_birth}
                                readOnly
                                placeholder="Select date"
                                className="flex-1 px-4 py-3 bg-white border-2 border-gray-300 rounded-lg text-gray-600"
                            />
                            <input
                                type="date"
                                value={formData.date_of_birth}
                                onChange={(e) => setFormData({ ...formData, date_of_birth: e.target.value })}
                                className="px-4 py-3 bg-united-light-blue text-white rounded-lg font-semibold cursor-pointer hover:bg-blue-400 transition"
                            />
                        </div>
                    </div>

                    {/* Membership Level */}
                    <div>
                        <label className="block text-sm text-text-secondary mb-2">
                            Membership Level
                        </label>
                        <select
                            value={formData.membership_level}
                            onChange={(e) => setFormData({ ...formData, membership_level: e.target.value as Passenger['membership_level'] })}
                            className="w-full px-4 py-3 bg-white border-2 border-gray-300 rounded-lg focus:border-united-blue focus:outline-none"
                        >
                            <option value="Economy">Economy</option>
                            <option value="Premium">Premium</option>
                            <option value="Business">Business</option>
                            <option value="First Class">First Class</option>
                        </select>
                    </div>

                    {/* Active Account Checkbox */}
                    <div className="flex items-center">
                        <input
                            type="checkbox"
                            id="is_active"
                            checked={formData.is_active}
                            onChange={(e) => setFormData({ ...formData, is_active: e.target.checked })}
                            className="w-5 h-5 text-united-blue border-gray-300 rounded focus:ring-united-blue"
                        />
                        <label htmlFor="is_active" className="ml-3 text-base text-gray-700">
                            Active Account
                        </label>
                    </div>

                    {/* Save Button */}
                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-united-blue text-white py-4 rounded-lg text-lg font-bold hover:bg-united-dark-blue disabled:bg-gray-400 disabled:cursor-not-allowed transition mt-8"
                    >
                        {loading ? 'Saving...' : 'Save Passenger'}
                    </button>

                    {/* Cancel Link */}
                    <button
                        type="button"
                        onClick={() => router.push('/passengers')}
                        className="w-full text-text-secondary py-2 text-center hover:text-gray-700 transition"
                    >
                        Cancel
                    </button>
                </form>
            </div>
        </div>
    )
}
