package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by lhq on 2016/4/8.
 */
public class SlidingMenu extends HorizontalScrollView {
    private LinearLayout mWapper;
    private ViewGroup mContent;
    private ViewGroup menu;
    private int mMenuWith;
    private int menuRightPadding;
    private int mScreenWith;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWith = displayMetrics.widthPixels;
        menuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, displayMetrics);

    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWapper = (LinearLayout) getChildAt(0);
        menu = (ViewGroup) mWapper.getChildAt(0);
        mContent = (ViewGroup) mWapper.getChildAt(1);
        mMenuWith = menu.getLayoutParams().width = mScreenWith - menuRightPadding;
        mContent.getLayoutParams().width = mScreenWith;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //设置初始位置
        if (changed)
            this.scrollTo(mMenuWith, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mScreenWith / 2) {
                    this.smoothScrollTo(mMenuWith, 0);
                } else {
                    this.smoothScrollTo(0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        ViewHelper.setTranslationX(menu,l*0.5f);
    }
}
