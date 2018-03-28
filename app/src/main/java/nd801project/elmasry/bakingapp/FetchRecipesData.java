package nd801project.elmasry.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.List;

import nd801project.elmasry.bakingapp.idlingResource.SimpleIdlingResource;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.provider.RecipeContract;
import nd801project.elmasry.bakingapp.provider.RecipeProvider;
import nd801project.elmasry.bakingapp.retrofit.RetrofitBakingService;
import nd801project.elmasry.bakingapp.ui.RecipeAdapter;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yahia on 3/23/18.
 */

public class FetchRecipesData {

    private static final String LOG_TAG = FetchRecipesData.class.getSimpleName();

    private static final String BAKING_API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private final Retrofit mRetrofit;
    private final SimpleIdlingResource mIdlingResource;
    private final RecipeAdapter mRecipeAdapter;
    private final Context mContext;

    private Call<List<Recipe>> mCall;


    public FetchRecipesData(final Context context, final RecipeAdapter recipeAdapter,
                            final SimpleIdlingResource idlingResource) {

        // handling the retrofit stuff
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BAKING_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mIdlingResource = idlingResource;
        mRecipeAdapter = recipeAdapter;
        mContext = context;
    }

    public void execute() {

        if (mIdlingResource != null)
            mIdlingResource.setIdleState(false);

        RetrofitBakingService.BakingApi bakingApi =
                mRetrofit.create(RetrofitBakingService.BakingApi.class);

        mCall = bakingApi.getRecipes();

        mCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();

                // handling the error if exist
                if (recipes == null) {
                    Log.e(LOG_TAG, "retrofit can't get recipes from network (recipes is null)");
                    switch (response.code()) {
                        case 401:
                            Log.e(LOG_TAG, "retrofit error unauthenticated");
                            break;
                        default:
                            Log.e(LOG_TAG,
                                    "retrofit client error: " + response.code() + " " + response.message());
                    }
                    return;
                }

                // if there is no error we will swap our list in the recycler view
                mRecipeAdapter.swapRecipes(recipes);

                // handling idling resource
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }

                // insert data in the database
                for (Recipe recipe : recipes) {
                    List<Recipe.RecipeStep> recipeSteps = recipe.getSteps();

                    String recipeIngredientsText =
                            StoringInDbUtil.getRecipeIngredientsText(mContext, recipe.getIngredients());
                    String stepsLongDescEncodeText = StoringInDbUtil.getEncodedTextForStepsLongDesc(recipeSteps);
                    String stepsShortDescEncodedText = StoringInDbUtil.getEncodedTextForStepsShortDesc(recipeSteps);
                    String stepsVideosEncodedText = StoringInDbUtil.getEncodedTextForStepsVideos(recipeSteps);
                    String stepsImagesEncodedText = StoringInDbUtil.getEncodedTextForStepsImages(recipeSteps);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(RecipeContract.COLUMN_RECIPE_NAME, recipe.getName());
                    contentValues.put(RecipeContract.COLUMN_RECIPE_IMAGE, recipe.getImageUrl());
                    contentValues.put(RecipeContract.COLUMN_RECIPE_INGREDIENTS, recipeIngredientsText);
                    contentValues.put(RecipeContract.COLUMN_RECIPE_STEPS_LONG_DESC_ENCODED_TEXT, stepsLongDescEncodeText);
                    contentValues.put(RecipeContract.COLUMN_RECIPE_STEPS_SHORT_DESC_ENCODED_TEXT, stepsShortDescEncodedText);
                    contentValues.put(RecipeContract.COLUMN_RECIPE_STEPS_VIDEOS_ENCODED_TEXT, stepsVideosEncodedText);
                    contentValues.put(RecipeContract.COLUMN_RECIPE_STEPS_IMAGES_ENCODED_TEXT, stepsImagesEncodedText);

                    mContext.getContentResolver().insert(RecipeProvider.Recipes.CONTENT_URI, contentValues);
                }

            }



            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(LOG_TAG, "getRecipes() throws error: " + t.getMessage());
            }
        });
    }

    public Call<List<Recipe>> getCall() {
        return mCall;
    }
}
