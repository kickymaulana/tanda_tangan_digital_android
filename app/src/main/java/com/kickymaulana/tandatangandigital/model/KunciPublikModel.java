package com.kickymaulana.tandatangandigital.model;

public class KunciPublikModel {
    String email;
    String nama;
    String bilangan_e;
    String bilangan_n;

    public KunciPublikModel(String email, String nama, String bilangan_e, String bilangan_n) {
        this.email = email;
        this.nama = nama;
        this.bilangan_e = bilangan_e;
        this.bilangan_n = bilangan_n;
    }

    public String getEmail() {
        return email;
    }

    public String getNama() {
        return nama;
    }

    public String getBilangan_e() {
        return bilangan_e;
    }

    public String getBilangan_n() {
        return bilangan_n;
    }
}
