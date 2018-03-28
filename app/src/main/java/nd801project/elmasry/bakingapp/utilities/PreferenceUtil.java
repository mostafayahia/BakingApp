package nd801project.elmasry.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yahia on 3/26/18.
 */

public class PreferenceUtil {

    // if you change the value of this constant you will retrieve wrong value from
    // the preference which corresponding to the new key not the old one
    private static final String LAST_SEEN_RECIPE_ID_KEY = "last-seen-recipe-id";

    public static void setLastSeenRecipeId(Context context,
                                                          int recipeId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LAST_SEEN_RECIPE_ID_KEY, recipeId);
        editor.commit();
    }

    public static int getLastSeenRecipeId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(LAST_SEEN_RECIPE_ID_KEY, -1);
    }
}
