package com.example.mysql;

import android.net.Uri;

import io.realm.RealmObject;

public class Data2 extends RealmObject {

    public static final String NAME = "table_data2";

    public String name;
    public String age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
