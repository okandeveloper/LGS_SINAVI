package com.lgs.sinavi;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Geliştirilmiş PomodoroView:
 * - progress gösterir
 * - çalışma modu: sabit accent rengi + pulse animasyonu Activity tarafından yapılıyor
 * - mola modu: renk geçişli animasyon ValueAnimator ile burada çalışır
 */
public class PomodoroView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint innerPaint;
    private RectF arcRect = new RectF();

    private float progress = 0f; // 0..1
    private int progressColor;
    private ValueAnimator colorAnimator;

    public PomodoroView(Context context) {
        super(context);
        init();
    }

    public PomodoroView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PomodoroView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(dp(14));
        backgroundPaint.setColor(getResources().getColor(R.color.progressBackground));

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(dp(14));
        progressColor = getResources().getColor(R.color.accent);
        progressPaint.setColor(progressColor);

        innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setColor(getResources().getColor(R.color.innerCircle));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float padding = dp(10);
        float left = padding;
        float top = padding;
        float right = w - padding;
        float bottom = h - padding;
        arcRect.set(left, top, right, bottom);

        // arka plan çemberi
        canvas.drawArc(arcRect, 0, 360, false, backgroundPaint);

        // progress arc
        float sweep = progress * 360f;
        progressPaint.setColor(progressColor);
        canvas.drawArc(arcRect, -90, sweep, false, progressPaint);

        // iç daire
        float innerRadius = Math.min(w, h) * 0.28f;
        float cx = w / 2f;
        float cy = h / 2f;
        canvas.drawCircle(cx, cy, innerRadius, innerPaint);
    }

    public void setProgress(float p) {
        if (p < 0f) p = 0f;
        if (p > 1f) p = 1f;
        this.progress = p;
        invalidate();
    }

    // Molada renk animasyonu başlat
    public void startBreakColorAnimation() {
        stopColorAnimation(); // varsa önce durdur
        int c1 = getResources().getColor(R.color.breakColorStart);
        int c2 = getResources().getColor(R.color.breakColorEnd);

        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), c1, c2);
        colorAnimator.setDuration(1800);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.addUpdateListener(animation -> {
            progressColor = (int) animation.getAnimatedValue();
            invalidate();
        });
        colorAnimator.start();
    }

    public void stopColorAnimation() {
        if (colorAnimator != null) {
            colorAnimator.cancel();
            colorAnimator = null;
            progressColor = getResources().getColor(R.color.accent); // varsayılan dönüş
            invalidate();
        }
    }

    private float dp(float v) {
        return v * getResources().getDisplayMetrics().density;
    }
}
