package com.nkv.covid.ui.global;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nkv.covid.MainActivity;
import com.nkv.covid.R;
import com.nkv.covid.model.GlobalCardModel;

import java.util.Objects;

public class GlobalFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_global, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
//        String cases, deaths, recovered;

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_bar_1));

        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) Objects.requireNonNull(getActivity())).fetchGlobalData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//        ((MainActivity) getActivity()).fetchGlobalData(view);
        try{
            GlobalCardModel[] myListData = ((MainActivity) getActivity()).getGlobalSavedData();

            if (myListData == null)
            {
                ((MainActivity) getActivity()).fetchGlobalData();
            }else{
                ((MainActivity) getActivity()).outputGlobalData(myListData);
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed loading data!", Toast.LENGTH_SHORT).show();
        }

    }

}