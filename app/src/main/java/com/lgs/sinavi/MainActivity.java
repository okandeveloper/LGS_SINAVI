package com.lgs.sinavi;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView txtDay, txtHour, txtMin, txtSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // TextView'leri bağla
        txtDay = findViewById(R.id.txtDay);
        txtHour = findViewById(R.id.txtHour);
        txtMin = findViewById(R.id.txtMin);
        txtSec = findViewById(R.id.txtSec);

        CardView btnPomodoro = findViewById(R.id.btnPomodoro);
        CardView btnDersTakip = findViewById(R.id.btnDersTakip);
        CardView btnDersProgrami = findViewById(R.id.btnDersProgrami);

        btnPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Eğer PomodoroActivity'ye bazı ayarlar/parametreler göndermek isterseniz,
                // intent.putExtra("workMinutes", 25); gibi ekleyebilirsiniz.
                Intent intent = new Intent(MainActivity.this, PomodoroActivity.class);
                startActivity(intent);
                // Eğer MainActivity'yi kapatmak isterseniz uncomment yapın:
                // finish();
            }
        });

        btnDersTakip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Eğer PomodoroActivity'ye bazı ayarlar/parametreler göndermek isterseniz,
                // intent.putExtra("workMinutes", 25); gibi ekleyebilirsiniz.
                Intent intent = new Intent(MainActivity.this, DersTakipActivity.class);
                startActivity(intent);
                // Eğer MainActivity'yi kapatmak isterseniz uncomment yapın:
                // finish();
            }
        });
        btnDersProgrami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Eğer PomodoroActivity'ye bazı ayarlar/parametreler göndermek isterseniz,
                // intent.putExtra("workMinutes", 25); gibi ekleyebilirsiniz.
                Intent intent = new Intent(MainActivity.this, DersProgramiActivity.class);
                startActivity(intent);
                // Eğer MainActivity'yi kapatmak isterseniz uncomment yapın:
                // finish();
            }
        });


        // Bounce animasyonu
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Hedef tarih: 2 Haziran 2026
        Calendar target = Calendar.getInstance();
        target.set(2026, Calendar.JUNE, 2, 0, 0, 0);

        Calendar now = Calendar.getInstance();
        long diff = target.getTimeInMillis() - now.getTimeInMillis();

        // Süre geçmişse sıfırla
        if (diff <= 0) {
            txtDay.setText("0");
            txtHour.setText("0");
            txtMin.setText("0");
            txtSec.setText("0");
            return;
        }

        // Geri sayım başlat
        new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millis) {

                long days = millis / (1000 * 60 * 60 * 24);
                long hours = (millis / (1000 * 60 * 60)) % 24;
                long min = (millis / (1000 * 60)) % 60;
                long sec = (millis / 1000) % 60;

                txtDay.setText(String.valueOf(days));
                txtHour.setText(String.valueOf(hours));
                txtMin.setText(String.valueOf(min));
                txtSec.setText(String.valueOf(sec));

                // Sadece saniye sürekli zıplasın (daha eğlenceli)
                txtSec.startAnimation(bounce);
            }

            @Override
            public void onFinish() {}
        }.start();
    }
}
