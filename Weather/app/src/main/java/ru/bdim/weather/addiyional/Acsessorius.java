package ru.bdim.weather.addiyional;

import android.graphics.Color;

import ru.bdim.weather.R;

public class Acsessorius {
    // метод определяет цвет в спектре от синего до красного
    private static int[] imgSkyArray = {
            R.drawable.sun, R.drawable.sun_clouds, R.drawable.clouds,
            R.drawable.rain, R.drawable.lightning, R.drawable.snow};
    public static int getSkyImage(int num){
        return imgSkyArray[num];
    }
    public static int getRGB (int x, int t0, int t){
        int r, g, b;
        int dt = (t - t0);
        if (x < t0){
            r = g = 0; b = 255;
        } else if (x < t0 + dt / 4) {
            r = 0; g = 1024*(x - t0)/dt; b = 255;
        } else if (x < t0 + dt/2) {
            r = 0; g = 255; b = -1024/dt*(x - t0 - dt/2);
        } else if (x < t0 + 3*dt/4) {
            r = 1024*(x - t0 - dt/2)/dt; g = 255; b = 0;
        } else if (x < t) {
            r = 255; g =-1024/dt*(x - t); b = 0;
        } else {
            r = 255; g = b = 0;
        }
        return Color.rgb(r, g, b);
    }
}
