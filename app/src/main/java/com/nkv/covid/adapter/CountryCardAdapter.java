package com.nkv.covid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nkv.covid.R;
import com.nkv.covid.model.CountryCardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CountryCardAdapter extends RecyclerView.Adapter<CountryCardAdapter.ViewHolder> implements Filterable {
    private CountryCardModel[] listdata;
    private CountryCardModel[] listdataFiltered;

    // RecyclerView recyclerView;
    public CountryCardAdapter(CountryCardModel[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.country_cards_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CountryCardModel myListData;
        try{
            if (listdataFiltered != null ){
                myListData = listdataFiltered[position];
            } else {
                myListData = listdata[position];
            }

            holder.textViewName.setText(myListData.getName());
            holder.textViewCases.setText(myListData.getCases());
            holder.textViewDeaths.setText(myListData.getDeaths());
            holder.textViewRecovered.setText(myListData.getRecovered());
            holder.imageView.setImageResource(myListData.getImgId());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        if (listdataFiltered != null ){
           return listdataFiltered.length;
        } else {
            return  listdata.length;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewName, textViewCases, textViewDeaths, textViewRecovered;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewCases = (TextView) itemView.findViewById(R.id.textViewCases);
            this.textViewDeaths = (TextView) itemView.findViewById(R.id.textViewDeaths);
            this.textViewRecovered = (TextView) itemView.findViewById(R.id.textViewRecovered);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                try {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        listdataFiltered = listdata;
                    } else {
                        List<CountryCardModel> filteredList = new ArrayList<>();
                        for (CountryCardModel row : listdata) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        listdataFiltered = new CountryCardModel[filteredList.size()];
                        for (int i = 0; i < filteredList.size(); i++) {
                            listdataFiltered[i] = filteredList.get(i);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listdataFiltered;
                    return filterResults;
                } catch (Exception e) {
                    e.printStackTrace();
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listdata;
                    return filterResults;
                }
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listdataFiltered = (CountryCardModel[]) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
