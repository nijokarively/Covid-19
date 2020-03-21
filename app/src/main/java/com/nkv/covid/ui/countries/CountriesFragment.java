package com.nkv.covid.ui.countries;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nkv.covid.BaseActivity;
import com.nkv.covid.MainActivity;
import com.nkv.covid.R;
import com.nkv.covid.adapter.CountryCardAdapter;
import com.nkv.covid.model.CountryCardModel;

import java.util.Objects;


public class CountriesFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CountryCardAdapter mAdapter;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_countries, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_bar_2));

        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) Objects.requireNonNull(getActivity())).fetchCountriesData();
                searchView.setQuery("", false);
                searchView.clearFocus();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        try {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
            searchView =
                    (SearchView) menu.findItem(R.id.action_search).getActionView();
            assert searchManager != null;
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    // do your search
                    mAdapter = ((MainActivity) Objects.requireNonNull(getActivity())).getCountryAdapter();
                    mAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    mAdapter = ((MainActivity) Objects.requireNonNull(getActivity())).getCountryAdapter();
                    mAdapter.getFilter().filter(s);
                    return false;
                }
            });


        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void onResume(){
        super.onResume();
        //OnResume Fragment
        try{
            CountryCardModel[] myListData = ((MainActivity) Objects.requireNonNull(getActivity())).getCountriesSavedData();

            if (myListData == null)
            {
                ((MainActivity) getActivity()).fetchCountriesData();
            }else{
                ((MainActivity) getActivity()).outputCountriesData(myListData);
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed loading data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
    }
}