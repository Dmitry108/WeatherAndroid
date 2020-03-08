package ru.bdim.weather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.Parcel;

public class StartActivity extends AppCompatActivity implements Constants {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxCity = findViewById(R.id.tv_choice_city);
                String city = etxCity.getText().toString();
                CheckBox cbxAdditional = findViewById(R.id.cbx_extra_parameters);
                CheckBox cbxSunMoon = findViewById(R.id.cbx_sun_moon);
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra(CITY, new Parcel(city,
                        cbxAdditional.isChecked(), cbxSunMoon.isChecked()));
                startActivity(intent);
            }
        });
    }
}