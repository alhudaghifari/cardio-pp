package com.urbannightdev.cardiopp.model;

/**
 * Created by ghifar on 24/02/18.
 */

public class SaranKesehatanModel {
    private String level;
    private String pesanAwalKesehatan;
    private String namaPenyakit;
    private String perawatanDiri;
    private String spesialis;

    public SaranKesehatanModel() {
    }

    public SaranKesehatanModel(String level, String pesanAwalKesehatan, String namaPenyakit, String perawatanDiri, String spesialis) {
        this.level = level;
        this.pesanAwalKesehatan = pesanAwalKesehatan;
        this.namaPenyakit = namaPenyakit;
        this.perawatanDiri = perawatanDiri;
        this.spesialis = spesialis;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPesanAwalKesehatan() {
        return pesanAwalKesehatan;
    }

    public void setPesanAwalKesehatan(String pesanAwalKesehatan) {
        this.pesanAwalKesehatan = pesanAwalKesehatan;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    public String getPerawatanDiri() {
        return perawatanDiri;
    }

    public void setPerawatanDiri(String perawatanDiri) {
        this.perawatanDiri = perawatanDiri;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }
}
