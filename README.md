# weather-generator
Pseudo-realisitc weather data generator for a game!
Program takes in an elevation file in the form of a bitmap (currently assumes position (0,0) is latitude: 0, longitude +90) and produces realistic weather data output to stdout.

## Installation 
 
#### Pre-requisites:

1. Java

This application is written in Java and requires the JVM/JRE installed on your machine in order to run the program. 
 
For compiling from source, you will also need a JDK. Please refer to this link (http://www.oracle.com/technetwork/java/javase/index-137561.html) and follow the instructions relevant to your system. 

2. Maven / Dependency Manager

This project is being developed in intelliJ IDE, using maven for dependency injection. You can use an alternative if you wish.

3. Elevation Source File

Unfortunately, github.com has a max file size of 100mb. In order to gain access to the file, please email me at my email address (if you don't have my email address, please visit this[http://visibleearth.nasa.gov/view.php?id=73934] page, download the D2 file and convert it to a bitmap.

Once you have the file, place it in the working directory.

#### Running the Program

###### Linux

Check your version of Java. This program assumes version 1.8 or higher.

`java --version`

Clone the repository into a new directory:

`git clone https://www.github.com/ramboze/weather-generator.git`

Place the D2 elevation bitmap file in the newly created directory.

Now, you need to compile the source code and run the generated .class file. The simplest method for this is to import the project into intelliJ and run it in the IDE.

You can compile it manually however you will need to manage the dependencies from both source files as well as ensure your classpath is set appropriatetly to point to the compiled .class files.

> I will write a makefile for this in the future.

###### Mac

ToDo: add installation notes.

###### Windows

ToDo: add installation notes.
 
## Design notes

- I used one of the geoTiff files suggested as found on NASA's, but for ease of processing, converted it to a bitmap. This may have lost some data as the height graduations are only 25m (rather than 1m), resulting in some locations showing as 0m elevation (e.g. Brisbane). To recover the granularity it would be necessary to use the open source java library 'geotools.' (I have left this for another day). 
- I have designed in some meteorological approximations – see additional notes below. 
- I have assumed that the third parameter in "Position" is elevation. 
- The spec calls for 'a reasonable number of positions'. I have assumed this means print data for approximately 10 locations (not positions as defined in the table). I have selected 10 cities located within the map, given below. Note, this means the locations weather are independent. 

 - Sydney        33.8688° S, 151.2093° E 
 - Adelaide      34.9285° S, 138.6007° E 
 - Brisbane      27.4698° S, 153.0251° E 
 - Perth         31.9505° S, 115.8605° E 
 - Darwin        12.4634° S, 130.8456° E 
 - Jakarta        6.1745° S, 106.8227° E 
 - Casey Station 66.2823° S, 110.5278° E 
 - Auckland      36.8485° S, 174.7633° E 
 - Noumea        22.2558° S, 166.4505° E 
 
Noting that there are random dates and times used for each location in the example; I have generated random dates and times accordingly since 1 Jan 1970 to present. This also means that each location's weather is independent. 
 
### Parameters (fixed)

- Name 
- Location 
 
### Parameters (variable) 

- Local Time 
- Conditions 
- Temperature 
- Pressure 
- Humidity 
 
### Side-effects of variable parameters 

- Temperature = function( latitude, elevation, time of day, time of year) 
- Pressure = function( elevation ) 
- Humidity = function( latitude, conditions ) 
 
 
## Meteorological Approximations 
 
#### Elevation 

###### Effect on pressure: 
- Pressure range is 14.7psi (1,013.565hPa) at 0m elevation; 1.6psi (110.32hPa) at 15,240m elevation. 
- 1psi = 6,894.76Pa = 68.95hPa 
- Assumed linear. 
- So, assuming a typical daily pressure range is 980-1,050hPa at sea level, every 25m of elevation will reduce pressure approximately 1.47hPa: 
 - 15,240/(1,013-110) = 17m per hPa 
 - 25m/17m = 1.47hPa per 25m 
 
###### Effect on temperature: 
- -1 degree Celsius for every 100m of elevation 
 
#### Time of year 

###### Effect on temperature: 
- Offset: 0 - 20 degree range, linearly distributed between midsummer and midwinter 
- Uses month to determine where on range (July = coldest; January = hottest) 
- Program assumes southern hemisphere 
 
#### Time of day 

###### Effect on temperature: 
Diurnal variation, linearly distributed (max at midday, min at midnight) 
0-10 degree range 
 
#### Latitude 

###### Effect on temperature: 
Offset: -45 to 10 degree Celsius range, between 0 and –90 degrees latitude 
 
#### Conditions 

###### Types:
- Rain 
- Snow 
- Sunny 
- Windy 
- Cloud 
 
- Snow will occur with 20% probability when temperature is < 0 degrees Celsius. 
- Other types will occur with varying probability: Rain, Sunny, Windy, Cloud 
 
#### Humidity 

Offset for latitude and given a random value based on conditions. 


#### References 
 
http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html 
http://www.farmonlineweather.com.au/climate/station.jsp 
 
