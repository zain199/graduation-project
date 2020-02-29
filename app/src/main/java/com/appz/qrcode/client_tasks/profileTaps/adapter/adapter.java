package com.appz.qrcode.client_tasks.profileTaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;

import com.appz.qrcode.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adapter extends RecyclerView.Adapter<adapter.dataViewHolder> {

    private Context context;
    private List<Map<String , String>> data;
    LayoutInflater inflater ;

    public adapter(Context context, ArrayList data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public dataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_id , parent , false);
        dataViewHolder dataViewHolder = new dataViewHolder(v);
        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull dataViewHolder holder, int position) {
        Map<String, String> current = data.get(position);
        holder.childID.setText(current.get("childID"));
        holder.childName.setText(current.get("childName"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class dataViewHolder extends RecyclerView.ViewHolder
    {
        TextView childID , childName;
        public dataViewHolder(@NonNull View itemView) {
            super(itemView);
            childID = itemView.findViewById(R.id.childID);
            childName = itemView.findViewById(R.id.childName);
        }
    }
}
