package com.google.myemployee.Model;

public class User {
    private int id_karyawan;
    private String email;

    public User(int id_karyawan, String email) {
        this.id_karyawan = id_karyawan;
        this.email = email;
    }

    public int getId() {
        return id_karyawan;
    }

    public String getEmail() {
        return email;
    }
}
