package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Created by lhq on 2016/4/1.
 */
public class CheckableLinearLayout extends RelativeLayout implements Checkable {

    private boolean mChecked;
    private Context mContext;
    private View view;
    public CheckableLinearLayout(Context context) {
        super(context);
        mContext = context;
    }
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        for (int i = 0;i<this.getChildCount();i++) {
            view = this.getChildAt(i);
            if (view instanceof CheckBox){
                ((CheckBox)view).setChecked(checked);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
