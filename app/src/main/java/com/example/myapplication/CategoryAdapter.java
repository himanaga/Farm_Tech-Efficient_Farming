package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category>items) { this.items=items; }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent,  false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.titleTxt.setText(items.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("CategoryId",items.get(position).getId());
                intent.putExtra("CategoryName",items.get(position).getName());
                context.startActivity(intent);
            }
        });
        //trail
        holder.titleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("CategoryId",items.get(position).getId());
                intent.putExtra("CategoryName",items.get(position).getName());
                context.startActivity(intent);
            }
        });
        int leftDrawableResId = 0;
        int backgroundColorResId = 0;

        switch(position) {
            case 0: {
                leftDrawableResId = R.drawable.tractor;
                backgroundColorResId = R.color.tractor;
                break;
            }
            case 1: {
                leftDrawableResId = R.drawable.crops;
                backgroundColorResId = R.color.crops;
                break;
            }
            case 2: {
                leftDrawableResId = R.drawable.fertilizer;
                backgroundColorResId = R.color.fertilizers;
                break;
            }
            case 3: {
                leftDrawableResId = R.drawable.equip;
                backgroundColorResId = R.color.equip;
                break;
            }
        }
        // Set left drawable
        if (leftDrawableResId != 0) {
            holder.titleTxt.setCompoundDrawablesWithIntrinsicBounds(leftDrawableResId, 0, 0, 0);
        }
        if (backgroundColorResId != 0) {
            holder.titleTxt.setBackgroundResource(backgroundColorResId);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.title);

        }
    }
}
