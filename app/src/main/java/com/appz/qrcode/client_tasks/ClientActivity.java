package com.appz.qrcode.client_tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appz.qrcode.R;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void genetrateQrCode(View view) {
        startActivity(new Intent(getApplicationContext(),GenerateQrCodeActivity.class));

    }

    public void gotoProfile(View view) {

        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
    }
}
