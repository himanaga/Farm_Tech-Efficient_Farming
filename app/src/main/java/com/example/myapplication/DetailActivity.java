package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private Farm object;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void getIntentExtra() {
        object = (Farm) getIntent().getSerializableExtra("object");
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Glide.with(DetailActivity.this).load(object.getImagePath()).into(binding.pic1);

        binding.detailtitle.setText(object.getTitle());
        binding.detailprice.setText("₹" + object.getPrice());
        binding.startxtdetail.setText(object.getStar() + " Rating");
        binding.descrip.setText(object.getDescription());
        binding.ratingBar2.setRating((float) object.getStar());
        binding.detailtotalprice.setText(num * object.getPrice() + " ₹");

        binding.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
            }
        });

        binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1) {
                    num = num - 1;
                    binding.num.setText(num + " ");
                    binding.detailtotalprice.setText("₹" + (num * object.getPrice()));
                }
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = num + 1;
                binding.num.setText(num + " ");
                binding.detailtotalprice.setText("₹" + (num * object.getPrice()));
            }
        });
    }
}
