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
    private final DatabaseReference rationTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);
    private final DatabaseReference fakeTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.FAKE_DATA);


    //vars
    int points ;
    SharedPreferences sharedPreferences ;
    String parent ;
    List ids = new ArrayList();
    List correctIds = new ArrayList();


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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        parent = sharedPreferences.getString("parent1","");
        getdata(ref.child(parent).child("points"));
        getIDs(rationTable.child(parent));
        getCorrectIDs(fakeTable.child(parent).child(AllFinal.CHILDS));
    }

    private void findByID()
    {
        add = findViewById(R.id.btn_add);
        id = findViewById(R.id.ed_id);
        name = findViewById(R.id.ed_name);
    }


    private void getdata(DatabaseReference reff)
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

    }

    private void addChild()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString().trim();
                String Name = name.getText().toString().trim();

                if (isCorrect(ID))
                {


                    if(isAlreadyExist(ID))
                    {
                        Toast.makeText(getBaseContext(),"This ID is Already Exist",Toast.LENGTH_LONG).show();
                    }else
                    {
                        points+=50;
                        ref.child(parent).child(ID);
                        ref.child(parent).child(ID).child("Name").setValue(Name);
                        ref.child(parent).child("points").setValue(points);
                        Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_LONG).show();
                    }


                }else
                {
                    Toast.makeText(getBaseContext(),"Enter Correct ID",Toast.LENGTH_LONG).show();
                }



            }
        });


    }

    private void getIDs (DatabaseReference ref)
    {

        ids.clear();
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    ids.add(data.getKey());
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



    }

    private boolean isAlreadyExist(String id)
    {


        for ( int i = 0 ; i <ids.size();++i)
        {
            if(ids.get(i).equals(id))
                return true;
        }
        return false ;
    }

    private void getCorrectIDs (DatabaseReference ref)
    {

        correctIds.clear();
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    correctIds.add(data.getKey());
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



    }

    private boolean isCorrect(String id)
    {
        for ( int i = 0 ; i <correctIds.size();++i)
        {
            if(correctIds.get(i).equals(id))
                return true;
        }
        return false ;
    }
}
