package com.example.android.architecture.blueprints.todoapp;

import com.bluelinelabs.conductor.Controller;

public abstract class BaseController extends Controller {
    private boolean mActive = false;

    protected void setActive(boolean active) {
        mActive = active;
    }

    public boolean isActive() {
        return mActive;
    }
}