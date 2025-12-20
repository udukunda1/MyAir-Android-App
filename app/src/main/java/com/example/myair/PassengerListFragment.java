package com.example.myair;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PassengerListFragment extends Fragment implements PassengerAdapter.OnPassengerClickListener {

    private RecyclerView recyclerView;
    private TextView tvEmptyMessage;
    private PassengerAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Passenger> passengerList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_passengers);
        tvEmptyMessage = view.findViewById(R.id.tv_empty_message);

        dbHelper = new DatabaseHelper(getContext());
        passengerList = new ArrayList<>();

        setupRecyclerView();
        loadPassengers();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PassengerAdapter(getContext(), passengerList, this);
        recyclerView.setAdapter(adapter);
    }

    public void loadPassengers() {
        // First, load from local SQLite (immediate display)
        passengerList = dbHelper.getAllPassengers();
        adapter.updateList(passengerList);
        updateEmptyState();
        
        // Then, try to fetch from server (async update)
        NetworkService.getInstance(getContext()).getAllPassengers(
            new NetworkService.NetworkCallback<org.json.JSONArray>() {
                @Override
                public void onSuccess(org.json.JSONArray response) {
                    try {
                        // Parse server response
                        List<Passenger> serverPassengers = new ArrayList<>();
                        
                        for (int i = 0; i < response.length(); i++) {
                            org.json.JSONObject passengerJson = response.getJSONObject(i);
                            
                            Passenger passenger = new Passenger();
                            passenger.setId(passengerJson.getInt("id"));
                            passenger.setFullName(passengerJson.getString("full_name"));
                            passenger.setEmail(passengerJson.getString("email"));
                            passenger.setPhone(passengerJson.getString("phone"));
                            passenger.setDateOfBirth(passengerJson.optString("date_of_birth", ""));
                            passenger.setMembershipLevel(passengerJson.optString("membership_level", "Economy"));
                            passenger.setActive(passengerJson.optBoolean("is_active", true));
                            passenger.setProfileImagePath(passengerJson.optString("profile_image", ""));
                            
                            serverPassengers.add(passenger);
                        }
                        
                        // SYNC TO SQLITE: Clear local database and insert server data
                        // This ensures server is the source of truth
                        syncServerDataToSQLite(serverPassengers);
                        
                        // Update UI with server data
                        passengerList.clear();
                        passengerList.addAll(serverPassengers);
                        adapter.updateList(passengerList);
                        updateEmptyState();
                        
                        Toast.makeText(getContext(), "Synced with server (" + serverPassengers.size() + " passengers)", Toast.LENGTH_SHORT).show();
                        
                    } catch (org.json.JSONException e) {
                        Toast.makeText(getContext(), "Error parsing server data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onError(String error) {
                    // Silently fail - we already have local data
                    // Only show error if list is empty
                    if (passengerList.isEmpty()) {
                        Toast.makeText(getContext(), "Using offline mode: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    private void syncServerDataToSQLite(List<Passenger> serverPassengers) {
        // Get all local passengers
        List<Passenger> localPassengers = dbHelper.getAllPassengers();
        
        // Create a map of server passenger IDs for quick lookup
        java.util.Set<Integer> serverIds = new java.util.HashSet<>();
        for (Passenger serverPassenger : serverPassengers) {
            serverIds.add(serverPassenger.getId());
            
            // Check if passenger exists locally
            boolean existsLocally = false;
            for (Passenger localPassenger : localPassengers) {
                if (localPassenger.getId() == serverPassenger.getId()) {
                    existsLocally = true;
                    break;
                }
            }
            
            if (existsLocally) {
                // Update existing passenger
                dbHelper.updatePassenger(serverPassenger);
            } else {
                // Add new passenger from server WITH SERVER ID
                dbHelper.addPassengerWithId(serverPassenger);
            }
        }
        
        // Delete local passengers that don't exist on server
        for (Passenger localPassenger : localPassengers) {
            if (!serverIds.contains(localPassenger.getId())) {
                dbHelper.deletePassenger(localPassenger.getId());
            }
        }
    }
    
    private void updateEmptyState() {
        if (passengerList.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditClick(Passenger passenger) {
        // Switch to add passenger tab and load data
        if (getActivity() instanceof PassengerActivity) {
            ((PassengerActivity) getActivity()).editPassenger(passenger);
        }
    }

    @Override
    public void onDeleteClick(Passenger passenger) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.btn_delete)
                .setMessage(R.string.msg_confirm_delete)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Delete from local SQLite first
                    dbHelper.deletePassenger(passenger.getId());
                    
                    // Sync deletion with server
                    NetworkService.getInstance(getContext()).deletePassenger(passenger.getId(),
                        new NetworkService.NetworkCallback<org.json.JSONObject>() {
                            @Override
                            public void onSuccess(org.json.JSONObject response) {
                                // Refresh UI after successful server deletion
                                loadPassengers();
                                Toast.makeText(getContext(), "Passenger deleted", Toast.LENGTH_SHORT).show();
                            }
                            
                            @Override
                            public void onError(String error) {
                                // Still refresh UI even if server sync fails (already deleted locally)
                                loadPassengers();
                                Toast.makeText(getContext(), "Deleted locally. Server sync failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPassengers();
    }
}
