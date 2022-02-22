package com.mop.base.utils.bus;

import static kotlin.jvm.internal.Intrinsics.checkNotNull;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haohz on 2020/3/19.
 * https://mp.weixin.qq.com/s/IN9t1729jlHd8RIZUU_6kg
 */
public class LiveBusStick {
    private static volatile LiveBusStick instance;

    private final Map<Object, MutableLiveData<Object>> mLiveBus;

    private LiveBusStick() {
        mLiveBus = new HashMap<>();
    }

    public static LiveBusStick get() {
        if (instance == null) {
            synchronized (LiveBusStick.class) {
                if (instance == null) {
                    instance = new LiveBusStick();
                }
            }
        }
        return instance;
    }

    public <T> MutableLiveData<T> subscribe(Object eventKey) {
        checkNotNull(eventKey);
        return (MutableLiveData<T>) subscribe(eventKey, Object.class);
    }

    public <T> MutableLiveData<T> subscribe(Object eventKey, Class<T> tMutableLiveData) {
        checkNotNull(eventKey);
        checkNotNull(tMutableLiveData);
        if (!mLiveBus.containsKey(eventKey)) {
            mLiveBus.put(eventKey, new LiveBusData<>());
        } else {
            LiveBusData liveBusData = (LiveBusData) mLiveBus.get(eventKey);
        }

        return (MutableLiveData<T>) mLiveBus.get(eventKey);
    }


    @MainThread
    public void remove(Object eventKey) {
        checkNotNull(eventKey);
        mLiveBus.remove(eventKey);
    }


    public <T> MutableLiveData<T> postEvent(Object eventKey, T value) {
        checkNotNull(eventKey);
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.postValue(value);
        return mutableLiveData;
    }

    public <T> MutableLiveData<T> postMainTreadEvent(Object eventKey, T value) {
        checkNotNull(eventKey);
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.setValue(value);
        return mutableLiveData;
    }

    public static class LiveBusData<T> extends MutableLiveData<T> {

        public LiveBusData() {
        }

        private ObserverWrapper obw;

        ObserverWrapper getObserverWrapper() {
            return obw;
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {

            obw = new ObserverWrapper<>(observer);
            super.observe(owner, obw);
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {
        private Observer<T> observer;

        private ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (observer != null) {
                observer.onChanged(t);
            }
        }
    }
}
