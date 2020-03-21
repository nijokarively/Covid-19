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
                ((MainActivity) Objects.requireNonNull(getActivity())).reloadGlobalData();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.navBarIconActive);

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
            Toast.makeText(getContext(), "Oops can't load data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
    }

}