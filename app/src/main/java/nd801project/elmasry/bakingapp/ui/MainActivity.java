package nd801project.elmasry.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nd801project.elmasry.bakingapp.FetchRecipesData;
import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.idlingResource.SimpleIdlingResource;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.provider.RecipeProvider;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;
import nd801project.elmasry.bakingapp.utilities.HelperUtil;

public class MainActivity extends AppCompatActivity implements RecipeNameAdapter.RecipeItemCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_RECIPES = 53;
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    private FetchRecipesData mFetchRecipesData;
    private RecipeNameAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting members' values for the recycler view
        mRecipeAdapter = new RecipeNameAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recipes_recycler_view);
        LinearLayoutManager layoutManager;
        if (findViewById(R.id.empty_view_in_table_only) != null) {
            // in this case we know this device is tablet
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mRecipeAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID_RECIPES, null, this);

        getIdlingResource();

        // we want first to check if there is any stored data in the database
        Cursor cursor = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI,
                null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if (HelperUtil.isDeviceOnline(this)) {
                // mFetchRecipesData must be initialized after calling getIdlingResource() method
                mFetchRecipesData = new FetchRecipesData(this, mRecipeAdapter, mIdlingResource);
            } else {
                // we want to fetch data from the internet but we can't do that
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            // for testing if the widget open the correct RecipeGeneralActivity uncomment the next line
//            BakingWidgetService.startActionPickAnotherRecipeRandom(this);
        }
        if (cursor != null) cursor.close();

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mFetchRecipesData != null) {
            // we fetch data in onStart() to make sure espresso register the idlingResource
            // before fetching the data
            mFetchRecipesData.execute();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();

        if (mFetchRecipesData != null) {
            mFetchRecipesData.getCall().cancel();
            mFetchRecipesData = null; // we no longer want to fetch data
        }
    }

    @Override
    public void clickHandler(Recipe recipe) {
        Intent recipeGeneralIntent = new Intent(this, RecipeGeneralActivity.class);
        recipeGeneralIntent.putExtra(RecipeGeneralActivity.EXTRA_RECIPE_DATA, recipe);
        startActivity(recipeGeneralIntent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, RecipeProvider.Recipes.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.getCount() > 0) {
            // important to move the cursor to the first to get the correct display when rotating the device
            data.moveToFirst();

            List<Recipe> recipes = new ArrayList<>();
            do {
                Recipe recipe = StoringInDbUtil.getRecipeFromDbCursor(data, this);
                recipes.add(recipe);
            } while (data.moveToNext());

            mRecipeAdapter.swapRecipes(recipes);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipeAdapter.swapRecipes(null);
    }
}
