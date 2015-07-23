package com.adzerk.android.sdk.sample;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainView extends ActivityView {

    @Bind(R.id.recycler) RecyclerView recyclerView;

    public MainView(Activity activity) {
        super(activity);

        ButterKnife.bind(this, activity);
        setupWidgets(activity);
    }

    private void setupWidgets(Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

}
