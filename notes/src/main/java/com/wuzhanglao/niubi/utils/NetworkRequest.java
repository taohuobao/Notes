package com.wuzhanglao.niubi.utils;


import com.wuzhanglao.niubi.mvp.model.HeBeiBeiBean;
import com.wuzhanglao.niubi.mvp.model.HeWeatherBean;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wuming on 16/10/19.
 */

public class NetworkRequest {

    private static class NetworkRequestSingletonHolder {
        private static final NetworkRequest instance = new NetworkRequest();
        private static final NetworkService heweather_service = NetworkService.Factory.create(NetworkService.BASE_URL_HeWeather);
        private static final NetworkService hebeibei_service = NetworkService.Factory.create(NetworkService.BASE_URL_HeBeiBei);
    }

    public static final NetworkRequest getInstance() {
        return NetworkRequestSingletonHolder.instance;
    }

    private static final NetworkService getHeWeatherService() {
        return NetworkRequestSingletonHolder.heweather_service;
    }

    private static final NetworkService getHeBeiBeiService() {
        return NetworkRequestSingletonHolder.hebeibei_service;
    }

    private class ComposeThread<T> implements Observable.Transformer<T, T> {
        @Override
        public Observable<T> call(Observable<T> observable) {
            return observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public void getWeather(String city, Action1 onNext) {
        getHeWeatherService().getWeatherService(city, GlobleData.HEFENG_KEY).compose(new ComposeThread<HeWeatherBean>()).subscribe(onNext);
    }

    public void getScenic(String cityid, Action1 onNext) {
        getHeWeatherService().getScenicService(cityid, GlobleData.HEFENG_KEY).compose(new ComposeThread<HeWeatherBean>()).subscribe(onNext);
    }

    public void getHeBeiBeiData(Action1 onNext) {
        getHeBeiBeiService().getHeBeiBeiDate("64").compose(new ComposeThread<HeBeiBeiBean>()).subscribe(onNext);
    }
}
