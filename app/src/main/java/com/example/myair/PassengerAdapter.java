package com.example.myair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder> {

    private Context context;
    private List<Passenger> passengerList;
    private OnPassengerClickListener listener;

    public interface OnPassengerClickListener {
        void onEditClick(Passenger passenger);
        void onDeleteClick(Passenger passenger);
    }

    public PassengerAdapter(Context context, List<Passenger> passengerList, OnPassengerClickListener listener) {
        this.context = context;
        this.passengerList = passengerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_passenger, parent, false);
        return new PassengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerViewHolder holder, int position) {
        Passenger passenger = passengerList.get(position);

        holder.tvName.setText(passenger.getFullName());
        holder.tvEmail.setText(passenger.getEmail());
        holder.tvMembership.setText(passenger.getMembershipLevel());

        // Load profile image if exists
        if (passenger.getProfileImagePath() != null && !passenger.getProfileImagePath().isEmpty()) {
            File imgFile = new File(passenger.getProfileImagePath());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imgProfile.setImageBitmap(bitmap);
            }
        } else {
            holder.imgProfile.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Card click to view details
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context, PassengerDetailsActivity.class);
            intent.putExtra("PASSENGER_ID", passenger.getId());
            context.startActivity(intent);
        });

        // Edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(passenger);
            }
        });

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(passenger);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public void updateList(List<Passenger> newList) {
        this.passengerList = newList;
        notifyDataSetChanged();
    }

    static class PassengerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile, btnEdit, btnDelete;
        TextView tvName, tvEmail, tvMembership;

        public PassengerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.img_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvMembership = itemView.findViewById(R.id.tv_membership);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
