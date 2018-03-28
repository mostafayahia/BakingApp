package nd801project.elmasry.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.provider.RecipeProvider;
import nd801project.elmasry.bakingapp.utilities.PreferenceUtil;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;

/**
 * Created by yahia on 3/26/18.
 */

public class BakingWidgetService extends IntentService {

    private static final String ACTION_DISPLAY_LAST_SEEN_RECIPE =
            "nd801project.elmasry.bakingapp.action.display_last_seen_recipe";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    public static void startActionDisplayLastSeenRecipe(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_DISPLAY_LAST_SEEN_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(ACTION_DISPLAY_LAST_SEEN_RECIPE)) {
                handleActionDisplayLastSeenRecipe();
            }
        }
    }

    private void handleActionDisplayLastSeenRecipe() {

        int recipeId = PreferenceUtil.getLastSeenRecipeId(this);

        Cursor data;
        if (recipeId < 1) {
            // we just display the first recipe in the table in case of in valid id
            data = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI,
                    null, "_id=1", null, null);
        } else {
            data = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI,
                    null, "_id=?", new String[]{String.valueOf(recipeId)}, null);
        }

        if (data != null && data.getCount() == 1) {
            data.moveToFirst();
            Recipe recipe = StoringInDbUtil.getRecipeFromDbCursor(data, this);
            data.close();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
            BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, recipe);
        }
    }
}
