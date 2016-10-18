# BlockDemo

首先给各位道个歉，公司加班已有两个多月，博客也迟迟没有更新。还非常感谢认真阅读博客并提出错误的地方的童鞋，我也非常鼓励这种做法，对任何有疑问的地方，大胆提出。给你们点个赞。

老规矩，先来看看魔方的效果图：

![block](http://img.blog.csdn.net/20161017230805307)

学习博客也是学习一种变通的思想，能够举一反三，才能掌握真正的精髓。

这里留下一个小小的挑战：

![block](http://img.blog.csdn.net/20161017231240950)

怎么获取菱形区域的点击事件？欢迎留言，欢迎讨论，谢谢。

我临时的处理方法是以菱形的内切圆来处理点击事情的。

###魔方布局（BlockFrameLayout）

分析效果图，可以看出魔方布局是由一块块小的菱形按照某种规律组合而成的，我们这里以繁化简，先来看看一块小的菱形：

![block](http://img.blog.csdn.net/20161018110743671)

接着看看三块菱形组成的图案：

![block](http://img.blog.csdn.net/20161018111151501)

看看最后的效果图：

![block](http://img.blog.csdn.net/20161018111456124)

是不是已经发现规律了啊？对的，以三块菱形为一组，从上往下就组成了最终的图案。

核心思想：分析前三块菱形和后三块菱形的坐标变化。

相信你已经找到了：

```
    if ((i + 1) % 3 == 1) { //第一块
        startX = getWidth() / 2 - mChildSize / 2;
        startY = mChildSize * bulge;
    } else if ((i + 1) % 3 == 2) { //第二块
        startX = getWidth() / 2 - mChildSize;
        startY = mChildSize * bulge + mChildSize / 2;
    } else if ((i + 1) % 3 == 0) { //第三块
        startX = getWidth() / 2;
        startY = mChildSize * bulge + mChildSize / 2;
        bulge++;
    }
```

那么 onLayout 方法：


```
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
```

如果对于自定义 ViewGroup 流程有什么疑问的童鞋，请查看我自定义系类前面的几篇博客。

###菱形（BlockView ）

BlockView 类比较简单，我这里就直接贴出代码，有什么疑问的请留言，会第一时间给你回复。

```
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
                   //注意下这里以菱形的内切圆来处理点击事件，如果你有什么好的方案请留言
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

```
