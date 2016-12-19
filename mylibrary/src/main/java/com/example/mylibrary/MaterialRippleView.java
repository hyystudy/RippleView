package com.example.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/12/18/018.
 */

public class MaterialRippleView extends FrameLayout{

    private static final String TAG = "MaterialRippleView";
    private static final int ROUND_RIPPLE = 0;
    private static final int RECTANGLE_RIPPLE = 1;
    private static final int DURATION = 400;
    private static final int FRAME_RATE = 10;
    private GestureDetector mGestureDetector;
    private float WIDTH = -1;
    private float HEIGHT = -1;
    private float mToutchX = -1;
    private float mTouchY = -1;
    private boolean mIsAniming;
    private Paint mPaint;
    private int mRippleType = 1;
    private float MAX_RADIUS;
    private int mTimer;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public MaterialRippleView(Context context) {
        this(context, null);
    }

    public MaterialRippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setDrawingCacheEnabled(true);
        setClickable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#10ffffff"));

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        WIDTH = w;
        HEIGHT = h;

        if (mRippleType == ROUND_RIPPLE) {
            MAX_RADIUS = Math.min(WIDTH, HEIGHT)/2;
        } else if (mRippleType == RECTANGLE_RIPPLE){
            MAX_RADIUS = Math.max(WIDTH, HEIGHT);
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIsAniming) {
                float fraction = getFraction();
                if (fraction < 1.0f) {
                    //canvas.save();
                    Log.d(TAG, "dispatchDraw: " + fraction);
                    canvas.drawCircle(mToutchX, mTouchY, MAX_RADIUS * fraction, mPaint);
                    mTimer ++;
                    mHandler.postDelayed(mRunnable, FRAME_RATE);
                } else {
                    mTimer = 0;
                    mIsAniming = false;
                    canvas.restore();
                }
                Log.d(TAG, "draw: ");
        }

    }

    private float getFraction() {
        return mTimer * FRAME_RATE * 1.0f/ DURATION;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)){
            Log.d(TAG, "onTouchEvent: ");
            animateRipple(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    private void animateRipple(MotionEvent event) {
        createAnim(event.getX(), event.getY());
    }

    private void createAnim(float x, float y) {
        if (mIsAniming) return;
        mIsAniming = true;
        if (mRippleType == ROUND_RIPPLE) {
            mToutchX = WIDTH/2;
            mTouchY = HEIGHT/2;
        } else {
            mToutchX = x;
            mTouchY = y;
        }
        invalidate();
    }
}
