const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Passenger = sequelize.define('Passenger', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    full_name: {
        type: DataTypes.STRING,
        allowNull: false,
        validate: {
            notEmpty: true
        }
    },
    email: {
        type: DataTypes.STRING,
        allowNull: false,
        unique: true,
        validate: {
            isEmail: true,
            notEmpty: true
        }
    },
    phone: {
        type: DataTypes.STRING,
        allowNull: false,
        validate: {
            notEmpty: true
        }
    },
    date_of_birth: {
        type: DataTypes.DATEONLY,
        allowNull: true
    },
    membership_level: {
        type: DataTypes.ENUM('Economy', 'Premium', 'Business', 'First Class'),
        defaultValue: 'Economy'
    },
    is_active: {
        type: DataTypes.BOOLEAN,
        defaultValue: true
    },
    profile_image: {
        type: DataTypes.TEXT, // Changed to TEXT to support Base64 encoded images
        allowNull: true
    }
}, {
    tableName: 'passengers',
    timestamps: true
});

module.exports = Passenger;
