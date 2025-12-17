package com.example.myair;

public class FlightBooking {
    private int id;
    private int passengerId;
    private String flightNumber;
    private String bookingDate;
    private String seatNumber;
    private String status;

    // Constructor
    public FlightBooking() {
    }

    public FlightBooking(int id, int passengerId, String flightNumber, String bookingDate,
                        String seatNumber, String status) {
        this.id = id;
        this.passengerId = passengerId;
        this.flightNumber = flightNumber;
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
