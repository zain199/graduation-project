package com.appz.qrcode.seller_tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.seller_tasks.adapters.StoreAdapter;
import com.appz.qrcode.seller_tasks.adapters.StoreOnClickItem;
import com.appz.qrcode.seller_tasks.models.ChartItem;
import com.appz.qrcode.seller_tasks.models.ItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity implements StoreOnClickItem {
    // ui
    private RecyclerView recyclerView_store;
    private TextView txt_num_item, txt_all_points;
    private ProgressDialog progressDialog;


    // var
    public static List<ItemModel> itemModelList;
    private StoreAdapter adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    public static Map<String, ChartItem> chartItemList;
    private double point = 0;
    private int item = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        reference = database.getReference(AllFinal.STORE_ITEMS);
        buildUI();

    }


    private void getDataOfRecycle() {
        showProgress(true);
        reference.child(mAuth.getCurrentUser().getUid())
                .child(AllFinal.ITEMS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    showProgress(false);
                    Toast.makeText(StoreActivity.this, "no item in a store try again later", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemModel itemModel = snapshot.getValue(ItemModel.class);
                    itemModel.setId(snapshot.getKey());
                    itemModelList.add(itemModel);
                }
                adapter.setModel(itemModelList);
                recyclerView_store.setAdapter(adapter);

                showProgress(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void buildUI() {
        progressDialog = new ProgressDialog(StoreActivity.this);
        progressDialog.setMessage("Wait Please");

        txt_all_points = findViewById(R.id.txt_item_price);
        txt_num_item = findViewById(R.id.txt_item_num);

        recyclerView_store = findViewById(R.id.rec_store);
        recyclerView_store.setHasFixedSize(true);
        recyclerView_store.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        itemModelList = new ArrayList<>();
        adapter = new StoreAdapter();
        adapter.onClickItem(this);

        chartItemList = new HashMap<>();

        getDataOfRecycle();
    }

    public void gotoChart(View view) {
        Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
        intent.putExtra(AllFinal.ALL_POINT,point );
        startActivity(intent);


    }

    private void showProgress(boolean s) {
        if (s == true) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClickPlus(final int pos, TextView view, TextView v2) {
        int it = Integer.parseInt(v2.getText().toString());
        if (it <= 0) {
            Toast.makeText(this, "sorry " + itemModelList.get(pos).getName() + " not avaliable", Toast.LENGTH_SHORT).show();
            return;
        }

        int a = Integer.parseInt(view.getText().toString());
        a++;

        view.setText(a + "");
        item++;
        txt_num_item.setText(item + "");
        double d = itemModelList.get(pos).getPoint();
        point += d;
        txt_all_points.setText(point + " point");
        it--;
        v2.setText(it + "");
        ChartItem chartItem = new ChartItem(itemModelList.get(pos).getImg_url(),
                itemModelList.get(pos).getId(), itemModelList.get(pos).getName(), a, (a * d));


        chartItemList.put(chartItem.getId(), chartItem);


    }

    @Override
    public void onClickMinus(final int pos, TextView view, TextView v2) {
        final int y = itemModelList.get(pos).getNumber_units();
        int it = Integer.parseInt(v2.getText().toString());
        int a = Integer.parseInt(view.getText().toString());
        a--;
        if (a < 0) {
            a = 0;
            if (chartItemList.containsKey(itemModelList.get(pos).getId())) {
                chartItemList.remove(itemModelList.get(pos).getId());
            }
            return;

        }


        view.setText(a + "");
        item--;
        if (item < 0)
            item = 0;
        txt_num_item.setText(item + "");
        double d = itemModelList.get(pos).getPoint();
        point -= d;
        if (point < 0)
            point = 0.0;
        txt_all_points.setText(point + " point");
        it++;
        if (it > y) {
            it = y;
        }
        v2.setText(it + "");

        ChartItem chartItem = new ChartItem(itemModelList.get(pos).getImg_url(),
                itemModelList.get(pos).getId(), itemModelList.get(pos).getName(), a, (a * d));
        chartItemList.put(chartItem.getId(), chartItem);

    }
}
