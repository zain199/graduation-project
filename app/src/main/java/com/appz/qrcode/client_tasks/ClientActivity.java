package com.appz.qrcode.client_tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.detect_text;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
    }

    public void generateQrCode(View view) {
        startActivity(new Intent(getApplicationContext(), detect_text.class));

    }

    public void gotoProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
    }


    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


}
