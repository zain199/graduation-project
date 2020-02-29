package com.appz.qrcode.client_tasks.profileTaps;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.helperUi.NoInternet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class addActivity extends AppCompatActivity {

    // ui
    Button add ;
    EditText id , name;

    //firebase
    FirebaseDatabase database ;
    DatabaseReference ref ;
    FirebaseAuth auth;
    FirebaseUser CurrentUser;
    private final DatabaseReference rationTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);
    private final DatabaseReference fakeTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.FAKE_DATA);
    private  DatabaseReference ownerId ;


    //vars
    int points ;
    String Name ;
    String parent , ParentID;
    List ids = new ArrayList();
    List correctIds = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        checkInternetConnection();
        init();
        findByID();
        addChild();

    }


    private void init()
    {
        parent= getIntent().getStringExtra("id");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);
        auth = FirebaseAuth.getInstance();

        CurrentUser= auth.getCurrentUser();
        ownerId = rationTable.child(parent).child("uid");
        getdata(ref.child(parent).child("points"));
        getIDs(rationTable.child(parent));
        getCorrectIDs(fakeTable.child(parent).child(AllFinal.CHILDS));
        getOnwerUid(ownerId);
    }

    private void findByID()
    {
        add = findViewById(R.id.btn_add);
        id = findViewById(R.id.ed_id);
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
                if(checkInternetConnection())
                {

                    if(CurrentUser.getUid().equals(ParentID))
                    {

                        if (isCorrect(ID))
                        {
                            if(isAlreadyExist(ID))
                            {
                                Toast.makeText(getBaseContext(),"This ID is Already Exist",Toast.LENGTH_LONG).show();
                            }else
                            {
                                points+=50;
                                getAndSetNameAndID(fakeTable,rationTable,ID);
                                ref.child(parent).child("points").setValue(points);
                                Toast.makeText(getBaseContext(),"Added Successfully",Toast.LENGTH_LONG).show();
                            }
                        }else
                        {
                            Toast.makeText(getBaseContext(),"Enter Correct ID",Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Operation Failed",Toast.LENGTH_LONG).show();
                    }
                }else
                    startActivity(new Intent(addActivity.this, NoInternet.class));
            }
        });
    }

    private void getAndSetNameAndID(DatabaseReference ref, final DatabaseReference ref1, final String id) {
        Name="";
        ref = ref.child(parent).child(AllFinal.CHILDS).child(id).child("name");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ref1.child(parent).child(id).child("Name").setValue(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void getOnwerUid(DatabaseReference ref) {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ParentID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private  Boolean checkInternetConnection()
    {
        Boolean internetConnection = false ;
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();

        if(connection!=null)
        {
            if(connection.getType()==ConnectivityManager.TYPE_WIFI)
                return internetConnection = true;
            else if (connection.getType()==ConnectivityManager.TYPE_MOBILE)
                return  internetConnection= true;
            else
                return  internetConnection= false;
        }

        return internetConnection;
    }
}
