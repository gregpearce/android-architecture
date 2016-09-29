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

package com.example.android.architecture.blueprints.todoapp.taskdetail;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.android.architecture.blueprints.todoapp.MainActivity;
import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.TestUtils;
import com.example.android.architecture.blueprints.todoapp.data.FakeTasksRemoteDataSource;
import com.example.android.architecture.blueprints.todoapp.data.Task;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests for the tasks screen, the main screen which contains a list of all tasks.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskDetailScreenTest {

    private static String TASK_TITLE = "ATSL";

    private static String TASK_DESCRIPTION = "Rocks";

    /**
     * {@link Task} stub that is added to the fake service API layer.
     */
    private static Task ACTIVE_TASK = new Task(TASK_TITLE, TASK_DESCRIPTION, false);

    /**
     * {@link Task} stub that is added to the fake service API layer.
     */
    private static Task COMPLETED_TASK = new Task(TASK_TITLE, TASK_DESCRIPTION, true);

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     *
     * <p>
     * Sometimes an {@link Activity} requires a custom start {@link Intent} to receive data
     * from the source Activity. ActivityTestRule has a feature which let's you lazily start the
     * Activity under test, so you can control the Intent that is used to start the target Activity.
     */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true /* Initial touch mode  */,
                                          false /* Lazily launch activity */);

    private void loadActiveTask() {
        startWithStubbedTask(ACTIVE_TASK);
    }

    private void loadCompletedTask() {
        startWithStubbedTask(COMPLETED_TASK);
        // click the task to navigate to it
        onView(withText(COMPLETED_TASK.getTitle())).perform(click());
    }

    /**
     * Setup your test fixture with a fake task already initialised.
     * The {@link MainActivity} is started after the fake service API has been configured.
     */
    private void startWithStubbedTask(Task task) {
        // Add a task stub to the fake service api layer.
        TasksRepository.destroyInstance();
        FakeTasksRemoteDataSource.getInstance().deleteAllTasks();
        FakeTasksRemoteDataSource.getInstance().addTasks(task);

        // Lazily start the Activity from the ActivityTestRule
        mMainActivityTestRule.launchActivity(new Intent());

        // click the task in the Tasks screen to navigate to it
        onView(withText(task.getTitle())).perform(click());
    }

    @Test
    public void activeTaskDetails_DisplayedInUi() throws Exception {
        loadActiveTask();

        // Check that the task title and description are displayed
        onView(withId(R.id.task_detail_title)).check(matches(withText(TASK_TITLE)));
        onView(withId(R.id.task_detail_description)).check(matches(withText(TASK_DESCRIPTION)));
        onView(withId(R.id.task_detail_complete)).check(matches(not(isChecked())));
    }

    @Test
    public void completedTaskDetails_DisplayedInUi() throws Exception {
        loadCompletedTask();

        // Check that the task title and description are displayed
        onView(withId(R.id.task_detail_title)).check(matches(withText(TASK_TITLE)));
        onView(withId(R.id.task_detail_description)).check(matches(withText(TASK_DESCRIPTION)));
        onView(withId(R.id.task_detail_complete)).check(matches(isChecked()));
    }

    @Test
    public void orientationChange_menuAndTaskPersist() {
        loadActiveTask();

        // Check delete menu item is displayed and is unique
        onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));

        TestUtils.rotateOrientation(mMainActivityTestRule);

        // Check that the task is shown
        onView(withId(R.id.task_detail_title)).check(matches(withText(TASK_TITLE)));
        onView(withId(R.id.task_detail_description)).check(matches(withText(TASK_DESCRIPTION)));

        // Check delete menu item is displayed and is unique
        onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));
    }
}
