package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    /**
     * Converts any object array to a csv string
     *
     * @param array array of type object that should be converted
     * @return csv string
     *
     * @BigO: O(n)
     * **/
    public static String convertArrayToCsvString(Object[] array){
        StringBuilder s = new StringBuilder();

        for(int i = 0; i < array.length; i++){
            s.append(array[i]);

            if(i < array.length - 1)
                s.append(";");
        }

        return s.toString();
    }

    /**
     * Converts any csv string to an object array
     *
     * @param line string line in csv format
     * @param removeSpaces should blank between words been removed
     * @return object array
     *
     * @BigO: O(n)
     * **/
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
            if(countCharsByChar(rawData[i], '\"') == 2){
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
            else if(endIndex != -1 && endIndex >= startIndex){
                innerList = new ArrayList<>(Arrays.asList(rawData).subList(startIndex, endIndex + 1));
                outerList.add(innerList);

                //Reset start and end index for multiple arrays inside one line
                startIndex = -1;
                endIndex = -1;
            }
        }

        return outerList.toArray();
    }

    /**
     * Removes quotes from object array
     *
     * @param lineAsArray of type object where quotes should be removed
     *
     * @BigO: O(n)
     * **/
    public static void removeQuotes(Object[] lineAsArray){
        for (Object o : lineAsArray) {
            if (o instanceof List<?>) {
                ArrayList<String> innerArrayAsList = (ArrayList<String>) o;
                innerArrayAsList.replaceAll(s -> s.replace("\"", ""));
            }
        }
    }

    /**
     * Removes quotes from object array
     *
     * @param s string in where chars should be searched
     * @param searchFor char that will be looked for and counted
     *
     * @return number of how much times the passed char is in the passed string
     *
     * @BigO: O(n)
     * **/
    public static int countCharsByChar(String s, char searchFor){
        int counter = 0;
        for(Character c : s.toCharArray())
            if(c == searchFor) counter++;

        return counter;
    }

    /**
     * Joins an array to a string and separates it by passed separator
     *
     * @param array array of type object which should be converted to a string
     * @param separator string which will separate the objects in between. Pass empty string to not separate the objects
     *
     * @return separated string
     *
     * @BigO: O(n)
     * **/
    public static String joinArray(Object[] array, String separator){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < array.length; i++){
            stringBuilder.append(array[i]);

            if(i != array.length - 1)
                stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    /**
     * Joins a list to a string and separates it by passed separator
     *
     * @param list list of type unknown which should be converted to a string
     * @param separator string which will separate the objects in between. Pass empty string to not separate the objects
     *
     * @return separated string
     *
     * @BigO: O(n)
     * **/
    public static String joinList(List<?> list, String separator){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < list.size(); i++){
            stringBuilder.append(list.get(i));

            if(i != list.size() - 1)
                stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    /**
     * Formats any passed string to hash map key format
     *
     * @param s string that should be formatted to key
     *
     * @return string formated as hash map key
     *
     * @BigO: O(n)
     * **/
    public static String stringToKeyFormat(String s){
        return s.toLowerCase().replace(" ", "_");
    }

    private static final Pattern pattern2 = Pattern.compile("[^a-zA-Z0-9 ,_]");
    /**
     * Removes all forbidden characters in the passed string and returns it
     *
     * @param s string from which characters should be removed
     *
     * @return string without the forbidden characters
     *
     * @BigO: O(n)
     * **/
    public static String removeForbiddenChars(String s){
        return s.replaceAll(pattern2.pattern(), "");
    }

    /**
     * Uppercase all words in a passed string
     *
     * @param s string which should be formatted
     *
     * @return formatted string
     *
     * @BigO: O(n)
     * **/
    public static String uppercaseAll(String s){
        if(s.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        if(s.contains(" ")){
            String[] arr = s.split(" ");

            for (String value : arr) {
                if(!value.isEmpty())
                    sb.append(Character.toUpperCase(value.charAt(0)))
                            .append(value.substring(1).toLowerCase()).append(" ");
            }
            return sb.toString().trim();
        }

        return sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase()).toString().trim();
    }

    /**
     * Checks if a passed enum type contains a value
     *
     * @param value value which should be searched in the enum
     * @param enumeration enumeration type which should be iterated
     *
     * @return true when enum contains value, otherwise false
     *
     * @BigO: O(n)
     * **/
    public static boolean containsEnumValue(String value, Class<?> enumeration){
        boolean contains = false;

        for(Object s : enumeration.getEnumConstants()){
            if(value.toLowerCase().contains(s.toString().toLowerCase()))
                contains = true;
        }

        return contains;
    }

    private static final Pattern pattern1 = Pattern.compile("-?\\d+(\\.\\d+)?");
    /**
     * Checks if a passed string is numeric only
     *
     * @param s string which should be checked
     *
     * @return true if s contains out of numeric values only, otherwise false
     *
     * @BigO: O(n)
     * **/
    public static boolean isNumeric(String s){
        return pattern1.matcher(s).matches();
    }
}
