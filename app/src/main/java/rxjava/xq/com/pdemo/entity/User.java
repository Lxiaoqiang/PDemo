package rxjava.xq.com.pdemo.entity;

/**
 * Created by lhq on 2016/3/31.
 */
public class User {
    private int id;
    private String name;
    private int sex;

    public User(int id, String name, int sex) {
        this.name = name;
        this.sex = sex;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
