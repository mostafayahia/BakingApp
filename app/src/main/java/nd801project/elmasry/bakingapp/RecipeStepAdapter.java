package nd801project.elmasry.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;

/**
 * Created by yahia on 3/17/18.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepAdapterViewHolder> {

    private List<Recipe.RecipeStep> mRecipeSteps;
    private RecipeStepItemCallback mCallback;

    public RecipeStepAdapter(List<Recipe.RecipeStep> recipeSteps, RecipeStepItemCallback callback) {
        mRecipeSteps = recipeSteps;
        mCallback = callback;
    }

    interface RecipeStepItemCallback {
        void clickHandler(int recipeStepIndex);
    }


    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepAdapterViewHolder holder, int position) {
        String shortDescription = mRecipeSteps.get(position).getShortDescription();
        holder.recipeStepTextView.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        if (mRecipeSteps == null) return 0;
        return mRecipeSteps.size();
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
            mCallback.clickHandler(getAdapterPosition());
        }
    }
}

