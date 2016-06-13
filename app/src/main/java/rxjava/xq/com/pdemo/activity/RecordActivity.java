package rxjava.xq.com.pdemo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.entity.VideoBean;
import rxjava.xq.com.pdemo.utils.DensityUtils;
import rxjava.xq.com.pdemo.utils.L;
import rxjava.xq.com.pdemo.utils.SharedPreferencesHelper;
import rxjava.xq.com.pdemo.utils.VideoUtil;

public class RecordActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private final String TAG = RecordActivity.class.getSimpleName();
    /**
     * 开始录制
     */
    @BindView(R.id.btn_start)
    Button btn_start;
    /**
     * 删除
     */
    @BindView(R.id.btn_delete)
     Button btn_delete;
    /**
     * 闪动的imageview
     */
    @BindView(R.id.iv_shan)
     ImageView iv_shan;
    /**
     * 进度条
     */
    @BindView(R.id.ll_progress)
     LinearLayout ll_progress;
    @BindView(R.id.sufaceviewre)
     SurfaceView surfaceView;
    /**
     * 摄像头
     */
    private Camera mCamera;
    /**
     * 视频最大长度
     */
    private final int maxTime = 20;
    /**
     * 视频最小长度
     */
    private final int minTime = 3;
    /**
     * 点击删除，偶数才删除
     */
    private int deleteEven = 0;
    /**
     * 前面录制了多少时间
     */
    private int old = 0;
    /**
     * 当前录制了多少秒
     */
    private int currentTime = 0;

    private TimeCount timeCount;
    /**
     * 录制视频的类
     */
    private MediaRecorder mMediaRecorder;
    /**
     * 视频输出质量
     */
    private CamcorderProfile mProfile;
    /**
     * 回调
     */
    private SurfaceHolder mHolder;
    /**
     * 摄像头参数
     */
    private Camera.Parameters mParameters;
    /**
     * 录制视频保存文件
     */
    private String vedioPath;

    /**
     * 1表示后置，0表示前置
     */
    private int cameraPosition = 1;

    private ArrayList<VideoBean> list;

    /**
     * 屏幕宽度
     */
    private int mWidth;
    /**
     * 合并之后的视频文件
     */
    private String videoPath_merge;
    /**
     * 路径
     */
    private String Ppath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/DemoVideo/";
    private VideoBean bean;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        btn_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (ll_progress.getChildCount() > 1)
                            ll_progress.getChildAt(ll_progress.getChildCount() - 2).setBackgroundColor(getResources().getColor(R.color.green));
                        deleteEven = 0;
                        addProgressView();
                        timeCount = new TimeCount(maxTime * 1000 - old, 50);
                        timeCount.start();
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        old = currentTime + old;
                        addBlackView();
                        timeCount.cancel();
                        stopRecord();
                        break;
                }
                return false;
            }
        });
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCamera = getCamera();
        if (mCamera != null) {
            // 因为android不支持竖屏录制，所以需要顺时针转90度，让其显示正常
            mCamera.setDisplayOrientation(90);
            mCamera.lock();
            initCameraParameters();
        }
    }

    private void init() {
        list = new ArrayList<>();
        handler.postDelayed(shan, 0);
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        //创建文件夹
        File file = new File(Ppath);
        if (!file.exists()) {
            file.mkdir();
        }
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    private void addProgressView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(getResources().getColor(R.color.green));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) DensityUtils.px2dp(this, 1), LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        ll_progress.addView(imageView);
    }

    /**
     * 每次录制后添加显示间隔
     */
    private void addBlackView() {
        ImageView interval = new ImageView(this);
        interval.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) DensityUtils.px2dp(this, 5), LinearLayout.LayoutParams.MATCH_PARENT);
        interval.setLayoutParams(layoutParams);
        ll_progress.addView(interval);
    }

    /**
     * 第一次点击删除，变色
     */
    private void addDeleteColor() {
        ImageView imageView = (ImageView) ll_progress.getChildAt(ll_progress.getChildCount() - 2);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 初始化摄像头参数
     */
    private void initCameraParameters() {
        // 初始化摄像头参数
        mParameters = mCamera.getParameters();

        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        // 设置白平衡参数。
        String whiteBalance = SharedPreferencesHelper.getValueByKey(this, "pref_camera_whitebalance_key", "auto");
        if (isSupported(whiteBalance, mParameters.getSupportedWhiteBalance())) {
            mParameters.setWhiteBalance(whiteBalance);
        }

        // 参数设置颜色效果。
        String colorEffect = SharedPreferencesHelper.getValueByKey(this, "pref_camera_coloreffect_key", "none");
        if (isSupported(colorEffect, mParameters.getSupportedColorEffects())) {
            mParameters.setColorEffect(colorEffect);
        }

        try {
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置参数
     */
    private void readVideoPreferences() {
        mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mProfile.videoBitRate = 256000 * 3;

        CamcorderProfile highProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mProfile.videoCodec = highProfile.videoCodec;
        mProfile.audioCodec = highProfile.audioCodec;
        mProfile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
    }

    private static boolean isSupported(String value, List<String> supported) {
        return supported == null ? false : supported.indexOf(value) >= 0;
    }

    public static boolean getVideoQuality(String quality) {
        return "youtube".equals(quality) || "high".equals(quality);
    }

    /**
     * 开启预览
     */
    private void setStartPreView(SurfaceHolder holder) {
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
     * 获取camera
     *
     * @return
     */
    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open(cameraPosition);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 释放camera
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 闪动图片
     */
    private Runnable shan = new Runnable() {
        @Override
        public void run() {
            if (iv_shan.isShown()) {
                iv_shan.setVisibility(View.GONE);
            } else {
                iv_shan.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(shan, 500);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        resetSource();
    }

    private void resetSource() {
        int count = ll_progress.getChildCount();
        list.clear();
        ViewDisplay();
        if (count > 0)
            ll_progress.removeAllViews();
        if (timeCount != null)
            timeCount.cancel();
        old = 0;
        currentTime = 0;
    }

    private void startRecord() {
        bean = new VideoBean();
        vedioPath = Ppath + System.currentTimeMillis() + ".mp4";
        bean.setPath(vedioPath);
        mMediaRecorder = new MediaRecorder();
        if (mCamera != null) {
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
        }
        // 设置录制视频源为Camera(相机)
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (cameraPosition == 0) {
            mMediaRecorder.setOrientationHint(270);
        } else {
            mMediaRecorder.setOrientationHint(90);
        }
        // 录音源为麦克风
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        // 清晰度（影响内存大小）
        camcorderProfile.videoBitRate = 256000 * 3;
        mMediaRecorder.setProfile(camcorderProfile);

        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        //2的时候清晰度还行,大小20秒左右,大概4M
        //3的时候清晰度可以,大小20秒左右,大概6M
        mMediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);// 设置视频一次写多少字节(可调节视频空间大小)
        try {
            // 设置视频文件输出的路径
            mMediaRecorder.setOutputFile(vedioPath);
            mMediaRecorder.setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
//        try {
//            bean = new VideoBean();
//            vedioPath = Ppath + System.currentTimeMillis() + ".mp4";
//            bean.setPath(vedioPath);
//            mCamera.unlock();
//            mMediaRecorder = new MediaRecorder();// 创建mediaRecorder对象
//            mMediaRecorder.setCamera(mCamera);
//            // 设置录制视频源为Camera(相机)
//            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////            mMediaRecorder.setProfile(mProfile);
////            mMediaRecorder.setVideoSize(720, 1280);//设置视频大小（分辨率）
//            mMediaRecorder.setVideoEncodingBitRate(1024 * 1024);// 设置视频一次写多少字节(可调节视频空间大小)
//            // 最大期限
//            mMediaRecorder.setMaxDuration(maxTime * 1000);
//            // 第4步:指定输出文件 ， 设置视频文件输出的路径
//            mMediaRecorder.setOutputFile(vedioPath);
//            mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
//            // // 设置保存录像方向
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//                if (cameraPosition == 1) {
//                    //由于不支持竖屏录制，后置摄像头需要把视频顺时针旋转90度、、但是视频本身在电脑上看还是逆时针旋转了90度
//                    mMediaRecorder.setOrientationHint(90);
//                } else if (cameraPosition == 0) {
//                    //由于不支持竖屏录制，前置摄像头需要把视频顺时针旋转270度、、而前置摄像头在电脑上则是顺时针旋转了90度
//                    mMediaRecorder.setOrientationHint(270);
//                }
//            }
//            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
//
//                @Override
//                public void onInfo(MediaRecorder mr, int what, int extra) {
//
//                }
//            });
//            mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
//
//                @Override
//                public void onError(MediaRecorder mr, int what, int extra) {
//
//                }
//            });
//            // 第6步:根据以上配置准备MediaRecorder
//            mMediaRecorder.prepare();
//            mMediaRecorder.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
    }

    private void stopRecord() {
        if (bean != null) {
//            if (list.size() > 0) {
//                bean.setTime(now - list.get(list.size() - 1).getTime());
//            } else {
//                bean.setTime(now);
//            }
//            bean.setCameraPosition(cameraPosition);
            list.add(bean);
        }

        if (mMediaRecorder != null) {
            try {
                // 停止录像，释放camera
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.stop();
                // 清除recorder配置
                mMediaRecorder.reset();
                // 释放recorder对象
                mMediaRecorder.release();
                mMediaRecorder = null;
                // 没超过3秒就删除录制所有数据
                if (old < minTime) {
                    clearList();
                } else {
                    ViewDisplay();
                }

            } catch (Exception e) {
                clearList();
            }
        }
    }

    /**
     * 显示影藏删除
     */
    private void ViewDisplay() {
        if (list == null || list.size() == 0) {
            btn_delete.setVisibility(View.INVISIBLE);
        } else {
            btn_delete.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 清楚数据
     *
     * @version 1.0
     * @createTime 2015年6月25日, 下午6:04:28
     * @updateTime 2015年6月25日, 下午6:04:28
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void clearList() {
        Toast.makeText(RecordActivity.this, "单次录制视频最少3秒", Toast.LENGTH_LONG)
                .show();
        if (ll_progress.getChildCount() > 1) {
            ll_progress.removeViewAt(ll_progress.getChildCount() - 1);
            ll_progress.removeViewAt(ll_progress.getChildCount() - 1);
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                File file = new File(list.get(list.size() - 1).getPath());
                if (file.exists()) {
                    file.delete();
                }
            }
            list.remove(list.size() - 1);
            ViewDisplay();
        }
    }

    private class TimeCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {// 参数依次为总时长,和计时的时间间隔
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示 millisUntilFinished:倒计时剩余时间
            currentTime = (int) (maxTime * 1000 - millisUntilFinished - old);
            if (ll_progress.getChildCount() > 0) {
                ImageView iv = (ImageView) ll_progress.getChildAt(ll_progress.getChildCount() - 1);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                params.width = (int) (((float) currentTime / 1000f) * (mWidth / maxTime)) + 1;
                iv.setLayoutParams(params);
            }
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            onComplete();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreView(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * 切换摄像头
     *
     * @version 1.0
     * @createTime 2015年6月16日, 上午10:40:17
     * @updateTime 2015年6月16日, 上午10:40:17
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    @SuppressLint("NewApi")
    private void switchCamera() {
        releaseCamera();
        if (cameraPosition == 0) {
            mCamera = Camera.open(1);
//            mMediaRecorder.setOrientationHint();
            cameraPosition = 1;
        } else {
            mCamera = Camera.open(0);
            cameraPosition = 0;
        }
        initCameraParameters();
        mCamera.setDisplayOrientation(90);
        mCamera.lock();
        setStartPreView(mHolder);
//        // 切换前后摄像头
//        int cameraCount = 0;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
//
//        for (int i = 0; i < cameraCount; i++) {
//            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
//            if (cameraPosition == 1) {
//                // 现在是后置，变更为前置
//                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
//                    // CAMERA_FACING_BACK后置
//                    // 前置摄像头时必须关闭闪光灯，不然会报错
//                    if (mParameters != null) {
//                        if (mParameters.getFlashMode() != null
//                                && mParameters.getFlashMode().equals(
//                                Camera.Parameters.FLASH_MODE_TORCH)) {
//                            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
////                            img_flashlight.setImageResource(R.drawable.img_video_new_flashlight_close);
//                        }
//                        if (mCamera != null) {
//                            mCamera.setParameters(mParameters);
//                        }
//                    }
//
//                    // 释放Camera
//                    releaseCamera();
//
//                    // 打开当前选中的摄像头
//                    mCamera = Camera.open(i);
//                    mCamera.setDisplayOrientation(90);
//                    mCamera.lock();
//
////                    // 通过surfaceview显示取景画面
////					setStartPreview(GLSurfaceview.getHolder());
//
//                    cameraPosition = 0;
//
//                    break;
//                }
//            } else {
//                // 现在是前置， 变更为后置
//                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
//                    // CAMERA_FACING_BACK后置
//                    // 释放Camera
//                    releaseCamera();
//                    // 打开当前选中的摄像头
//                    mCamera = Camera.open(i);
//                    mCamera.setDisplayOrientation(90);
//                    mCamera.lock();
//
//                    // 通过surfaceview显示取景画面
////					setStartPreview(GLSurfaceview.getHolder());
//
//                    cameraPosition = 1;
//
//                    break;
//                }
//            }
//        }
    }
    @OnClick(R.id.btn_delete)
    void clickDeleteVideo(){
        if (list != null && list.size() > 0) {
            deleteEven++;
            addDeleteColor();
            if (deleteEven % 2 == 0) {
                if (ll_progress.getChildCount() > 1) {
                    ll_progress.removeViewAt(ll_progress.getChildCount() - 1);
                    ll_progress.removeViewAt(ll_progress.getChildCount() - 1);
                }
                old = old - currentTime;
                //删除本地文件
                File file = new File(list.get(list.size() - 1).getPath());
                if (file.exists()) {
                    file.delete();
                }
                list.remove(list.size() - 1);
            } else {
                if (ll_progress.getChildCount() > 1)
                    (ll_progress.getChildAt(ll_progress.getChildCount() - 2)).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            ViewDisplay();
        }
    }
    @OnClick(R.id.btn_back)
    void clickBack(){
        back();
    }
    @OnClick(R.id.btn_complete)
    void clickComplete(){
        onComplete();
    }
    @OnClick(R.id.iv_switch_camera)
    void clickSwitchCamera(){
        switchCamera();
    }

    private void back() {
        if (list != null && list.size() > 0) {
            deleteVideo();
        } else {
            finish();
        }
    }

    private void onComplete() {
        stopRecord();
        int size = list.size();
        String[] strs = new String[size];
        videoPath_merge = Ppath + System.currentTimeMillis()
                + ".mp4";
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                strs[i] = list.get(i).getPath();
            }
            try {
                VideoUtil.appendVideo(strs, videoPath_merge);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = size - 1; i >= 0; i--) {
            File file = new File(list.get(i).getPath());
            if (file.exists()) {
                file.delete();
            }
            list.remove(i);
        }

        Intent it = new Intent(this,
                PlayActivity.class);
        it.putExtra("path", videoPath_merge);
        startActivity(it);
    }

    /**
     * 用户放弃该段视频，删除所有视频文件
     */
    private void deleteVideo() {
        if (list != null && list.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确定放弃这段视频吗？");
            builder.setTitle("温馨提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    for (int i = 0; i < list.size(); i++) {
                        File file = new File(list.get(i).getPath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    finish();
                }
            });
            builder.create().show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        deleteVideo();
    }
}

