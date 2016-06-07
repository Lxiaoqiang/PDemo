package rxjava.xq.com.pdemo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import rxjava.xq.com.pdemo.R;

public class PlayActivity extends AppCompatActivity {
    /**视频控件*/
    private VideoView videoview;
    /**传过来的路径*/
    private String path;
    /**播放按钮*/
    private ImageView img_start;

    private RelativeLayout play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        videoview = (VideoView) findViewById(R.id.videoView);
        play = (RelativeLayout) findViewById(R.id.play);
        RelativeLayout.LayoutParams layoutParams=
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        img_start = (ImageView) findViewById(R.id.img_start);
//        path  = "/storage/emulated/0/DemoVideo/1463472142798.mp4";///storage/emulated/0/DemoVideo/1463472142798.mp4
        path = getIntent().getExtras().getString("path");
        videoview.setVideoPath(path);
        videoview.setVideoURI(Uri.parse(path));
        videoview.setLayoutParams(layoutParams);
        videoview.requestFocus();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoview.isPlaying()) {
                    videoview.pause();
                    img_start.setVisibility(View.VISIBLE);
                }else{
                    videoview.start();
                    img_start.setVisibility(View.GONE);
                }
            }
        });
    }
}
