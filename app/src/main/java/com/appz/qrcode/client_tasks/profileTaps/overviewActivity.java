package com.appz.qrcode.client_tasks.profileTaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class overviewActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    ArrayList<Map<String,String>> childData ;
    List id = new ArrayList();
    List name = new ArrayList();
    adapter adapter ;
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
       // getchildData();
       // adapter.notifyDataSetChanged();

    }

    private void init()
    {
       /* childData = new ArrayList<>();
        adapter = new adapter(getApplicationContext(),childData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));*/

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);

        parentID = getIntent().getStringExtra("id");
        Toast.makeText(getApplicationContext(),""+parentID , Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getPoints(ref.child(parentID).child("points"));
                Toast.makeText(getApplicationContext(),""+points , Toast.LENGTH_LONG).show();
                getParentName(ref.child(parentID).child("name"));
                Toast.makeText(getApplicationContext(),""+parentName , Toast.LENGTH_LONG).show();

                //getIDs(rationTable.child(parentID));

                //getNames(id);
            }
        },1000);

       // setParent();
    }

    private void findbyid()
    {
        recyclerView = findViewById(R.id.recyclerView);
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

    private void getchildData()
    {
        for (int i=0 ; i<id.size() ; i++)
        {
            Map<String , String> temp = new HashMap<>();
            temp.put("childID", (String) id.get(i));
            temp.put("childName", (String) name.get(i));
            childData.add(temp);
        }
    }

    private void setParent()
    {
        name_parent.setText(parentName);
        parent_id.setText(parentID);
        parent_points.setText(points);
    }

}
