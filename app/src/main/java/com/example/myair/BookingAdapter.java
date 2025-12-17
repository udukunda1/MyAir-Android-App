package com.example.myair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<FlightBooking> bookingList;

    public BookingAdapter(Context context, List<FlightBooking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        FlightBooking booking = bookingList.get(position);

        holder.tvFlightNumber.setText(booking.getFlightNumber());
        holder.tvBookingDate.setText("Booked: " + booking.getBookingDate());
        holder.tvSeatNumber.setText("Seat: " + booking.getSeatNumber());
        holder.tvStatus.setText(booking.getStatus());

        // Set status color based on booking status
        int statusColor;
        if (booking.getStatus().equals("Confirmed")) {
            statusColor = context.getResources().getColor(R.color.success_green);
        } else if (booking.getStatus().equals("Pending")) {
            statusColor = context.getResources().getColor(R.color.warning_orange);
        } else {
            statusColor = context.getResources().getColor(R.color.error_red);
        }
        holder.tvStatus.setBackgroundColor(statusColor);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateList(List<FlightBooking> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvFlightNumber, tvBookingDate, tvSeatNumber, tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlightNumber = itemView.findViewById(R.id.tv_flight_number);
            tvBookingDate = itemView.findViewById(R.id.tv_booking_date);
            tvSeatNumber = itemView.findViewById(R.id.tv_seat_number);
            tvStatus = itemView.findViewById(R.id.tv_booking_status);
        }
    }
}
