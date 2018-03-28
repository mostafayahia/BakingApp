package nd801project.elmasry.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.ui.RecipeGeneralActivity;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, Recipe recipe) {

        String recipeName = recipe.getName();
        String ingredientsText = StoringInDbUtil.getRecipeIngredientsText(context,
                recipe.getIngredients());

        Intent intent = new Intent(context, RecipeGeneralActivity.class);
        intent.putExtra(RecipeGeneralActivity.EXTRA_RECIPE_DATA, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.widget_recipe_name_text_view, recipeName);
        views.setTextViewText(R.id.widget_ingredients_text_view, ingredientsText);
        views.setOnClickPendingIntent(R.id.widget_main_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Note: I disabled the auto update for the widget in baking_app_widget_info.xml
        // this is method called at first time the user drag widget in his device screen
        BakingWidgetService.startActionDisplayLastSeenRecipe(context);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, Recipe recipe) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

