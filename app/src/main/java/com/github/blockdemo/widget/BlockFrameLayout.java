package com.github.blockdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

/**
 * Created by Administrator on 10/17 0017.
 */
public class BlockFrameLayout extends FrameLayout {

    private Context mContext;

    private ListAdapter mAdapter;

    private int mChildSize;

    public BlockFrameLayout(Context context) {
        this(context, null);
    }

    public BlockFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mChildSize = dip2px(100);
        //setPadding(mPadding, mPadding, mPadding, mPadding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = dip2px(200);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension);
        }

        measureChildViews();

    }


    private void measureChildViews() {

        final int childCount = getChildCount();

        int childMode = MeasureSpec.EXACTLY;

        int childSize = mChildSize;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            int measureSpec = -1;

            measureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);

            //childView.measure(measureSpec,measureSpec);

            measureChildren(measureSpec, measureSpec);

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onChildLayout();
    }

    //凸
    private void onChildLayout() {

        final int childCount = getChildCount();

        int startX = 0;

        int startY = 0;

        int bulge = 0;

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            if ((i + 1) % 3 == 1) {
                startX = getWidth() / 2 - mChildSize / 2;
                startY = mChildSize * bulge;
            } else if ((i + 1) % 3 == 2) {
                startX = getWidth() / 2 - mChildSize;
                startY = mChildSize * bulge + mChildSize / 2;
            } else if ((i + 1) % 3 == 0) {
                startX = getWidth() / 2;
                startY = mChildSize * bulge + mChildSize / 2;
                bulge++;
            }

            childView.layout(startX, startY, startX + mChildSize, startY + mChildSize);

        }

    }

    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
    }

    private void buildItems() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final View itemView = mAdapter.getView(i, null, this);
            final int position = i;
            if (itemView != null) {

            }
            addView(itemView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        if (mAdapter != null && !mAdapter.isEmpty()) {
            buildItems();
        }
        super.onAttachedToWindow();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
