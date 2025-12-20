package com.example.myair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

public class PassengerDetailsActivity extends AppCompatActivity {

    private ImageView imgProfileLarge, backButton;
    private TextView tvDetailName, tvDetailMembership, tvDetailEmail, tvDetailPhone, tvDetailDob;
    private TextView tvNoBookings;
    private RecyclerView recyclerBookings;
    private FloatingActionButton fabBookFlight;

    private DatabaseHelper dbHelper;
    private BookingAdapter bookingAdapter;
    private Passenger passenger;
    private int passengerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        initViews();
        dbHelper = new DatabaseHelper(this);

        // Get passenger ID from intent
        passengerId = getIntent().getIntExtra("PASSENGER_ID", -1);
        if (passengerId == -1) {
            finish();
            return;
        }

        loadPassengerDetails();
        loadBookings();

        backButton.setOnClickListener(v -> finish());

        fabBookFlight.setOnClickListener(v -> {
            Intent intent = new Intent(PassengerDetailsActivity.this, BookFlightActivity.class);
            intent.putExtra("PASSENGER_ID", passengerId);
            intent.putExtra("PASSENGER_NAME", passenger.getFullName());
            startActivity(intent);
        });
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
        imgProfileLarge = findViewById(R.id.img_profile_large);
        tvDetailName = findViewById(R.id.tv_detail_name);
        tvDetailMembership = findViewById(R.id.tv_detail_membership);
        tvDetailEmail = findViewById(R.id.tv_detail_email);
        tvDetailPhone = findViewById(R.id.tv_detail_phone);
        tvDetailDob = findViewById(R.id.tv_detail_dob);
        recyclerBookings = findViewById(R.id.recycler_bookings);
        tvNoBookings = findViewById(R.id.tv_no_bookings);
        fabBookFlight = findViewById(R.id.fab_book_flight);

        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadPassengerDetails() {
        passenger = dbHelper.getPassenger(passengerId);
        if (passenger == null) {
            finish();
            return;
        }

        tvDetailName.setText(passenger.getFullName());
        tvDetailMembership.setText(passenger.getMembershipLevel());
        tvDetailEmail.setText("Email: " + passenger.getEmail());
        tvDetailPhone.setText("Phone: " + passenger.getPhone());
        tvDetailDob.setText("DOB: " + passenger.getDateOfBirth());

        // Load profile image (Base64 encoded)
        if (passenger.getProfileImagePath() != null && !passenger.getProfileImagePath().isEmpty()) {
            try {
                // Decode Base64 to Bitmap
                byte[] decodedBytes = android.util.Base64.decode(passenger.getProfileImagePath(), android.util.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bitmap != null) {
                    imgProfileLarge.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Keep default image if decoding fails
            }
        }
    }

    private void loadBookings() {
        List<FlightBooking> bookings = dbHelper.getBookingsByPassengerId(passengerId);
        
        if (bookings.isEmpty()) {
            tvNoBookings.setVisibility(View.VISIBLE);
            recyclerBookings.setVisibility(View.GONE);
        } else {
            tvNoBookings.setVisibility(View.GONE);
            recyclerBookings.setVisibility(View.VISIBLE);
            bookingAdapter = new BookingAdapter(this, bookings);
            recyclerBookings.setAdapter(bookingAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings(); // Refresh bookings when returning from BookFlightActivity
    }
}
