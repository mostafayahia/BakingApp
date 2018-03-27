package nd801project.elmasry.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;

/**
 * Created by yahia on 3/17/18.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepAdapterViewHolder> {

    private final Context mContext;
    private final Recipe mRecipe;
    private final RecipeStepItemCallback mCallback;

    private static final int VIEW_TYPE_RECIPE_INGREDIENTS = 0;
    private static final int VIEW_TYPE_RECIPE_STEP = 1;

    private int selectedPos = RecyclerView.NO_POSITION;

    public RecipeStepAdapter(Recipe recipe, RecipeStepItemCallback callback, Context context) {
        mContext = context;
        mRecipe = recipe;
        mCallback = callback;
    }

    interface RecipeStepItemCallback {
        void clickHandler(int recipeStepIndex);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_RECIPE_INGREDIENTS;
        return VIEW_TYPE_RECIPE_STEP;
    }


    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_RECIPE_INGREDIENTS) {
            view = inflater.inflate(R.layout.recipe_step_ingredients_list_item, parent, false);
        } else if (viewType == VIEW_TYPE_RECIPE_STEP) {
            view = inflater.inflate(R.layout.recipe_step_list_item, parent, false);
        } else {
            throw new IllegalArgumentException("unknown viewType: " + viewType);
        }
        return new RecipeStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepAdapterViewHolder holder, int position) {
        // Recall: first item is reserved to display recipe ingredients
        if (position == 0) {
            // display recipe ingredients
            String ingredientsText =
                    StoringInDbUtil.getRecipeIngredientsText(mContext, mRecipe.getIngredients());
            holder.recipeStepTextView.setText(ingredientsText);
        } else {
            // (position-1) because the first item is reserved to display recipe ingredients
            String shortDescription = mRecipe.getSteps().get(position - 1).getShortDescription();
            holder.recipeStepTextView.setText(shortDescription);

            // to highlight the selected item in recycler view
            holder.recipeStepTextView.setSelected(selectedPos == position);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null) return 0;

        // Note: first item is reserved to display recipe ingredients
        List<Recipe.RecipeStep> recipeSteps = mRecipe.getSteps();
        if (recipeSteps == null) return 1;
        return recipeSteps.size() + 1;
    }


    class RecipeStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView recipeStepTextView;

        public RecipeStepAdapterViewHolder(View itemView) {
            super(itemView);
            recipeStepTextView = itemView.findViewById(R.id.list_item_recipe_step_text_view);
            recipeStepTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Recall: first item is reserved for displaying recipe ingredients
            mCallback.clickHandler(getAdapterPosition() - 1);

            // used to trigger updating the highlighting for the new selected item
            int position = getAdapterPosition();
            setSelectedItem(position);
        }
    }

    public void setSelectedItem(int position) {
        // Recall: first item is reserved for displaying recipe ingredients
        if (position != 0) {
            notifyItemChanged(selectedPos);
            selectedPos = position;
            notifyItemChanged(selectedPos);
        }
    }
}

