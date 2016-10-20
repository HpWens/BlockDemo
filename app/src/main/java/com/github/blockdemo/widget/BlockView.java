package com.github.blockdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 10/17 0017.
 */
public class BlockView extends View {

    private Context mContext;

    private Paint mPaint;

    private Paint mTextPaint;

    private Path mPath;

    private String mText;

    private Paint.FontMetrics mMetrics;

    private OnClickListener mOnClickListener;

    private boolean mClickEnable = true;

    public BlockView(Context context) {
        this(context, null);
    }

    public BlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#3F51B5"));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.parseColor("#FFFFFF"));

        mPath = new Path();

        mText = "8";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();

        mPath.moveTo(getWidth() / 2, 0);
        mPath.lineTo(0, getHeight() / 2);
        mPath.lineTo(getWidth() / 2, getHeight());
        mPath.lineTo(getWidth(), getHeight() / 2);

        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mTextPaint.setTextSize(getWidth() / 4);
        mMetrics = mTextPaint.getFontMetrics();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText, getWidth() / 2, getHeight() / 2 +
                (mMetrics.bottom - mMetrics.top) / 2 - mMetrics.bottom, mTextPaint);

    }

    public void setPaintColor(int... colors) {
        if (colors != null)
            mPaint.setColor(colors[0]);
    }

    public void setText(String text) {
        mText = text;
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                if ((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y) <= getWidth() * getWidth() / 2) {
                    if (mOnClickListener != null && mClickEnable) {
                        mOnClickListener.BlockOnClickListener(mText);
                        mClickEnable = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mClickEnable = true;
                break;
            default:
                break;
        }
        return true;
    }

    public interface OnClickListener {
        void BlockOnClickListener(String text);
    }

    public void setBlockOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
