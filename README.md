# coordinatesConverter
**ENGLISH DESCRIPTION**

####################################################################

A basic geographical coordinates converter based on Android / Kotlin

For the main script please access the following link:
[MainActivity.kt](https://github.com/sebibrindus/coordinatesConverter/blob/master/app/src/main/java/com/example/convertcoordinates/MainActivity.kt)

The application has the following functionalities:  
-User inputs data of type double representing the geographical coordinates in decimal format  
-By pressing the convert button the app converts the decimal coordinates into DMS format (degrees, minutes and seconds)  
-There is an export button that exports the data in form of pairs (decimal + DMS format) into a csv file in the app directory  
-There is also a share button in the top right corner of the screen that allows the user to share the data to other apps, maybe even copy to clipboard  
-The app has two override functions implemented that prevent the data to be lost when the screen is rotated (usually Android destroys the activity when the screen rotates)

-The variable that I used to store the data (both inputted and converted) is a LinkedHashMap. I chose this type of variable so that I can store the data in pairs (converted value matches the input) and also I wanted to preserve the order in which the data was entered. When the export button is pressed, the variable is converted to string to be parsed to file. After trying a few options, I have seen that this is the best data type to combine with the **fileOutputStream** class and its methods.


######################################################################



