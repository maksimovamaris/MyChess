package com.maksimovamaris.chess.view.players;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.presenter.AddGameView;

public class PlayerContainer extends Fragment {
    private PlayerAdapter playerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    protected AddGameView addGameView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddGameView) {
            addGameView = (AddGameView) context;
        } else {
            addGameView = (AddGameView) getParentFragment();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        playerAdapter = new PlayerAdapter(getChildFragmentManager(), 1);
        playerAdapter.notifyDataSetChanged();
        View view = inflater.inflate(R.layout.fragment_players, container, false);
        viewPager = view.findViewById(R.id.playerViewPager);
        tabLayout = view.findViewById(R.id.playerTabLayout);
        appBarLayout = getActivity().findViewById(R.id.app_bar_layout);
        viewPager.setAdapter(playerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout.setExpanded(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
