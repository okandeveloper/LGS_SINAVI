package com.lgs.sinavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class KonularEkrani extends AppCompatActivity {

    Spinner spinnerDersler, spinnerKonular;
    Button btnVideolariGetir;

    // Konuları tutacak veri yapısı (Ders -> Konu Listesi)
    HashMap<String, String[]> konuHaritasi = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_konular_ekrani);
        spinnerDersler = findViewById(R.id.spinnerDersler);
        spinnerKonular = findViewById(R.id.spinnerKonular);
        btnVideolariGetir = findViewById(R.id.btnVideolariGetir);

        veriTabaniniDoldur(); // Ders ve Konuları yükle

        // Ders Spinner Adaptörü
        String[] dersler = {"Matematik", "Fen Bilimleri", "Türkçe", "T.C. İnkılap Tarihi ve Atatürkçülük","İngilizce ","Din Kültürü ve Ahlâk Bilgisi"};
        ArrayAdapter<String> dersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dersler);
        dersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDersler.setAdapter(dersAdapter);

        // Ders seçilince Konu Spinner'ını güncelleme mantığı
        spinnerDersler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String secilenDers = dersler[position];
                konuListesiniGuncelle(secilenDers);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Butona tıklandığında çalışacak algoritma
        btnVideolariGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secilenDers = spinnerDersler.getSelectedItem().toString();
                String secilenKonu = spinnerKonular.getSelectedItem().toString();

                // 1. ADIM: Arama Algoritması (Query Builder)
                // YouTube API olmadan arama yapmak için doğrudan arama URL'si oluşturuyoruz.
                // Örnek Çıktı: "LGS Matematik Çarpanlar ve Katlar konu anlatımı"
                String aramaSorgusu = "LGS " + secilenDers + " " + secilenKonu + " konu anlatımı";

                // 2. ADIM: URL Encode (Boşlukları + veya %20 yapmak için)
                // Basitçe boşlukları + ile değiştiriyoruz.
                String finalUrl = "https://www.youtube.com/results?search_query=" + aramaSorgusu.replace(" ", "+");

                // 3. ADIM: Diğer sayfaya gönder
                Intent intent = new Intent(KonularEkrani.this, YoutubeEkrani.class);
                intent.putExtra("url", finalUrl);
                startActivity(intent);
            }
        });
    }

    private void veriTabaniniDoldur() {

        konuHaritasi.put("Matematik", new String[]{
                "Çarpanlar ve Katlar",
                "Üslü İfadeler",
                "Kareköklü İfadeler",
                "Veri Analizi",
                "Basit Olayların Olma Olasılığı",
                "Cebirsel İfadeler ve Özdeşlikler",
                "Doğrusal Denklemler",
                "Eşitsizlikler",
                "Üçgenler",
                "Eşlik ve Benzerlik",
                "Dönüşüm Geometrisi",
                "Geometrik Cisimler"
        });


        konuHaritasi.put("Fen Bilimleri", new String[]{
                "Mevsimler ve İklim",
                "DNA ve Genetik Kod",
                "Basit Makineler",
                "Madde ve Endüstri",
                "Enerji Dönüşümleri",
                "Elektrik Yükleri ve Devreler",
                "Hücre Bölünmeleri",
                "Periyodik Sistem",
                "Ses",
                "Işık",
                "Kuvvet ve Hareket"
        });


        konuHaritasi.put("Türkçe", new String[]{
                "Fiilimsiler (İsim-Fiil, Sıfat-Fiil, Zarf-Fiil)",
                "Cümlenin Ögeleri",
                "Cümle Türleri",
                "İsim ve Fiil Cümlesi",
                "Kurallı ve Devrik Cümle",
                "Basit Cümle",
                "Birleşik Cümle",
                "Sıralı Cümle",
                "Bağlı Cümle",
                "Cümlede Anlam İlişkileri",
                "Cümle Yorumlama",
                "Fıkra",
                "Makale",
                "Deneme",
                "Roman",
                "Destan",
                "Haber",
                "Günlük",
                "Anı",
                "Hikâye",
                "Masal",
                "Fabl",
                "Röportaj",
                "Biyografi",
                "Otobiyografi",
                "Dilekçe",
                "Reklam",
                "Abartma",
                "Benzetme",
                "Kişileştirme",
                "Konuşturma",
                "Karşıtlık",
                "Yazım Kuralları",
                "Noktalama İşaretleri",
                "Paragrafın Anlam Yönü",
                "Paragrafın Yapı Yönü",
                "Tablo ve Grafik İnceleme",
                "Görsel Yorumlama",
                "Sözel Mantık",
                "Fiillerde Çatı",
                "Anlatım Bozuklukları"
        });


        konuHaritasi.put("T.C. İnkılap Tarihi ve Atatürkçülük", new String[]{
                "Bir Kahraman Doğuyor",
                "Milli Uyanış",
                "Ya İstiklal Ya Ölüm",
                "Çağdaş Türkiye",
                "Demokratikleşme",
                "Atatürkçülük",
                "Atatürk Dönemi Dış Politika",
                "II. Dünya Savaşı ve Sonrası"


        });

        konuHaritasi.put("İngilizce ", new String[]{
                "Teen Life",
                "In The Kitchen",
                "On The Phone",
                "My Environment",
                "Movies",
                "Helpful Tips",
                "Tourism",
                "Science",
                "Natural Forces"
        });


        konuHaritasi.put("Din Kültürü ve Ahlâk Bilgisi", new String[]{
                "Kader ve Kaza İnancı",
                "Kader ve Evrendeki Yasalar",
                "Allah Her Şeyi Bir Ölçüye Göre Yaratmıştır",
                "İnsanın İradesi ve Kader",
                "Kaderle İlgili Kavramlar",
                "Hz. Musa",
                "Ayetel Kürsi",
                "Paylaşma ve Yardımlaşma",
                "Zekât ve Sadaka",
                "Zekâtın Faydaları",
                "Hz. Şuayb",
                "Maun Suresi",
                "Din, Birey ve Toplum",
                "Dinin Gayesi",
                "Hz. Yusuf",
                "Asr Suresi",
                "Hz. Muhammed’in Doğruluğu",
                "Hz. Muhammed’in Merhameti",
                "Hz. Muhammed’in İstişaresi",
                "Hz. Muhammed’in Cesareti",
                "Hz. Muhammed’in Hak Gözetmesi",
                "Hz. Muhammed’in İnsanlara Değer Vermesi",
                "Kureyş Suresi",
                "İslam’ın Temel Kaynakları",
                "Kur’an’ın Ana Konuları",
                "Kur’an’ın Temel Özellikleri",
                "Hz. Nuh"
        });










    }

    private void konuListesiniGuncelle(String ders) {
        String[] konular = konuHaritasi.get(ders);
        if (konular != null) {
            ArrayAdapter<String> konuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, konular);
            konuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKonular.setAdapter(konuAdapter);
        }
    }
}