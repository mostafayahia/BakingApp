package nd801project.elmasry.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.BakingAppUtil;

/**
 * Created by yahia on 3/15/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private final Context mContext;
    private List<Recipe> mRecipes;
    private final RecipeItemCallback mCallback;

    public RecipeAdapter(Context context, RecipeItemCallback recipeItemCallback) {
        mContext = context;
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
        Recipe recipe = mRecipes.get(position);

        // setting the name if the recipe
        String recipeName = recipe.getName();
        holder.recipeNameTextView.setText(recipeName);

        // setting the image of the recipe if possible
        String imageUrl = recipe.getImageUrl();
        if (BakingAppUtil.validImageUrl(imageUrl)) {
            Picasso.with(mContext)
                    .load(imageUrl)
                    .error(R.drawable.error_in_loading_image)
                    .into(holder.recipeImageView);
        } else {
            // for testing
//            Picasso.with(mContext)
//                    .load(R.drawable.error_in_loading_image)
//                    .error(R.drawable.error_in_loading_image)
//                    .into(holder.recipeImageView);
            holder.recipeImageView.setVisibility(View.GONE);
        }
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

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private final ImageView recipeImageView;
        private final TextView recipeNameTextView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.list_item_recipe_name_text_view);
            recipeImageView = itemView.findViewById(R.id.list_item_recipe_image);
            itemView.findViewById(R.id.list_item_recipe_main_layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mRecipes.get(getAdapterPosition());
            mCallback.clickHandler(recipe);
        }
    }
}
