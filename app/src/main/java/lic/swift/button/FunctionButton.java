package lic.swift.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class FunctionButton extends View {

    private final Paint paint;
    private static final int BACKGROUND_START_COLOR = Color.parseColor("#FFC9BFFF");
    private static final int BACKGROUND_END_COLOR = Color.parseColor("#FF91C2FE");
    private LinearGradient backgroundGradient;

    private static final int FOREGROUND_START_COLOR = Color.parseColor("#FF9980FF");
    private static final int FOREGROUND_END_COLOR = Color.parseColor("#FF2386FD");
    private LinearGradient foregroundGradient;

    private RectF contentRect;
    private float contentRectRadius;

    private final RectF foregroundRect = new RectF();

    private String text;
    private static final int textColor = Color.WHITE;
    private float baseline;

    public FunctionButton(Context context) {
        this(context, null);
    }

    public FunctionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunctionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);

        setTextSizeSP(18, context);
    }

    public void setTextSizeSP(float value, Context context) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics()));
        baseline = Math.abs(paint.ascent() + paint.descent()) / 2;
    }

    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public void setProgress(float progress) {
        if (contentRect == null)
            return;

        foregroundRect.set(contentRect.left, contentRect.top, contentRect.left, contentRect.bottom);
        foregroundRect.union(contentRect.left + contentRect.width() * progress, contentRect.centerY());
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        final int contentWidth = width - paddingLeft - paddingRight;
        final int contentHeight = height - paddingTop - paddingBottom;
        contentRect = new RectF(paddingLeft, paddingTop, contentWidth, contentHeight);

        backgroundGradient = new LinearGradient(contentRect.left, contentRect.top, contentRect.right, contentRect.bottom,
                BACKGROUND_START_COLOR, BACKGROUND_END_COLOR, Shader.TileMode.MIRROR);

        foregroundGradient = new LinearGradient(contentRect.left, contentRect.top, contentRect.right, contentRect.bottom,
                FOREGROUND_START_COLOR, FOREGROUND_END_COLOR, Shader.TileMode.MIRROR);

        contentRectRadius = contentRect.height() / 2F;

        setProgress(1F);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (contentRect == null)
            return;

        paint.setShader(backgroundGradient);
        canvas.drawRoundRect(contentRect, contentRectRadius, contentRectRadius, paint);

        canvas.save();
        canvas.clipRect(foregroundRect);
        paint.setShader(foregroundGradient);
        canvas.drawRoundRect(contentRect, contentRectRadius, contentRectRadius, paint);
        canvas.restore();

        if (TextUtils.isEmpty(text))
            return;

        paint.setShader(null);
        paint.setColor(textColor);
        canvas.drawText(text, contentRect.centerX(), contentRect.centerY() + baseline, paint);

    }

}