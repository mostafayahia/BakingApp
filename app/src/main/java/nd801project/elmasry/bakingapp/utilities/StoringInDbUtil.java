package nd801project.elmasry.bakingapp.utilities;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.provider.RecipeContract;

/**
 * Created by yahia on 3/24/18.
 */

public class StoringInDbUtil {

    // if you CHANGE the values of these constants we can NOT retrieve the data from the database
    private static final String LINE_BREAKS = "\n";
    private static final String SEPARATOR = " ";
    private static final String ENCODE_SEPARATOR = "SSSSS";

    /**
     * Getting simple recipe ingredients string from the recipe ingredients list
     *
     * @param recipeIngredients
     * @return simple recipe ingredients string from the recipe ingredients list
     */
    public static String getRecipeIngredientsText(Context context, List<Recipe.RecipeIngredient> recipeIngredients) {

        if (recipeIngredients == null) return null;

        StringBuilder ingredientsText = new StringBuilder(context.getString(R.string.label_ingredients) + LINE_BREAKS);

        for (Recipe.RecipeIngredient ingredient : recipeIngredients) {
            ingredientsText.append(ingredient.getQuantity()).append(SEPARATOR)
                    .append(ingredient.getMeasure()).append(SEPARATOR)
                    .append(ingredient.getIngredient()).append(LINE_BREAKS);
        }

        // remove line breaks at the end of ingredientsText
        ingredientsText = new StringBuilder(removeLastSeparatorFromText(ingredientsText.toString(), LINE_BREAKS));

        return ingredientsText.toString();
    }

    /**
     * Getting list of ingredients from the simple recipe ingredients String which obtained
     * from getRecipeIngredientsText() method
     *
     * @param context
     * @param recipeIngredientsText
     * @return list of ingredients from the simple recipe ingredients String
     */
    public static List<Recipe.RecipeIngredient> getIngredientsListFromIngredientsText(
            Context context, String recipeIngredientsText) {

        if (recipeIngredientsText == null) return null;

        List<Recipe.RecipeIngredient> recipeIngredients = new ArrayList<>();

        String[] ingredientStringArray = recipeIngredientsText.split(LINE_BREAKS);

        String ingredientsLabel = context.getString(R.string.label_ingredients);

        for (String ingredientString : ingredientStringArray) {
            if (!ingredientString.equals(ingredientsLabel)) {
                String quantityText = ingredientString.split(SEPARATOR)[0];
                String measure = ingredientString.split(SEPARATOR)[1];
                // example for ingredientString "2.0 CUP  Graham Cracker crumbs"
                String ingredientName = ingredientString.substring(
                        quantityText.length() + SEPARATOR.length() + measure.length() + SEPARATOR.length());
                Recipe.RecipeIngredient recipeIngredient = new Recipe.RecipeIngredient();
                recipeIngredient.setMeasure(measure);
                recipeIngredient.setQuantity(Double.valueOf(quantityText));
                recipeIngredient.setIngredient(ingredientName);
                recipeIngredients.add(recipeIngredient);
            }
        }

        return recipeIngredients;
    }

    /**
     * Getting encoded text for all recipe steps videos urls to store in the database
     * @param recipeSteps
     * @return encoded text for all recipe steps videos urls to store in the database
     */
    public static String getEncodedTextForStepsVideos(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder videosEncodedText = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            videosEncodedText.append(recipeStep.getVideoURL()).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(videosEncodedText.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting encoded text for all recipe steps long description to store in the database
     * @param recipeSteps
     * @return encoded text for all recipe steps long description to store in the database
     */
    public static String getEncodedTextForStepsLongDesc(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder stepsLongDescText = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            stepsLongDescText.append(recipeStep.getDescription()).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(stepsLongDescText.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting encoded text for all recipe steps short description to store in the database
     * @param recipeSteps
     * @return encoded text for all recipe steps short description to store in the database
     */
    public static String getEncodedTextForStepsShortDesc(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder stepsShortDescText = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            stepsShortDescText.append(recipeStep.getShortDescription()).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(stepsShortDescText.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting of recipe steps list from encoded texts which are stored in the database
     * @param stepsShortDescEncodedText
     * @param stepsLongDescEncodedText
     * @param stepsVideosEncodedText
     * @return recipe steps list from encoded texts which are stored in the database
     */
    public static List<Recipe.RecipeStep> getRecipeStepListFrom(String stepsShortDescEncodedText,
                                                                String stepsLongDescEncodedText, String stepsVideosEncodedText) {
        if (stepsLongDescEncodedText == null || stepsShortDescEncodedText == null ||
                stepsVideosEncodedText == null)
            return null;

        String[] videosUrlArray = stepsVideosEncodedText.split(ENCODE_SEPARATOR);
        String[] longDescArray = stepsLongDescEncodedText.split(ENCODE_SEPARATOR);
        String[] shortDescArray = stepsShortDescEncodedText.split(ENCODE_SEPARATOR);

        if (videosUrlArray.length != longDescArray.length || videosUrlArray.length != shortDescArray.length)
            throw new IllegalArgumentException("Arguments' arrays doesn't have the same size after decoding");

        List<Recipe.RecipeStep> recipeSteps = new ArrayList<>(videosUrlArray.length);

        for (int i = 0; i < videosUrlArray.length; i++) {
            Recipe.RecipeStep recipeStep = new Recipe.RecipeStep();
            recipeStep.setDescription(longDescArray[i]);
            recipeStep.setShortDescription(shortDescArray[i]);
            recipeStep.setVideoURL(videosUrlArray[i]);
            recipeSteps.add(recipeStep);
        }

        return recipeSteps;
    }

    private static String removeLastSeparatorFromText(String text, String separator) {
        return text.substring(0, text.length() - separator.length());
    }

    /**
     * After moving the cursor to a certain position, this method extract the data from it
     * and return the corresponding recipe object
     * @param cursorAfterMovingToCertainPos
     * @return corresponding recipe after extracting the data from cursorAfterMovingToCertainPos
     */
    public static Recipe getRecipeFromDbCursor(Cursor cursorAfterMovingToCertainPos,
                                               Context context) {
        int recipeNameIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_NAME);
        int recipeIngredientsIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_INGREDIENTS);
        int stepsLongDescEncodeTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_LONG_DESC_ENCODED_TEXT);
        int stepsShortDescEncodedTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_SHORT_DESC_ENCODED_TEXT);
        int stepsVideosEncodedTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_VIDEOS_ENCODED_TEXT);

        String recipeName = cursorAfterMovingToCertainPos.getString(recipeNameIndex);
        String recipeIngredientsText = cursorAfterMovingToCertainPos.getString(recipeIngredientsIndex);
        String stepsLongDescEncodedText = cursorAfterMovingToCertainPos.getString(stepsLongDescEncodeTextIndex);
        String stepsShortDescEncodedText = cursorAfterMovingToCertainPos.getString(stepsShortDescEncodedTextIndex);
        String stepsVideosEncodedText = cursorAfterMovingToCertainPos.getString(stepsVideosEncodedTextIndex);

        List<Recipe.RecipeStep> recipeSteps = StoringInDbUtil.getRecipeStepListFrom(
                stepsShortDescEncodedText, stepsLongDescEncodedText, stepsVideosEncodedText);

        List<Recipe.RecipeIngredient> ingredients =
                StoringInDbUtil.getIngredientsListFromIngredientsText(context, recipeIngredientsText);

        Recipe recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setIngredients(ingredients);
        recipe.setSteps(recipeSteps);

        return recipe;
    }

}
