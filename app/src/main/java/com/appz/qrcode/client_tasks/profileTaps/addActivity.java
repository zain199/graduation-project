package com.appz.qrcode.client_tasks.profileTaps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.helperUi.NoInternet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addActivity extends AppCompatActivity {

    private final DatabaseReference rationTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.Ration_Data);
    private final DatabaseReference fakeTable = FirebaseDatabase.getInstance().getReference().child(AllFinal.FAKE_DATA);
    // ui
    Button add;
    EditText id, name;
    //firebase
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser CurrentUser;
    //vars
    int points;
    String Name;
    String parent, ParentID;
    List ids = new ArrayList();
    List correctIds = new ArrayList();
    private ImageButton btn_create_qrcode;
    private DatabaseReference ownerId;

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        init();
        findByID();
        addChild();

    }

    private void init() {
        parent = getIntent().getStringExtra("id");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(AllFinal.Ration_Data);
        auth = FirebaseAuth.getInstance();

        CurrentUser = auth.getCurrentUser();
        ownerId = rationTable.child(parent).child("uid");
        getdata(ref.child(parent).child("points"));
        getIDs(rationTable.child(parent));
        getCorrectIDs(fakeTable.child(parent).child(AllFinal.CHILDS));
        getOnwerUid(ownerId);
    }

    private void findByID() {
        add = findViewById(R.id.btn_add);
        id = findViewById(R.id.ed_id);
        btn_create_qrcode = findViewById(R.id.btn_selectImg_qrcode_member);

        selectAndDetectImgForeQrCode();
    }

    private void selectAndDetectImgForeQrCode() {

//        btn_create_qrcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 30);
//
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 30) {
            Uri chosenImageUri = data.getData();


            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                recognizeText(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private void recognizeText(Bitmap bitmap) {
        //تحويل بيتماب الى بيتماب قابل للتعديل
        final Bitmap mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(mBitmap);
        //الحصول على الكود
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        // انتهى بنجاح
//                        Paint redPaint = getPaint(Color.RED, .5f);
//                        Paint blackPaint = getPaint(Color.BLACK, .5f);
//                        Paint bluePaint = getPaint(Color.CYAN, .5f);
                        String text = "";

                        //تجلب البلوكس
                        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                            //احدثيات النص
                            Rect boundingBox = block.getBoundingBox();
                            //Padding


                            boundingBox.top = boundingBox.top - 5;
                            boundingBox.bottom = boundingBox.bottom + 5;
                            //رسم مستطيل احمر حول النص
                            //canvas.drawRect(boundingBox,redPaint);
                            //تجلب الاسطر داخل كل بلوك
                            for (FirebaseVisionText.Line line : block.getLines()) {
                                //اخد كل سطر و الانتقال للسطر التالي
                                text += line.getText() + "\n";
                                //Padding
                                line.getBoundingBox().top = line.getBoundingBox().top - 2;
                                line.getBoundingBox().bottom = line.getBoundingBox().bottom + 2;
                                line.getBoundingBox().right = line.getBoundingBox().right + 2;
                                //رسم مستطيل اسود حول كل سطر
                                // canvas.drawRect(line.getBoundingBox(), blackPaint);
                                //تجلب العناصر او الكلمات داخل كل سطر
                                for (FirebaseVisionText.Element element : line.getElements()) {
                                    //رسم مستطيل ازرق حول كل كلمة
                                    // canvas.drawRect(element.getBoundingBox(), bluePaint);
                                }
                            }
                        }
                        //اضافة النص للعنصر
                        String[] split1 = text.split("\n");

                        for (String s : split1) {
                            Log.d("wwwwwwww111", s);
                        }
                        if (split1.length <= 0 || split1.length < 3) {
                            Toast.makeText(getApplicationContext(), "failed image change image and try again", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String textwithid = split1[1];
                        String[] split2 = textwithid.split(" ");
                        if (split2.length <= 0 || split2.length < 2) {
                            Toast.makeText(getApplicationContext(), "failed image change image and try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (String s : split1) {
                            Log.d("wwwwwwww1", s);
                        }
                        for (String s : split2) {
                            Log.d("wwwwwwww2", s);
                        }
                        Log.d("wwwwwwwwllllllll", split2[1].length() + " ");
                        if (!isNumeric(split2[1]) || split2[1].length() != 17) {
                            Toast.makeText(getApplicationContext(), "failed image change image and try again", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        name.setText(split1[3]);
                        id.setText(split2[1]);
                        //وضع الصورة على العنصر
                        // imageView.setImageBitmap(mBitmap);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // خطا ما وقع
                Toast.makeText(getApplicationContext(), "Sorry, something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getdata(DatabaseReference reff) {
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

    private void addChild() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString().trim();
                if (checkInternetConnection()) {

                    if (CurrentUser.getUid().equals(ParentID)) {

                        if (isCorrect(ID)) {
                            if (isAlreadyExist(ID)) {
                                Toast.makeText(getBaseContext(), "This ID is Already Exist", Toast.LENGTH_LONG).show();
                            } else {
                                points += 50;
                                getAndSetNameAndID(fakeTable, rationTable, ID);
                                ref.child(parent).child("points").setValue(points);
                                Toast.makeText(getBaseContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                                id.setText("");
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Enter Correct ID", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Operation Failed", Toast.LENGTH_LONG).show();
                    }
                } else
                    startActivity(new Intent(addActivity.this, NoInternet.class));
            }
        });
    }

    private void getAndSetNameAndID(DatabaseReference ref, final DatabaseReference ref1, final String id) {
        Name = "";
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

    private void getIDs(DatabaseReference ref) {
        ids.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ids.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isAlreadyExist(String id) {

        for (int i = 0; i < ids.size(); ++i) {
            if (ids.get(i).equals(id))
                return true;
        }
        return false;
    }

    private void getCorrectIDs(DatabaseReference ref) {

        correctIds.clear();
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    correctIds.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private boolean isCorrect(String id) {
        for (int i = 0; i < correctIds.size(); ++i) {
            if (correctIds.get(i).equals(id))
                return true;
        }
        return false;
    }

    private Boolean checkInternetConnection() {
        Boolean internetConnection = false;
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();

        if (connection != null) {
            if (connection.getType() == ConnectivityManager.TYPE_WIFI)
                return internetConnection = true;
            else if (connection.getType() == ConnectivityManager.TYPE_MOBILE)
                return internetConnection = true;
            else
                return internetConnection = false;
        }

        return internetConnection;
    }
}
