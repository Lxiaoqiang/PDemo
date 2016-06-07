package rxjava.xq.com.pdemo.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import rxjava.xq.com.pdemo.activity.PlayActivity;
import rxjava.xq.com.pdemo.entity.VideoBean;
import rxjava.xq.com.pdemo.utils.VideoUtil;

/**
 * Created by lhq on 2016/5/25.
 */
public class MediaRecorderHelper {
    MediaRecorder mMediaRecorder;

    public static final int ERROR = -1;
    public static final int RECORD_TIME_ERROR = 0;
    /**
     * 最大录制时间
     */
    private int maxTime = 20;
    /**
     * 录制视频保存文件
     */
    private String vedioPath;
    private ArrayList<VideoBean> list;
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

    public MediaRecorderHelper() {
        bean = new VideoBean();
        list = new ArrayList<>();

        File file = new File(Ppath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void startRecord(SurfaceHolder mHolder,Camera camera) {
        vedioPath = Ppath + System.currentTimeMillis() + ".mp4";
        bean.setPath(vedioPath);
        mMediaRecorder = new MediaRecorder();
        if (camera != null) {
            camera.unlock();
            mMediaRecorder.setCamera(camera);
        }
        // 设置录制视频源为Camera(相机)
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (CameraHelper.cameraPosition == CameraHelper.FONT) {
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
        // 最大期限
        mMediaRecorder.setMaxDuration(maxTime * 1000);
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
            if (onMediaRecorderListener != null)
                onMediaRecorderListener.onFailure(ERROR);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            if (onMediaRecorderListener != null)
                onMediaRecorderListener.onFailure(ERROR);
            return;
        }
    }

    public void stopRecord() {
        if (bean != null){
            list.add(bean);
        }
        if (mMediaRecorder != null) {
            //设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            // 停止
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;

        }
    }
    private void onComplete(Activity activity) {
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

        Intent it = new Intent(activity,
                PlayActivity.class);
        it.putExtra("path", videoPath_merge);
        activity.startActivity(it);
    }
    /**
     * 用户放弃该段视频，删除所有视频文件
     */
    private void deleteVideo(final Activity activity) {
        if (list != null && list.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                    activity.finish();
                }
            });
            builder.create().show();
        } else {
            activity.finish();
        }
    }


    OnMediaRecorderListener onMediaRecorderListener;

    public void setOnMediaRecorderListener(OnMediaRecorderListener onMediaRecorderListener) {
        this.onMediaRecorderListener = onMediaRecorderListener;
    }

    public interface OnMediaRecorderListener {
        void onFailure(int message);
    }

}
