package dev.weather_generator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Program created by Samuel Brooks 13/12/2016
 * samuel.brooks@gmail.com
 *
 * This program generates pseudo-correct weather information
 * for 10 pre-configured locations within and near to Australia
 * (see local bitmap file showing local geography).
 *
 * Future versions:
 * (1) Implement Commons CLI and accept a set of coordinates (lat, long) provided by the user
 * and provide the weather at that location.
 *
 * (2) Upgrade the image to include other bitmaps / GeoTiff and
 * remove code relating to southern-hemisphere.
 *
 */

public class WeatherGenerator {

    public static void main( String[] args ) throws Exception {

        System.out.println( "Welcome to WeatherGenerator!" );
        System.out.println();

        /*
         * Variable declaration:
         */

        Random random = new Random();
        long dateSeed;
        Date date;

        int j,k;
        int month, hour;
        WeatherEngine engine = new WeatherEngine();
        DateFormat dfFull  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateFormat dfHours = new SimpleDateFormat("HH");
        DateFormat dfMonth = new SimpleDateFormat("MM");



        /*
         * Read in elevation data from local bitmap file:
         */

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("elev_D2.bmp"));
        } catch (IOException e) {
            System.out.println("DEBUG: IO Exception"+e);
        }

        WritableRaster raster = img.getRaster();
        double elevation;



        /*
         *  Calculate bitmap elevation scaling factor:
         *
         *  Note: bitmap array starts at (rowNum:0,colNum:0) and goes to (width -1, height -1)
         *
         *  To normalise the elevation bitmap used in this program:
         *  Elevation at 90S, 180E (bottom right) is approximately 2,801m (above sea level)
         *  Elevation at 0N, 90E (top left) is approximately 0m (this is sea level)
         *  In the bitmap image, the greyscale value of 90S and 180E is: 110
         *  Note 1: there is a black pixel in the image at bottom right corner (10799,10799),
         *  so (10798,10798) was used instead.
         *  Note 2: this could have been hard-coded into a constant, but is retained for demonstration purposes.
         */

        /*  Third argument is 0 for greyscale images  */
        elevation = raster.getSample(10798,10798,0);

        /*  Elevation approximation calculation:
         *  So whatever number is returned from the bitmap,
         *  multiply this by 'factor' to get the elevation in meters.
         */
        double factor = 2801.0/elevation; // = 2801/110 = 25.46363...

        String[] locations = {"Sydney|","Adelaide|","Brisbane|","Perth|",
                "Darwin|","Jakarta|","Casey Stn|","Auckland|","Noumea|"};

        /*
         * 0 - Sydney        33.8688° S, 151.2093° E
         * 1 - Adelaide      34.9285° S, 138.6007° E
         * 2 - Brisbane      27.4698° S, 153.0251° E
         * 3 - Perth         31.9505° S, 115.8605° E
         * 4 - Darwin        12.4634° S, 130.8456° E
         * 5 - Jakarta       6.1745°  S, 106.8227° E
         * 6 - Casey Station 66.2823° S, 110.5278° E
         * 7 - Auckland      36.8485° S, 174.7633° E
         * 8 - Noumea        22.2558° S, 166.4505° E
         */

        // Latitude and longitude coordinates supplied by Google maps.
        double[][] coords =
                {{-33.869,151.209}, // Sydney
                 {-34.929,138.601}, // Adelaide
                 {-27.470,153.025}, // Brisbane
                 {-31.951,115.861}, // Perth
                 {-12.463,130.846}, // Darwin
                 { -6.175,106.823}, // Jakarta
                 {-66.282,110.528}, // Casey Station
                 {-36.849,174.763}, // Auckland
                 {-22.256,166.451}, // Noumea
                };


        /*
         * For each predefined location:
         * (1) concatenate coordinates,
         * (2) retrieve elevation,
         * (3) generate a date and time, and
         * (4) generate weather data
         */

        for( int i = 0; i < coords.length; i++ ){

            // Concat coordinates onto the location string:
            locations[i] = locations[i].concat(String.valueOf(coords[i][0]));
            locations[i] = locations[i].concat(",");
            locations[i] = locations[i].concat(String.valueOf(coords[i][1]));
            locations[i] = locations[i].concat(",");

            // Concat elevation:
            // First, scale the latitude and longitude values to the bitmap pixel height and width:
            j = (int)((-1000*coords[i][0])*(108)/900);
            k = (int)((1000*(coords[i][1]-90))*(108)/900);

            // Then, grab pixel from the bitmap image at the given coordinates,
            // multiply this number by the elevation scaling factor, and
            // concat onto the location string (coords are in reverse order):
            elevation = raster.getSample(k,j,0)*factor;
            locations[i] = locations[i].concat(String.valueOf((int)elevation));
            locations[i] = locations[i].concat("|");

            // Generate a pseudo-random date and time since epoch (1st Jan, 1970).
            // Create a new date and concat onto location[i] string:
            dateSeed = (long)(random.nextDouble()*(System.currentTimeMillis()));
            date = new Date(dateSeed);
            locations[i] = locations[i].concat(dfFull.format(date));
            locations[i] = locations[i].concat("|");

            // Grab the month and the hour from the current date:
            month = Integer.valueOf(dfMonth.format(date));
            hour  = Integer.valueOf(dfHours.format(date));

            // Calculate weather data for the coordinates, elevation, date and time.
            // Concatenate the resultant string with the existing locations[i] string.
            // Note that while fixed coordinates are provided by the double[][] coords,
            // this function will work with any coordinates that are provided.
            locations[i] = locations[i].concat(engine.getWeather(coords[i][0], month, hour, (int)elevation));

        }

        /*
         * Output simulated weather to terminal:
         */

        System.out.println( "The weather is: ");
        for( int i = 0; i < coords.length; i++ )
            System.out.println(locations[i]);

    }

}
