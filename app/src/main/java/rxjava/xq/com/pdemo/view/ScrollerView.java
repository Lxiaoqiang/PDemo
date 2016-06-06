package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖保佑             永无BUG
 * //          佛曰:
 * //                  写字楼里写字间，写字间里程序员；
 * //                  程序人员写程序，又拿程序换酒钱。
 * //                  酒醒只在网上坐，酒醉还来网下眠；
 * //                  酒醉酒醒日复日，网上网下年复年。
 * //                  但愿老死电脑间，不愿鞠躬老板前；
 * //                  奔驰宝马贵者趣，公交自行程序员。
 * //                  别人笑我忒疯癫，我笑自己命太贱；
 * //                  不见满街漂亮妹，哪个归得程序员？
 */
public class ScrollerView extends ViewGroup{

    private Scroller scroller;
    //滑动最小偏移量
    private float touchSlop;
    //手指点下的X坐标
    private float xDown;
    //手指偏移后X坐标
    private float xMove;
    //上次最终X坐标
    private float xLast;
    //左边距
    private int xLeft;
    //右边距
    private int xRight;

    public ScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        scroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        for (int i=0;i<getChildCount();i++){
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            for (int i=0;i<getChildCount();i++){
                View view = getChildAt(i);
                view.layout(i*view.getMeasuredWidth(),0,(i+1)*view.getMeasuredWidth(),view.getMeasuredHeight());
            }
            xLeft = getChildAt(0).getLeft();
            xRight = getChildAt(getChildCount()-1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawY();
                xLast = xDown;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getRawY();
                float diff = Math.abs(xMove-xDown);
                xLast = xMove;
                /**
                 * 如果手指滑动偏移量大于系统滑动最小偏移量则拦截子控件的点击事件
                 */
                if(diff > touchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            /**
             *
             */
//            case MotionEvent.ACTION_DOWN:
//                return true;
            case MotionEvent.ACTION_UP:
                /**
                 * 算出隐藏部分+显示部分的总个数（例：隐藏的部分为1.5个(等于)以上子view，则count就为2，如果隐藏部分为1.5个以下，则count就为1）
                 */
                int totalCount = (getScrollX()+getWidth()/2)/getWidth();
                /**
                 * 实际滑到距离
                 */
                int scrollX = totalCount*getWidth()-getScrollX();
                scroller.startScroll(getScrollX(),0,scrollX,0);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int diff = (int) (xLast-xMove);
                /**
                 * 当滑动偏移量+控件相对于频幕的x轴距离和大于控件左边距则控件已经滑到最左边
                 */
                if(diff+getScrollX()<xLeft){
                    scrollTo(xLeft,0);
                    return true;
                }
                /**
                 * 当滑动偏移量+控件相对于频幕的x轴距离+手指按下的坐标大于控件右边距则控件已经滑到最右边
                 */
                else if(diff+getScrollX()+getWidth()>xRight){
                    scrollTo(xRight-getWidth(),0);
                    return true;
                }
                scrollBy(diff,0);
                xLast = xMove;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
}
