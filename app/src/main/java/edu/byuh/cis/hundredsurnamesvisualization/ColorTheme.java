package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Context;

/**
 * This class only contain the color String for convenience.
 * The first string is the lightest color and the last one is the darkest color
 */
public class ColorTheme {
    String c1,c2,c3,c4;

    public ColorTheme(Context context){
        String colorPrefs=PrefsActivity.getColorOptionPref(context);
        if(colorPrefs.equals("COLOR80")){
            //Color 80 Shabby Chic Neutrals
            c1="#DAC3B3";
            c2="#CDAB81";
            c3="#6C5F5B";
            c4="#4F4A45";
        }else if(colorPrefs.equals("COLOR75")){
            //color 75 Green Fields
            c1="#FFFAE1";
            c2="#919636";
            c3="#5A5F37";
            c4="#524A3A";
        }else{
            //color 79 Beyond Black and White
            c1="#F0EFFE";
            c2="#31A2AC";
            c3="#AF1C1C";
            c4="#2F2F28";
        }











    }

}
