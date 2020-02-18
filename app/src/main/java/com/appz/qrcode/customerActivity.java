package com.appz.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class customerActivity extends AppCompatActivity {

    //ui
    Button overview , edit,add, generateQr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        findByid();
        overviewClicked();
        editClicked();
        generateQrClicked();

    }


    private void findByid()
    {
        overview = findViewById(R.id.overview);
        edit = findViewById(R.id.edit);
        add = findViewById(R.id.add);
        generateQr = findViewById(R.id.generate_qr);

    }

    private void overviewClicked()
    {
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customerActivity.this,rationServiceReview.class);
                startActivity(intent);
            }
        });
    }

    private void editClicked()
    {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customerActivity.this,updateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void generateQrClicked()
    {
        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customerActivity.this,detect_text.class);
                startActivity(intent);
            }
        });
    }
}
