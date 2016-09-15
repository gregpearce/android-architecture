package com.example.android.architecture.blueprints.todoapp.controller;

/**
 *  Used to return a result from a Controller.
 *  <p>
 *  Equivalent to {@link android.app.Activity#RESULT_OK} and
 *  {@link android.app.Activity#RESULT_CANCELED} in a multi-Activity architecture.
 */
public enum ControllerResult {
    /**
     * Equivalent to {@link android.app.Activity#RESULT_OK}.
     */
    OK,

    /**
     * Equivalent to {@link android.app.Activity#RESULT_CANCELED}.
     */
    CANCELED
}