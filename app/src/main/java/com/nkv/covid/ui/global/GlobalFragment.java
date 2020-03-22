package com.nkv.covid.ui.global;

import android.os.Bundle;
import android.os.Handler;
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
    private Handler mHandler = new Handler();
    private Handler rHandler = new Handler();
    private Runnable rRunnable;
    private final int DATA_REFRESH_LENGTH = 300000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_global, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_bar_1));

        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
//                ((MainActivity) Objects.requireNonNull(getActivity())).reloadGlobalData();
//
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 3000);
            }
        });

        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.navBarIconActive);

        // Refresh data
        rHandler.postDelayed(rRunnable = new Runnable() {
            @Override
            public void run() {
                //Do something after 300 seconds
                ((MainActivity) Objects.requireNonNull(getActivity())).reloadGlobalData();
                Toast.makeText(getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
                rHandler.postDelayed(this, DATA_REFRESH_LENGTH);
            }
        }, DATA_REFRESH_LENGTH);  //the time is in miliseconds

    }

    @Override
    public void onResume(){
        super.onResume();
        //OnResume Fragment
        try{

            GlobalCardModel[] myListData = ((MainActivity) Objects.requireNonNull(getActivity())).getGlobalSavedData();

            if (myListData == null)
            {
                ((MainActivity) getActivity()).reloadGlobalData();
            }else{
                ((MainActivity) getActivity()).outputGlobalData(myListData);
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Oop! Can't load data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy () {
        try {
            rHandler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy ();
    }

    private void refresh(){
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) Objects.requireNonNull(getActivity())).reloadGlobalData();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

}