package rxjava.xq.com.pdemo.entity;

import java.io.Serializable;

/**
 * Created by lhq on 2016/5/3.
 */
public class VideoBean implements Serializable{
    private static final long serialVersionUID = 8436677359660067402L;
    /** 路径 */
    private String path;
    /** 该视频录制了多少时间 */
    private int time;
    /** 该视频是前置录的还是后置录的 */
    private int cameraPosition;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(int cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

}
