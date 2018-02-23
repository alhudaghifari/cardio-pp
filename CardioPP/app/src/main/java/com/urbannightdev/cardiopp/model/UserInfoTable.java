package com.urbannightdev.cardiopp.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Alhudaghifari on 11/8/2017.
 */

@IgnoreExtraProperties
public class UserInfoTable {

    private String name;
    private String username;
    private String email;
    private String password;
    private String handphone;
    private String poin;
    private String saldo;

    public UserInfoTable() {
    }

    public UserInfoTable(String name, String username, String email,
                         String password, String handphone, String poin) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.handphone = handphone;
        this.poin = poin;
    }

    public UserInfoTable(String name, String username, String email,
                         String password, String handphone, String poin, String saldo) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.handphone = handphone;
        this.poin = poin;
        this.saldo = saldo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        username = u;
    }

    public String getName() {
        return name;
    }

    public void setName(String u) {
        name = u;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String a) {
        email = a;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        password = p;
    }

    public String gethandphone() {
        return handphone;
    }

    public void sethandphone(String t) {
        handphone = t;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}