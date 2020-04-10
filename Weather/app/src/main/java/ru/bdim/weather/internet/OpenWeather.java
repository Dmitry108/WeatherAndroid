package ru.bdim.weather.internet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.bdim.weather.addiyional.Constants;

public interface OpenWeather {
    @GET(Constants.WEATHER_CURRENT)
    Call<WeatherRequest> loadCurrentWeather(@Query("q") String city,
                                            @Query("units") String units,
                                            @Query("appid") String keyApi,
                                            @Query("lang") String lang);
    @GET(Constants.WEATHER_FORECAST)
    Call<ForecastRequest> loadHourlyForecast(@Query("cnt") int number,
                                             @Query("q") String city,
                                             @Query("units") String units,
                                             @Query("appid") String keyApi);
}