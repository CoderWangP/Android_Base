package com.wp.android_base.api.gituser;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.model.gituser.GitUser;

import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by wangpeng on 2018/6/22.
 */

public class GitUserApiImpl {

    public static Observable<HttpResult<GitUser>> testApiLifecycle(final LifecycleProvider lifecycleProvider){
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<HttpResult<GitUser>>>() {
                    @Override
                    public ObservableSource<HttpResult<GitUser>> apply(final Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<HttpResult<GitUser>>() {

                            @Override
                            public void subscribe(ObservableEmitter<HttpResult<GitUser>> emitter) throws Exception {
                                HttpResult<GitUser> httpResult = new HttpResult<>();
    /*                            if(aLong == 3){
                                    httpResult.setCode(401);
                                }*/
                                GitUser gitUserData = new GitUser();
                                gitUserData.setCount(aLong);
                                httpResult.setData(gitUserData);
                                emitter.onNext(httpResult);
                            }
                        });
                    }
                })
                .compose(HttpRequestManager.createDefaultTransformer(lifecycleProvider));
    }


    public static Observable<HttpResult<List<GitUser>>> getGitUser(LifecycleProvider lifecycleProvider, String user){
        Api gitUserApi = HttpRequestManager.createApi(Api.class);
        return gitUserApi.getGitUser(user).compose(HttpRequestManager.createDefaultTransformer(lifecycleProvider));
    }
}
