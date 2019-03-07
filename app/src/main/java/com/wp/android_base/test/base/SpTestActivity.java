package com.wp.android_base.test.base;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.model.gituser.GitUser;
import com.wp.android_base.base.utils.CheckLifecycleUtil;
import com.wp.android_base.base.utils.Sp;
import com.wp.android_base.base.utils.log.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangpeng on 2018/10/12.
 * <p>
 * Description:
 */

public class SpTestActivity extends BaseActivity {

    private static final String TAG = "SpTestActivity";

    private ProgressBar mProgressBar;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_sp_test;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        TextView textView = findViewById(R.id.tx_json);

        mProgressBar = findViewById(R.id.progressbar);

        GitUser gitUser = new GitUser();
        gitUser.setCount(1000);

        Sp.from(this).writeJson("json", gitUser)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Sp.SpObserver<String>() {
                    @Override
                    public void onNext(String json) {
                        if (CheckLifecycleUtil.isUnAlive(SpTestActivity.this)) {
                            return;
                        }
                        Logger.json(TAG + ">>writeJson", json);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG + ">>writeJson", e.getMessage());
                    }
                });

        Observable<String> json = Sp.from(this).writeJson("json", gitUser);
        LifecycleTransformer<Object> objectLifecycleTransformer = bindUntilEvent(ActivityEvent.DESTROY);
        Observable<Object> compose = json.compose(objectLifecycleTransformer);
        Observable<String> compose1 = json.compose(bindUntilEvent(ActivityEvent.DESTROY));
        compose.subscribe(new Sp.SpObserver<Object>() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
        json.compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Sp.SpObserver<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


        Sp.from(this).readJson("json", GitUser.class)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Sp.SpObserver<GitUser>() {

                    @Override
                    public void onNext(GitUser gitUser) {
                        if (CheckLifecycleUtil.isUnAlive(SpTestActivity.this)) {
                            return;
                        }
                        textView.setText(new Gson().toJson(gitUser));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG + ">>readJson", e.getMessage());
                    }
                });
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Thread.sleep(10000);
                e.onNext(new Object());
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }
}
