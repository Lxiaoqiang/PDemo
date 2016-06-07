package rxjava.xq.com.pdemo.video;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by lhq on 2016/5/25.
 */
public class CameraHelper {
    public Camera mCamera;
    private Camera.Parameters mParameters;
    /**
     * 1表示后置，0表示前置
     */
    public static int cameraPosition = 1;

    public static final int FONT = 0;
    public static final int BACK = 1;

    public CameraHelper() {

    }

    public void onResume() {
        getCamera(cameraPosition);
    }

    public void onPause() {
        releaseCamera();
    }

    /**
     * 获取camera
     *
     * @return
     */
    private Camera getCamera(int i) {
        if (mCamera == null) {
            try {
                mCamera = Camera.open(i);
                mCamera.setDisplayOrientation(90);
                mCamera.lock();
                initCameraParamter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mCamera;
    }

    /**
     * 释放camera
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开启预览
     */
    public void setStartPreView(SurfaceHolder holder) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换摄像头
     */
    @SuppressLint("NewApi")
    public void switchCamera(SurfaceHolder mHolder) {
        if (cameraPosition == FONT) {
            releaseCamera();
            mCamera = Camera.open(BACK);
            mCamera.setDisplayOrientation(90);
            mCamera.lock();
            setStartPreView(mHolder);
            cameraPosition = BACK;
        } else {
            releaseCamera();
            mCamera = Camera.open(FONT);
            mCamera.setDisplayOrientation(90);
            mCamera.lock();
            setStartPreView(mHolder);
            cameraPosition = FONT;
        }
    }

    /***
     * 初始化camera参数
     */
    private void initCameraParamter() {
        mParameters = mCamera.getParameters();
        //设置自动聚焦
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        try {
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
