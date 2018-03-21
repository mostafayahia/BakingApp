package nd801project.elmasry.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;

public class RecipeGeneralActivity extends AppCompatActivity implements
        RecipeStepAdapter.RecipeStepItemCallback {

    private Recipe mRecipe;
    private boolean mTwoPane;
    public static final String EXTRA_RECIPE_DATA = "nd801project.elmasry.bakingapp.extra.RECIPE_DATA";
    private RecipeDetailFragment mRecipeDetailFragment;
    private RecipeGeneralFragment mRecipeGeneralFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_general);

        // getting recipe object from the activity that started this activity
        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE_DATA);

        if (mRecipe == null) {
            Toast.makeText(this, R.string.error_no_recipe_data, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mRecipeGeneralFragment = (RecipeGeneralFragment)
                getSupportFragmentManager().findFragmentById(R.id.recipe_general_fragment);

        // setting recipe for recipeGeneralFragment to update its recycler view
        mRecipeGeneralFragment.setRecipe(mRecipe);

        // set the title of this activity to the recipe name
        setTitle(mRecipe.getName());

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
