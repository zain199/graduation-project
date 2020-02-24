package com.appz.qrcode.client_tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appz.qrcode.R;
import com.appz.qrcode.client_tasks.profileTaps.addActivity;
import com.appz.qrcode.client_tasks.profileTaps.deleteActivity;
import com.appz.qrcode.client_tasks.profileTaps.overviewActivity;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    Button add , delete , overview , restore , logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findByID();
        add();
        delete();
        overview();
        logout();

    }

    private void findByID()
    {
        add = findViewById(R.id.addchild);
        delete = findViewById(R.id.delete);
        overview = findViewById(R.id.view);
        restore = findViewById(R.id.restoreQR);
        logout = findViewById(R.id.logout);
    }

    private void add()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this , addActivity.class));
            }
        });

    }

    private void delete()
    {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this , deleteActivity.class));
            }
        });

    }

    private void overview()
    {
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this , overviewActivity.class));
            }
        });

    }

    private void logout()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });


    }
}
