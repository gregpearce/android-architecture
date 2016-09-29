package com.example.android.architecture.blueprints.todoapp;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public interface ActionBarInterface {
    ActionBar getSupportActionBar();

    void setSupportActionBar(Toolbar toolbar);
}
