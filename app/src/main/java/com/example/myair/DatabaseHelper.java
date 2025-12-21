package com.example.myair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "passenger_database.db";
    private static final int DATABASE_VERSION = 10;

    // Table Names
    private static final String TABLE_PASSENGERS = "passenger_accounts";
    private static final String TABLE_BOOKINGS = "flight_bookings";

    // Passenger Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    private static final String KEY_MEMBERSHIP_LEVEL = "membership_level";
    private static final String KEY_IS_ACTIVE = "is_active";
    private static final String KEY_PROFILE_IMAGE = "profile_image";

    // Booking Table Columns
    private static final String KEY_BOOKING_ID = "id";
    private static final String KEY_PASSENGER_ID = "passenger_id";
    private static final String KEY_FLIGHT_NUMBER = "flight_number";
    private static final String KEY_BOOKING_DATE = "booking_date";
    private static final String KEY_SEAT_NUMBER = "seat_number";
    private static final String KEY_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Passenger Accounts Table
        String CREATE_PASSENGERS_TABLE = "CREATE TABLE " + TABLE_PASSENGERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FULL_NAME + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT NOT NULL,"
                + KEY_PHONE + " TEXT,"
                + KEY_DATE_OF_BIRTH + " TEXT,"
                + KEY_MEMBERSHIP_LEVEL + " TEXT,"
                + KEY_IS_ACTIVE + " INTEGER DEFAULT 1,"
                + KEY_PROFILE_IMAGE + " TEXT"
                + ")";
        db.execSQL(CREATE_PASSENGERS_TABLE);

        // Create Flight Bookings Table
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + KEY_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PASSENGER_ID + " INTEGER NOT NULL,"
                + KEY_FLIGHT_NUMBER + " TEXT,"
                + KEY_BOOKING_DATE + " TEXT,"
                + KEY_SEAT_NUMBER + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + "FOREIGN KEY(" + KEY_PASSENGER_ID + ") REFERENCES "
                + TABLE_PASSENGERS + "(" + KEY_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGERS);
        onCreate(db);
    }

    // Add a new passenger
    public long addPassenger(Passenger passenger) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_FULL_NAME, passenger.getFullName());
        values.put(KEY_EMAIL, passenger.getEmail());
        values.put(KEY_PHONE, passenger.getPhone());
        values.put(KEY_DATE_OF_BIRTH, passenger.getDateOfBirth());
        values.put(KEY_MEMBERSHIP_LEVEL, passenger.getMembershipLevel());
        values.put(KEY_IS_ACTIVE, passenger.isActive() ? 1 : 0);
        values.put(KEY_PROFILE_IMAGE, passenger.getProfileImagePath());

        long id = db.insert(TABLE_PASSENGERS, null, values);
        db.close();
        return id;
    }

    // Add a new passenger with specific ID (for server sync)
    public long addPassengerWithId(Passenger passenger) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        // Include the ID from server
        values.put(KEY_ID, passenger.getId());
        values.put(KEY_FULL_NAME, passenger.getFullName());
        values.put(KEY_EMAIL, passenger.getEmail());
        values.put(KEY_PHONE, passenger.getPhone());
        values.put(KEY_DATE_OF_BIRTH, passenger.getDateOfBirth());
        values.put(KEY_MEMBERSHIP_LEVEL, passenger.getMembershipLevel());
        values.put(KEY_IS_ACTIVE, passenger.isActive() ? 1 : 0);
        values.put(KEY_PROFILE_IMAGE, passenger.getProfileImagePath());

        long id = db.insert(TABLE_PASSENGERS, null, values);
        db.close();
        return id;
    }

    // Get a single passenger by ID
    public Passenger getPassenger(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_PASSENGERS,
                new String[]{KEY_ID, KEY_FULL_NAME, KEY_EMAIL, KEY_PHONE,
                        KEY_DATE_OF_BIRTH, KEY_MEMBERSHIP_LEVEL, KEY_IS_ACTIVE, KEY_PROFILE_IMAGE},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        Passenger passenger = null;
        if (cursor != null && cursor.moveToFirst()) {
            passenger = new Passenger(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6) == 1,
                    cursor.getString(7)
            );
            cursor.close();
        }
        
        db.close();
        return passenger;
    }

    // Get all passengers
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengerList = new ArrayList<>();
        // Order by ID (latest created first)
        String selectQuery = "SELECT * FROM " + TABLE_PASSENGERS + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Passenger passenger = new Passenger(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6) == 1,
                        cursor.getString(7)
                );
                passengerList.add(passenger);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return passengerList;
    }

    // Update a passenger
    public int updatePassenger(Passenger passenger) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_FULL_NAME, passenger.getFullName());
        values.put(KEY_EMAIL, passenger.getEmail());
        values.put(KEY_PHONE, passenger.getPhone());
        values.put(KEY_DATE_OF_BIRTH, passenger.getDateOfBirth());
        values.put(KEY_MEMBERSHIP_LEVEL, passenger.getMembershipLevel());
        values.put(KEY_IS_ACTIVE, passenger.isActive() ? 1 : 0);
        values.put(KEY_PROFILE_IMAGE, passenger.getProfileImagePath());

        int rowsAffected = db.update(TABLE_PASSENGERS, values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(passenger.getId())});
        
        db.close();
        return rowsAffected;
    }

    // Delete a passenger
    public void deletePassenger(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSENGERS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Get passenger count
    public int getPassengerCount() {
        String countQuery = "SELECT * FROM " + TABLE_PASSENGERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Add a booking for a passenger
    public long addBooking(int passengerId, String flightNumber, String bookingDate,
                          String seatNumber, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_PASSENGER_ID, passengerId);
        values.put(KEY_FLIGHT_NUMBER, flightNumber);
        values.put(KEY_BOOKING_DATE, bookingDate);
        values.put(KEY_SEAT_NUMBER, seatNumber);
        values.put(KEY_STATUS, status);

        long id = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    // Get all bookings for a specific passenger
    public List<FlightBooking> getBookingsByPassengerId(int passengerId) {
        List<FlightBooking> bookingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_BOOKINGS,
                new String[]{KEY_BOOKING_ID, KEY_PASSENGER_ID, KEY_FLIGHT_NUMBER,
                        KEY_BOOKING_DATE, KEY_SEAT_NUMBER, KEY_STATUS},
                KEY_PASSENGER_ID + "=?",
                new String[]{String.valueOf(passengerId)},
                null, null, KEY_BOOKING_DATE + " DESC");
        
        if (cursor.moveToFirst()) {
            do {
                FlightBooking booking = new FlightBooking(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return bookingList;
    }

    // Get booking count for a passenger
    public int getBookingCountForPassenger(int passengerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS,
                null,
                KEY_PASSENGER_ID + "=?",
                new String[]{String.valueOf(passengerId)},
                null, null, null);
        
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Delete a booking
    public void deleteBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, KEY_BOOKING_ID + " = ?",
                new String[]{String.valueOf(bookingId)});
        db.close();
    }
}
