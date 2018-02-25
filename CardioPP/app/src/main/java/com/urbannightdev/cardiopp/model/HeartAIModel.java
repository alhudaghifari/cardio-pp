package com.urbannightdev.cardiopp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartAIModel {

    @SerializedName("Penyakit")
    @Expose
    private String penyakit;
    @SerializedName("Saran")
    @Expose
    private String saran;
    @SerializedName("Spesialis")
    @Expose
    private String spesialis;

    public String getPenyakit() {
        return penyakit;
    }

    public void setPenyakit(String penyakit) {
        this.penyakit = penyakit;
    }

    public String getSaran() {
        return saran;
    }

    public void setSaran(String saran) {
        this.saran = saran;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }

}