package rxjava.xq.com.pdemo.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rxjava.xq.com.pdemo.entity.BaseEntity;

/**
 * Created by lhq on 2016/6/14.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("student/searchTeacher2.php")
    Observable<BaseEntity> getTeacherList(@Field("userID") String userID, @Field("userSex") String userSex, @Field("courseMode") String courseMode,
                                          @Field("courseGrade") String courseGrade, @Field("courseType") String courseType);

}
