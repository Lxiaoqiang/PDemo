package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import rxjava.xq.com.pdemo.R;


public class MyListView extends ListView implements OnScrollListener
{
	private static final String TAG = MyListView.class.getSimpleName();

	/**
	 * 每个屏幕显示多少个Item
	 */
	private int mItemCountInOneScreen;
	/**
	 * 每个Item的高度
	 */
	private int mItemHeight;
	/**
	 * 记录第一个显示的Item
	 */
	private int mFirstVisibleItem = 0;

	private int count;
	public MyListView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);

	}

	/**
	 * 构造方法中拿到自定义属性ItemCount，计算每个Item高度
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyListView(Context context, AttributeSet attrs, int defStyle)
	{
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

		// 设置一个滚动监听
		setOnScrollListener(this);

		Log.e(TAG, mItemCountInOneScreen + "");
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// 滚动结束
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			checkForReset();
		}

	}

	private void checkForReset()
	{
		// 获取第一个Item的top
		int top = getChildAt(0).getTop();
		if (top == 0)
			return;
		// 绝对值不为0时，如果绝对值大于mItemHeight的一半，则收缩，即显示下一个Item
		if (Math.abs(top) > mItemHeight / 2)
		{
			smoothScrollToPosition(this.getFirstVisiblePosition()+1);
//			this.setSelection(mFirstVisibleItem);

			// this.scrollTo(x, y)
			// smoothScrollToPosition(mFirstVisibleItem - 1);
			// scrollBy(0, mItemHeight- Math.abs(top));
			// smoothScrollBy(mItemHeight- Math.abs(top), 200);

		} else
		// 绝对值不为0时，如果绝对值小于于mItemHeight的一半，则展开，显示当前完整的Item
		{

			smoothScrollToPosition(this.getFirstVisiblePosition());
//			this.setSelection(mFirstVisibleItem);

			// this.scrollTo(x, y)
			// smoothScrollBy( -Math.abs(top), 200);
			// smoothScrollByOffset(offset);
			// scrollBy(0, -Math.abs(top));
			// smoothScrollToPosition(mFirstVisibleItem);
		}
		// smoothScrollToPosition(mFirstVisibleItem);
	}

	/**
	 * 滚动过程中不断记录当前显示的第一个Item
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount)
	{
//		mFirstVisibleItem = firstVisibleItem;
		// Log.e(TAG, mFirstVisibleItem + "");
	}

	/**
	 * 对外公布每个Item的高度
	 * 
	 * @return
	 */
	public int getItemHeight()
	{
		return mItemHeight;
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
