# TODO-MVP-Conductor

Based on the [TODO-MVP](https://github.com/googlesamples/android-architecture/tree/todo-mvp) sample and uses the [Conductor](https://github.com/bluelinelabs/Conductor) framework to refactor to a single Activity architecture.

Project maintainer: [grepx](https://github.com/grepx).

The basic components of the Conductor framework architecture [are outlined here](https://github.com/bluelinelabs/Conductor#components-to-know).

`Activity` and `Fragment` are replaced with `Controller` classes, which have a simpler lifecycle [outlined in the Conductor documentation](https://github.com/bluelinelabs/Conductor#controller-lifecycle). All classes below the View layer stayed the same as in the original TODO-MVP app.

<img src="https://raw.githubusercontent.com/wiki/grepx/android-architecture/images/mvp.png" alt="Diagram"/>

## Code

### Router setup

Navigation between Controllers is handled by the `Router` class. Routers are responsible rendering Controllers, navigating between Controllers, and managing the backstack of Controllers.

In this example, we only use a single `Router`, which is configured in `MainActivity` and given a container view to render it's content in - much like `Fragment` in a vanilla Android architecture.

```java
mRouter = Conductor.attachRouter(this, mContainer, savedInstanceState);
```

The Router and backstack of Controllers is automatically kept safe in a retained `Fragment` during configuration changes and can also be automatically serialised/deserialised during other destructive events.

Therefore, when we attach the main `Router` in `MainActivity` we check to see if it already has a `Controller` stack from a previous session, otherwise push the first `Controller` to the stack.

```java
if (!mRouter.hasRootController()) {
  mRouter.setRoot(RouterTransaction.with(new TasksController()));
}
```

Back presses are communicated from `MainActivity` to the main `Router`.

```java
@Override
public void onBackPressed() {
  if (!mRouter.handleBack()) {
    super.onBackPressed();
  }
}
```

### Controller navigation

We can get a reference to the current `Router` from any `Controller` class by calling the `getRouter` method. We can get backstack support by pushing a new `Controller` to the `Router`. We can also animate the push/pop transition to/from the new `Controller` by supplying an `AnimatorChangeHandler`:

```java
getRouter().pushController(RouterTransaction.with(addEditTaskController)
  .pushChangeHandler(new HorizontalChangeHandler())
  .popChangeHandler(new HorizontalChangeHandler()));
```

Note: When you are testing using Espresso, make sure you turn off animations on the device [as outlined in the Espresso setup instructions](https://google.github.io/android-testing-support-library/docs/espresso/setup/index.html#setup-your-test-environment).

### Communicating back from a Controller

The original TODO-MVP code makes use use of `Activity::setResult` and `Activity::onActivityResult` to pass a result back from `AddEditTaskFragment` to `TaskDetailFragment`. Controllers achieve this through holding a direct reference to the previous `Controller`. This reference is set using `setTargetController` such as in `TaskDetailController`:

```java
AddEditTaskController addEditTaskController = new AddEditTaskController(taskId);
addEditTaskController.setTargetController(this);
```

`AddEditTaskController` can now perform a callback on `TaskDetailController` when it is finished, using the `ControllerResultHandler` interface:

```java
Controller targetController = getTargetController();
if (targetController instanceof ControllerResultHandler) {
  ((ControllerResultHandler) targetController).onResult(ControllerResult.OK);
}
```

### The navigation drawer

The `NavigationView` used for the navigation drawer needs to be configured in the `Activity` layout file. A `getDrawerLayout` method is provided by `BaseController` so that each `Controller` can access the drawer layout and reconfigure it as required (this is only actually used to set whether or not the user should be able to slide it out while they are on that screen).

Configuring the drawer layout so that the user can't slide it out in a `Controller`:

```java
DrawerLayout drawerLayout = getDrawerLayout();
drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
```

##### Espresso

The navigation drawer also caused the Espresso tests in `AppNavigationTest` to become flakey. This is due to the time taken to animate the drawer as it opens/closes, which cannot be switched off like other animations.

The only way I could find to fix this way to use the `EspressoIdlingResource` to tell Espresso when the drawer has finished animating. This code is contained in `EspressoDrawerListener`.

## Features

### Complexity - understandability

#### Use of architectural frameworks/libraries/tools:

[Conductor](https://github.com/bluelinelabs/Conductor)

#### Conceptual complexity

Developers need to be familiar with the Conductor framework. It is a simple framework relative to using Fragments but still non-standard Android development.

### Testability

#### Unit testing

All the Unit tests remain the same as in the original TODO-MVP app.

#### UI testing

Several tweaks were made to the Espresso tests to get them working properly, the most significant is in `AppNavigationTest` as outlined above.

##### Hermetic UI tests

`EditTaskScreenTest`, `StatisticsScreenTest` and `TaskDetailScreenTest` were previously hermetic tests that only tested their respective screens. This was possible since you could configure the test environment and then craft an `Intent` to launch directly to the `Activity` without touching the rest of the app.

Since you can no longer launch directly to any one screen in the app, these tests are no longer hermetic. The test environment is set up as before, but we launch to `MainActivity` and manually navigate to the correct screen to perform the test.

### Code metrics

There is roughly the same amount code as in the original app.

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            49           1129           1436           3628
XML                             31             85            291            559
-------------------------------------------------------------------------------
SUM:                            80           1214           1727           4187
-------------------------------------------------------------------------------
```
### Maintainability

#### Ease of amending or adding a feature

Similar to the original app. This app doesn't have any particularly complex screens composed of multiple elements, which is really what the Conductor framework is designed for. 

When considering Conductor, you should also take into account that there are some UI styles that Conductor doesn't properly support yet, such as dialogs or anything else that uses Fragments.

#### Learning cost

Medium as the Conductor framework is quite simple but there are far fewer resources available.
