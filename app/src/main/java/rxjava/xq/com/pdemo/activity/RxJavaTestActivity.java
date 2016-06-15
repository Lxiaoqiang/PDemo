package rxjava.xq.com.pdemo.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.api.ApiService;
import rxjava.xq.com.pdemo.entity.BaseEntity;
import rxjava.xq.com.pdemo.entity.Teacher;
import rxjava.xq.com.pdemo.net.NetService;
import rxjava.xq.com.pdemo.utils.L;

public class RxJavaTestActivity extends AppCompatActivity {
    private final String TAG = RxJavaTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test);
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                L.i(TAG, integer + "");
            }
        });
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 5;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                L.i(TAG, integer + "");
            }
        });


    }

    public Subscriber<Teacher> login(String userID, String userSex, String courseMode,
                                     String courseGrade, String courseType, Subscriber<Teacher> subscriber) {
        Observable<BaseEntity> observable = NetService.getService(ApiService.class).getTeacherList(userID, userSex, courseMode, courseGrade, courseType);
        new NetService<Teacher>().invoke(observable, loginHandlers, subscriber, Teacher.class);
        return subscriber;
    }

    private Action1<Teacher> loginHandlers = new Action1<Teacher>() {
        @Override
        public void call(Teacher teacher) {

        }
    };
}
