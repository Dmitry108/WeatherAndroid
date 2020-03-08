package ru.bdim.weather.addiyional;

import java.io.Serializable;

public class Parcel implements Serializable {
    private String city;
    private boolean isExtra;
    private boolean isSunAndMoon;

    public Parcel(String city, boolean isExtra, boolean isSunAndMoon){
        this.city = city;
        this.isExtra = isExtra;
        this.isSunAndMoon = isSunAndMoon;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isExtra() {
        return isExtra;
    }

    public void setExtra(boolean extra) {
        isExtra = extra;
    }

    public boolean isSunAndMoon() {
        return isSunAndMoon;
    }

    public void setSunAndMoon(boolean sunAndMoon) {
        isSunAndMoon = sunAndMoon;
    }
}