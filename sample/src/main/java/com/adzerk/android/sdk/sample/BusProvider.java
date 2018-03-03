package com.adzerk.android.sdk.sample;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class BusProvider {
    private BusProvider() { }

    static RxBus instance;

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }

        return instance;
    }

    public static void post(Object event) {
        getInstance().send(event);
    }

    static class RxBus {

        private final PublishSubject<Object> _bus = PublishSubject.create();

        public void send(Object o) {
            _bus.onNext(o);
        }

        public Observable<Object> toObserverable() {
            return _bus;
        }
    }
}
