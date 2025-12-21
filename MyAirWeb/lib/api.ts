// API client for MyAir REST API

const API_BASE_URL = 'http://localhost:3000';

export interface ApiResponse<T> {
    success: boolean;
    data?: T;
    count?: number;
    message?: string;
}

// Passengers API
export const passengersApi = {
    async getAll() {
        const res = await fetch(`${API_BASE_URL}/api/passengers`);
        return res.json();
    },

    async getOne(id: number) {
        const res = await fetch(`${API_BASE_URL}/api/passengers/${id}`);
        return res.json();
    },

    async create(data: any) {
        const res = await fetch(`${API_BASE_URL}/api/passengers`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });
        return res.json();
    },

    async update(id: number, data: any) {
        const res = await fetch(`${API_BASE_URL}/api/passengers/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });
        return res.json();
    },

    async delete(id: number) {
        const res = await fetch(`${API_BASE_URL}/api/passengers/${id}`, {
            method: 'DELETE',
        });
        return res.json();
    },
};

// Bookings API
export const bookingsApi = {
    async getByPassenger(passengerId: number) {
        const res = await fetch(`${API_BASE_URL}/api/bookings/passenger/${passengerId}`);
        return res.json();
    },

    async create(data: any) {
        const res = await fetch(`${API_BASE_URL}/api/bookings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });
        return res.json();
    },

    async delete(id: number) {
        const res = await fetch(`${API_BASE_URL}/api/bookings/${id}`, {
            method: 'DELETE',
        });
        return res.json();
    },
};
