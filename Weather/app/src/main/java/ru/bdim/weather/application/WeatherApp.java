package ru.bdim.weather.application;

import android.app.Application;
import androidx.room.Room;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.databases.HistoryDao;
import ru.bdim.weather.databases.HistoryDatabase;
import ru.bdim.weather.internet.OpenWeather;

public class WeatherApp extends Application implements Constants {
    private static WeatherApp instance;
    private HistoryDatabase history;

    private OpenWeather openWeather;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //база данных
        history = Room.databaseBuilder(getApplicationContext(), HistoryDatabase.class, "history.db")
                .allowMainThreadQueries()
                .build();
        //интернет
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL_retrofit)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public static WeatherApp getInstance(){
        return instance;
    }

    public HistoryDao getDao(){
        return history.getHistoryDao();
    }
    public OpenWeather getInternet(){
        return openWeather;
    }
}