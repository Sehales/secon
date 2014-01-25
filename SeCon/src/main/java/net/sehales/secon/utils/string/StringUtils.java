
package net.sehales.secon.utils.string;

import java.util.regex.Pattern;

public class StringUtils {
    
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    
    /**
     * get an array splitted at spaces from a string
     * 
     * @param string
     * @return a string-array
     */
    public static String[] getArrayOutOfString(String string) {
        return PATTERN_ON_SPACE.split(string);
    }
    
    /**
     * get a string out of the given array, starting at the given position
     * 
     * @param array
     * @param position
     * @return string
     */
    public static String getStringOfArray(String[] array, int position) {
        StringBuilder sb = new StringBuilder();
        for (int i = position; i < array.length; i++) {
            sb.append(array[i]);
            sb.append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
