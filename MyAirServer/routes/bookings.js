const express = require('express');
const router = express.Router();
const { Booking, Passenger } = require('../models');

// GET all bookings
router.get('/', async (req, res) => {
    try {
        const bookings = await Booking.findAll({
            include: [{
                model: Passenger,
                as: 'passenger'
            }],
            order: [['createdAt', 'DESC']]
        });

        res.json({
            success: true,
            count: bookings.length,
            data: bookings
        });
    } catch (error) {
        console.error('Error fetching bookings:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching bookings',
            error: error.message
        });
    }
});

// GET bookings by passenger ID
router.get('/passenger/:passengerId', async (req, res) => {
    try {
        const bookings = await Booking.findAll({
            where: { passenger_id: req.params.passengerId },
            include: [{
                model: Passenger,
                as: 'passenger'
            }],
            order: [['booking_date', 'DESC']]
        });

        res.json({
            success: true,
            count: bookings.length,
            data: bookings
        });
    } catch (error) {
        console.error('Error fetching bookings:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching bookings',
            error: error.message
        });
    }
});

// GET single booking by ID
router.get('/:id', async (req, res) => {
    try {
        const booking = await Booking.findByPk(req.params.id, {
            include: [{
                model: Passenger,
                as: 'passenger'
            }]
        });

        if (!booking) {
            return res.status(404).json({
                success: false,
                message: 'Booking not found'
            });
        }

        res.json({
            success: true,
            data: booking
        });
    } catch (error) {
        console.error('Error fetching booking:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching booking',
            error: error.message
        });
    }
});

// POST create new booking
router.post('/', async (req, res) => {
    try {
        const { passenger_id, flight_number, booking_date, seat_number, status } = req.body;

        // Validate required fields
        if (!passenger_id || !flight_number || !booking_date) {
            return res.status(400).json({
                success: false,
                message: 'Missing required fields: passenger_id, flight_number, booking_date'
            });
        }

        // Check if passenger exists
        const passenger = await Passenger.findByPk(passenger_id);
        if (!passenger) {
            return res.status(404).json({
                success: false,
                message: 'Passenger not found'
            });
        }

        const booking = await Booking.create({
            passenger_id,
            flight_number,
            booking_date,
            seat_number,
            status: status || 'Pending'
        });

        // Fetch the created booking with passenger details
        const createdBooking = await Booking.findByPk(booking.id, {
            include: [{
                model: Passenger,
                as: 'passenger'
            }]
        });

        res.status(201).json({
            success: true,
            message: 'Booking created successfully',
            data: createdBooking
        });
    } catch (error) {
        console.error('Error creating booking:', error);
        res.status(500).json({
            success: false,
            message: 'Error creating booking',
            error: error.message
        });
    }
});

// PUT update booking
router.put('/:id', async (req, res) => {
    try {
        const booking = await Booking.findByPk(req.params.id);

        if (!booking) {
            return res.status(404).json({
                success: false,
                message: 'Booking not found'
            });
        }

        const { passenger_id, flight_number, booking_date, seat_number, status } = req.body;

        // If passenger_id is being updated, verify it exists
        if (passenger_id && passenger_id !== booking.passenger_id) {
            const passenger = await Passenger.findByPk(passenger_id);
            if (!passenger) {
                return res.status(404).json({
                    success: false,
                    message: 'Passenger not found'
                });
            }
        }

        await booking.update({
            passenger_id: passenger_id || booking.passenger_id,
            flight_number: flight_number || booking.flight_number,
            booking_date: booking_date || booking.booking_date,
            seat_number: seat_number !== undefined ? seat_number : booking.seat_number,
            status: status || booking.status
        });

        // Fetch updated booking with passenger details
        const updatedBooking = await Booking.findByPk(booking.id, {
            include: [{
                model: Passenger,
                as: 'passenger'
            }]
        });

        res.json({
            success: true,
            message: 'Booking updated successfully',
            data: updatedBooking
        });
    } catch (error) {
        console.error('Error updating booking:', error);
        res.status(500).json({
            success: false,
            message: 'Error updating booking',
            error: error.message
        });
    }
});

// DELETE booking
router.delete('/:id', async (req, res) => {
    try {
        const booking = await Booking.findByPk(req.params.id);

        if (!booking) {
            return res.status(404).json({
                success: false,
                message: 'Booking not found'
            });
        }

        await booking.destroy();

        res.json({
            success: true,
            message: 'Booking deleted successfully'
        });
    } catch (error) {
        console.error('Error deleting booking:', error);
        res.status(500).json({
            success: false,
            message: 'Error deleting booking',
            error: error.message
        });
    }
});

module.exports = router;
