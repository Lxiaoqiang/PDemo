package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import rxjava.xq.com.pdemo.R;


/**
 * Created by lhq on 2016/5/24.
 */
public class MyRecycleView extends RecyclerView {
    private static final String TAG = RecyclerView.class.getSimpleName();
    /**
     * 每个屏幕显示多少个Item
     */
    private int mItemCountInOneScreen;
    /**
     * 每个Item的高度
     */
    private int mItemHeight;
    public MyRecycleView(Context context) {
        this(context,null);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        // 获取MyListView_itemCount
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MyListView, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.MyListView_itemCount:
                    mItemCountInOneScreen = a.getInt(attr, 6);
                    break;
            }
        }
        a.recycle();

        // 计算每个Item高度
        mItemHeight = (outMetrics.heightPixels - getStatusHeight(context))
                / mItemCountInOneScreen;

        Log.e(TAG, mItemCountInOneScreen + "");
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // 滚动结束
                if (newState ==  RecyclerView.SCROLL_STATE_IDLE)
                {
                    checkForReset();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP)
        {
            checkForReset();
            return true;
        }
        return super.onTouchEvent(ev);
    }
    private void checkForReset()
    {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        // 获取第一个Item的top
        int top = getChildAt(0).getTop();
        if (top == 0)
            return;
        // 绝对值不为0时，如果绝对值大于mItemHeight的一半，则收缩，即显示下一个Item
        if (Math.abs(top) > mItemHeight / 2)
        {
            smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition()+1);

        } else
        // 绝对值不为0时，如果绝对值小于于mItemHeight的一半，则展开，显示当前完整的Item
        {

            smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition());
        }
    }
    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public int getStatusHeight(Context context)
    {

        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
