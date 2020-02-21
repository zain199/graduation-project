package com.appz.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.appz.qrcode.review_tasks.activities.rationServiceReviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class customerActivity extends AppCompatActivity {

    //ui
    Button overview , edit,add, generateQr;

    // var
    private String test;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference reference=database.getReference(AllFinal.ROOT_LOGIN_FIRE);
    private static String who_am_I="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        findByid();
        overviewClicked();
        editClicked();
        generateQrClicked();



        checkWhoAmiI();




    }

    private void checkWhoAmiI() {
        reference.child(AllFinal.CLIENT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid()))
                {
                    who_am_I="iam client";
                    Toast.makeText(getApplicationContext(), who_am_I, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child(AllFinal.SELLER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid()))
                {
                    who_am_I="iam Seller";
                    Toast.makeText(getApplicationContext(), who_am_I, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void findByid()
    {
        mAuth=FirebaseAuth.getInstance();
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
                Intent intent = new Intent(customerActivity.this, rationServiceReviewActivity.class);
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

    public void signout(View view) {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }
}
