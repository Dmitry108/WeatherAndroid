package ru.bdim.weather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Acsessorius;

public class MainActivity extends AppCompatActivity {
    SecureRandom rn = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources resources = getResources();
        //фон
        ScrollView layout = findViewById(R.id.lyt_main_activity);
        layout.setBackgroundResource(R.drawable.sprint);
        //дата
        TextView date = findViewById(R.id.tvw_date_today);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date dateToday = new Date();
        date.setText(dateFormat.format(dateToday));
        //город
        TextView tvCity = findViewById(R.id.tvw_city);
        tvCity.setText(resources.getString(R.string.mow));
        //температура
        int t = rn.nextInt(61)-30;
        TextView tvTemperature = findViewById(R.id.tvw_temperature_now);
        tvTemperature.setText(String.format(Locale.ROOT, "%s%d \u00B0C", t > 0 ? "+" : "", t));
        tvTemperature.setTextColor(Acsessorius.getRGB(t,-30, 30));
        //небо
        int cl = rn.nextInt(t<0?6:5);
        TextView tvSky = findViewById(R.id.tvw_sky);
        tvSky.setText(resources.getStringArray(R.array.txt_sky)[cl]);
        ImageView ivSky = findViewById(R.id.img_sky);
        ivSky.setImageResource(Acsessorius.getSkyImage(cl));
        //ветер
        int v = rn.nextInt(15);
        int dir = v == 0 ? 0: rn.nextInt(8) + 1;
        TextView tvwWind = findViewById(R.id.tvw_wind);
        tvwWind.setText(String.format(Locale.ROOT, "%s %d %s",
                resources.getStringArray(R.array.wind_direction)[dir], v,
                resources.getString(R.string.m_s)));
        //влажность
        int h = rn.nextInt(101);
        TextView tvwHumidity = findViewById(R.id.tvw_humidity);
        tvwHumidity.setText(String.format(Locale.ROOT,"%d %%", h));
        //давление
        int p = rn.nextInt(101) + 700;
        TextView tvwPressure = findViewById(R.id.tvw_pressure);
        tvwPressure.setText(String.format(Locale.ROOT, "%d %s", p,
                resources.getString(R.string.mmHg)));
    }
}