package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private boolean isCashOnDeliverySelected = false;
    private boolean isUpiPaymentSelected = false;


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
        radioCashOnDelivery = findViewById(R.id.radioCashOnDelivery);
        radioUpiPayment = findViewById(R.id.radioUpiPayment);
        placeOrderButton = findViewById(R.id.placeorder);
        radioCashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCashOnDeliverySelected = true;
                isUpiPaymentSelected = false;
                radioUpiPayment.setChecked(false);
            }
        });
        radioUpiPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCashOnDeliverySelected = false;
                isUpiPaymentSelected = true;
                radioCashOnDelivery.setChecked(false);
            }
        });
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCashOnDeliverySelected) {
                    handleCashOnDelivery();
                } else if (isUpiPaymentSelected) {
                    handleUpiPayment(total);
                } else {
                    Toast.makeText(cart.this, "Please select a payment option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleCashOnDelivery() {
        List<Farm> cartItems = managmentCart.getListCart();
        managmentCart.clearCart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
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
      /*  Intent orderDetailsIntent = new Intent(cart.this, MainActivity.class);
        orderDetailsIntent.putExtra("orderDetails", orderDetails);
        startActivity(orderDetailsIntent);
        finish();*/
        showSuccessDialogWithDelay(orderDetails,total2);
    } else {
        Toast.makeText(cart.this, "User not authenticated", Toast.LENGTH_SHORT).show();
    }
    }

    private void handleUpiPayment(double total3) {
        Toast.makeText(cart.this, "Launching UPI Payment...", Toast.LENGTH_SHORT).show();
     /*   RazorpayClient razorpay = new RazorpayClient("rzp_test_gxgjYLbq58RuG1", "Gy14sbVUYF1rCkpSn6w5EUli");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", 500);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_11");

        Order order = razorpay.Orders.create(orderRequest);
     catch (RazorpayException e) {
        System.out.println(e.getMessage());
    }
*/
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_gxgjYLbq58RuG1");
        checkout.setImage(R.drawable.logo);
        final cart activity = this;
        try {
            JSONObject options = new JSONObject();
    /*        options.put("name", "Farm Tech");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", 35);
            options.put("prefill.email", userEmail);
            options.put("prefill.contact",userPhone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(cart.this,options);*/
            options.put("name", "Farm Tech");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
         //   options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount",String.valueOf(total3 * 100)); //30X100
            options.put("prefill.email",userEmail);
            options.put("prefill.contact", userPhone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);

        }
        catch(Exception e) {
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
    @Override
    public void onPaymentSuccess(String s) {
        List<Farm> cartItems = managmentCart.getListCart();
        managmentCart.clearCart();
        String orderDetails = "Order placed successfully (Upi payment)\nOrder of: ₹" + total2;
        Order order = new Order(userId,userPhone,userAdres,cartItems);
        order.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.setPhoneNumber(userPhone);
        order.setAddress(userAdres);
        order.setItems(cartItems);
        addOrderToFirebase(order);
        Toast.makeText(cart.this, "Order placed successfully (UPI payment)", Toast.LENGTH_SHORT).show();
        Log.d("ONSUCCESS","onPaymentSuccess:  "+s );
        showSuccessDialogWithDelay(orderDetails,total2);
    }
    @Override
    public void onPaymentError(int i, String s) {
        Log.d("ONERROR","onPaymentError:  "+s );
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

    public void showSuccessDialogWithDelay(String orderDetails,double total2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_ordersuces, null);
        builder.setView(dialogView);
        LottieAnimationView lottieAnimationView = dialogView.findViewById(R.id.sucs);
        TextView messageTextView = dialogView.findViewById(R.id.sucsmsg);

        lottieAnimationView.setAnimation(R.raw.sucs);
        lottieAnimationView.playAnimation();
        messageTextView.setText(orderDetails);

        AlertDialog dialog = builder.create();
        new Handler().postDelayed(() -> {
            dialog.show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 4000);
        }, 1120);
    }
}