# weather-generator
pseudo realisitc weather data generator for a game

Installation 
 
This application is written in Java and requires the JVM/JRE installed on your machine in order to run the program. 
 
For compiling from source, you will also need a JDK. Please refer to this link (http://www.oracle.com/technetwork/java/javase/index-137561.html) and follow the instructions relevant to your system. 
 
todo: Additional information for installation.  
 
 
Design notes

I used one of the geoTiff files suggested as found on NASA's, but for ease of processing, converted it to a bitmap. This may have lost some data as the height graduations are only 25m (rather than 1m), resulting in some locations showing as 0m elevation (e.g. Brisbane). To recover the granularity it would be necessary to use the open source java library 'geotools.' (I have left this for another day). 
I have designed in some meteorological approximations – see additional notes below. 
I have assumed that the third parameter in "Position" is elevation. 
The spec calls for 'a reasonable number of positions'. I have assumed this means print data for approximately 10 locations (not positions as defined in the table). I have selected 10 cities located within the map, given below. Note, this means the locations weather are independent. 

Sydney 33.8688° S, 151.2093° E 
Adelaide 34.9285° S, 138.6007° E 
Brisbane 27.4698° S, 153.0251° E 
Perth 31.9505° S, 115.8605° E 
Darwin 12.4634° S, 130.8456° E 
Jakarta 6.1745° S, 106.8227° E 
Casey Station 66.2823° S, 110.5278 E 
Auckland 36.8485° S, 174.7633° E 
Noumea 22.2558° S, 166.4505° E 
 
Noted that there are random dates and times used for each location in the example; I have generated random dates and times accordingly since 1 Jan 1970 to present. This also means that each location's weather is independent. 
 
Parameters (fixed)

Name 
Location 
 
Parameters (variable) 

Local Time 
Conditions 
Temperature 
Pressure 
Humidity 
 
Side-effects of variable parameters 

Temperature = function( latitude, elevation, time of day, time of year) 
Pressure = function( elevation ) 
Humidity = function( latitude, conditions ) 
 
 
Meteorological Approximations 
 
Elevation 

Effect on pressure: 
Pressure range is 14.7psi (1,013.565hPa) at 0m elevation; 1.6psi (110.32hPa) at 15,240m elevation. 
1psi = 6,894.76Pa = 68.95hPa 
Assumed linear. 
So, assuming a typical daily pressure range is 980-1,050hPa at sea level, every 25m of elevation will reduce pressure approximately 1.47hPa: 
15,240/(1,013-110) = 17m per hPa 
25m/17m = 1.47hPa per 25m 
 
Effect on temperature: 
-1degree Celsius for every 100m of elevation 
 
Time of year 

Effect on temperature: 
Offset: 0 - 20 degree range, linearly distributed between midsummer and midwinter 
Uses month to determine where on range (July = coldest; January = hottest) 
Assuming southern hemisphere 
 
Time of day 

Effect on temperature: 
Diurnal variation, linearly distributed (max at midday, min at midnight) 
0-10 degree range 
 
Latitude 

Effect on temperature: 
Offset: -45 to 10 degree Celsius range, between 0 and –90 degrees latitude 
 
Conditions 

Types: 
Rain 
Snow 
Sunny 
Windy 
Cloud 
 
Snow will occur with 20% probability when temperature is < 0 degrees Celsius. 
Other types will occur with varying probability: Rain, Sunny, Windy, Cloud 
 
Humidity 

Offset for latitude and given a random value based on conditions. 
 
 
References 
 
http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html 
http://www.farmonlineweather.com.au/climate/station.jsp 
 
