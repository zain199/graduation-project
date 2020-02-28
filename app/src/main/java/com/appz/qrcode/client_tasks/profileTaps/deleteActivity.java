package com.appz.qrcode.client_tasks.profileTaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    ProgressDialog progressDialog ;
    TextView textView;

    //var
    List ids = new ArrayList() ;
    List name = new ArrayList();
    String parentID ;
    int points  ;


    //database
    private FirebaseDatabase database ;
    private DatabaseReference ref ;

    private final DatabaseReference rationTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        textView=findViewById(R.id.txt);
        init();
    }


    private void init()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();


        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);


        parentID = getIntent().getStringExtra("id");

        getIDs(rationTable.child(parentID));
        getdata(ref.child(parentID).child("points"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNames(ids);
            }
        },1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("omar","done");
                progressDialog.dismiss();
                listView = findViewById(R.id.listView);
                if(ids.size()>0)
                {
                    textView.setVisibility(View.VISIBLE);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(deleteActivity.this ,android.R.layout.simple_list_item_1 ,name);
                    listView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                    onitemclick();
                }
                else
                {
                    textView.setText("There Are No Children To Delete");
                    textView.setVisibility(View.VISIBLE);
                }

            }
        },1000);


    }


    private void getIDs (final DatabaseReference ref)
    {

        ids.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                        if(Character.isDigit(data.getKey().charAt(0)))
                        {
                            ids.add(data.getKey());

                        }
                }




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

                final DatabaseReference reference = rationTable.child(parentID).child(String.valueOf(ids.get(i)));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                        {
                            String name2 = dataSnapshot1.getValue(String.class);
                            name.add(name2);
                        }
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

    private void onitemclick()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),dialogActivity.class);
                intent.putExtra("name",String.valueOf(name.get(position)));
                intent.putExtra("childid",String.valueOf(ids.get(position)));
                intent.putExtra("parentID",parentID);
                intent.putExtra("parentPoints",points);
                startActivity(intent);
            }
        });
    }
}
