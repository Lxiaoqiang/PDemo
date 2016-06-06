package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ListView;

/**
 * Created by lhq on 2016/5/5.
 */
public class ScrollOneItemListView extends ListView {
    private String TAG = ScrollOneItemListView.class.getSimpleName();
    int width;
    int height;

    public ScrollOneItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ScrollOneItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollOneItemListView(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (getChildCount()>0)
//            this.getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }


}
