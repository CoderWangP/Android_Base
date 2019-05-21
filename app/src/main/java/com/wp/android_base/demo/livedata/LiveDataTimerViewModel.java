package com.wp.android_base.demo.livedata;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.SystemClock;

import com.wp.android_base.base.SimpleObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;


/**
 * Created by wp on 2019/5/20.
 * <p>
 * Description:
 */

public class LiveDataTimerViewModel extends ViewModel{

    private MutableLiveData<Long> mElapsedTime = new MutableLiveData<>();

    public LiveDataTimerViewModel() {
        long initialTime = SystemClock.elapsedRealtime();
        Observable.interval(1000,1000, TimeUnit.SECONDS).subscribe(new SimpleObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                long time = (SystemClock.elapsedRealtime() - initialTime) / 1000;
                mElapsedTime.postValue(time);
            }
        });
    }
}
