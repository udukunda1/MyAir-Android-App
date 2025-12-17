package com.example.myair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPassengerFragment extends Fragment {

    private TextInputLayout tilFullName, tilEmail, tilPhone;
    private TextInputEditText etFullName, etEmail, etPhone;
    private TextView tvDateOfBirth;
    private Spinner spinnerMembership;
    private CheckBox checkboxActive;
    private ImageView profileImagePreview;
    private Button btnSelectImage, btnSelectDate, btnSave;

    private DatabaseHelper dbHelper;
    private String selectedDate = "";
    private String selectedImagePath = "";
    private Passenger editingPassenger = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_passenger, container, false);

        initViews(view);
        setupImagePicker();
        setupMembershipSpinner();
        setupDatePicker();
        setupSaveButton();

        dbHelper = new DatabaseHelper(getContext());

        return view;
    }

    private void initViews(View view) {
        tilFullName = view.findViewById(R.id.til_full_name);
        tilEmail = view.findViewById(R.id.til_email);
        tilPhone = view.findViewById(R.id.til_phone);
        
        etFullName = view.findViewById(R.id.et_full_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        
        tvDateOfBirth = view.findViewById(R.id.tv_date_of_birth);
        spinnerMembership = view.findViewById(R.id.spinner_membership);
        checkboxActive = view.findViewById(R.id.checkbox_active);
        profileImagePreview = view.findViewById(R.id.profile_image_preview);
        
        btnSelectImage = view.findViewById(R.id.btn_select_image);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnSave = view.findViewById(R.id.btn_save);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            try {
                                InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                profileImagePreview.setImageBitmap(bitmap);
                                
                                // Save image to internal storage
                                selectedImagePath = saveImageToInternalStorage(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = getContext().getFilesDir();
        String fileName = "passenger_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setupMembershipSpinner() {
        String[] membershipLevels = {
                getString(R.string.membership_economy),
                getString(R.string.membership_premium),
                getString(R.string.membership_business),
                getString(R.string.membership_first_class)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                membershipLevels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMembership.setAdapter(adapter);
    }

    private void setupDatePicker() {
        btnSelectDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date of Birth")
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = sdf.format(new Date(selection));
                tvDateOfBirth.setText(selectedDate);
                tvDateOfBirth.setTextColor(getResources().getColor(R.color.text_primary));
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                savePassenger();
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate name
        String name = etFullName.getText().toString().trim();
        if (name.isEmpty()) {
            tilFullName.setError(getString(R.string.error_name_required));
            isValid = false;
        } else {
            tilFullName.setError(null);
        }

        // Validate email
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.error_email_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalid));
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Validate phone
        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            tilPhone.setError(getString(R.string.error_phone_required));
            isValid = false;
        } else {
            tilPhone.setError(null);
        }

        // Validate date
        if (selectedDate.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_date_required, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void savePassenger() {
        Passenger passenger = new Passenger();
        passenger.setFullName(etFullName.getText().toString().trim());
        passenger.setEmail(etEmail.getText().toString().trim());
        passenger.setPhone(etPhone.getText().toString().trim());
        passenger.setDateOfBirth(selectedDate);
        passenger.setMembershipLevel(spinnerMembership.getSelectedItem().toString());
        passenger.setActive(checkboxActive.isChecked());
        passenger.setProfileImagePath(selectedImagePath);

        if (editingPassenger != null) {
            // Update existing passenger
            passenger.setId(editingPassenger.getId());
            dbHelper.updatePassenger(passenger);
            Toast.makeText(getContext(), R.string.msg_passenger_updated, Toast.LENGTH_SHORT).show();
        } else {
            // Add new passenger
            dbHelper.addPassenger(passenger);
            Toast.makeText(getContext(), R.string.msg_passenger_saved, Toast.LENGTH_SHORT).show();
        }

        clearForm();
        
        // Refresh the list in the other fragment
        if (getActivity() instanceof PassengerActivity) {
            ((PassengerActivity) getActivity()).refreshPassengerList();
        }
    }

    public void loadPassengerData(Passenger passenger) {
        editingPassenger = passenger;
        
        etFullName.setText(passenger.getFullName());
        etEmail.setText(passenger.getEmail());
        etPhone.setText(passenger.getPhone());
        
        selectedDate = passenger.getDateOfBirth();
        tvDateOfBirth.setText(selectedDate);
        tvDateOfBirth.setTextColor(getResources().getColor(R.color.text_primary));
        
        // Set membership spinner
        String[] membershipLevels = {
                getString(R.string.membership_economy),
                getString(R.string.membership_premium),
                getString(R.string.membership_business),
                getString(R.string.membership_first_class)
        };
        for (int i = 0; i < membershipLevels.length; i++) {
            if (membershipLevels[i].equals(passenger.getMembershipLevel())) {
                spinnerMembership.setSelection(i);
                break;
            }
        }
        
        checkboxActive.setChecked(passenger.isActive());
        
        // Load image
        selectedImagePath = passenger.getProfileImagePath();
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            File imgFile = new File(selectedImagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profileImagePreview.setImageBitmap(bitmap);
            }
        }
        
        btnSave.setText(R.string.btn_update);
    }

    public void clearForm() {
        etFullName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        tvDateOfBirth.setText(R.string.btn_select_date);
        tvDateOfBirth.setTextColor(getResources().getColor(R.color.text_hint));
        spinnerMembership.setSelection(0);
        checkboxActive.setChecked(true);
        profileImagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
        
        selectedDate = "";
        selectedImagePath = "";
        editingPassenger = null;
        
        btnSave.setText(R.string.btn_save);
        
        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
    }
}
