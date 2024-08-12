package com.kickymaulana.tandatangandigital.model;

public class DokumenModel {
    String id;
    String nama;
    String dokumen;
    String signature;
    String ditandatangani;

    public DokumenModel(String id, String nama, String dokumen, String signature, String ditandatangani) {
        this.id = id;
        this.nama = nama;
        this.dokumen = dokumen;
        this.signature = signature;
        this.ditandatangani = ditandatangani;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDokumen() {
        return dokumen;
    }

    public String getSignature() {
        return signature;
    }

    public String getDitandatangani() {
        return ditandatangani;
    }
}
