package com.appz.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    // UI

    EditText ed_username , ed_pass;
    Button loginbtn , forgetbtn , registerbtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findByid();
        login();
    }

    private void findByid()
    {
        ed_username = findViewById(R.id.ed_username);
        ed_pass = findViewById(R.id.ed_pass);
        loginbtn = findViewById(R.id.loginBtn);
        forgetbtn = findViewById(R.id.forget);
        registerbtn = findViewById(R.id.Register);
    }

    private void login()
    {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this , detect_text.class);
                startActivity(intent);

            }
        });
    }

    private void forget()
    {
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void Register()
    {
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
