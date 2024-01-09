package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
    }
            public void openLink1(View view) {
                openWebLink("https://github.com/06Arjun");
            }
            public void openLink2(View view) {
                openWebLink("https://github.com/himanaga");
            }

            public void openLink3(View view) {
                openWebLink("https://github.com/gurusasikumar");
            }

            public void openLink4(View view) {
                openWebLink("https://github.com/Rishab97m");
            }

            private void openWebLink(String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
}