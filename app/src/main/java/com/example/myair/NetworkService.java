package com.example.myair;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkService {
    private static final String TAG = "NetworkService";
    private static NetworkService instance;
    private RequestQueue requestQueue;
    private Context context;

    private NetworkService(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkService getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkService(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    // ==================== PASSENGER OPERATIONS ====================

    /**
     * Create a new passenger
     */
    public void createPassenger(Passenger passenger, 
                               final NetworkCallback<JSONObject> callback) {
        try {
            JSONObject jsonBody = passengerToJson(passenger);
            
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiConfig.CREATE_PASSENGER,
                jsonBody,
                response -> {
                    Log.d(TAG, "Passenger created: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error creating passenger: " + error.getMessage());
                    callback.onError(getErrorMessage(error));
                }
            );
            
            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error: " + e.getMessage());
            callback.onError("Error preparing data: " + e.getMessage());
        }
    }

    /**
     * Get all passengers
     */
    public void getAllPassengers(final NetworkCallback<JSONArray> callback) {
        // Server returns: { "success": true, "count": X, "data": [...] }
        // So we need JsonObjectRequest, not JsonArrayRequest
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            ApiConfig.GET_ALL_PASSENGERS,
            null,
            response -> {
                try {
                    // Extract the "data" array from the response object
                    if (response.has("data")) {
                        JSONArray passengers = response.getJSONArray("data");
                        Log.d(TAG, "Passengers retrieved: " + passengers.length());
                        callback.onSuccess(passengers);
                    } else {
                        // Fallback: if no "data" field, return empty array
                        Log.w(TAG, "No 'data' field in response");
                        callback.onSuccess(new JSONArray());
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing passengers: " + e.getMessage());
                    callback.onError("Error parsing server response: " + e.getMessage());
                }
            },
            error -> {
                Log.e(TAG, "Error getting passengers: " + error.getMessage());
                callback.onError(getErrorMessage(error));
            }
        );
        
        requestQueue.add(request);
    }

    /**
     * Get single passenger by ID
     */
    public void getPassenger(int id, final NetworkCallback<JSONObject> callback) {
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            ApiConfig.GET_PASSENGER + id,
            null,
            response -> {
                Log.d(TAG, "Passenger retrieved: " + response.toString());
                callback.onSuccess(response);
            },
            error -> {
                Log.e(TAG, "Error getting passenger: " + error.getMessage());
                callback.onError(getErrorMessage(error));
            }
        );
        
        requestQueue.add(request);
    }

    /**
     * Update passenger
     */
    public void updatePassenger(Passenger passenger, 
                               final NetworkCallback<JSONObject> callback) {
        try {
            JSONObject jsonBody = passengerToJson(passenger);
            
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                ApiConfig.UPDATE_PASSENGER + passenger.getId(),
                jsonBody,
                response -> {
                    Log.d(TAG, "Passenger updated: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error updating passenger: " + error.getMessage());
                    callback.onError(getErrorMessage(error));
                }
            );
            
            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error: " + e.getMessage());
            callback.onError("Error preparing data: " + e.getMessage());
        }
    }

    /**
     * Delete passenger
     */
    public void deletePassenger(int id, final NetworkCallback<JSONObject> callback) {
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.DELETE,
            ApiConfig.DELETE_PASSENGER + id,
            null,
            response -> {
                Log.d(TAG, "Passenger deleted: " + response.toString());
                callback.onSuccess(response);
            },
            error -> {
                Log.e(TAG, "Error deleting passenger: " + error.getMessage());
                callback.onError(getErrorMessage(error));
            }
        );
        
        requestQueue.add(request);
    }

    // ==================== BOOKING OPERATIONS ====================

    /**
     * Create a new booking
     */
    public void createBooking(int passengerId, String flightNumber, 
                             String bookingDate, String seatNumber, String status,
                             final NetworkCallback<JSONObject> callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("passenger_id", passengerId);
            jsonBody.put("flight_number", flightNumber);
            jsonBody.put("booking_date", bookingDate);
            jsonBody.put("seat_number", seatNumber);
            jsonBody.put("status", status);
            
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiConfig.CREATE_BOOKING,
                jsonBody,
                response -> {
                    Log.d(TAG, "Booking created: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error creating booking: " + error.getMessage());
                    callback.onError(getErrorMessage(error));
                }
            );
            
            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error: " + e.getMessage());
            callback.onError("Error preparing data: " + e.getMessage());
        }
    }

    /**
     * Get bookings by passenger ID
     */
    public void getBookingsByPassenger(int passengerId, 
                                      final NetworkCallback<JSONArray> callback) {
        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET,
            ApiConfig.GET_BOOKINGS_BY_PASSENGER + passengerId,
            null,
            response -> {
                Log.d(TAG, "Bookings retrieved: " + response.length());
                callback.onSuccess(response);
            },
            error -> {
                Log.e(TAG, "Error getting bookings: " + error.getMessage());
                callback.onError(getErrorMessage(error));
            }
        );
        
        requestQueue.add(request);
    }

    /**
     * Delete booking
     */
    public void deleteBooking(int id, final NetworkCallback<JSONObject> callback) {
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.DELETE,
            ApiConfig.DELETE_BOOKING + id,
            null,
            response -> {
                Log.d(TAG, "Booking deleted: " + response.toString());
                callback.onSuccess(response);
            },
            error -> {
                Log.e(TAG, "Error deleting booking: " + error.getMessage());
                callback.onError(getErrorMessage(error));
            }
        );
        
        requestQueue.add(request);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Convert Passenger object to JSON
     */
    private JSONObject passengerToJson(Passenger passenger) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("full_name", passenger.getFullName());
        json.put("email", passenger.getEmail());
        json.put("phone", passenger.getPhone());
        json.put("date_of_birth", passenger.getDateOfBirth());
        json.put("membership_level", passenger.getMembershipLevel());
        json.put("is_active", passenger.isActive());
        json.put("profile_image", passenger.getProfileImagePath());
        return json;
    }

    /**
     * Extract error message from VolleyError
     */
    private String getErrorMessage(VolleyError error) {
        if (error.networkResponse != null) {
            return "Server error: " + error.networkResponse.statusCode;
        } else if (error.getMessage() != null) {
            return error.getMessage();
        } else {
            return "Network error occurred";
        }
    }

    // ==================== CALLBACK INTERFACE ====================

    /**
     * Generic callback interface for network operations
     */
    public interface NetworkCallback<T> {
        void onSuccess(T response);
        void onError(String error);
    }
}
