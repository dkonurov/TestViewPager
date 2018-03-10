package com.example.dmitry.testviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class FadedTextView extends View {
    private String mOutText;

    private TextPaint mInTextPaint;

    private String mInText;

    private StaticLayout mInStaticLayout;

    private TextPaint mOutTextPaint;

    private StaticLayout mOutStaticLayout;

    private int mWidth = -1;

    public FadedTextView(Context context) {
        super(context);
        initLabelView();
    }

    public FadedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLabelView();
    }

    public FadedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLabelView();
    }

    /* ===========================================================
     * Overrides
     * =========================================================== */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        if (mInStaticLayout != null) {
            mInStaticLayout.draw(canvas);
        }
        if (mOutStaticLayout != null) {
            mOutStaticLayout.draw(canvas);
        }
        canvas.restore();

    }

    public void setOutText(String text) {
        mOutText = text;
        update();
    }

    private void update() {
        initLabelView();
        requestLayout();
    }

    public void setInAlpha(float alpha) {
        mInTextPaint.setAlpha((int) (alpha * 255));
        update();
    }

    public void setOutAlpha(float alpha) {
        mOutTextPaint.setAlpha((int) (alpha * 255));
        update();
    }

    public void setInText(String inText) {
        mInText = inText;
        update();
    }


    private void initLabelView() {
        if (mInTextPaint == null) {
            initInTextPaint();
        }
        if (mOutTextPaint == null) {
            initOutTextPaint();
        }

        if (!TextUtils.isEmpty(mInText)) {
            // default to a single line of text
            int widthIn;
            if (mWidth < 0) {
                widthIn = (int) mInTextPaint.measureText(mInText);
            } else {
                widthIn = mWidth;
            }
            mInStaticLayout = new StaticLayout(mInText, mInTextPaint, widthIn, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        }

        if (!TextUtils.isEmpty(mOutText)) {
            int widthOut;
            if (mWidth < 0) {
                widthOut = (int) mOutTextPaint.measureText(mOutText);
            } else {
                widthOut = mWidth;
            }
            mOutStaticLayout = new StaticLayout(mOutText, mOutTextPaint, widthOut, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        }
    }

    private void initOutTextPaint() {
        mOutTextPaint = new TextPaint();
        mOutTextPaint.setAntiAlias(true);
        mOutTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mOutTextPaint.setColor(Color.BLACK);
    }

    private void initInTextPaint() {
        mInTextPaint = new TextPaint();
        mInTextPaint.setAntiAlias(true);
        mInTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mInTextPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Tell the parent layout how big this view would like to be
        // but still respect any requirements (measure specs) that are passed down.

        // determine the width
        int width;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthRequirement = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthRequirement;
        } else {
            width = measureWidth(widthMode, widthRequirement);
        }

        // determine the height
        int height;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightRequirement = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightRequirement;
        } else {
            height = measureHeight(heightMode, heightRequirement);
        }

        // Required call: set width and height
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int heightMode, int heightRequirement) {
        int height;
        int heightIn = 0;
        int heightOut = 0;
        if (mInStaticLayout != null) {
            heightIn = mInStaticLayout.getHeight() + getPaddingTop() + getPaddingBottom();
        }
        if (mOutStaticLayout != null) {
            heightOut = mOutStaticLayout.getHeight() + getPaddingTop() + getPaddingBottom();
        }
        height = Math.max(heightIn, heightOut);
        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(height, heightRequirement);
        }
        return height;
    }

    private int measureWidth(int widthMode, int widthRequirement) {
        int widthIn = 0;
        int width;
        int widthOut = 0;
        if (mInStaticLayout != null) {
            widthIn = mInStaticLayout.getWidth() + getPaddingLeft() + getPaddingRight();
        }
        if (mOutStaticLayout != null) {
            widthOut = mOutStaticLayout.getWidth() + getPaddingLeft() + getPaddingRight();
        }
        width = Math.max(widthIn, widthOut);
        if (widthMode == MeasureSpec.AT_MOST) {
            if (width > widthRequirement) {
                width = widthRequirement;
                mWidth = width;
                // too long for a single line so relayout as multiline
                if (!TextUtils.isEmpty(mInText)) {
                    mInStaticLayout = new StaticLayout(mInText, mInTextPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                } else {
                    mOutStaticLayout = null;
                }
                if (!TextUtils.isEmpty(mOutText)) {
                    mOutStaticLayout = new StaticLayout(mOutText, mOutTextPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                } else {
                    mOutStaticLayout = null;
                }
            }
        }
        return width;
    }

    void setTextSize(int textSize) {
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getContext().getResources().getDisplayMetrics());
        mInTextPaint.setTextSize(size);
        mOutTextPaint.setTextSize(size);
        update();
    }
}