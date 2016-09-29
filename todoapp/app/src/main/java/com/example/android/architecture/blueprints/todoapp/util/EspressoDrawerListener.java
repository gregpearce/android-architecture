package com.example.android.architecture.blueprints.todoapp.util;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class EspressoDrawerListener implements DrawerLayout.DrawerListener {
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == DrawerLayout.STATE_IDLE) {
            EspressoIdlingResource.decrement();
        } else {
            EspressoIdlingResource.increment();
        }
    }
}
