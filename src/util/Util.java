package util;

import util.enums.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static Language[] getLanguages(){
        return Language.values();
    }

    public static String getArrayAsCsvString(Object[] array){
        StringBuilder s = new StringBuilder("\"");

        for(int i = 0; i < array.length; i++){
            s.append(array[i]);

            if(i < array.length - 1)
                s.append(",");
        }

        s.append("\"");

        return s.toString();
    }

    public static Object[] splitCsvLine(String line, boolean removeSpaces){
        //Remove spaces
        if( removeSpaces)
            line = line.replace(" ", "");

        //Split line in raw data
        String[] rawData = line.split(",");

        ArrayList<Object> outerList = new ArrayList<>(); //E.g title, etc. => no arrays
        ArrayList<String> innerList; //E.g. array inside the line (actors, etc.)

        int startIndex = -1, endIndex = -1; //Start/End of an array marked by "
        for(int i = 0; i < rawData.length; i++){
            //Check if inner array contains only one object => simultaneously start and end of array
            if(countChars(rawData[i], '\"') == 2){
                startIndex = i;
                endIndex = i;
            }
            //Check if start of array
            else if(rawData[i].startsWith("\"")){
                startIndex = i;
            }
            //Check if end of array
            else if(rawData[i].endsWith("\"")){
                endIndex = i;
            }

            //When not inside an array then just add it to the outer list
            if(startIndex == -1){
                outerList.add(rawData[i]);
            }
            //If start of array and end of array found then merge the inner list and add it to the outer list
            else if(endIndex != -1){
                innerList = new ArrayList<>(Arrays.asList(rawData).subList(startIndex, endIndex + 1));
                outerList.add(innerList);

                //Reset start and end index for multiple arrays inside one line
                startIndex = -1;
                endIndex = -1;
            }
        }

        return outerList.toArray();
    }

    public static void removeQuotes(Object[] lineAsArray){
        for (Object o : lineAsArray) {
            if (o instanceof List<?>) {
                ArrayList<String> innerArrayAsList = (ArrayList<String>) o;
                innerArrayAsList.replaceAll(s -> s.replace("\"", ""));
            }
        }
    }

    public static int countChars(String s, char searchFor){
        int counter = 0;
        for(Character c : s.toCharArray())
            if(c == searchFor) counter++;

        return counter;
    }

    public static boolean containsEnum(String value, Class<?> enumeration){
        boolean contains = false;

        for(Object s : enumeration.getEnumConstants()){
            if(value.equalsIgnoreCase(s.toString()))
                contains = true;
        }

        return contains;
    }
}
