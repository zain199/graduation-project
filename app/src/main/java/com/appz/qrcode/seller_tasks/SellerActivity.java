package com.appz.qrcode.seller_tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.appz.qrcode.R;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SellerActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        button = findViewById(R.id.btn_show_qr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSell(v);
                // Intent intent=new Intent(this,SellActivity.class);
            }
        });

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
        startActivity(new Intent(this, SellActivity.class));
    }
}
