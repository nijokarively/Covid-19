package com.nkv.covid.ui.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nkv.covid.MainActivity;
import com.nkv.covid.R;

import java.util.Objects;

public class AboutFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_bar_3));

        final TextView textResources = view.findViewById(R.id.textResources);
        final TextView textResourcesWho = view.findViewById(R.id.textResourcesWho);
        final TextView textResourcesCdc = view.findViewById(R.id.textResourcesCdc);
        final TextView textResourcesNhs = view.findViewById(R.id.textResourcesNhs);
        textResources.setText(R.string.resources);
        textResourcesWho.setText(R.string.resources_who);
        textResourcesCdc.setText(R.string.resources_cdc);
        textResourcesNhs.setText(R.string.resources_nhs);
        textResourcesWho.setMovementMethod(LinkMovementMethod.getInstance());
        textResourcesCdc.setMovementMethod(LinkMovementMethod.getInstance());
        textResourcesNhs.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView textCredits = view.findViewById(R.id.textCredits);
        final TextView textCreditsData = view.findViewById(R.id.textCreditsData);
        final TextView textCreditsFlags = view.findViewById(R.id.textCreditsFlags);
        final TextView textCreditsEmojis = view.findViewById(R.id.textCreditsEmojis);
        final TextView textCreditsIcons = view.findViewById(R.id.textCreditsIcons);
        textCredits.setText(R.string.credits);
        textCreditsData.setText(R.string.credits_data);
        textCreditsFlags.setText(R.string.credits_flags);
        textCreditsEmojis.setText(R.string.credits_emojis);
        textCreditsIcons.setText(R.string.credits_icons);

    }
}