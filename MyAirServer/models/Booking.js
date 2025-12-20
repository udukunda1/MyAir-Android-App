const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Booking = sequelize.define('Booking', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    passenger_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'passengers',
            key: 'id'
        },
        onDelete: 'CASCADE',
        onUpdate: 'CASCADE'
    },
    flight_number: {
        type: DataTypes.STRING,
        allowNull: false,
        validate: {
            notEmpty: true
        }
    },
    booking_date: {
        type: DataTypes.DATEONLY,
        allowNull: false
    },
    seat_number: {
        type: DataTypes.STRING,
        allowNull: true
    },
    status: {
        type: DataTypes.ENUM('Confirmed', 'Pending', 'Cancelled'),
        defaultValue: 'Pending'
    }
}, {
    tableName: 'bookings',
    timestamps: true
});

module.exports = Booking;
