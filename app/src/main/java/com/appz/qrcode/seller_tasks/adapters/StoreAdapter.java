package com.appz.qrcode.seller_tasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appz.qrcode.R;
import com.appz.qrcode.seller_tasks.models.ItemModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.VH> {
    private List<ItemModel> itemModels;
    private StoreOnClickItem clickItem;


    public void setModel(List<ItemModel>itemModels)
    {
        this.itemModels=itemModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);
        return new VH(view);
    }

    public void onClickItem(StoreOnClickItem clickItem) {
        this.clickItem = clickItem;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.fillData(itemModels.get(position));

    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView img_store_item;
        private TextView name_store, all_num_units, point_store,num;
        private ImageButton btn_minis, btn_plus;
        private View view;



        public VH(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            buildView(itemView);
        }

        void fillData(ItemModel model) {
            name_store.setText(model.getName());
            all_num_units.setText(model.getNumber_units() + "");
            point_store.setText(model.getPoint() + "");

            if (model.getImg_url() == "") {
                Glide.with(view.getContext())
                        .load(view.getResources().getDrawable(R.drawable.img_no))
                        .into(img_store_item);
            } else {
                Glide.with(view.getContext())
                        .load(model.getImg_url())
                        .into(img_store_item);
            }


        }

        private void buildView(View v) {
            img_store_item = v.findViewById(R.id.img_store);
            name_store = v.findViewById(R.id.txt_name_store);
            all_num_units = v.findViewById(R.id.txt_all_number_store);
            point_store = v.findViewById(R.id.txt_point_store);
            num=v.findViewById(R.id.txt_store_n);


            btn_minis = v.findViewById(R.id.btn_minus);
            btn_plus = v.findViewById(R.id.btn_plus);

            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickItem != null) {
                        clickItem.onClickPlus(getAdapterPosition(), num,all_num_units);
                    }

                }
            });
            btn_minis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickItem != null) {
                        clickItem.onClickMinus(getAdapterPosition(), num,all_num_units);
                    }
                }
            });

        }


    }
}
