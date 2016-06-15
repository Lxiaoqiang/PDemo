package rxjava.xq.com.pdemo.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rxjava.xq.com.pdemo.entity.BaseEntity;

/**
 * Created by lhq on 2016/6/14.
 */
public class NetService<C> {
    public static final String BASE_URL = "http://139.196.29.70:80/uuuutest/";


    Gson gson = new Gson();

    public static <T> T getService(Class<T> clazz){
        return getRetrofit().create(clazz);
    }

    /**
     *
     * @param observable
     * @param dataHandlers
     * @param subscribe
     * @param callCls 返回数据Bean Class
     */
    public  void invoke(@NonNull final Observable<BaseEntity> observable, @NonNull final Action1<C> dataHandlers, @NonNull final Subscriber<C> subscribe, @NonNull final Class<C> callCls) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<BaseEntity, Boolean>() {//返回数据异常处理过滤
                    @Override
                    public Boolean call(BaseEntity baseData) {
                        if (!baseData.isResult()) {//如果返回状态为false，则输出失败原因并结束
                            subscribe.onError(new Exception("返回状态为false"));
                            subscribe.onCompleted();
                            return false;
                        }
                        return true;
                    }
                })
                .map(new Func1<BaseEntity, C>() {//数据转换处理
                    @Override
                    public C call(BaseEntity baseData) {
//                        return JSON.parseObject(JSON.toJSONString(baseData.getData()), callCls);
//                        C c =
                        return  gson.fromJson(gson.toJson(baseData.getDatas()),callCls);
                    }
                })
                .doOnNext(dataHandlers)//数据二次加工处理
                .subscribe(subscribe);//请求回调
    }


//    /**
//     *
//     * @param observable
//     * @param dataHandlers
//     * @param subscribe
//     * @param callCls 返回数据Bean Class
//     */
//    public  void invoke(@NonNull final Observable<BaseData> observable, @NonNull final Action1<C> dataHandlers, @NonNull final Subscriber<C> subscribe, @NonNull final Class<C> callCls) {
//        observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(new Func1<BaseData, Boolean>() {//返回数据异常处理过滤
//                    @Override
//                    public Boolean call(BaseData baseData) {
//                        if (!baseData.isResult()) {//如果返回状态为false，则输出失败原因并结束
//                            subscribe.onError(new Exception(baseData.getMessage()));
//                            subscribe.onCompleted();
//                            return false;
//                        }
//                        return true;
//                    }
//                })
//                .map(new Func1<BaseData, C>() {//数据转换处理
//                    @Override
//                    public C call(BaseData baseData) {
//                        return JSON.parseObject(JSON.toJSONString(baseData.getData()), callCls);
//                    }
//                })
//                .doOnNext(dataHandlers)//数据二次加工处理
//                .subscribe(subscribe);//请求回调
//    }
    public static Retrofit getRetrofit(){
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }
}
