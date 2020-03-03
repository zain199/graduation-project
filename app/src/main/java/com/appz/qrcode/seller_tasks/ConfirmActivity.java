package com.appz.qrcode.seller_tasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.seller_tasks.adapters.ConfirmAdpter;
import com.appz.qrcode.seller_tasks.adapters.ConfirmOnCloseListener;
import com.appz.qrcode.seller_tasks.models.ChartItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConfirmActivity extends AppCompatActivity {

    // ui
    private RecyclerView rec_orders;
    private TextView txt_all_points;


    // var
    private double all_points = 0;
    private List<ChartItem> chartItemList;
    private ConfirmAdpter adpter;
    public String cllient;
    public double clientPoints_before;
    private double points_after_sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
       //////////////////////////////////////////////
        Intent intent =getIntent();
        cllient=intent.getStringExtra("clientt_id");
        clientPoints_before=intent.getDoubleExtra("client_points_before_sell",0);

       //////////////////////////////////////////////
        buildConfirm();

    }

    private void buildConfirm() {
        chartItemList = new ArrayList<>();

        if (getIntent().hasExtra(AllFinal.ALL_POINT)) {
            all_points = getIntent().getDoubleExtra(AllFinal.ALL_POINT, 0);

            if (all_points <= 0) {
                Toast.makeText(this, "select item first and try again", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }


        }
        for (String k : StoreActivity.chartItemList.keySet()) {

            chartItemList.add(StoreActivity.chartItemList.get(k));
        }
        txt_all_points = findViewById(R.id.txt_confirm_item_price);
        txt_all_points.setText(new DecimalFormat("##.##").format(all_points) + " point");

        buildRec();
    }

    private void buildRec() {

        rec_orders = findViewById(R.id.rec_order);
        rec_orders.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rec_orders.setHasFixedSize(true);
        adpter = new ConfirmAdpter();

        adpter.addList(chartItemList);
        rec_orders.setAdapter(adpter);
        adpter.onClosrListerner(new ConfirmOnCloseListener() {
            @Override
            public void onClose(double points) {
                all_points -= points;
                if (all_points <= 0) {
                    all_points = 0;
                }
                txt_all_points.setText(new DecimalFormat("##.##").format(all_points) + " point");
                Toast.makeText(ConfirmActivity.this, points + " order canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void confirm(View view) {
        if (all_points <= 0) {
            Toast.makeText(this, "no item founded select item and try again", Toast.LENGTH_SHORT).show();
            return;
        }
        points_after_sell=clientPoints_before-all_points;
        Toast.makeText(this, "Confirm successed your points is : "+points_after_sell, Toast.LENGTH_SHORT).show();
        ReduceClientPoints();
    }


   public void ReduceClientPoints(){
       DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Ration_Data")
               .child(cllient).child("points");
        reference.setValue(points_after_sell);
   }

   public  void ReduceItemNum(){
        for (int i=0;i<chartItemList.size();i++){
            Log.d("omar",chartItemList.get(i).getId());
            Log.d("omar",chartItemList.get(i).getName());
            Log.d("omar",String.valueOf(chartItemList.get(i).getPoint()));
        }

   }

}














/*for (int i=0;i<=chartItemList.size();i++){
    Log.d("omar",chartItemList.get(i).getId());
    Log.d("omar",chartItemList.get(i).getName());
    Log.d("omar",String.valueOf(chartItemList.get(i).getPoint()));

}
*/