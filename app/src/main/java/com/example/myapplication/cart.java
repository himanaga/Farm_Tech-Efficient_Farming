package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//payment
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.jar.JarException;

import java.util.List;
public class cart extends AppCompatActivity implements  PaymentResultListener {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private double dtax;
    private RadioButton radioCashOnDelivery;
    private RadioButton radioUpiPayment;
    private Button placeOrderButton;
    private static final int requestCodeForUpiPayment = 123;
    private String userPhone;
    private String userEmail;
    private String userAdres;
    private  String userId;
    private double total;
    private  double total2;
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

        Checkout.preload(getApplicationContext());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("user").document(userId);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        userPhone = document.getString("profileUserPhone");
                        userEmail = document.getString("profileUserEmail");
                        userAdres = document.getString("profileUserAddress");

                    }
                }
            });
        }
    }
    private void initUI() {
        RadioGroup paymentRadioGroup = findViewById(R.id.paymentRadioGroup);

        radioCashOnDelivery = findViewById(R.id.radioCashOnDelivery);
        radioUpiPayment = findViewById(R.id.radioUpiPayment);
        placeOrderButton = findViewById(R.id.placeorder);

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = paymentRadioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    // No radio button selected
                    Toast.makeText(cart.this, "Please select a payment option", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedId == R.id.radioCashOnDelivery) {
                    handleCashOnDelivery();
                } else if (selectedId == R.id.radioUpiPayment) {
                    handleUpiPayment();
                }
            }
        });
    }
    private void handleCashOnDelivery() {
        List<Farm> cartItems = managmentCart.getListCart();
        managmentCart.clearCart();
        // Creating Order object
        Order order =   new Order(userId,userPhone,userAdres,cartItems);
        order.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.setPhoneNumber(userPhone);
        order.setAddress(userAdres);
        order.setItems(cartItems);
        addOrderToFirebase(order);

        total2 = total;

        String orderDetails = "Order placed successfully (Cash on Delivery)\nOrder of: ₹" + total2;
        Toast.makeText(cart.this, "Order placed successfully (Cash on Delivery)", Toast.LENGTH_SHORT).show();
        Intent orderDetailsIntent = new Intent(cart.this, MainActivity.class);
        orderDetailsIntent.putExtra("orderDetails", orderDetails);
        startActivity(orderDetailsIntent);
        finish();
    }

    private void handleUpiPayment() {
        // Handle UPI Payment logic here
        Toast.makeText(cart.this, "Launching UPI Payment...", Toast.LENGTH_SHORT).show();

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_Mp7AUEHoTYHHZS");
        checkout.setImage(R.drawable.logo);

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Farm Tech");
            options.put("description", "Reference No. #123456 payment gateway");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", total);
            options.put("prefill.email", userEmail);
            options.put("prefill.contact",userPhone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(cart.this,options);
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }


    }
    private void addOrderToFirebase(Order order) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(cart.this, "Order added to Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(cart.this, "Error adding order to Firebase", Toast.LENGTH_SHORT).show();
                });
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
        total =Math.round((managmentCart.getTotalFee()+tax + dtax)*100)/100;
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

    @Override
    public void onPaymentSuccess(String s) {
        List<Farm> cartItems = managmentCart.getListCart();
        managmentCart.clearCart();

        Order order = new Order(userId,userPhone,userAdres,cartItems);
        order.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.setPhoneNumber(userPhone);
        order.setAddress(userAdres);
        order.setItems(cartItems);

        // Add the order to the "orders" collection in Firebase
        addOrderToFirebase(order);
        Toast.makeText(cart.this, "Order placed successfully (UPI payment)", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onPaymentError(int i, String s) {

    }
}