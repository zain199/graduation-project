package com.appz.qrcode.client_tasks.profileTaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dialogActivity extends AppCompatActivity {

    //ui
    TextView message ;
    Button yes , no;


    //var
    String name , id , parentID;
    int points;

    //database
    DatabaseReference ref , ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

       findbyid();
       init();
       showDialog();
       yesBtn();
       noBtn();

    }

    private void findbyid()
    {
        message=findViewById(R.id.msg);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
    }

    private void showDialog()
    {
        message.setText("Are You Sure You Want To Delete "+name+ " From Your Ration Card");
    }

    private void init()
    {
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("childid");
        parentID = getIntent().getStringExtra("parentID");

        points = getIntent().getIntExtra("parentPoints",0);


        ref2 = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data).child(parentID).child("points");
        ref = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data).child(parentID).child(id);
    }


    private void yesBtn()
    {
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref2.setValue(points-50);
                ref.removeValue();
                Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void noBtn()
    {
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
