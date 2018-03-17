package nd801project.elmasry.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;

/**
 * Created by yahia on 3/15/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipes;
    private RecipeItemCallback mCallback;

    public RecipeAdapter(List<Recipe> recipes, RecipeItemCallback recipeItemCallback) {
        this.mRecipes = recipes;
        this.mCallback = recipeItemCallback;
    }

    interface RecipeItemCallback {
        void clickHandler(Recipe recipe);
    }
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        String recipeName = mRecipes.get(position).getName();
        holder.recipeNameTextView.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        return mRecipes.size();
    }

    public void swapRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        this.notifyDataSetChanged();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView recipeNameTextView;
        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.list_item_recipe_name_text_view);
            recipeNameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mRecipes.get(getAdapterPosition());
            mCallback.clickHandler(recipe);;
        }
    }
}
