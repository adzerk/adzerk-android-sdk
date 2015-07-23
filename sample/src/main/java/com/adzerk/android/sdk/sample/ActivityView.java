package com.adzerk.android.sdk.sample;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class ActivityView {

    WeakReference<Activity> activityRef;

    public ActivityView(Activity activity) {
        activityRef = new WeakReference<Activity>(activity);
    }

    public Activity getActivity() {
        return activityRef.get();
    }
}
