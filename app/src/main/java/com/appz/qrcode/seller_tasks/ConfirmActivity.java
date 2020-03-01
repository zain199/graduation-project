package com.appz.qrcode.seller_tasks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appz.qrcode.R;
import com.appz.qrcode.helperUi.AllFinal;
import com.appz.qrcode.seller_tasks.adapters.ConfirmAdpter;
import com.appz.qrcode.seller_tasks.adapters.ConfirmOnCloseListener;
import com.appz.qrcode.seller_tasks.models.ChartItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity {

    // ui
    private RecyclerView rec_orders;
    private TextView txt_all_points;


    // var
    private double all_points = 0;
    private List<ChartItem> chartItemList;
    private ConfirmAdpter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

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
        txt_all_points.setText(new DecimalFormat("##.##").format(all_points));

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
                Toast.makeText(ConfirmActivity.this, points + "order canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void confirm(View view) {
        if (all_points <= 0) {
            Toast.makeText(this, "no item founded select item and try again", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Confirm successed ", Toast.LENGTH_SHORT).show();

        onBackPressed();
    }
}
