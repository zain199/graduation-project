package com.appz.qrcode.client_tasks.profileTaps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class overviewActivity extends AppCompatActivity {

    List id = new ArrayList();
    List name = new ArrayList();
    TextView name_parent ;
    EditText parent_id , parent_points;
    RecyclerView recyclerView;
    adapter adapter ;
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(overviewActivity.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);
        parentID = getIntent().getStringExtra("id");
        getIDs(rationTable.child(parentID));



    }

    private void findbyid()
    {
        recyclerView=findViewById(R.id.recyclerView);
        //childid = findViewById(R.id.listID);
        //childname = findViewById(R.id.listName);
        name_parent = findViewById(R.id.parentName);
        parent_id = findViewById(R.id.parentIDet);
        parent_points = findViewById(R.id.parentPointset);

    }

    private void getIDs (final DatabaseReference ref)
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
                getPoints(ref.child("points"));
                getParentName(ref.child("name"));
                getNames(id);
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

                        adapter = new adapter(getApplicationContext() , id , name);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    private void getPoints(final DatabaseReference reff)
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
                setParent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setParent()
    {

        name_parent.setText(parentName);
        parent_id.setText(parentID);
        parent_points.setText(String.valueOf(points));

    }
}
