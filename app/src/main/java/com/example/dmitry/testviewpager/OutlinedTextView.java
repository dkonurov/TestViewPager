package com.example.dmitry.testviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class OutlinedTextView extends AppCompatTextView {
    /* ===========================================================
     * Constants
     * =========================================================== */
    private static final float OUTLINE_PROPORTION = 0.1f;

    /* ===========================================================
     * Members
     * =========================================================== */
    private final Paint mStrokePaint = new Paint();

    private final Rect mTextBounds = new Rect();

    private int mOutlineColor = Color.TRANSPARENT;

    /* ===========================================================
     * Constructors
     * =========================================================== */
    public OutlinedTextView(Context context) {
        super(context);
        this.setupPaint();
    }

    public OutlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setupPaint();
    }

    public OutlinedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setupPaint();
    }

    /* ===========================================================
     * Overrides
     * =========================================================== */
    @Override
    protected void onDraw(Canvas canvas) {
        // Get the text to print
        final float textSize = super.getTextSize();
        final String text = super.getText().toString();

        // setup stroke
        mStrokePaint.setColor(mOutlineColor);
        mStrokePaint.setStrokeWidth(textSize * OUTLINE_PROPORTION);
        mStrokePaint.setTextSize(textSize);
        mStrokePaint.setFlags(super.getPaintFlags());
        mStrokePaint.setTypeface(super.getTypeface());

        // Figure out the drawing coordinates
        super.getPaint().getTextBounds(text, 0, text.length(), mTextBounds);

        // draw everything
        canvas.drawText(text,
                super.getWidth() * 0.5f, (super.getHeight() + mTextBounds.height()) * 0.5f,
                mStrokePaint);
        super.onDraw(canvas);
    }

    /* ===========================================================
     * Private/Protected Methods
     * =========================================================== */
    private final void setupPaint() {
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setTextAlign(Paint.Align.CENTER);
    }
}