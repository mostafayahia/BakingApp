package nd801project.elmasry.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.HelperUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemCallback {

    private static final String BAKING_API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Call<List<Recipe>> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if the device is not connected to the internet there is nothing to do
        if (!HelperUtil.isDeviceOnline(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // setting members' values for the recycler view
        final RecipeAdapter recipeAdapter = new RecipeAdapter(null, this);
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
        recyclerView.setAdapter(recipeAdapter);

        // handling the retrofit stuff
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BAKING_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitBakingService.BakingApi bakingApi =
                retrofit.create(RetrofitBakingService.BakingApi.class);

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
                recipeAdapter.swapRecipes(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(LOG_TAG, "getRecipes() throws error: " + t.getMessage());
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCall.cancel();
    }

    @Override
    public void clickHandler(Recipe recipe) {
        Intent recipeGeneralIntent = new Intent(this, RecipeGeneralActivity.class);
        recipeGeneralIntent.putExtra(RecipeGeneralActivity.EXTRA_RECIPE_DATA, recipe);
        startActivity(recipeGeneralIntent);
    }
}
