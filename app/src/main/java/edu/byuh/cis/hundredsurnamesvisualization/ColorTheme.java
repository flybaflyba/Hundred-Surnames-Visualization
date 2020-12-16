package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Context;
import android.graphics.Color;

/**
 * This class only contain the color String for convenience.
 * The first string is the lightest color and the last one is the darkest color
 */
public class ColorTheme {
    public static String c1="#DAC3B3";
    public static String c2="#CDAB81";
    public static String c3="#6C5F5B";
    public static String c4="#4F4A45";


    public static void changeColorTheme(String colorPrefs){
//        String colorPrefs=PrefsActivity.getColorOptionPref(context);
        if(colorPrefs.equals("COLOR80")){
            //Color 80 Shabby Chic Neutrals
            c1="#DAC3B3";
            c2="#CDAB81";
            c3="#6C5F5B";
            c4="#4F4A45";
        }else if(colorPrefs.equals("COLOR75")) {
            //color 75 Green Fields
            c1 = "#FFFAE1";
            c2 = "#919636";
            c3 = "#5A5F37";
            c4 = "#524A3A";
        }else if(colorPrefs.equals("COLOR05")){
            //color 05 Cool Blues
            c1 = "#C4DFE6";
            c2 = "#66A5AD";
            c3 = "#07575B";
            c4 = "#003B46";
        }else{
            //color 79 Beyond Black and White
            //should never exists
            c1="#F0EFFE";
            c2="#31A2AC";
            c3="#AF1C1C";
            c4="#2F2F28";
        }

    }

}
