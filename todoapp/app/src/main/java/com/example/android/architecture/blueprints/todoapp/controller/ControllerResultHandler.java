package com.example.android.architecture.blueprints.todoapp.controller;

import com.bluelinelabs.conductor.Controller;

/**
 * Used to pass a result from a {@link com.bluelinelabs.conductor.Controller Controller} to its
 * {@link Controller#getTargetController() target Controller}.
 */
public interface ControllerResultHandler {
    /**
     * Used to pass a result from a {@link com.bluelinelabs.conductor.Controller Controller} to its
     * {@link Controller#getTargetController() target Controller}.
     * <p>
     * Equivalent to {@link android.app.Activity#onActivityResult} in a multi-Activity architecture.
     */
    void onResult(ControllerResult result);
}
