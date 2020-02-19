package com.appz.qrcode.review_tasks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appz.qrcode.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class rationServiceReviewActivity extends AppCompatActivity {


    //TODO ezzat
    // دي الاكتفتي اللى هتااخد من الفايربيز البيانات بتاعت البطاقة و تحطها بيانات زي عدد الافراد مين هم ايه ال balance الحالي و كدا


    // var
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference data=database.getReference().child("clients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ration_service_review);
    }
}
