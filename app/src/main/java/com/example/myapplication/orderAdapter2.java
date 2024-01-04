package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class orderAdapter2 extends RecyclerView.Adapter<orderAdapter2.OrderViewHolder> {

    private ArrayList<Order2> ordersList;
    private Context context;

    public orderAdapter2(ArrayList<Order2> ordersList) {
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order2 order = ordersList.get(position);

        holder.titleTextView.setText(order.getTitle());
        holder.priceTextView.setText(String.valueOf(order.getPrice()));
        holder.quantityTextView.setText(String.valueOf(order.getNumberInCart()));


        // Load image using Glide
        Glide.with(context).load(order.getImagePath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView quantityTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.o_image);
            titleTextView = itemView.findViewById(R.id.o_name);
            priceTextView = itemView.findViewById(R.id.o_price);
            quantityTextView = itemView.findViewById(R.id.o_quantity);
        }
    }
}
