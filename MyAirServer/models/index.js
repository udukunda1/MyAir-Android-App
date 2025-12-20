const Passenger = require('./Passenger');
const Booking = require('./Booking');

// Define relationships
Passenger.hasMany(Booking, {
    foreignKey: 'passenger_id',
    as: 'bookings',
    onDelete: 'CASCADE'
});

Booking.belongsTo(Passenger, {
    foreignKey: 'passenger_id',
    as: 'passenger'
});

module.exports = {
    Passenger,
    Booking
};
