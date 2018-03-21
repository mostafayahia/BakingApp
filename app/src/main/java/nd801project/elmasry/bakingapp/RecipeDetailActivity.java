package nd801project.elmasry.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import nd801project.elmasry.bakingapp.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_STEP_LIST = "nd801project.elmasry.bakingapp.extra.RECIPE_STEP_LIST";
    public static final String EXTRA_RECIPE_STEP_INDEX = "nd801project.elmasry.bakingapp.extra.RECIPE_STEP_INDEX";
    public static final String EXTRA_RECIPE_NAME = "nd108project.elmasry.bakingapp.extra.RECIPE_NAME";

    private static final String STEP_INDEX_KEY = "step-index-key";
    private ArrayList<Recipe.RecipeStep> mRecipeSteps;
    private int mStepIndex;
    public static final int INVALID_STEP_INDEX = -1;
    private RecipeDetailFragment mRecipeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // retrieving recipe steps data from the activity started this activity
        mRecipeSteps = getIntent().getParcelableArrayListExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_LIST);

        if (savedInstanceState == null) {
            // getting recipe step index from the activity that started this activity
            mStepIndex = getIntent().getIntExtra(EXTRA_RECIPE_STEP_INDEX, INVALID_STEP_INDEX);
            // if no recipe step info, there is nothing to do
            if (mStepIndex == INVALID_STEP_INDEX) {
                Toast.makeText(this, R.string.error_no_recipe_step_data, Toast.LENGTH_LONG)
                        .show();
                return;
            }
        } else if (savedInstanceState.containsKey(STEP_INDEX_KEY)) {
            mStepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
        }

        // make the recipe name the title of this activity
        String recipeName = getIntent().getStringExtra(RecipeDetailActivity.EXTRA_RECIPE_NAME);
        if (recipeName != null) setTitle(recipeName);

        mRecipeDetailFragment = (RecipeDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.recipe_detail_fragment);

        // at first created this activity we set recipeStep for the fragment and make updates in it
        if (savedInstanceState == null)
            mRecipeDetailFragment.makeUpdates(mRecipeSteps.get(mStepIndex));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mStepIndex != INVALID_STEP_INDEX)
            outState.putInt(STEP_INDEX_KEY, mStepIndex);
    }



    /**
     * Click handler for back button in this activity
     * @param view
     */
    public void clickBackHandler(View view) {
        if (mStepIndex <= 0) return;
        mStepIndex--;
        mRecipeDetailFragment.makeUpdates(mRecipeSteps.get(mStepIndex));
    }

    /**
     * Click handler for Forward button in this activity
     * @param view
     */
    public void clickForwardHandler(View view) {
        if (mStepIndex >= mRecipeSteps.size() - 1) return;
        mStepIndex++;
        mRecipeDetailFragment.makeUpdates(mRecipeSteps.get(mStepIndex));
    }


}
