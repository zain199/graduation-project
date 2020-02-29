package com.appz.qrcode.client_tasks.profileTaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.appz.qrcode.R;
import com.appz.qrcode.client_tasks.profileTaps.adapter.adapter;
import com.appz.qrcode.helperUi.AllFinal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class overviewActivity extends AppCompatActivity {
    ListView childid , childname;
    List id = new ArrayList();
    List name = new ArrayList();
    TextView name_parent ;
    EditText parent_id , parent_points;

    //database
    private FirebaseDatabase database ;
    private DatabaseReference ref ;

    private final DatabaseReference rationTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);

    //var
    String parentID , parentName;
    int points ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        findbyid();
        init();

    }

    private void init()
    {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);

        parentID = getIntent().getStringExtra("id");


        getPoints(ref.child(parentID).child("points"));


        getParentName(ref.child(parentID).child("name"));


        getIDs(rationTable.child(parentID));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNames(id);
            }
        },1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setParent();
            }
        },1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getchildID();
            }
        },1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getchildname();
            }
        },1000);
    }

    private void findbyid()
    {
        childid = findViewById(R.id.listID);
        childname = findViewById(R.id.listName);
        name_parent = findViewById(R.id.parentName);
        parent_id = findViewById(R.id.parentIDet);
        parent_points = findViewById(R.id.parentPointset);

    }

    private void getIDs (DatabaseReference ref)
    {
        id.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(Character.isDigit(data.getKey().charAt(0)))
                    {
                        id.add(data.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
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

    private void getPoints(DatabaseReference reff)
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

    private void getParentName(DatabaseReference reff)
    {
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parentName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getchildID()
    {
        ArrayAdapter Adapter = new ArrayAdapter(getApplicationContext() ,android.R.layout.simple_list_item_1 ,id);
        childid.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
    }

    private void getchildname()
    {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext() ,android.R.layout.simple_list_item_1 ,name);
        childname.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private void setParent()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                name_parent.setText(parentName);
            }
        },1000);

        parent_id.setText(parentID);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                parent_points.setText(String.valueOf(points));
            }
        },1000);
    }
}
