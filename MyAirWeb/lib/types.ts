// TypeScript types for MyAir application

export interface Passenger {
    id: number;
    full_name: string;
    email: string;
    phone: string;
    date_of_birth: string;
    membership_level: 'Economy' | 'Premium' | 'Business' | 'First Class';
    is_active: boolean;
    profile_image?: string; // Base64 encoded image
    createdAt?: string;
    updatedAt?: string;
}

export interface Booking {
    id: number;
    passenger_id: number;
    flight_number: string;
    booking_date: string;
    seat_number?: string;
    status: 'Confirmed' | 'Pending' | 'Cancelled';
    createdAt?: string;
    updatedAt?: string;
}

export interface ApiResponse<T> {
    success: boolean;
    data?: T;
    count?: number;
    message?: string;
}
