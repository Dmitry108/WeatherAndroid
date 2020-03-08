package ru.bdim.weather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Acsessorius;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.Parcel;
import ru.bdim.weather.addiyional.Weather;

public class MainActivity extends AppCompatActivity implements Constants {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources resources = getResources();
        //фон
        ScrollView layout = findViewById(R.id.lyt_main_activity);
        layout.setBackgroundResource(R.drawable.sprint);
        //обработка событий
        View.OnLongClickListener infoListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String str = ((TextView)v).getText().toString();
                if (v.getId() == R.id.tvw_date_today) {
                    str = str.substring(0, str.length() - 5);
                }
                Uri uri = Uri.parse(String.format(Locale.ROOT,
                        "%s%s", resources.getString(R.string.wiki), str));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ActivityInfo isIntentOk = intent.resolveActivityInfo(
                        getPackageManager(),intent.getFlags());
                if (isIntentOk != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,
                            resources.getString(R.string.browser_abs),
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        };
        //дата
        TextView date = findViewById(R.id.tvw_date_today);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date dateToday = new Date();
        date.setText(dateFormat.format(dateToday));
        date.setOnLongClickListener(infoListener);
        //город
        Parcel parcel = (Parcel) getIntent().getSerializableExtra(CITY);
        TextView tvCity = findViewById(R.id.tvw_city);
        tvCity.setText(parcel.getCity());
        tvCity.setOnLongClickListener(infoListener);
        //температура
        int t = Weather.getInstance().getTemperature();
        TextView tvTemperature = findViewById(R.id.tvw_temperature_now);
        tvTemperature.setText(String.format(Locale.ROOT, "%s%d \u00B0C", t > 0 ? "+" : "", t));
        tvTemperature.setTextColor(Acsessorius.getRGB(t,-30, 30));
        //небо
        int cl = Weather.getInstance().getSky();
        TextView tvSky = findViewById(R.id.tvw_sky);
        tvSky.setText(resources.getStringArray(R.array.txt_sky)[cl]);
        ImageView ivSky = findViewById(R.id.img_sky);
        TypedArray imageArray = resources.obtainTypedArray(R.array.img_sky);
        ivSky.setImageResource(imageArray.getResourceId(cl,-1));

        LinearLayout lltExtra = findViewById(R.id.llt_extra_parameters);
        if (parcel.isExtra()){
            lltExtra.setVisibility(View.VISIBLE);
            //ветер
            int v = Weather.getInstance().getWindSpeed();
            int dir = Weather.getInstance().getWindDir();
            TextView tvwWind = findViewById(R.id.tvw_wind);
            tvwWind.setText(String.format(Locale.ROOT, "%s %d %s",
                    resources.getStringArray(R.array.wind_direction)[dir], v,
                    resources.getString(R.string.m_s)));
            //влажность
            int h = Weather.getInstance().getHumidity();
            TextView tvwHumidity = findViewById(R.id.tvw_humidity);
            tvwHumidity.setText(String.format(Locale.ROOT,"%d %%", h));
            //давление
            int p = Weather.getInstance().getPressure();
            TextView tvwPressure = findViewById(R.id.tvw_pressure);
            tvwPressure.setText(String.format(Locale.ROOT, "%d %s", p,
                    resources.getString(R.string.mmHg)));
        } else {
            lltExtra.setVisibility(View.GONE);
        }
    }
}