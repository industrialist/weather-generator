package dev.weather_generator;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Samuel Brooks on 13/12/16.
 *
 * This class implements a number of meteorological functions to provide
 * pseudo-correct weather data.
 *
 * It provides a single public function "getData" which takes some basic information about a location,
 * including latitude, time of year, time of day, and elevation above sea level, and provides back
 * weather information for conditions, temperature, pressure and humidity in the form of a String.
 *
 * Consult the ReadMe file for more information on the meteorological functions.
 *
 */

public class WeatherEngine {

    private double temp;
    private int    conditionFlag;

    public String getWeather(double latitude, int month, int hour, int elevation){

        // Create temperature first as this influences the other meteorological variables:
        String temperature = makeTemperature(latitude, month, hour+1, elevation);

        String weather = "";
        weather = weather.concat(makeConditions());
        weather = weather.concat("|");
        weather = weather.concat(temperature);
        weather = weather.concat("|");
        weather = weather.concat(makePressure(elevation));
        weather = weather.concat("|");
        weather = weather.concat(makeHumidity(latitude));

        return weather;
    }

    // Return a string with the forecast weather conditions
    private String makeConditions() {
        String conditions = "Cloud";
        conditionFlag = 0;
        double rand = Math.random()*11;

        if( temp < 0 && (rand < 2) ) {
            conditionFlag = 1;
            return "Snow";
        }

        if( rand > 4 ) {
            conditions = "Sunny";
            conditionFlag = 2;

            if( rand > 8 ){
                conditions = "Rain";
                conditionFlag = 3;

                if( rand > 9 ) {
                    conditions = "Wind";
                    conditionFlag = 4;
                }
            }
        }

        return conditions;
    }

    // Generate the temperature, considering latitude, date, time and elevation
    private String makeTemperature(double latitude, int month, int hour, int elevation){
        // Create base minimum temperature before modifications:
        // See ReadMe for these figures:
        temp = (-1*latitude)/90;
        temp = -1*temp + 1;
        temp = temp*55 - 40;


        // Add time-of-year offset (summer/winter, 0-20 degrees) for southern hemisphere:
        // y = -mx + b
        // y = -(20/6)*month + 20 degrees (20 degrees is maximum additional temperature for summer)
        //
        // y = -3.3*month + 20 | where 0 <= month < 6
        // y = 0               | where month = 6
        // y = +1.7*month      | where 6 < month <= 12
        if( month < 6)
            temp = temp - (3.3*(month-1)) + 20; // month 1 = january

        if( month > 6)
            temp = temp + (1.7*(month-1));


        // Add time-of-day offset (day/night, 0-10 degrees)
        // Similar to time-of-year, except inverted (highest point in the middle - midday)
        if( hour < 12)
            temp = temp + 0.83*(hour);

        if( hour > 12)
            temp = temp - (0.42*hour) + 10;

        if( hour == 12 )
            temp = temp + 10;


        // Add elevation offset (100m = -1 degree Celsius)
        temp = temp - elevation/100;

        // Add random offset:
        temp = temp + Math.random()*11; // returns random value between 0 and 10

        // Return a string formatted to 1 decimal place:
        return String.format("%1$,.1f",temp);
    }

    private String makePressure(int elevation) {

        double pressure = ThreadLocalRandom.current().nextDouble(980.0, 1050.0);

        // 25 is the elevation factor calculated earlier to account for low bitmap granularity
        // 1.47 is the pressure reduction in hPa for every 25m of elevation
        pressure = pressure - (elevation/25)*1.47;

        return String.format("%1$,.1f",pressure);
    }

    private String makeHumidity(double latitude) {

        double humidity;
        humidity = latitude/90 + 1;
        //humidity = -1*humidity + 1;
        humidity = humidity*100 - 30;
        humidity = humidity + ThreadLocalRandom.current().nextInt(0,20);

        // Cloudy
        if(conditionFlag == 0)
            humidity = humidity + ThreadLocalRandom.current().nextInt(0,15);

        // Rainy
        if(conditionFlag == 3)
            humidity = humidity + ThreadLocalRandom.current().nextInt(10, 30);

        if( humidity < 0 )
            humidity = 0;

        if( humidity > 100 )
            humidity = 100;

        return String.format("%1$,.0f",humidity);
    }

}
