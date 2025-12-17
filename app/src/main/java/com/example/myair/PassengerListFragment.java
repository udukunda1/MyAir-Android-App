package com.example.myair;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        passengerList = dbHelper.getAllPassengers();
        adapter.updateList(passengerList);

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
                    dbHelper.deletePassenger(passenger.getId());
                    loadPassengers();
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
