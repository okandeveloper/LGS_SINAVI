package com.lgs.sinavi;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class LessonFragment extends Fragment {

    private static final String ARG_LESSON = "lesson_name";
    private String lessonName;
    private SharedPreferences sharedPreferences;
    private List<String> topics;
    private int completedTopics = 0;

    private TextView progressText;
    private ProgressBar progressBar;
    private ListView topicList;

    public static LessonFragment newInstance(String lessonName) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LESSON, lessonName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lessonName = getArguments().getString(ARG_LESSON);
        }
        sharedPreferences = requireActivity().getSharedPreferences("ProgressApp", Context.MODE_PRIVATE);
        topics = loadTopics();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson, container, false);

        progressText = view.findViewById(R.id.progress_text);
        progressBar = view.findViewById(R.id.progress_bar);
        topicList = view.findViewById(R.id.topic_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, topics);
        topicList.setAdapter(adapter);
        topicList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Kaydedilen durumu yükle
        loadSelections();

        // Tıklama ile seçim işlemleri
        topicList.setOnItemClickListener((parent, view1, position, id) -> {
            boolean isChecked = topicList.isItemChecked(position);
            String topic = topics.get(position);

            if (isChecked) {
                completedTopics++;
            } else {
                completedTopics--;
            }

            int progress = (int) (((double) completedTopics / topics.size()) * 100);
            progressBar.setProgress(progress);
            progressText.setText("Tamamlanma Oranı: " + progress + "%");

            // Seçimi kaydet
            sharedPreferences.edit().putBoolean(lessonName + "_" + topic, isChecked).apply();
            sharedPreferences.edit().putInt(lessonName + "_completed", completedTopics).apply();
        });

        return view;
    }

    private void loadSelections() {
        if (topicList == null || progressBar == null || progressText == null) return;

        completedTopics = 0; // Tamamlanmış konuları sıfırla

        for (int i = 0; i < topics.size(); i++) {
            String topic = topics.get(i);
            boolean isChecked = sharedPreferences.getBoolean(lessonName + "_" + topic, false);
            topicList.setItemChecked(i, isChecked);

            if (isChecked) {
                completedTopics++; // Sadece seçili olanları say
            }
        }

        int progress = (int) (((double) completedTopics / topics.size()) * 100);
        progressBar.setProgress(progress);
        progressText.setText("Tamamlanma Oranı: " + progress + "%");
    }


    private List<String> loadTopics() {
        List<String> topics = new ArrayList<>();

        switch (lessonName) {

            // ------------------ TÜRKÇE ------------------
            case "Türkçe":
                topics.add("Fiilimsiler (İsim-Fiil, Sıfat-Fiil, Zarf-Fiil)");
                topics.add("Cümlenin Ögeleri");
                topics.add("Cümle Türleri");
                topics.add("İsim ve Fiil Cümlesi");
                topics.add("Kurallı ve Devrik Cümle");
                topics.add("Basit Cümle");
                topics.add("Birleşik Cümle");
                topics.add("Sıralı Cümle");
                topics.add("Bağlı Cümle");
                topics.add("Cümlede Anlam İlişkileri");
                topics.add("Cümle Yorumlama");
                topics.add("Fıkra");
                topics.add("Makale");
                topics.add("Deneme");
                topics.add("Roman");
                topics.add("Destan");
                topics.add("Haber");
                topics.add("Günlük");
                topics.add("Anı");
                topics.add("Hikâye");
                topics.add("Masal");
                topics.add("Fabl");
                topics.add("Röportaj");
                topics.add("Biyografi");
                topics.add("Otobiyografi");
                topics.add("Dilekçe");
                topics.add("Reklam");
                topics.add("Abartma");
                topics.add("Benzetme");
                topics.add("Kişileştirme");
                topics.add("Konuşturma");
                topics.add("Karşıtlık");
                topics.add("Yazım Kuralları");
                topics.add("Noktalama İşaretleri");
                topics.add("Paragrafın Anlam Yönü");
                topics.add("Paragrafın Yapı Yönü");
                topics.add("Tablo ve Grafik İnceleme");
                topics.add("Görsel Yorumlama");
                topics.add("Sözel Mantık");
                topics.add("Fiillerde Çatı");
                topics.add("Anlatım Bozuklukları");
                break;

            // ------------------ MATEMATİK ------------------
            case "Matematik":
                topics.add("Çarpanlar ve Katlar");
                topics.add("Üslü İfadeler");
                topics.add("Kareköklü İfadeler");
                topics.add("Veri Analizi");
                topics.add("Basit Olayların Olma Olasılığı");
                topics.add("Cebirsel İfadeler ve Özdeşlikler");
                topics.add("Doğrusal Denklemler");
                topics.add("Eşitsizlikler");
                topics.add("Üçgenler");
                topics.add("Eşlik ve Benzerlik");
                topics.add("Dönüşüm Geometrisi");
                topics.add("Geometrik Cisimler");
                break;

            // ------------------ DİN KÜLTÜRÜ ------------------
            case "Din Kültürü":
                topics.add("Kader ve Kaza İnancı");
                topics.add("Kader ve Evrendeki Yasalar");
                topics.add("Allah Her Şeyi Bir Ölçüye Göre Yaratmıştır");
                topics.add("İnsanın İradesi ve Kader");
                topics.add("Kaderle İlgili Kavramlar");
                topics.add("Hz. Musa");
                topics.add("Ayetel Kürsi");
                topics.add("Paylaşma ve Yardımlaşma");
                topics.add("Zekât ve Sadaka");
                topics.add("Zekâtın Faydaları");
                topics.add("Hz. Şuayb");
                topics.add("Maun Suresi");
                topics.add("Din, Birey ve Toplum");
                topics.add("Dinin Gayesi");
                topics.add("Hz. Yusuf");
                topics.add("Asr Suresi");
                topics.add("Hz. Muhammed’in Doğruluğu");
                topics.add("Hz. Muhammed’in Merhameti");
                topics.add("Hz. Muhammed’in İstişaresi");
                topics.add("Hz. Muhammed’in Cesareti");
                topics.add("Hz. Muhammed’in Hak Gözetmesi");
                topics.add("Hz. Muhammed’in İnsanlara Değer Vermesi");
                topics.add("Kureyş Suresi");
                topics.add("İslam’ın Temel Kaynakları");
                topics.add("Kur’an’ın Ana Konuları");
                topics.add("Kur’an’ın Temel Özellikleri");
                topics.add("Hz. Nuh");
                break;

            // ------------------ İNKILAP ------------------
            case "İnkılap":
                topics.add("Bir Kahraman Doğuyor");
                topics.add("Milli Uyanış");
                topics.add("Ya İstiklal Ya Ölüm");
                topics.add("Çağdaş Türkiye");
                topics.add("Demokratikleşme");
                topics.add("Atatürkçülük");
                topics.add("Atatürk Dönemi Dış Politika");
                topics.add("II. Dünya Savaşı ve Sonrası");
                break;

            // ------------------ FEN BİLİMLERİ ------------------
            case "Fen":
                topics.add("Mevsimler ve İklim");
                topics.add("DNA ve Genetik Kod");
                topics.add("Basit Makineler");
                topics.add("Madde ve Endüstri");
                topics.add("Enerji Dönüşümleri");
                topics.add("Elektrik Yükleri ve Devreler");
                topics.add("Hücre Bölünmeleri");
                topics.add("Periyodik Sistem");
                topics.add("Ses");
                topics.add("Işık");
                topics.add("Kuvvet ve Hareket");
                break;

            // ------------------ İNGİLİZCE ------------------
            case "İngilizce":
                topics.add("Teen Life");
                topics.add("In The Kitchen");
                topics.add("On The Phone");
                topics.add("My Environment");
                topics.add("Movies");
                topics.add("Helpful Tips");
                topics.add("Tourism");
                topics.add("Science");
                topics.add("Natural Forces");
                break;
        }

        return topics;
    }
}