package nd801project.elmasry.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.PreferenceUtil;
import nd801project.elmasry.bakingapp.widget.BakingWidgetService;

public class RecipeGeneralActivity extends AppCompatActivity implements
        RecipeStepAdapter.RecipeStepItemCallback {

    private static final String LOG_TAG = RecipeGeneralActivity.class.getSimpleName();
    private Recipe mRecipe;
    private boolean mTwoPane;
    public static final String EXTRA_RECIPE_DATA = "nd801project.elmasry.bakingapp.extra.RECIPE_DATA";
    private RecipeDetailFragment mRecipeDetailFragment;
    private RecipeGeneralFragment mRecipeGeneralFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_general);

        mRecipeGeneralFragment = (RecipeGeneralFragment)
                getSupportFragmentManager().findFragmentById(R.id.recipe_general_fragment);

        // getting recipe object from the activity that started this activity
        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE_DATA);

        if (mRecipe == null) {
            Toast.makeText(this, R.string.error_no_recipe_data, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Log.d(LOG_TAG, "recipe name: " + mRecipe.getName());
        /*
         * something I don't understand here I run the same next statement in the MainActivity
         * just before starting this activity and get the correct thumbnail url (corresponding to the one in the
         * api but here I got null !!!!!
         */
        if (mRecipe.getName().equals("Nutella Pie"))
            Log.d(LOG_TAG, "in Nutella Pie recipe mRecipe.getSteps().get(5).getThumbnailURL() gives: " +
                mRecipe.getSteps().get(5).getThumbnailURL());

        // setting recipe for recipeGeneralFragment to update its recycler view
        mRecipeGeneralFragment.setRecipe(mRecipe);

        // set the title of this activity to the recipe name
        setTitle(mRecipe.getName());

        // setting last seen recipe in the preference and also update the widget
        PreferenceUtil.setLastSeenRecipeId(this, mRecipe.getId());
        BakingWidgetService.startActionDisplayLastSeenRecipe(this);

        // changing the behaviour according to the device whether is tablet or mobile device
        if (findViewById(R.id.recipe_detail_fragment) != null) {
            mTwoPane = true;
            // hide previous step and next step buttons
            findViewById(R.id.recipe_step_prev_next_layout).setVisibility(View.GONE);
            // getting recipe detail fragment
            mRecipeDetailFragment = (RecipeDetailFragment)
                    getSupportFragmentManager().findFragmentById(R.id.recipe_detail_fragment);
            if (savedInstanceState == null) {
                // make the fragment display first step in the recipe at first time created this
                // activity
                Recipe.RecipeStep recipeStep = mRecipe.getSteps().get(0);
                mRecipeDetailFragment.makeUpdates(recipeStep);

                // Note: setSelectedItem(1) in next line because
                // first item displays recipe ingredients
                mRecipeGeneralFragment.setSelectedItem(1);

            }
        } else {
            mTwoPane = false;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(LOG_TAG, "Up button is pressed");
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickHandler(int recipeStepIndex) {
        if (recipeStepIndex < 0) return;
        if (mTwoPane) {
            Recipe.RecipeStep recipeStep = mRecipe.getSteps().get(recipeStepIndex);
            mRecipeDetailFragment.makeUpdates(recipeStep);
            // Note: setSelectedItem(recipeStepIndex+1) in next line because
            // first item displays recipe ingredients
            mRecipeGeneralFragment.setSelectedItem(recipeStepIndex+1);
        } else {
            Intent recipeDetailIntent = new Intent(this, RecipeDetailActivity.class);
            recipeDetailIntent.putParcelableArrayListExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_LIST,
                    new ArrayList<>(mRecipe.getSteps()));
            recipeDetailIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_INDEX, recipeStepIndex);
            recipeDetailIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_NAME, mRecipe.getName());
            startActivity(recipeDetailIntent);
        }
    }
}
