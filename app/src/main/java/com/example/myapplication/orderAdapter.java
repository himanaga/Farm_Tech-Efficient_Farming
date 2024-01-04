package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Farm;
import com.example.myapplication.R;

import java.util.List;

public class orderAdapter  extends RecyclerView.Adapter<orderAdapter.ViewHolder> {
    private List<Farm> orderItems;

    public orderAdapter(List<Farm> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Farm farm = orderItems.get(position);
        holder.titleTextView.setText(farm.getTitle());
        holder.priceTextView.setText("Price: ₹" + farm.getPrice());
        holder.quantityTextView.setText("Quantity: " + farm.getNumberInCart());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView priceTextView;
        public TextView quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.o_name);
            priceTextView = itemView.findViewById(R.id.o_price);
            quantityTextView = itemView.findViewById(R.id.o_quantity);
        }
    }
}



