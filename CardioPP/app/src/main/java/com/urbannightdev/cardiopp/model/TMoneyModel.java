package com.urbannightdev.cardiopp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ghifar on 25/02/18.
 */

public class TMoneyModel {
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("idTmoney")
    @Expose
    private String idTmoney;
    @SerializedName("idFusion")
    @Expose
    private String idFusion;
    @SerializedName("custName")
    @Expose
    private String custName;
    @SerializedName("custPhone")
    @Expose
    private String custPhone;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("tokenExpiry")
    @Expose
    private String tokenExpiry;
    @SerializedName("verified")
    @Expose
    private String verified;
    @SerializedName("changeDevice")
    @Expose
    private String changeDevice;

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getIdTmoney() {
        return idTmoney;
    }

    public void setIdTmoney(String idTmoney) {
        this.idTmoney = idTmoney;
    }

    public String getIdFusion() {
        return idFusion;
    }

    public void setIdFusion(String idFusion) {
        this.idFusion = idFusion;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(String tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getChangeDevice() {
        return changeDevice;
    }

    public void setChangeDevice(String changeDevice) {
        this.changeDevice = changeDevice;
    }

}
