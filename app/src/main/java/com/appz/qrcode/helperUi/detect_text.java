package com.appz.qrcode.helperUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appz.qrcode.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class detect_text extends AppCompatActivity {

    //UI
    ImageButton imageButton;
    ImageView imageView;
    EditText ed_id,ed_name , ed_address , ed_phone , ed_email , ed_password;
    Button add;


    // variables
    private static final int CAMERA_CODE=200;
    private static final int STORAGE_CODE=400;
    private static final int PICK_CAMERA_CODE=1001;
    private static final int PICK_STORAGE_CODE=1000;
    private Uri image_uri;
    private DatabaseReference ref,ref1;
    private FirebaseUser CurrentUser;
    private boolean exist = false,generated = false;
    private String id ,name ;

    // arrays
    String [] CAMERA_PERMISSION;
    String [] STORAGE_PERMISSSION;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_text);

        connectDB();
        findByid();
        SetPermisssions();
        uploadAction();
        generateQR();


    }

    private void connectDB()
    {
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);
        ref1 = FirebaseDatabase.getInstance().getReference().child(AllFinal.Generated);

    }

    private void SetPermisssions()
    {
        CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        STORAGE_PERMISSSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void findByid()
    {
        imageButton = findViewById(R.id.selectImg);
        imageView = findViewById(R.id.OutImg);
        add= findViewById(R.id.btn_add_all);
        ed_id = findViewById(R.id.ed_id);
        ed_name = findViewById(R.id.ed_name);
    }

    private void showDialog()
    {
        String [] options = {"CAMERA" , "GALLERY"};
        AlertDialog.Builder  builder = new AlertDialog.Builder(detect_text.this);


        builder.setTitle("upload image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i==0)
                        {
                            if(!checkCameraPermission())
                                requestCameraPermission();
                            else
                                pickCamera();

                        }
                        else if(i==1)
                        {
                            if(!checkStoragePermission())
                                requestStoragePermission();
                            else
                                pickGallery();
                        }

                    }
                })
                .create().show();
    }

    private void msgDialog(String s)
    {
        AlertDialog.Builder  builder = new AlertDialog.Builder(detect_text.this);


        builder.setMessage(s);

        builder.create();


    }

    private void pickGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_STORAGE_CODE);
    }

    private void pickCamera()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE , "new Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION , "image to text");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , values);

        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam.putExtra(MediaStore.EXTRA_OUTPUT , image_uri);
        startActivityForResult(cam ,PICK_CAMERA_CODE);
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(detect_text.this ,CAMERA_PERMISSION ,CAMERA_CODE);
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(detect_text.this ,STORAGE_PERMISSSION ,STORAGE_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean r1 = ContextCompat.checkSelfPermission(detect_text.this , Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean r2 = ContextCompat.checkSelfPermission(detect_text.this , Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  r1&&r2;
    }

    private boolean checkStoragePermission()
    {
        boolean r = ContextCompat.checkSelfPermission(detect_text.this , Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  r;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_CODE:
                if(grantResults.length>0)
                {
                    boolean cam = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean storage = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                    if(cam && storage)
                        pickCamera();
                    else
                        Toast.makeText(detect_text.this , "Permission Denied" ,Toast.LENGTH_SHORT ).show();
                }

                break;

            case STORAGE_CODE:
                if(grantResults.length>0)
                {
                    boolean storage = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                    if(storage)
                        pickGallery();
                    else
                        Toast.makeText(detect_text.this , "Permission Denied" ,Toast.LENGTH_SHORT ).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_STORAGE_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(detect_text.this);
            } else if (requestCode == PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(detect_text.this);
            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = activityResult.getUri();

                imageView.setImageURI(resultUri);


                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap =  bitmapDrawable.getBitmap();

                TextRecognizer textRecognizer = new TextRecognizer.Builder(
                        getApplicationContext()).build();

                if (!textRecognizer.isOperational())
                    Toast.makeText(detect_text.this, "Error", Toast.LENGTH_SHORT).show();
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);



                    StringBuilder sb = new StringBuilder();


                    if(items.size()==0)
                        Toast.makeText(detect_text.this , "no text found" , Toast.LENGTH_SHORT).show();
                    else
                    {
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock textBlock = items.valueAt(i);
                            sb.append(textBlock.getValue());
                            //sb.append("\n");
                        }

                        String text = sb.toString().trim();
                        String temp ="";

                        // id text
                        for(int i = 0 ; i < text.length()-2;++i)
                        {

                            if(text.charAt(i)=='I'&&text.charAt(i+1)=='D'&&text.charAt(i+2)==' ')
                            {
                                int x = i+3;
                                for (int b = i+3;b-x<17;b++) {
                                    temp+=text.charAt(b);
                                }

                                break;
                            }
                        }
                        ed_id.setText(temp);



                        // name text
                        temp ="";
                        for(int i = 0 ; i < text.length()-3;++i)
                        {

                            if(text.charAt(i)=='N'&&text.charAt(i+1)=='a'&&text.charAt(i+2)=='m'&&text.charAt(i+3)=='e')
                            {

                                for (int b = i+4;;b++) {

                                    if(text.charAt(b)=='A'&&text.charAt(b+1)=='d'&&text.charAt(b+2)=='d'&&
                                            text.charAt(b+3)=='r'&&text.charAt(b+4)=='e'&&text.charAt(b+5)=='s'&& text.charAt(b+6)=='s')
                                        break;
                                    temp+=text.charAt(b);
                                }

                                break;
                            }
                        }
                        ed_name.setText(temp);


                    }

                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception exception = activityResult.getError();
                Toast.makeText(detect_text.this , ""+exception , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateQR()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = ed_id.getText().toString().trim();
                name = ed_name.getText().toString().trim();

                if(isGenerated(ref1))
                {
                    Toast.makeText(getBaseContext(),"You Can Generate one Qr Code",Toast.LENGTH_LONG).show();
                    generated=false;

                }else
                {

                    if(isAlreadyExist(ref))
                    {
                        Toast.makeText(getBaseContext(),"This ID is Already Generated Before",Toast.LENGTH_LONG).show();
                        exist = false ;
                    }else
                    {
                        ref.child(id).child("name").setValue(name);
                        ref.child(id).child("uid").setValue(CurrentUser.getUid());
                        ref.child(id).child("points").setValue(50);
                        ref1.child(CurrentUser.getUid()).child("Active").setValue(1);

                        Intent intent = new Intent(detect_text.this,QrActivity.class);
                        intent.putExtra("idCard",id);
                        startActivity(intent);
                    }

                }

            }
        });
    }

    private void uploadAction()
    {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.setImageURI(null);

                showDialog();
            }
        });
    }


    private boolean isAlreadyExist (DatabaseReference ref)
    {


        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              for(DataSnapshot data : dataSnapshot.getChildren())
              {
                  exist = data.getKey().equals(id);
                  if(exist)break;

              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        return exist ;

    }

    private boolean isGenerated (DatabaseReference ref)
    {


        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    generated = data.getKey().equals(CurrentUser.getUid());
                    if(generated)break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        return generated ;

    }

}
