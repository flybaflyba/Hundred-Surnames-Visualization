package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;


public class PrefsActivity extends PreferenceActivity {


    public static final String SPIRAL_EFFECT= "SPIRAL_EFFECT";
    public static final String SHOW_LABEL= "SHOW_LABEL";
    public static final String CHARACTER_OPTION = "CHARACTER";

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

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

        ListPreference characters_option = new ListPreference(this);
        characters_option.setTitle("Characters");
        characters_option.setSummary("Choose Your Character Preference");
        characters_option.setKey(CHARACTER_OPTION);
        characters_option.setEntries(R.array.CharactersOption);
        characters_option.setEntryValues(R.array.CharactersOption_value);
        characters_option.setValue("simplified");
        screen.addPreference(characters_option);

        setPreferenceScreen(screen);

        //Log.d("Prefs ", "preference screen here ");

    }

    public static String getSpiralEffectPref(Context c) {
        String effect = PreferenceManager.
                getDefaultSharedPreferences(c).getString(SPIRAL_EFFECT, "static");
        return effect;
    }

    public static boolean getShowLabelPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(SHOW_LABEL, true);
    }

    public static String getCharactersOptionPref(Context c) {
        String effect = PreferenceManager.
                getDefaultSharedPreferences(c).getString(CHARACTER_OPTION, "simplified");
        return effect;
    }
}
