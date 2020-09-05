package com.pisco.app.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pisco.app.Entity.ScreenItem;
import com.pisco.app.Screen.ItemOnBoardFragment;

import java.util.ArrayList;
import java.util.List;

public class OnBoardPagerAdapter extends FragmentStateAdapter {

    private List<ScreenItem> screenList;
    private ArrayList<Long> pageIds = new ArrayList<>();

    public OnBoardPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<ScreenItem> screenList) {
        super(fragmentManager, lifecycle);
        this.screenList = screenList;
        for (ScreenItem screen : screenList) {
            pageIds.add((long) screen.hashCode());
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ItemOnBoardFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", screenList.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return screenList.size();
    }

    @Override
    public long getItemId(int position) {
        return screenList.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return pageIds.contains(itemId);
    }
}
