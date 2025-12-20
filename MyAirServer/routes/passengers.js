const express = require('express');
const router = express.Router();
const { Passenger, Booking } = require('../models');

// GET all passengers
router.get('/', async (req, res) => {
    try {
        const passengers = await Passenger.findAll({
            include: [{
                model: Booking,
                as: 'bookings'
            }],
            order: [['createdAt', 'DESC']]
        });

        res.json({
            success: true,
            count: passengers.length,
            data: passengers
        });
    } catch (error) {
        console.error('Error fetching passengers:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching passengers',
            error: error.message
        });
    }
});

// GET single passenger by ID
router.get('/:id', async (req, res) => {
    try {
        const passenger = await Passenger.findByPk(req.params.id, {
            include: [{
                model: Booking,
                as: 'bookings'
            }]
        });

        if (!passenger) {
            return res.status(404).json({
                success: false,
                message: 'Passenger not found'
            });
        }

        res.json({
            success: true,
            data: passenger
        });
    } catch (error) {
        console.error('Error fetching passenger:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching passenger',
            error: error.message
        });
    }
});

// POST create new passenger
router.post('/', async (req, res) => {
    try {
        const { full_name, email, phone, date_of_birth, membership_level, is_active, profile_image } = req.body;

        // Validate required fields
        if (!full_name || !email || !phone) {
            return res.status(400).json({
                success: false,
                message: 'Missing required fields: full_name, email, phone'
            });
        }

        const passenger = await Passenger.create({
            full_name,
            email,
            phone,
            date_of_birth,
            membership_level,
            is_active: is_active !== undefined ? is_active : true,
            profile_image
        });

        res.status(201).json({
            success: true,
            message: 'Passenger created successfully',
            data: passenger
        });
    } catch (error) {
        console.error('Error creating passenger:', error);

        // Handle unique constraint violation (duplicate email)
        if (error.name === 'SequelizeUniqueConstraintError') {
            return res.status(409).json({
                success: false,
                message: 'Email already exists'
            });
        }

        res.status(500).json({
            success: false,
            message: 'Error creating passenger',
            error: error.message
        });
    }
});

// PUT update passenger
router.put('/:id', async (req, res) => {
    try {
        const passenger = await Passenger.findByPk(req.params.id);

        if (!passenger) {
            return res.status(404).json({
                success: false,
                message: 'Passenger not found'
            });
        }

        const { full_name, email, phone, date_of_birth, membership_level, is_active, profile_image } = req.body;

        await passenger.update({
            full_name: full_name || passenger.full_name,
            email: email || passenger.email,
            phone: phone || passenger.phone,
            date_of_birth: date_of_birth !== undefined ? date_of_birth : passenger.date_of_birth,
            membership_level: membership_level || passenger.membership_level,
            is_active: is_active !== undefined ? is_active : passenger.is_active,
            profile_image: profile_image !== undefined ? profile_image : passenger.profile_image
        });

        res.json({
            success: true,
            message: 'Passenger updated successfully',
            data: passenger
        });
    } catch (error) {
        console.error('Error updating passenger:', error);

        if (error.name === 'SequelizeUniqueConstraintError') {
            return res.status(409).json({
                success: false,
                message: 'Email already exists'
            });
        }

        res.status(500).json({
            success: false,
            message: 'Error updating passenger',
            error: error.message
        });
    }
});

// DELETE passenger
router.delete('/:id', async (req, res) => {
    try {
        const passenger = await Passenger.findByPk(req.params.id);

        if (!passenger) {
            return res.status(404).json({
                success: false,
                message: 'Passenger not found'
            });
        }

        await passenger.destroy();

        res.json({
            success: true,
            message: 'Passenger deleted successfully (bookings also deleted via CASCADE)'
        });
    } catch (error) {
        console.error('Error deleting passenger:', error);
        res.status(500).json({
            success: false,
            message: 'Error deleting passenger',
            error: error.message
        });
    }
});

module.exports = router;
