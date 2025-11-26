package com.lgs.sinavi;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PomodoroActivity extends AppCompatActivity {


    private PomodoroView pomodoroView;
    private TextView tvTimer, tvState;
    private Button btnStartPause, btnReset, btnSkip;
    private ImageButton btnSettingsIcon;

    private long workDuration = 25 * 60 * 1000L;
    private long shortBreak = 5 * 60 * 1000L;
    private long longBreak = 15 * 60 * 1000L;
    private int cyclesBeforeLong = 4;

    private long totalDuration = workDuration;
    private long remainingMillis = totalDuration;

    private CountDownTimer timer;
    private boolean isRunning = false;
    private State currentState = State.WORK;
    private int workCycleCount = 0;

    private ObjectAnimator pulseAnimator;
    private BottomSheetBehavior<View> sheetBehavior;
    private View bottomSheet;

    private Button btnPreset15, btnPreset25, btnPreset40, btnPreset60, btnApplySettings;
    private EditText etWorkMinutes, etShortBreak, etLongBreak, etCycleCount;

    private enum State { WORK, SHORT_BREAK, LONG_BREAK }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pomodoroView = findViewById(R.id.pomodoroView);
        tvTimer = findViewById(R.id.tvTimer);
        tvState = findViewById(R.id.tvState);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnReset = findViewById(R.id.btnReset);
        btnSkip = findViewById(R.id.btnSkip);
        btnSettingsIcon = findViewById(R.id.btnSettings);
        bottomSheet = findViewById(R.id.bottomSheet);

        if (bottomSheet == null) throw new RuntimeException("bottomSheet NULL!");

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setPeekHeight(300);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        btnPreset15 = bottomSheet.findViewById(R.id.btnPreset15);
        btnPreset25 = bottomSheet.findViewById(R.id.btnPreset25);
        btnPreset40 = bottomSheet.findViewById(R.id.btnPreset40);
        btnPreset60 = bottomSheet.findViewById(R.id.btnPreset60);
        btnApplySettings = bottomSheet.findViewById(R.id.btnApplySettings);

        etWorkMinutes = bottomSheet.findViewById(R.id.etWorkMinutes);
        etShortBreak = bottomSheet.findViewById(R.id.etShortBreak);
        etLongBreak = bottomSheet.findViewById(R.id.etLongBreak);
        etCycleCount = bottomSheet.findViewById(R.id.etCycleCount);

        btnPreset15.setOnClickListener(v -> { etWorkMinutes.setText("15"); applyManualWorkMinutes(15); sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); });
        btnPreset25.setOnClickListener(v -> { etWorkMinutes.setText("25"); applyManualWorkMinutes(25); sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); });
        btnPreset40.setOnClickListener(v -> { etWorkMinutes.setText("40"); applyManualWorkMinutes(40); sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); });
        btnPreset60.setOnClickListener(v -> { etWorkMinutes.setText("60"); applyManualWorkMinutes(60); sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); });

        btnApplySettings.setOnClickListener(v -> {
            applySettingsFromSheet();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null) imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        btnSettingsIcon.setOnClickListener(v -> {
            if (isRunning) {
                showPauseWarningDialog();
            } else {
                toggleBottomSheet();
            }
        });

        btnStartPause.setOnClickListener(v -> { if (isRunning) pauseTimer(); else startTimer(); });
        btnReset.setOnClickListener(v -> resetTimer());
        btnSkip.setOnClickListener(v -> skipPhase());

        setState(State.WORK, true);

        pulseAnimator = ObjectAnimator.ofFloat(pomodoroView, "scaleX", 1f, 1.06f);
        pulseAnimator.setDuration(700);
        pulseAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        pulseAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        pulseAnimator.addUpdateListener(animation -> pomodoroView.setScaleY((float) animation.getAnimatedValue()));
    }

    private void showPauseWarningDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Süreyi Durdur")
                .setMessage("Süreniz devam ediyor. Devam etmek için durdurmak istediğinize emin misiniz?")
                .setIcon(R.drawable.ic_warning) // drawable içine modern icon ekleyin
                .setPositiveButton("Evet, durdur", (dialog, which) -> { pauseTimer(); toggleBottomSheet(); })
                .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        else sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void applyManualWorkMinutes(int minutes) {
        workDuration = minutes * 60 * 1000L;
        totalDuration = workDuration;
        remainingMillis = totalDuration;
        updateUI();
    }

    private void applySettingsFromSheet() {
        try {
            String w = etWorkMinutes.getText().toString().trim();
            String s = etShortBreak.getText().toString().trim();
            String l = etLongBreak.getText().toString().trim();
            String c = etCycleCount.getText().toString().trim();

            if (!w.isEmpty()) workDuration = Integer.parseInt(w) * 60 * 1000L;
            if (!s.isEmpty()) shortBreak = Integer.parseInt(s) * 60 * 1000L;
            if (!l.isEmpty()) longBreak = Integer.parseInt(l) * 60 * 1000L;
            if (!c.isEmpty()) cyclesBeforeLong = Integer.parseInt(c);

            if (currentState == State.WORK) totalDuration = workDuration;
            else if (currentState == State.SHORT_BREAK) totalDuration = shortBreak;
            else totalDuration = longBreak;

            remainingMillis = Math.min(remainingMillis, totalDuration);
            updateUI();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void startTimer() {
        if (isRunning) return;
        isRunning = true;
        btnStartPause.setText("Duraklat");

        timer = new CountDownTimer(remainingMillis, 250) {
            @Override
            public void onTick(long millisUntilFinished) { remainingMillis = millisUntilFinished; updateUI(); }
            @Override
            public void onFinish() { remainingMillis = 0; updateUI(); onPhaseFinished(); }
        }.start();

        if (currentState == State.WORK) { pomodoroView.stopColorAnimation(); pulseAnimator.start(); }
        else { pulseAnimator.cancel(); pomodoroView.startBreakColorAnimation(); }
    }

    private void pauseTimer() {
        if (!isRunning) return;
        isRunning = false;
        btnStartPause.setText("Başlat");
        if (timer != null) { timer.cancel(); timer = null; }
        pulseAnimator.cancel();
        pomodoroView.setScaleX(1f); pomodoroView.setScaleY(1f);
        pomodoroView.stopColorAnimation();
    }

    private void resetTimer() { pauseTimer(); remainingMillis = totalDuration; updateUI(); }
    private void skipPhase() { if (timer != null) { timer.cancel(); timer = null; } remainingMillis = 0; updateUI(); onPhaseFinished(); }

    private void onPhaseFinished() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                v.vibrate(android.os.VibrationEffect.createOneShot(300, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
            else v.vibrate(300);
        }

        if (currentState == State.WORK) {
            workCycleCount++;
            if (workCycleCount % cyclesBeforeLong == 0) setState(State.LONG_BREAK, true);
            else setState(State.SHORT_BREAK, true);
        } else setState(State.WORK, true);

        startTimer();
    }

    private void setState(State newState, boolean resetTime) {
        currentState = newState;
        switch (newState) {
            case WORK: tvState.setText("Çalışma"); totalDuration = workDuration; pomodoroView.stopColorAnimation(); break;
            case SHORT_BREAK: tvState.setText("Kısa Mola"); totalDuration = shortBreak; break;
            case LONG_BREAK: tvState.setText("Uzun Mola"); totalDuration = longBreak; break;
        }
        if (resetTime) remainingMillis = totalDuration;
        updateUI();
    }

    private void updateUI() {
        long seconds = Math.max(0L, remainingMillis / 1000L);
        long min = seconds / 60; long sec = seconds % 60;
        tvTimer.setText(String.format("%02d:%02d", min, sec));

        float progress = 1f - (float) remainingMillis / (float) totalDuration;
        if (totalDuration == 0) progress = 1f;
        pomodoroView.setProgress(progress);

        if (!isRunning) pomodoroView.stopColorAnimation();
    }

    @Override
    protected void onPause() { super.onPause(); if (isRunning) pauseTimer(); }

    @Override
    protected void onDestroy() { super.onDestroy(); if (timer != null) timer.cancel(); if (pulseAnimator != null) pulseAnimator.cancel(); }


}
