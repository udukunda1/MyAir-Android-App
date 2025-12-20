package com.example.myair;

public class ApiConfig {
    // IMPORTANT: Change this to your computer's IP address
    // To find your IP:
    // - Windows: Open CMD and type "ipconfig" → Look for IPv4 Address
    // - Example: 192.168.1.100 or 192.168.42.129 (if using USB tethering)
    
    // TODO: UPDATE THIS IP ADDRESS BEFORE RUNNING THE APP
    //public static final String BASE_URL = "http://192.168.56.1:3000";
    public static final String BASE_URL = "http://172.31.76.205:3000";
    
    // Passenger endpoints
    public static final String GET_ALL_PASSENGERS = BASE_URL + "/api/passengers";
    public static final String GET_PASSENGER = BASE_URL + "/api/passengers/";  // + id
    public static final String CREATE_PASSENGER = BASE_URL + "/api/passengers";
    public static final String UPDATE_PASSENGER = BASE_URL + "/api/passengers/";  // + id
    public static final String DELETE_PASSENGER = BASE_URL + "/api/passengers/";  // + id
    
    // Booking endpoints
    public static final String GET_ALL_BOOKINGS = BASE_URL + "/api/bookings";
    public static final String GET_BOOKINGS_BY_PASSENGER = BASE_URL + "/api/bookings/passenger/";  // + passengerId
    public static final String GET_BOOKING = BASE_URL + "/api/bookings/";  // + id
    public static final String CREATE_BOOKING = BASE_URL + "/api/bookings";
    public static final String UPDATE_BOOKING = BASE_URL + "/api/bookings/";  // + id
    public static final String DELETE_BOOKING = BASE_URL + "/api/bookings/";  // + id
}
