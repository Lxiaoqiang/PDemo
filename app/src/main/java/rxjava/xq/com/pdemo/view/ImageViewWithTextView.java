package rxjava.xq.com.pdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import xdemo.xq.com.xdemo.R;

/**
 * Created by lhq on 2016/3/22.
 */
public class ImageViewWithTextView extends View {

    private int mColor = 0xFF45C01A;
    private Bitmap mBitmap;
    private Bitmap mIconBitmap;
    private String mText;
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

    private Rect mIconRect;
    private Rect mTextRect;
    private Paint mTextPaint;
    private Paint mPaint;
    private Canvas mCanvas;
    private float mAlpha = 1.0f;

    public ImageViewWithTextView(Context context) {
        this(context, null);
    }

    public ImageViewWithTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewWithTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrValue(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        setupTargetBitmap(alpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        drawTextSource(canvas,alpha);
        setupTargetText(canvas, alpha);
    }

    private void drawTextSource(Canvas canvas,int alpha) {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextRect.width() / 2;
        int y = mIconRect.bottom + mTextRect.height()-4;
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void setupTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextRect.width() / 2;
        int y = mIconRect.bottom + mTextRect.height()-4;
        canvas.drawText(mText, x, y, mTextPaint);
    }


    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(225);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth =Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextRect.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = (getMeasuredHeight() - mTextRect.height()) / 2 - iconWidth / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrValue(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithTextView);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ImageViewWithTextView_image_icon:
                    BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.ImageViewWithTextView_text:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.ImageViewWithTextView_text_color:
                    mColor = typedArray.getColor(attr, 0xFF45C01A);
                    break;
                case R.styleable.ImageViewWithTextView_text_size:
                    mTextSize = (int) typedArray.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        mTextRect = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

}
