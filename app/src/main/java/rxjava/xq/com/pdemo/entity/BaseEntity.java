package rxjava.xq.com.pdemo.entity;

/**
 * Created by lhq on 2016/6/14.
 */
public class BaseEntity {
    private int code;
    private String message;
    private User user;
    public boolean isResult(){
        return code == 200;
    }

    public User getDatas(){
        return user;
    }

}
