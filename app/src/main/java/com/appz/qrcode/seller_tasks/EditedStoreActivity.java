package com.appz.qrcode.seller_tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appz.qrcode.R;

public class EditedStoreActivity extends AppCompatActivity {

    // TODO ezzat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_store);
    }

    public void addItem(View view) {
        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        intent.putExtra("bundle1", "");
        startActivity(intent);
    }

    public void showStore(View view) {
        startActivity(new Intent(getApplicationContext(), StoreActivity.class));
    }
}
