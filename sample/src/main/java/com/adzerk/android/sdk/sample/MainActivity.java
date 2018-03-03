package com.adzerk.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createPresenter(savedInstanceState);
    }

    private void createPresenter(Bundle savedInstanceState) {
        if (presenter == null) {
            presenter = new MainPresenter(
                    new MainModel(),
                    new MainView(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().toObserverable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof MainPresenter.AdClickEvent) {
                    presenter.OnAdClick((MainPresenter.AdClickEvent)o);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
