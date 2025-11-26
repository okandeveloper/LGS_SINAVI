package com.lgs.sinavi;

import java.io.Serializable;

public class DersProgrami implements Serializable {
    private String dersAdi;
    private String konu;
    private int sure; // Dakika cinsinden
    private String gun;

    public DersProgrami(String dersAdi, String konu, int sure, String gun) {
        this.dersAdi = dersAdi;
        this.konu = konu;
        this.sure = sure;
        this.gun = gun;
    }

    public String getDersAdi() { return dersAdi; }
    public String getKonu() { return konu; }
    public int getSure() { return sure; }
    public String getGun() { return gun; }

    public void setDersAdi(String dersAdi) { this.dersAdi = dersAdi; }
    public void setKonu(String konu) { this.konu = konu; }
    public void setSure(int sure) { this.sure = sure; }
    public void setGun(String gun) { this.gun = gun; }
}
