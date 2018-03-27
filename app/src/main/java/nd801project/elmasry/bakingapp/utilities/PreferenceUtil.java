package nd801project.elmasry.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yahia on 3/26/18.
 */

public class PreferenceUtil {

    // if you change the value of this constant you will retrieve wrong name to update widget
    private static final String WIDGET_LAST_RECIPE_NAME_DISPLAYED_KEY = "widget_last_recipe_name_displayed";

    public static void setLastRecipeNameDisplayedInWidget(Context context,
                                                          String recipeNameDisplayedInWidget) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WIDGET_LAST_RECIPE_NAME_DISPLAYED_KEY, recipeNameDisplayedInWidget);
        editor.apply();
    }

    public static String getLastRecipeNameDisplayedInWidget(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(WIDGET_LAST_RECIPE_NAME_DISPLAYED_KEY, "");
    }
}
