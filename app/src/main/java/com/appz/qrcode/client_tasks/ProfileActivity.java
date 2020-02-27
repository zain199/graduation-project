package com.appz.qrcode.client_tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appz.qrcode.R;
import com.appz.qrcode.client_tasks.profileTaps.addActivity;
import com.appz.qrcode.client_tasks.profileTaps.deleteActivity;
import com.appz.qrcode.client_tasks.profileTaps.overviewActivity;
import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.helperUi.QrActivity;
import com.appz.qrcode.helperUi.detect_text;
import com.appz.qrcode.login_tasks.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    Button add , delete , overview , restore ;
    FirebaseUser CurrentUser= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(AllFinal.Generated);
    Boolean ok = false;
    List ids = new ArrayList();
    String id ;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        findByID();
        add();
        delete();
        overview();
        restoreQR();





    }

    private void init()
    {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        getids(reference);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isGenerated(ids);
            }
        },1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ok)
                {
                    getParentId(reference.child(CurrentUser.getUid()));
                }
            }
        },1000);







    }

    private void isGenerated(List ids) {

        for(int i = 0 ; i < ids.size();++i)
        {
            if(ids.get(i).equals(CurrentUser.getUid()))
            {
                ok = true;
                break;
            }
        }
    }

    private void getids(DatabaseReference ref) {
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

            }
        });

    }

    private void findByID()
    {
        add = findViewById(R.id.addchild);
        delete = findViewById(R.id.delete);
        overview = findViewById(R.id.view);
        restore = findViewById(R.id.restoreQR);

    }

    private void add()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ok)
                {

                    progressDialog.setMessage("Please Wait ...");
                    progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent =  new Intent(ProfileActivity.this , addActivity.class);
                                    intent.putExtra("id",id);
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                }
                            },1000);





                }

                else
                    Toast.makeText(getApplicationContext(),"You Don't Have A QR Code",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getParentId(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id = dataSnapshot.child("id").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void delete()
    {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ok)
                {
                    progressDialog.setMessage("Please Wait ...");
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent =  new Intent(ProfileActivity.this , deleteActivity.class);
                            intent.putExtra("id",id);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                    },1000);
                }

                else
                    Toast.makeText(getApplicationContext(),"You Don't Have A QR Code",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void overview()
    {
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ok)
                {
                    progressDialog.setMessage("Please Wait ...");
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent =  new Intent(ProfileActivity.this , overviewActivity.class);
                            intent.putExtra("id",id);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                    },1000);


                }
                else
                    Toast.makeText(getApplicationContext(),"You Don't Have A QR Code",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void restoreQR()
    {
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ok)
                {
                    progressDialog.setMessage("Please Wait ...");
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent =  new Intent(ProfileActivity.this , QrActivity.class);
                            intent.putExtra("idCard",id);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                    },500);
                }
                else
                    Toast.makeText(getApplicationContext(),"You Don't Have A QR Code",Toast.LENGTH_LONG).show();

            }
        });
    }


}
