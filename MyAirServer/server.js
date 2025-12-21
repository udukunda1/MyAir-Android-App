const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { sequelize, testConnection } = require('./config/database');
const { Passenger, Booking } = require('./models');

// Import routes
const passengerRoutes = require('./routes/passengers');
const bookingRoutes = require('./routes/bookings');

// Initialize Express app
const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors()); // Enable CORS for Android app
app.use(bodyParser.json({ limit: '1mb' })); // Parse JSON request bodies (increased limit for images)
app.use(bodyParser.urlencoded({ extended: true, limit: '1mb' })); // Parse URL-encoded bodies

// Request logging middleware
app.use((req, res, next) => {
    console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
    next();
});

// API Routes
app.use('/api/passengers', passengerRoutes);
app.use('/api/bookings', bookingRoutes);

// Root endpoint
app.get('/', (req, res) => {
    res.json({
        message: 'MyAir REST API Server',
        version: '1.0.0',
        endpoints: {
            passengers: {
                'GET /api/passengers': 'Get all passengers',
                'GET /api/passengers/:id': 'Get passenger by ID',
                'POST /api/passengers': 'Create new passenger',
                'PUT /api/passengers/:id': 'Update passenger',
                'DELETE /api/passengers/:id': 'Delete passenger'
            },
            bookings: {
                'GET /api/bookings': 'Get all bookings',
                'GET /api/bookings/passenger/:passengerId': 'Get bookings by passenger',
                'GET /api/bookings/:id': 'Get booking by ID',
                'POST /api/bookings': 'Create new booking',
                'PUT /api/bookings/:id': 'Update booking',
                'DELETE /api/bookings/:id': 'Delete booking'
            }
        }
    });
});

// 404 handler
app.use((req, res) => {
    res.status(404).json({
        success: false,
        message: 'Endpoint not found'
    });
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error('Server error:', err);
    res.status(500).json({
        success: false,
        message: 'Internal server error',
        error: err.message
    });
});

// Initialize database and start server
const startServer = async () => {
    try {
        // Test database connection
        await testConnection();

        // Sync database models (create tables if they don't exist)
        await sequelize.sync({ alter: true });
        console.log('✓ Database models synchronized');

        // Start server
        app.listen(PORT, () => {
            console.log('═══════════════════════════════════════════════════');
            console.log(`✓ MyAir REST API Server running on port ${PORT}`);
            console.log(`✓ Server URL: http://localhost:${PORT}`);
            console.log('✓ API Documentation: http://localhost:' + PORT);
            console.log('═══════════════════════════════════════════════════');
            console.log('\nAvailable endpoints:');
            console.log('  Passengers: /api/passengers');
            console.log('  Bookings:   /api/bookings');
            console.log('\nPress Ctrl+C to stop the server\n');
        });
    } catch (error) {
        console.error('Failed to start server:', error);
        process.exit(1);
    }
};

// Start the server
startServer();

module.exports = app;
