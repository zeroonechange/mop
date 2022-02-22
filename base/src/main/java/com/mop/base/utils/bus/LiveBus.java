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
public class LiveBus {
    private static volatile LiveBus instance;

    private final Map<Object, MutableLiveData<Object>> mLiveBus;

    private LiveBus() {
        mLiveBus = new HashMap<>();
    }

    public static LiveBus get() {
        if (instance == null) {
            synchronized (LiveBus.class) {
                if (instance == null) {
                    instance = new LiveBus();
                }
            }
        }
        return instance;
    }

    public <T> MutableLiveData<T> subscribe(Object eventKey) {
        checkNotNull(eventKey);
        return (MutableLiveData<T>) subscribe(eventKey, Object.class);
    }

    @MainThread
    public void remove(Object eventKey) {
        checkNotNull(eventKey);
        mLiveBus.remove(eventKey);
    }

    public <T> MutableLiveData<T> subscribe(Object eventKey, Class<T> tMutableLiveData) {
        checkNotNull(eventKey);
        checkNotNull(tMutableLiveData);
        if (!mLiveBus.containsKey(eventKey)) {
            mLiveBus.put(eventKey, new LiveBusData<>(true));
        } else {
            LiveBusData liveBusData = (LiveBusData) mLiveBus.get(eventKey);
            liveBusData.isFirstSubscribe = false;
        }

        return (MutableLiveData<T>) mLiveBus.get(eventKey);
    }

    public <T> MutableLiveData<T> postEvent(Object eventKey, T value) {
        checkNotNull(eventKey);
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.postValue(value);
        return mutableLiveData;
    }

    public synchronized <T> MutableLiveData<T> postMainThreadEvent(Object eventKey, T value) {
        checkNotNull(eventKey);
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.setValue(value);
        return mutableLiveData;
    }

    public static class LiveBusData<T> extends MutableLiveData<T> {

        private boolean isFirstSubscribe;

        private ObserverWrapper obw;

        ObserverWrapper<T> getObserverWrapper() {
            return obw;
        }

        public LiveBusData(boolean isFirstSubscribe) {
            this.isFirstSubscribe = isFirstSubscribe;
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            obw = new ObserverWrapper<>(observer, isFirstSubscribe);
            super.observe(owner, obw);
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {
        private Observer<T> observer;

        private boolean isChanged;

        private ObserverWrapper(Observer<T> observer, boolean isFirstSubscribe) {
            this.observer = observer;
            isChanged = isFirstSubscribe;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (isChanged) {
                if (observer != null) {
                    observer.onChanged(t);
                }
            } else {
                isChanged = true;
            }
        }

    }

}
