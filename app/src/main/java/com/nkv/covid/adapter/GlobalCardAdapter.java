package com.nkv.covid.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nkv.covid.R;
import com.nkv.covid.model.GlobalCardModel;

public class GlobalCardAdapter extends RecyclerView.Adapter<GlobalCardAdapter.ViewHolder>{
    private GlobalCardModel[] listdata;

    // RecyclerView recyclerView;
    public GlobalCardAdapter(GlobalCardModel[] listdata) {
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.global_cards_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GlobalCardModel myListData = listdata[position];
        holder.textViewName.setText(listdata[position].getName());
        holder.textViewValue.setText(listdata[position].getValue());
        holder.imageView.setImageResource(listdata[position].getImgId());
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+myListData.getName(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName,textViewValue;
        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewValue = (TextView) itemView.findViewById(R.id.textViewValue);
        }
    }
}

