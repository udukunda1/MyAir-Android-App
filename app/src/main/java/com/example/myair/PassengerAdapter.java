package com.example.myair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // Load profile image if available
        if (passenger.getProfileImagePath() != null && !passenger.getProfileImagePath().isEmpty()) {
            android.graphics.Bitmap bitmap = ImageUtils.decodeBase64(passenger.getProfileImagePath());
            if (bitmap != null) {
                holder.ivProfile.setImageBitmap(bitmap);
            } else {
                holder.ivProfile.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.ivProfile.setImageResource(android.R.drawable.ic_menu_gallery);
        }

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

        // Card click to view details
        holder.itemView.setOnClickListener(v -> {
            // Navigate to passenger details
            android.content.Intent intent = new android.content.Intent(context, PassengerDetailsActivity.class);
            intent.putExtra("PASSENGER_ID", passenger.getId());
            context.startActivity(intent);
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
        ImageView ivProfile;
        TextView tvName;
        TextView tvEmail;
        TextView tvMembership;
        ImageView btnEdit;
        ImageView btnDelete;

        public PassengerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.img_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvMembership = itemView.findViewById(R.id.tv_membership);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
