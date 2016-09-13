package com.example.android.architecture.blueprints.todoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

public abstract class BaseController extends Controller {
    private boolean mActive = false;

    public BaseController() {
    }

    public BaseController(Bundle args) {
    }

    protected ActionBar getActionBar() {
        ActionBarProvider actionBarProvider = ((ActionBarProvider)getActivity());
        return actionBarProvider != null ? actionBarProvider.getSupportActionBar() : null;
    }

    protected DrawerLayout getDrawerLayout() {
        DrawerLayoutProvider actionBarProvider = ((DrawerLayoutProvider)getActivity());
        return actionBarProvider != null ? actionBarProvider.getDrawerLayout() : null;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        // Default settings to reset Activity UI state to each time a new controller is loaded.
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);

        DrawerLayout drawerLayout = getDrawerLayout();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        setActive(false);
        // Note: in a real application you may wish to unbind your view references by
        // overriding this method and setting each reference to null. This releases the Views
        // that are on the back-stack and saves memory.
    }

    protected void setActive(boolean active) {
        mActive = active;
    }

    public boolean isActive() {
        return mActive;
    }
}