package rxjava.xq.com.pdemo.utils;

import android.os.Build;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by lhq on 2016/4/12.
 * view的点击效果
 */
public class ClickStateUtils {
    /**
     * 点击改变透明度，实现类似selector的效果
     * @param view
     */
    public static void click(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        view.getBackground().setAlpha(170);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.getBackground().setAlpha(255);
                }
                return false;
            }
        });
    }
}
