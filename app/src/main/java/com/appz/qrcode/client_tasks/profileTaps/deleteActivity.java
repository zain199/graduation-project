package com.appz.qrcode.client_tasks.profileTaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appz.qrcode.R;

import com.appz.qrcode.client_tasks.models.childName;
import com.appz.qrcode.helperUi.AllFinal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class deleteActivity extends AppCompatActivity {

    //ui
    ListView listView ;
    private SharedPreferences sharedPreferences ;
    ProgressDialog progressDialog ;

    //var
    List ids = new ArrayList() ;
    List name = new ArrayList();
    String parent ;
    int points , cnt , total ;


    //database
    private FirebaseDatabase database ;
    private DatabaseReference ref ;

    private final DatabaseReference rationTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        init();
    }


    private void init()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        parent = sharedPreferences.getString("parent1","");

        getdata(ref.child(parent).child("points"));

        getIDs(rationTable.child(parent));

    }


    private void getIDs (final DatabaseReference ref)
    {

        ids.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                        total++;

                        if(Character.isDigit(data.getKey().charAt(0)))
                        {
                            ids.add(data.getKey());
                        }
                }

                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        cnt++;
                        if(cnt>=total)
                        {
                            progressDialog.dismiss();

                            listView = findViewById(R.id.listView);

                            ArrayAdapter arrayAdapter = new ArrayAdapter(deleteActivity.this ,android.R.layout.simple_list_item_1 ,ids);

                            listView.setAdapter(arrayAdapter);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getNames(List ids)
    {
        if(ids.size()>0)
        {
            for (int i=0 ; i<ids.size();i++)
            {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(String.valueOf(ids.get(i)));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                        {
                            String name2 = dataSnapshot1.getValue(String.class);
                            name.add(name2);
                        }

                        reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        cnt++;
                        if(cnt>=total)
                        {
                            progressDialog.dismiss();

                            listView = findViewById(R.id.listView);

                             ArrayAdapter arrayAdapter = new ArrayAdapter(deleteActivity.this ,android.R.layout.simple_list_item_1 ,name);

                             listView.setAdapter(arrayAdapter);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

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
}
