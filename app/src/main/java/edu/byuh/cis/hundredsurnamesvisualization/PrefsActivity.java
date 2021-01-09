package edu.byuh.cis.hundredsurnamesvisualization;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class PrefsActivity extends PreferenceActivity {


    public static final String SPIRAL_EFFECT= "SPIRAL_EFFECT";
    public static final String SHOW_LABEL= "SHOW_LABEL";
    public static final String CHARACTER_OPTION = "CHARACTER_OPTION";
    public static final String PINYIN_OPTION = "PINYIN";
    public static final String COLOR_OPTION = "COLOR80";

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

//        Timer timer = new Timer();
//        TimerTask myTask = new TimerTask() {
//            @Override
//            public void run() {
//                String colorPrefs=PrefsActivity.getColorOptionPref(getBaseContext());
//                ColorTheme.changeColorTheme(colorPrefs);
//                ActionBar actionBar =  getActionBar();
//                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ColorTheme.c2))); // set your desired color
//            }
//        };
//        timer.schedule(myTask, 0, 1000);
//



        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() { // use the solve the bug: Only the original thread that created a view hierarchy can touch its views.
                    @Override
                    public void run() {
                        // Stuff that updates the UI

                        String colorPrefs=PrefsActivity.getColorOptionPref(getBaseContext());
                        ColorTheme.changeColorTheme(colorPrefs);
                        ActionBar actionBar =  getActionBar();
                        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ColorTheme.c2))); // set your desired color

                    }
                });
            }
        };
        timer.schedule(myTask, 0, 1000);


        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);

        ListPreference spiral_effect = new ListPreference(this);
        spiral_effect.setTitle(R.string.SpiralEffectTitle);
        spiral_effect.setSummary(R.string.SpiralEffectSummary);
        spiral_effect.setKey(SPIRAL_EFFECT);
        spiral_effect.setEntries(R.array.SpiralEffect);
        spiral_effect.setEntryValues(R.array.SpiralEffect_value);
        spiral_effect.setValue("static");
        screen.addPreference(spiral_effect);

        CheckBoxPreference show_label = new CheckBoxPreference(this);
        show_label.setTitle(R.string.show_label_title);
        show_label.setSummaryOn(R.string.show_label_on);
        show_label.setSummaryOff(R.string.show_label_off);
        show_label.setKey(SHOW_LABEL);
        show_label.setChecked(true);
        screen.addPreference(show_label);


        CheckBoxPreference circle = new CheckBoxPreference(this);
        circle.setTitle(R.string.circle_button_lable);
        circle.setSummaryOn(R.string.circle_button_on);
        circle.setSummaryOff(R.string.circle_button_off);
        circle.setKey("CIRCLE");
        circle.setChecked(true);
        screen.addPreference(circle);


        ListPreference character_option = new ListPreference(this);
        character_option.setTitle(R.string.charactersOption_title);
        character_option.setSummary(R.string.charactersOption_summary);
        character_option.setKey(CHARACTER_OPTION);
        character_option.setEntries(R.array.charactersOption);
        character_option.setEntryValues(R.array.charactersOption_value);
        character_option.setValue("simplified");
        screen.addPreference(character_option);

        ListPreference pinyin_option = new ListPreference(this);
        pinyin_option.setTitle(R.string.pinyinOption_title);
        pinyin_option.setSummary(R.string.pinyinOption_summary);
        pinyin_option.setKey(PINYIN_OPTION);
        pinyin_option.setEntries(R.array.pinyinsOption);
        pinyin_option.setEntryValues(R.array.pinyinsOption_value);
        pinyin_option.setValue("simplified");
        screen.addPreference(pinyin_option);

        ListPreference color_option = new ListPreference(this);
        color_option.setTitle(R.string.color_title);
        color_option.setSummary(R.string.color_summary);
        color_option.setKey(COLOR_OPTION);
        color_option.setEntries(R.array.ColorOption);
        color_option.setEntryValues(R.array.ColorOption_value);
        color_option.setValue("COLOR80");
        screen.addPreference(color_option);


        setPreferenceScreen(screen);



    }

    public static String getSpiralEffectPref(Context c) {
        String effect = PreferenceManager.
                getDefaultSharedPreferences(c).getString(SPIRAL_EFFECT, "static");
        return effect;
    }

    public static boolean getShowLabelPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(SHOW_LABEL, true);
    }
    public static boolean getCirclePref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("CIRCLE", true);
    }

    public static String getCharacterOptionPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(CHARACTER_OPTION, "simplified");

    }

    public static String getPinyinOptionPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(PINYIN_OPTION, "simplified");

    }

//    public static int getColorOptionPref(Context c){
//        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("COLOR_OPTION","COLOR80"));
//    }

    public static String getColorOptionPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(COLOR_OPTION, "COLOR80");

    }

}
