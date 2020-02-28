package com.appz.qrcode.seller_tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appz.qrcode.R;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

    public void gotoEditedStore(View view) {
        startActivity(new Intent(getApplicationContext(), EditedStoreActivity.class));
    }

    public void gotoSell(View view) {
        startActivity(new Intent(getApplicationContext(), ScanQR.class));
    }
}
