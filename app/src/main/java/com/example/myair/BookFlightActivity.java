package com.example.myair;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookFlightActivity extends AppCompatActivity {

    private TextView tvPassengerName, tvBookingDate;
    private TextInputLayout tilFlightNumber, tilSeatNumber;
    private TextInputEditText etFlightNumber, etSeatNumber;
    private Spinner spinnerStatus;
    private Button btnSelectBookingDate, btnSaveBooking;
    private ImageView backButton;

    private DatabaseHelper dbHelper;
    private int passengerId;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_flight);

        initViews();
        dbHelper = new DatabaseHelper(this);

        // Get passenger info from intent
        passengerId = getIntent().getIntExtra("PASSENGER_ID", -1);
        String passengerName = getIntent().getStringExtra("PASSENGER_NAME");

        if (passengerId == -1) {
            finish();
            return;
        }

        tvPassengerName.setText(passengerName);

        setupStatusSpinner();
        setupDatePicker();
        setupSaveButton();

        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
        tvPassengerName = findViewById(R.id.tv_passenger_name);
        tilFlightNumber = findViewById(R.id.til_flight_number);
        tilSeatNumber = findViewById(R.id.til_seat_number);
        etFlightNumber = findViewById(R.id.et_flight_number);
        etSeatNumber = findViewById(R.id.et_seat_number);
        tvBookingDate = findViewById(R.id.tv_booking_date);
        spinnerStatus = findViewById(R.id.spinner_status);
        btnSelectBookingDate = findViewById(R.id.btn_select_booking_date);
        btnSaveBooking = findViewById(R.id.btn_save_booking);
    }

    private void setupStatusSpinner() {
        String[] statusOptions = {
                getString(R.string.status_confirmed),
                getString(R.string.status_pending),
                getString(R.string.status_cancelled)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }

    private void setupDatePicker() {
        btnSelectBookingDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Booking Date")
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = sdf.format(new Date(selection));
                tvBookingDate.setText(selectedDate);
                tvBookingDate.setTextColor(getResources().getColor(R.color.text_primary));
            });

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });
    }

    private void setupSaveButton() {
        btnSaveBooking.setOnClickListener(v -> {
            if (validateForm()) {
                saveBooking();
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate flight number
        String flightNumber = etFlightNumber.getText().toString().trim();
        if (flightNumber.isEmpty()) {
            tilFlightNumber.setError(getString(R.string.error_flight_number_required));
            isValid = false;
        } else {
            tilFlightNumber.setError(null);
        }

        // Validate seat number
        String seatNumber = etSeatNumber.getText().toString().trim();
        if (seatNumber.isEmpty()) {
            tilSeatNumber.setError(getString(R.string.error_seat_required));
            isValid = false;
        } else {
            tilSeatNumber.setError(null);
        }

        // Validate date
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, R.string.error_date_required, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void saveBooking() {
        String flightNumber = etFlightNumber.getText().toString().trim();
        String seatNumber = etSeatNumber.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        long result = dbHelper.addBooking(passengerId, flightNumber, selectedDate, seatNumber, status);

        if (result > 0) {
            Toast.makeText(this, R.string.msg_booking_saved, Toast.LENGTH_SHORT).show();
            finish(); // Return to PassengerDetailsActivity
        } else {
            Toast.makeText(this, "Failed to save booking", Toast.LENGTH_SHORT).show();
        }
    }
}
