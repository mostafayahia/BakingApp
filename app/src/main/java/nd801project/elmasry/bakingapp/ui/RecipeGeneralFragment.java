package nd801project.elmasry.bakingapp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;

/**
 * Created by yahia on 3/20/18.
 */

public class RecipeGeneralFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecipeStepAdapter mRecipeStepAdapter;

    private static final int INVALID_POSITION = -1;
    private int mSelectedItemPos = INVALID_POSITION;

    private static final String SELECTED_ITEM_POS_KEY = "selected_item_pos";
    private static final String RECIPE_KEY = "recipe_key";
    private Recipe mRecipe;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_general,
                container, false);

        // adjust the recycler view for recipe steps
        mRecyclerView = rootView.findViewById(R.id.recipe_step_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(null);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_KEY)) {
                Recipe recipe = savedInstanceState.getParcelable(RECIPE_KEY);
                setRecipe(recipe);
            }
            if (savedInstanceState.containsKey(SELECTED_ITEM_POS_KEY)){
                mSelectedItemPos = savedInstanceState.getInt(SELECTED_ITEM_POS_KEY);

                // using "post()" method here to get smooth scroll to selected item
                // and getting it highlighted too
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        setSelectedItem(mSelectedItemPos);
                    }
                });

            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSelectedItemPos != INVALID_POSITION)
            outState.putInt(SELECTED_ITEM_POS_KEY, mSelectedItemPos);
        if (mRecipe != null)
            outState.putParcelable(RECIPE_KEY, mRecipe);
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        mRecipeStepAdapter = new RecipeStepAdapter(mRecipe,
                (RecipeGeneralActivity) getActivity(), getActivity());
        mRecyclerView.setAdapter(mRecipeStepAdapter);
    }

    public void setSelectedItem(int position) {
        mSelectedItemPos = position;
        mRecipeStepAdapter.setSelectedItem(position);
        mRecyclerView.smoothScrollToPosition(position);
    }
}
