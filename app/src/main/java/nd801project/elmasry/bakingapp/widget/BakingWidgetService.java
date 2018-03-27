package nd801project.elmasry.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.provider.RecipeContract;
import nd801project.elmasry.bakingapp.provider.RecipeProvider;
import nd801project.elmasry.bakingapp.utilities.PreferenceUtil;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;

/**
 * Created by yahia on 3/26/18.
 */

public class BakingWidgetService extends IntentService {

    private static final String ACTION_PICK_ANOTHER_RECIPE_RANDOM =
            "nd801project.elmasry.bakingapp.action.pick_random_recipe";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    public static void startActionPickAnotherRecipeRandom(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_PICK_ANOTHER_RECIPE_RANDOM);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(ACTION_PICK_ANOTHER_RECIPE_RANDOM)) {
                handleActionPickAnotherRecipeRandom();
            }
        }
    }

    private void handleActionPickAnotherRecipeRandom() {

        String excludedRecipeName = PreferenceUtil.getLastRecipeNameDisplayedInWidget(this);

        Cursor data = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI,
                null, null, null, null);

        if (data != null && data.getCount() > 0) {

            final int totRows = data.getCount();
            String newRecipeName;
            do {
                int randomRow = (int) (System.currentTimeMillis() % totRows);
                data.moveToPosition(randomRow);
                newRecipeName = data.getString(data.getColumnIndex(RecipeContract.COLUMN_RECIPE_NAME));
                if (totRows == 1)
                    break; // to avoid infinite while loop
            } while (newRecipeName.equals(excludedRecipeName));

            Recipe newRecipe = StoringInDbUtil.getRecipeFromDbCursor(data, this);

            data.close();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
            BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, newRecipe);
        }
    }
}
