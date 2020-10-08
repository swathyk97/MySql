package com.example.mysql;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Data extends RealmObject {
    public static final String NAME = "table_data";
    @PrimaryKey
    private Integer id;
    private String channel0Data, channel1Data;
    public short ch1Count;
    public short ch0Count;

    public String getChannel0Data() {
        return channel0Data;
    }

    public void setChannel0Data(String channel0Data) {
        this.channel0Data = channel0Data;
    }

    public String getChannel1Data() {
        return channel1Data;
    }

    public void setChannel1Data(String channel1Data) {
        this.channel1Data = channel1Data;
    }

    public short getCh1Count() {
        return ch1Count;
    }

    public void setCh1Count(short ch1Count) {
        this.ch1Count = ch1Count;
    }

    public short getCh0Count() {
        return ch0Count;
    }

    public void setCh0Count(short ch0Count) {
        this.ch0Count = ch0Count;
    }
}
