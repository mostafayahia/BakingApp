package nd801project.elmasry.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;

public class RecipeGeneralActivity extends AppCompatActivity implements
        RecipeStepAdapter.RecipeStepItemCallback {

    public static final String EXTRA_RECIPE_DATA = "nd801project.elmasry.bakingapp.extra.RECIPE_DATA";
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_general);

        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE_DATA);

        if (mRecipe == null) {
            Toast.makeText(this, R.string.error_no_recipe_data, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // set the title of this activity in the action bar to the recipe name
        setTitle(mRecipe.getName());

        TextView ingredientsTextView = findViewById(R.id.recipe_general_ingredients);

        // trying to get the ingredients of our recipe
        String ingredientsText = getString(R.string.label_ingredients) + "\n";
        for (Recipe.RecipeIngredient ingredient : mRecipe.getIngredients()) {
            ingredientsText = ingredientsText.concat(ingredient.getQuantity() + " " +
                    ingredient.getMeasure() + " " + ingredient.getIngredient().toLowerCase()+"\n");
        }

        ingredientsTextView.setText(ingredientsText);

        // adjust the recycler view for recipe steps
        RecyclerView recyclerView = findViewById(R.id.recipe_step_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        List<Recipe.RecipeStep> recipeSteps = mRecipe.getSteps();
        RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(recipeSteps, this);
        recyclerView.setAdapter(recipeStepAdapter);

    }


    @Override
    public void clickHandler(int recipeStepIndex) {
        Intent recipeDetailIntent = new Intent(this, RecipeDetailActivity.class);
        recipeDetailIntent.putParcelableArrayListExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_LIST,
                new ArrayList<>(mRecipe.getSteps()));
        recipeDetailIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_INDEX, recipeStepIndex);
        recipeDetailIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_NAME, mRecipe.getName());
        startActivity(recipeDetailIntent);
    }
}
