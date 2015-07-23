package com.adzerk.android.sdk.sample;

import com.squareup.otto.Bus;

public class BusProvider {
    private BusProvider() { }

    static Bus instance;

    public static Bus getInstance() {
        if (instance == null) {
            instance = new Bus();
        }

        return instance;
    }

    public static void register(Object... args) {
        for (Object o : args) {
            getInstance().register(o);
        }
    }

    public static void unregister(Object... args) {
        for (Object o : args) {
            getInstance().unregister(o);
        }
    }

}
