package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityCartBinding;

public class cart extends AppCompatActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private double dtax;
    private RadioButton radioCashOnDelivery;
    private RadioButton radioUpiPayment;
    private Button placeOrderButton;
    private static final int requestCodeForUpiPayment = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        setVariable();
        calculatorCart();
        initList();
        initUI();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //pay

        //Intent upiPaymentIntent = new Intent(/* Add necessary parameters for UPI payment */);
        // startActivityForResult(upiPaymentIntent, requestCodeForUpiPayment);*/
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==requestCodeForUpiPayment ) {
            if (resultCode == RESULT_OK && data != null) {
                handleSuccessfulUpiPayment();
            } else {
                handleFailedUpiPayment();
            }
        }
    }
    private void handleSuccessfulUpiPayment() {
        Toast.makeText(this, "UPI payment successful!", Toast.LENGTH_SHORT).show();
        // Add any other actions you need to perform after successful payment
        finish();
    }

    private void handleFailedUpiPayment() {
        Toast.makeText(this, "UPI payment failed or canceled", Toast.LENGTH_SHORT).show();
        // Add any other actions you need to perform after failed payment
    }*/
    private void initUI() {
        radioCashOnDelivery = findViewById(R.id.radioCashOnDelivery);
        radioUpiPayment = findViewById(R.id.radioUpiPayment);
        placeOrderButton = findViewById(R.id.placeorder);

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioCashOnDelivery.isChecked()) {
                    handleCashOnDelivery();
                } else if (radioUpiPayment.isChecked()) {
                    handleUpiPayment();
                } else {
                    Toast.makeText(cart.this, "Please select a payment option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleCashOnDelivery() {
        Toast.makeText(cart.this, "Order placed successfully (Cash on Delivery)", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleUpiPayment() {
        // Handle UPI Payment logic here
        // You can use a library or integrate with a UPI payment gateway
        Toast.makeText(cart.this, "Launching UPI Payment...", Toast.LENGTH_SHORT).show();

        // You should launch your UPI payment flow here
        // Example: StartActivityForResult to handle payment callback
    }
    //untilpay----------

    private void initList(){
        if (managmentCart.getListCart().isEmpty()){
            binding.empty.setVisibility(View.VISIBLE);
            binding.scroll.setVisibility(View.GONE);
        }else {
            binding.empty.setVisibility(View.GONE);
            binding.scroll.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.cartview.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(),this,new ChangeNumberItemsListener(){
            @Override
            public void change(){
                calculatorCart();
            }
        });
        binding.cartview.setAdapter(adapter);
    }
    private void calculatorCart(){
        double percentTax =0.15;
        double delivary = 10;

        tax = Math.round(managmentCart.getTotalFee()*percentTax*100)/100;
        dtax = Math.round(managmentCart.getTotalFee()+delivary*10)/10;
        double total =Math.round((managmentCart.getTotalFee()+tax + dtax)*100)/100;
        double itemTotal = Math.round(managmentCart.getTotalFee()*100)/100;

        binding.subtotal.setText("₹"+itemTotal);
        binding.tax.setText("₹"+tax);
        binding.delivary.setText("₹"+dtax);
        binding.total.setText("₹"+total);


    }
    private void setVariable(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}