package com.amxc.jc.admin.coustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Printer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.text.NumberFormat;


/**
 * Created by admin on 2018/1/16.
 */

public class SeekBar extends View implements GestureDetector.OnGestureListener {
    private final String TAG = this.getClass().getSimpleName();
    private int max;
    private int min;
    private String suffix;
    private int progressColor;
    private int backgroundColor;
    private float progressHeigh;
    private float suffixTextSize;
    private int suffixTextColor;
    private float textPadding;
    private int suffixScale;

    private Paint mProgressPaint;
    private Paint mBgPaint;
    private Paint mSuffixTextPaint;
    private Paint thumbPaint;
    private Paint thumbCoverPaint;
    // 宽高
    private int mWidth, mHeight;

    private float progressWidth;
    private float thumbRadius;

    private int progress;
    private int realProgress;
    private float progressToTop;

    private OnSeekBarChangeListener onSeekBarChangeListener;

    private GestureDetector gestureDetector;


    public SeekBar(Context context) {
        this(context, null);
    }

    public SeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBar);
        min = typedArray.getInt(R.styleable.SeekBar_min, 0);
        max = typedArray.getInt(R.styleable.SeekBar_max, 100);
        suffix = typedArray.getString(R.styleable.SeekBar_seekSuffix);
        progressColor = typedArray.getColor(R.styleable.SeekBar_progressColor, getResources().getColor(R.color.colorAccent));
        backgroundColor = typedArray.getColor(R.styleable.SeekBar_backgroundColor, getResources().getColor(R.color.c2));
        progressHeigh = typedArray.getDimension(R.styleable.SeekBar_progressHeigh, 10);
        progress = typedArray.getInt(R.styleable.SeekBar_progress, 0);
        suffixTextSize = typedArray.getDimension(R.styleable.SeekBar_seekSuffixTextSize, 20);
        suffixTextColor = typedArray.getColor(R.styleable.SeekBar_seekSuffixTextColor, getResources().getColor(R.color.black));
        textPadding = typedArray.getDimension(R.styleable.SeekBar_textPadding, 0);
        thumbRadius = typedArray.getDimension(R.styleable.SeekBar_thumbRadius, 0);
        suffixScale = typedArray.getInt(R.styleable.SeekBar_seekSuffixScale, 2);
        if (suffixScale < 0) {
            suffixScale = 2;
        }
        typedArray.recycle();
        gestureDetector = new GestureDetector(context, this);
        initPaint();
    }

    public OnSeekBarChangeListener getOnSeekBarChangeListener() {
        return onSeekBarChangeListener;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getProgressHeigh() {
        return progressHeigh;
    }

    public void setProgressHeigh(float progressHeigh) {
        this.progressHeigh = progressHeigh;
    }

    public float getSuffixTextSize() {
        return suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
    }

    public int getSuffixTextColor() {
        return suffixTextColor;
    }

    public void setSuffixTextColor(int suffixTextColor) {
        this.suffixTextColor = suffixTextColor;
    }

    public float getTextPadding() {
        return textPadding;
    }

    public void setTextPadding(float textPadding) {
        this.textPadding = textPadding;
    }

    public int getSuffixScale() {
        return suffixScale;
    }

    public void setSuffixScale(int suffixScale) {
        this.suffixScale = suffixScale;
    }

    public Paint getmProgressPaint() {
        return mProgressPaint;
    }

    public void setmProgressPaint(Paint mProgressPaint) {
        this.mProgressPaint = mProgressPaint;
    }

    public Paint getmBgPaint() {
        return mBgPaint;
    }

    public void setmBgPaint(Paint mBgPaint) {
        this.mBgPaint = mBgPaint;
    }

    public Paint getmSuffixTextPaint() {
        return mSuffixTextPaint;
    }

    public void setmSuffixTextPaint(Paint mSuffixTextPaint) {
        this.mSuffixTextPaint = mSuffixTextPaint;
    }

    public Paint getThumbPaint() {
        return thumbPaint;
    }

    public void setThumbPaint(Paint thumbPaint) {
        this.thumbPaint = thumbPaint;
    }

    public Paint getThumbCoverPaint() {
        return thumbCoverPaint;
    }

    public void setThumbCoverPaint(Paint thumbCoverPaint) {
        this.thumbCoverPaint = thumbCoverPaint;
    }

    public float getThumbRadius() {
        return thumbRadius;
    }

    public void setThumbRadius(float thumbRadius) {
        this.thumbRadius = thumbRadius;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setRealProgress(int realProgress) {
        this.realProgress = realProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        progressToTop = (float) (suffixTextSize + textPadding +
                Arith.sub(thumbRadius, Arith.div(progressHeigh, 2))) + getPaddingTop();
        progressWidth = progressToWith(progress);

        RectF progressRectF = new RectF(0, progressToTop, progressWidth + thumbRadius, progressHeigh + progressToTop);
        canvas.drawRoundRect(progressRectF, (float) Arith.div(progressHeigh, 2), (float) Arith.div(progressHeigh, 2), mProgressPaint);

        RectF bgRect = new RectF(progressWidth + thumbRadius, progressToTop, mWidth, progressHeigh + progressToTop);
        canvas.drawRoundRect(bgRect, (float) Arith.div(progressHeigh, 2), (float) Arith.div(progressHeigh, 2), mBgPaint);

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String text = numberFormat.format(getRealProgress()) + suffix;
        float textWidth = mSuffixTextPaint.measureText(text);
        canvas.drawText(text, Math.min(progressWidth, mWidth - textWidth),
                suffixTextSize + getPaddingTop(), mSuffixTextPaint);

        drawThumb(canvas, progressToTop);
    }

    private void drawThumb(Canvas canvas, float progressToTop) {
        float thumbWidth = Math.min(progressWidth + thumbRadius, mWidth - thumbRadius);
        canvas.drawCircle(thumbWidth, (float) (progressToTop + Arith.div(progressHeigh, 2)),
                thumbRadius, thumbPaint);

        canvas.drawCircle(thumbWidth, (float) (progressToTop + Arith.div(progressHeigh, 2)),
                thumbRadius - 5, thumbCoverPaint);
        float thumb = 20f;
        canvas.translate(thumbWidth, (float) (progressToTop + Arith.div(progressHeigh, 2)));
        canvas.rotate(45);
        RectF rectF = new RectF(0 - thumb / 2, 0 - thumb / 2, 0 + thumb / 2, thumb / 2);
        canvas.drawRect(rectF, thumbPaint);
    }

    private float getRealProgress() {
        //(progress/100)*(max-min)+min
        return (float) Math.max(Math.min(Arith.round(Arith.add(Arith.mul(Arith.div(progress, 100), Arith.sub(max, min)), min)
                , suffixScale), max), min);
    }

    private int withToProgress(float progressWidth) {
        progress = (int) Arith.mul(Arith.div(progressWidth, mWidth), 100);
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onProgressChange(this, progress);
        }
        return progress;
    }

    private float progressToWith(float progress) {
        //progress*(mWidth-thumbRadius*2)
        return (float) Math.max(Arith.mul(Arith.div(progress, 100), Arith.sub(mWidth, Arith.mul(thumbRadius, 2))), 0);
    }

    private void initPaint() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
         /*
         * 设置画笔样式为描边，圆环嘛……当然不能填充不然就么意思了
         *
         * 画笔样式分三种：
         * 1.Paint.Style.STROKE：描边
         * 2.Paint.Style.FILL_AND_STROKE：描边并填充
         * 3.Paint.Style.FILL：填充
         */
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setAntiAlias(true);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(backgroundColor);

        mSuffixTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSuffixTextPaint.setAntiAlias(true);
        mSuffixTextPaint.setColor(suffixTextColor);
        mSuffixTextPaint.setTextSize(suffixTextSize);

        thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setColor(progressColor);

        thumbCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbCoverPaint.setStyle(Paint.Style.FILL);
        thumbCoverPaint.setColor(getResources().getColor(R.color.white));

    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : (int) (suffixTextSize + textPadding + Arith.sub(dp2px(thumbRadius), 2));
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                    mWidth = result;
                } else {
                    result = Math.min(result, size);
                    mHeight = result;
                }
            }
        }
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(TAG, "按下");
        float y = event.getY();
        Log.d(TAG, "x:" + event.getX() + ";y:" + event.getY());
        if (y >= progressToTop && y <= Arith.add(progressToTop, Arith.mul(thumbRadius, 2))) {
            progress = withToProgress(event.getX());
            postInvalidate();
            return true;
        } else {
            return false;
        }
    }


    /**
     * 用户按下屏幕并且没有移动或松开。主要是提供给用户一个可视化的反馈，告诉用户他们的按下操作已经
     * 被捕捉到了。如果按下的速度很快只会调用onDown(),按下的速度稍慢一点会先调用onDown()再调用onShowPress().
     *
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll");
        int verticalMinDistance = 2;
        if (distanceX < -verticalMinDistance) {
            Log.d(TAG, "向右手势");
            float y = event.getY();
            Log.d(TAG, "x:" + event.getX() + ";y:" + event.getY());
            if (y >= progressToTop && y <= Arith.add(progressToTop, Arith.mul(thumbRadius, 2))) {
                progress = withToProgress(event.getX());
                postInvalidate();
            }
        } else if (distanceX > verticalMinDistance) {
            float y = event.getY();
            Log.d(TAG, "x:" + event.getX() + ";y:" + event.getY());
            if (y >= progressToTop && y <= Arith.add(progressToTop, Arith.mul(thumbRadius, 2))) {
                progress = withToProgress(event.getX());
                postInvalidate();
            }
            Log.d(TAG, "向左手势");

        } else if (distanceY < -verticalMinDistance) {
            Log.d(TAG, "向下手势");

        } else if (distanceY > verticalMinDistance) {
            Log.d(TAG, "向上手势");
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(TAG, "onLongPress");
        float y = event.getY();
        Log.d(TAG, "x:" + event.getX() + ";y:" + event.getY());
        if (y >= progressToTop && y <= Arith.add(progressToTop, Arith.mul(thumbRadius, 2))) {
            progress = withToProgress(event.getX());
            postInvalidate();
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling");
        return true;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChange(SeekBar seekBar, int progress);
    }
}
