package ru.bdim.weather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.Parcel;
import ru.bdim.weather.addiyional.RecyclerAdapter;
import ru.bdim.weather.addiyional.Weather;

public class StartActivity extends AppCompatActivity implements Constants {
    private Parcel parcel;
    private List<Weather> lastCitiesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AutoCompleteTextView actCity = findViewById(R.id.tv_choice_city);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.pop_cities));
        actCity.setAdapter(adapter);

        Button btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView actCity = findViewById(R.id.tv_choice_city);
                final String city = actCity.getText().toString();
                Weather.ConnectionListener listener = new Weather.ConnectionListener() {
                    @Override
                    public void settingComplete() {
                        start(parcel);
                    }
                    @Override
                    public void errorConnection() {
                        parcel = null;
                        Toast.makeText(StartActivity.this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
                    }
                };
                if (parcel == null || !parcel.getCity().equals(city)) {
                    parcel = new Parcel(city, getResources().getString(R.string.lang), listener);
                } else {
                    start(parcel);
                }
            }
        });
    }

    private void start(Parcel parcel){
        addCityToList(parcel.getWeather());
        CheckBox cbxAdditional = findViewById(R.id.cbx_extra_parameters);
        CheckBox cbxSunMoon = findViewById(R.id.cbx_sun_moon);
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        parcel.setParameters(cbxAdditional.isChecked(), cbxSunMoon.isChecked());
        intent.putExtra(CITY, parcel);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Взгляд в будущее
//        if (lastCitiesList.size()>0){
//            FragmentManager fm = getSupportFragmentManager();
//            Fragment frmLastCities = new LastCitiesFragment();
//            FragmentTransaction tf = fm.beginTransaction();
//            Bundle args = new Bundle();
//            //args.putParcelableArrayList(LAST_CITIES, lastCitiesList);
//            frmLastCities.setArguments(args);
//            tf.replace(R.id.flt_last_cities, frmLastCities);
//            tf.commit();
//        }
        final RecyclerView rcvLastCity = findViewById(R.id.rcv_last_cities);
        rcvLastCity.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcvLastCity.setLayoutManager(layoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(lastCitiesList);
        rcvLastCity.setAdapter(adapter);
        RecyclerAdapter.OnItemClickListener clickListener = new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                AutoCompleteTextView act = findViewById(R.id.tv_choice_city);
                TextView tvw = view.findViewById(R.id.tvw_last_city);
                Button btn = findViewById(R.id.btn_ok);
                act.setText(tvw.getText());
                parcel.setWeather(lastCitiesList.get(position));
                btn.callOnClick();
            }
        };
        adapter.setClickListener(clickListener);
    }

    private void addCityToList(Weather weather) {
        for (Weather w : lastCitiesList) {
            if (w.getCity().equals(weather.getCity())) {
                lastCitiesList.remove(w);
                break;
            }
        }
        lastCitiesList.add(0, weather);
    }
}