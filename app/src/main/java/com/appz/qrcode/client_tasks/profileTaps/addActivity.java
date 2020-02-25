package com.appz.qrcode.client_tasks.profileTaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class addActivity extends AppCompatActivity {

    // ui
    Button add ;
    EditText id , name;

    //firebase
    FirebaseDatabase database ;
    DatabaseReference ref ;
    FirebaseAuth auth;

    //vars
    int points ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        init();
        findByID();
        addChild();
    }

    private void init()
    {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);
        auth = FirebaseAuth.getInstance();

    }

    private void findByID()
    {
        add = findViewById(R.id.btn_add);
        id = findViewById(R.id.ed_id);
        name = findViewById(R.id.ed_name);
    }


    private int getdata(DatabaseReference reff)
    {
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                points = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return points;
    }

    private void addChild()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString().trim();
                String Name = name.getText().toString().trim();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String parent = sharedPreferences.getString("parent1","");


                ref.child(parent).child(ID);
                ref.child(parent).child(ID).child("Name").setValue(Name);

                points =  getdata(ref.child(parent).child("points"));

                points+=50;

                ref.child(parent).child("points").setValue(points);

            }
        });


    }
}
