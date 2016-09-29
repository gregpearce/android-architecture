/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.addedittask;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.example.android.architecture.blueprints.todoapp.BaseController;
import com.example.android.architecture.blueprints.todoapp.Injection;
import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.controller.ControllerResult;
import com.example.android.architecture.blueprints.todoapp.controller.ControllerResultHandler;
import com.example.android.architecture.blueprints.todoapp.util.BundleBuilder;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class AddEditTaskController extends BaseController implements AddEditTaskContract.View {

    private static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskContract.Presenter mPresenter;

    private final String mTaskId;

    private TextView mTitle;

    private TextView mDescription;

    public AddEditTaskController() {
        this(new Bundle());
    }

    public AddEditTaskController(String taskId) {
        this(new BundleBuilder(new Bundle())
                .putString(ARGUMENT_EDIT_TASK_ID, taskId)
                .build());
    }

    public AddEditTaskController(Bundle args) {
        super(args);
        mTaskId = args.getString(ARGUMENT_EDIT_TASK_ID);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View root = inflater.inflate(R.layout.addtask_controller, container, false);
        mTitle = (TextView) root.findViewById(R.id.add_task_title);
        mDescription = (TextView) root.findViewById(R.id.add_task_description);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        setActionBar(toolbar);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mTitle.requestFocus();

        FloatingActionButton fab =
                (FloatingActionButton) root.findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString());
            }
        });

        setHasOptionsMenu(true);

        setActive(true);

        // Create the presenter
        mPresenter = new AddEditTaskPresenter(
                mTaskId,
                Injection.provideTasksRepository(getApplicationContext()),
                this);

        return root;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        // Configure Activity level UI.
        ActionBar actionBar = getActionBar();
        if (mTaskId == null) {
            actionBar.setTitle(R.string.add_task);
        } else {
            actionBar.setTitle(R.string.edit_task);
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Start Presenter
        mPresenter.start();
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mTitle, getResources().getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        Controller targetController = getTargetController();

        if (targetController instanceof ControllerResultHandler) {
            ((ControllerResultHandler) targetController).onResult(ControllerResult.OK);
        }

        getRouter().popController(this);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDescription(String description) {
        mDescription.setText(description);
    }
}