package ru.bdim.weather.addiyional;

import java.security.SecureRandom;

public class Weather {
    private final SecureRandom rn = new SecureRandom();
    private static Weather instance;

    private int temperature;
    private int sky;
    private int windSpeed;
    private int windDir;
    private int humidity;
    private int pressure;

    private Weather() {
        refresh();
    }

    private void refresh() {
        temperature = rn.nextInt(61) - 30;
        sky = rn.nextInt(temperature < 0 ? 6 : 5);
        windSpeed = rn.nextInt(15);
        windDir = windSpeed == 0 ? 0 : rn.nextInt(8) + 1;
        humidity = rn.nextInt(101);
        pressure = rn.nextInt(101) + 700;
    }

    public static Weather getInstance() {
        if (instance == null) {
            instance = new Weather();
        }
        return instance;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getSky() {
        return sky;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDir() {
        return windDir;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }
}