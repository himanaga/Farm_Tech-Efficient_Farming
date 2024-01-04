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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
public class workerAdapter extends RecyclerView.Adapter<workerAdapter.viewholder> {
    ArrayList<Worker> items;
    Context context;

    public workerAdapter(ArrayList<Worker> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public workerAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.worker,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull workerAdapter.viewholder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.priceTxt.setText("₹"+items.get(position).getPrice()+"per hour");
        holder.skill.setText(items.get(position).getSkill());
        holder.phn.setText("call"+items.get(position).getNum());
        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(),new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView name,priceTxt,skill,phn;
        ImageView pic;
        public viewholder(@NonNull View itemView){
            super(itemView);
            name =itemView.findViewById(R.id.w_name);
            priceTxt = itemView.findViewById(R.id.w_price);
            skill = itemView.findViewById(R.id.w_skill);
            phn = itemView.findViewById(R.id.w_phone);

            pic = itemView.findViewById(R.id.w_image);
        }

    }
}
