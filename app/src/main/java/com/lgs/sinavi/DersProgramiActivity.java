package com.lgs.sinavi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DersProgramiActivity extends AppCompatActivity {

    private List<DersProgrami> dersList;
    private DersAdapter adapter;
    private SharedPreferences preferences;
    private final String KEY = "ders_programi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_programi);

        preferences = getSharedPreferences("ders_pref", MODE_PRIVATE);

        dersList = loadDersler();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DersAdapter(this, dersList, new DersAdapter.OnItemClickListener() {
            @Override
            public void onDelete(int position) {
                dersList.remove(position);
                saveDersler();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onEdit(int position) {
                showDersDialog(dersList.get(position), position);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showDersDialog(null, -1));
    }

    private void showDersDialog(DersProgrami ders, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_ders, null);
        builder.setView(view);

        Spinner spinnerDers = view.findViewById(R.id.spinnerDers);
        EditText konuEt = view.findViewById(R.id.konuEt);
        EditText sureEt = view.findViewById(R.id.sureEt);
        Spinner gunSpinner = view.findViewById(R.id.spinnerGun);

        String[] dersler = {"Türkçe","Matematik","Din Kültürü","İnkılap","Fen","İngilizce","Serbest Çalışma","Ödev"};
        ArrayAdapter<String> adapterDers = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dersler);
        spinnerDers.setAdapter(adapterDers);

        String[] gunler = {"Pazartesi","Salı","Çarşamba","Perşembe","Cuma","Cumartesi","Pazar"};
        ArrayAdapter<String> adapterGun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gunler);
        gunSpinner.setAdapter(adapterGun);

        if(ders != null){
            spinnerDers.setSelection(adapterDers.getPosition(ders.getDersAdi()));
            konuEt.setText(ders.getKonu());
            sureEt.setText(String.valueOf(ders.getSure()));
            gunSpinner.setSelection(adapterGun.getPosition(ders.getGun()));
        }

        builder.setPositiveButton(ders == null ? "Ekle" : "Güncelle", null); // Sonradan override edilecek
        builder.setNegativeButton("İptal", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Pozitif butonu override edip kontrol ekleyelim
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String dersAdi = spinnerDers.getSelectedItem().toString();
            String konu = konuEt.getText().toString().trim();
            String sureStr = sureEt.getText().toString().trim();
            String gun = gunSpinner.getSelectedItem().toString();

            if(dersAdi.isEmpty() || konu.isEmpty() || sureStr.isEmpty() || gun.isEmpty()) {
                Toast.makeText(DersProgramiActivity.this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                return; // Eklemeyi durdur
            }

            int sure;
            try {
                sure = Integer.parseInt(sureStr);
                if(sure <= 0) {
                    Toast.makeText(DersProgramiActivity.this, "Süre 0'dan büyük olmalıdır!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(DersProgramiActivity.this, "Geçerli bir süre girin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(ders == null) {
                dersList.add(new DersProgrami(dersAdi, konu, sure, gun));
            } else {
                ders.setDersAdi(dersAdi);
                ders.setKonu(konu);
                ders.setSure(sure);
                ders.setGun(gun);
            }
            saveDersler();
            adapter.notifyDataSetChanged();
            dialog.dismiss(); // Başarılı ekleme/güncelleme sonrası dialogu kapat
        });
    }


    private void saveDersler() {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dersList);
        editor.putString(KEY, json);
        editor.apply();
    }

    private List<DersProgrami> loadDersler() {
        Gson gson = new Gson();
        String json = preferences.getString(KEY, null);
        Type type = new TypeToken<ArrayList<DersProgrami>>(){}.getType();
        List<DersProgrami> list = gson.fromJson(json, type);
        if(list == null) list = new ArrayList<>();
        return list;
    }
}
