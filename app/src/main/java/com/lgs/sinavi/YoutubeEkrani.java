package com.lgs.sinavi;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class YoutubeEkrani extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youtube_ekrani);

        webView = findViewById(R.id.webViewYoutube);

        // MainActivity'den gelen URL'i al
        String url = getIntent().getStringExtra("url");

        setupWebView(url);
    }

    private void setupWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // YouTube JS gerektirir
        settings.setDomStorageEnabled(true); // Performans için

        // Bu kısım çok önemli: Linklere tıklanınca tarayıcıyı açmasın,
        // kendi içimizde kalsın.
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Her linki bu WebView içinde aç
                view.loadUrl(url);
                return true;
            }
        });

        // Videoların tam ekran oynaması vb. için ChromeClient
        webView.setWebChromeClient(new WebChromeClient());

        // URL'i yükle
        webView.loadUrl(url);
    }

    // Geri tuşuna basınca uygulamadan çıkmasın, önceki sayfaya (listeye) dönsün
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}