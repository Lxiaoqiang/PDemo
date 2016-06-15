package rxjava.xq.com.pdemo.test;

/**
 * Created by lhq on 2016/6/15.
 */
public class Test<T> {

    private T t;

    public Test(T t1) {
        t = t1;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public static void main(String[] args) {
        Test<People> peopleTest = new Test<>(new People());

    }

}