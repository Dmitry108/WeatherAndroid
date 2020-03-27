package ru.bdim.weather.addiyional;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.bdim.weather.internet.WeatherRequest;

public class Weather implements Parcelable {
    private final SecureRandom rn = new SecureRandom();

    private String city;
    private Date date;
    private int temperature;
    private int sky;
    private String skyDescription;
    private float windSpeed;
    private int windDir;
    private int humidity;
    private int pressure;

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String WEATHER_API_KEY = "&appid=6bdcb2b7e6c07df82dc5d96dc7b9ab2e";

    private ConnectionListener listener;

    //    public Weather(String city) {
//        refresh(city);
//    }
//    public Weather(WeatherRequest weatherRequest) {refresh(weatherRequest);}
    public Weather(String city, String lang, @NonNull ConnectionListener listener) {
        this.listener = listener; //продумать нужно ли оборачивать его в parcel
        refresh(city, lang);
    }

    protected Weather(Parcel in) {
        city = in.readString();
        date = (Date) in.readValue(Date.class.getClassLoader());
        temperature = in.readInt();
        sky = in.readInt();
        skyDescription = in.readString();
        windSpeed = in.readFloat();
        windDir = in.readInt();
        humidity = in.readInt();
        pressure = in.readInt();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    //    public void refresh(String city) {
//        this.city = city;
//        //обновлять дату только если отличается
//        Date dateToday = new Date();
//
//        if (date == null || !date.equals(dateToday)){
//            this.date = dateToday;
//        }
//
//        temperature = rn.nextInt(61) - 30;
//        sky = rn.nextInt(temperature < 0 ? 6 : 5);
//        windSpeed = rn.nextInt(15);
//        windDir = windSpeed == 0 ? 0 : rn.nextInt(8) + 1;
//        humidity = rn.nextInt(101);
//        pressure = rn.nextInt(101) + 700;
//    }
//    public void refresh(WeatherRequest weatherRequest) {
    public void refresh(String city, String lang) {
        final Handler handler = new Handler();
        try {
            String uri = String.format("%sq=%s&units=metric%s&lang=%s", WEATHER_URL, city,
                    WEATHER_API_KEY, lang);
            final URL url = new URL(uri);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        final String p = in.lines().collect(Collectors.joining("\n"));
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(p, WeatherRequest.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                initData(weatherRequest);
                                listener.settingComplete();
                            }
                        });
                    } catch (IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                listener.errorConnection();
                            }
                        });//listener.errorConnection();
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initData(WeatherRequest weatherRequest) {
        this.city = weatherRequest.getName();
        //обновлять дату только если отличается
        Date dateToday = new Date();

        if (date == null || !date.equals(dateToday)) {
            this.date = dateToday;
        }
        temperature = Math.round(weatherRequest.getMain().getTemperature());
        sky = getIconNumber(weatherRequest.getWeather().getIcon());//rn.nextInt(temperature < 0 ? 6 : 5);
        skyDescription = weatherRequest.getWeather().getDescription();
        windSpeed = weatherRequest.getWind().getSpeed();
        windDir = windSpeed == 0 ? 0 : rn.nextInt(8) + 1;
        humidity = weatherRequest.getMain().getHumidity();
        pressure = weatherRequest.getMain().getPressure();
    }

    private int getIconNumber(String icon) {
        int number;
        switch (icon.substring(0, 2)) {
            case "01": number = 0; break;
            case "02": number = 1; break;
            case "03":
            case "04": number = 2; break;
            case "09": number = 3; break;
            case "10": number = 4; break;
            case "11": number = 5; break;
            case "13": number = 6; break;
            case "50":
            default: number = 7;
        }
        return number;
    }

    public int getTemperature() { return temperature; }
    public int getSkyIcon() { return sky; }
    public String getSkyDescription() { return skyDescription; }
    public float getWindSpeed() { return windSpeed; }
    public int getWindDir() { return windDir; }
    public int getHumidity() { return humidity; }
    public int getPressure() { return pressure; }
    public String getCity() { return city; }
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeValue(date);
        dest.writeInt(temperature);
        dest.writeInt(sky);
        dest.writeString(skyDescription);
        dest.writeFloat(windSpeed);
        dest.writeInt(windDir);
        dest.writeInt(humidity);
        dest.writeInt(pressure);
    }

    public interface ConnectionListener {
        void settingComplete();
        void errorConnection();
    }
}