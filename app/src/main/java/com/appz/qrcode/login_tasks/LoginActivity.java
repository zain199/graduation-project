package com.appz.qrcode.login_tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appz.qrcode.R;
import com.appz.qrcode.customerActivity;
import com.appz.qrcode.delperUi.AllFinal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    // ui
    private EditText  ed_email, ed_password;
    private ProgressDialog progressDialog;


    // var
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference(AllFinal.ROOT_LOGIN_FIRE);
    private FirebaseAuth mAuth;
    private String email;
    private String password;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buildView();


    }

    @Override
    public void onStart() {
        super.onStart();


        if (mAuth.getCurrentUser()!=null)
        {

            Intent intent = new Intent(LoginActivity.this, customerActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private void showProgress(boolean s) {
        if (s == true) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    private Boolean checkEditedText() {
        boolean s1 = false;
        boolean s2 = false;
        email = ed_email.getText().toString().trim();
        password = ed_password.getText().toString().trim();

        if (Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), email)) {
            s1 = true;

        } else {
            s1 = false;
            ed_email.setError( "write valid email !");
            return false;
        }
        if (password.length() < 6) {
            ed_password.setError("write valid password minmum 6 letters");
            s2 = false;
        } else {
            s2 = true;
        }
        return s1 && s2;

    }
    private void buildView() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Wait Please");
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_pass);
        mAuth = FirebaseAuth.getInstance();


    }








    public void login(View view) {

        if (checkEditedText())
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(LoginActivity.this, customerActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(AllFinal.TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });



        }


    }



    public void regisiter(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterionActivity.class);
        startActivity(intent);

    }
}
