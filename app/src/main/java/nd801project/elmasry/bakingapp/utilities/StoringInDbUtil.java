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
    private static final String ENCODE_SEPARATOR = "SS5SSss";

    // using this holder to get correct output after using split method in the String class
    private static final String EMPTY_STRING_HOLDER = "WSsRV5T";

    /**
     * Getting simple recipe ingredients string from the recipe ingredients list
     *
     * @param recipeIngredients
     * @return simple recipe ingredients string from the recipe ingredients list
     */
    public static String getRecipeIngredientsText(Context context, List<Recipe.RecipeIngredient> recipeIngredients) {

        if (recipeIngredients == null) return null;

        StringBuilder ingredientsText =
                new StringBuilder(context.getString(R.string.label_ingredients) + LINE_BREAKS);

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
     *
     * @param recipeSteps
     * @return encoded text for all recipe steps videos urls to store in the database
     */
    public static String getEncodedTextForStepsVideos(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder videosEncodedText = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            String videoURL = recipeStep.getVideoURL();
            if (videoURL.length() > 0)
                videosEncodedText.append(videoURL).append(ENCODE_SEPARATOR);
            else
                videosEncodedText.append(EMPTY_STRING_HOLDER).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(videosEncodedText.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting encoded text for all recipe steps long description to store in the database
     *
     * @param recipeSteps
     * @return encoded text for all recipe steps long description to store in the database
     */
    public static String getEncodedTextForStepsLongDesc(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder stepsLongDescText = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            String description = recipeStep.getDescription();
            if (description.length() > 0)
                stepsLongDescText.append(description).append(ENCODE_SEPARATOR);
            else
                stepsLongDescText.append(EMPTY_STRING_HOLDER).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(stepsLongDescText.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting encoded text for all recipe steps short description to store in the database
     *
     * @param recipeSteps
     * @return encoded text for all recipe steps short description to store in the database
     */
    public static String getEncodedTextForStepsShortDesc(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder stepsShortDescText = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            String shortDescription = recipeStep.getShortDescription();
            if (shortDescription.length() > 0)
                stepsShortDescText.append(shortDescription).append(ENCODE_SEPARATOR);
            else
                stepsShortDescText.append(EMPTY_STRING_HOLDER).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(stepsShortDescText.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting encoded text for all recipe steps' images to store in the database
     *
     * @param recipeSteps
     * @return encoded text for all recipe steps' images to store in the database
     */
    public static String getEncodedTextForStepsImages(List<Recipe.RecipeStep> recipeSteps) {
        StringBuilder stepsImagesTextBuilder = new StringBuilder();
        for (Recipe.RecipeStep recipeStep : recipeSteps) {
            String thumbnailURL = recipeStep.getThumbnailURL();
            if (thumbnailURL.length() > 0)
                stepsImagesTextBuilder.append(thumbnailURL).append(ENCODE_SEPARATOR);
            else
                stepsImagesTextBuilder.append(EMPTY_STRING_HOLDER).append(ENCODE_SEPARATOR);
        }
        return removeLastSeparatorFromText(stepsImagesTextBuilder.toString(), ENCODE_SEPARATOR);
    }

    /**
     * Getting of recipe steps list from encoded texts which are stored in the database
     *
     * @param stepsShortDescEncodedText
     * @param stepsLongDescEncodedText
     * @param stepsVideosEncodedText
     * @return recipe steps list from encoded texts which are stored in the database
     */
    public static List<Recipe.RecipeStep> getRecipeStepListFrom(String stepsShortDescEncodedText,
                                                                String stepsLongDescEncodedText,
                                                                String stepsVideosEncodedText,
                                                                String stepsImagesEncodedText) {
        if (stepsLongDescEncodedText == null || stepsShortDescEncodedText == null ||
                stepsVideosEncodedText == null)
            return null;

        String[] videoUrlArray = stepsVideosEncodedText.split(ENCODE_SEPARATOR);
        String[] longDescArray = stepsLongDescEncodedText.split(ENCODE_SEPARATOR);
        String[] shortDescArray = stepsShortDescEncodedText.split(ENCODE_SEPARATOR);
        String[] imageArray = stepsImagesEncodedText.split(ENCODE_SEPARATOR);

        if (videoUrlArray.length != longDescArray.length || videoUrlArray.length != shortDescArray.length ||
                videoUrlArray.length != imageArray.length)
            throw new IllegalArgumentException("Arguments' arrays doesn't have the same size after decoding");

        // clean the array from EMPTY_STRING_HOLDER and replaces it with real empty string
        // we do that in the first place while encoding data to get correct result form split method
        // exist in the String class
        for (int i = 0; i < videoUrlArray.length; i++) {
            String videoUrl = videoUrlArray[i];
            String longDesc = longDescArray[i];
            String shortDesc = shortDescArray[i];
            String image = imageArray[i];
            if (videoUrl.equals(EMPTY_STRING_HOLDER)) videoUrlArray[i] = "";
            if (longDesc.equals(EMPTY_STRING_HOLDER)) longDescArray[i] = "";
            if (shortDesc.equals(EMPTY_STRING_HOLDER)) shortDescArray[i] = "";
            if (image.equals(EMPTY_STRING_HOLDER)) imageArray[i] = "";
        }

        List<Recipe.RecipeStep> recipeSteps = new ArrayList<>(videoUrlArray.length);

        for (int i = 0; i < videoUrlArray.length; i++) {
            Recipe.RecipeStep recipeStep = new Recipe.RecipeStep();
            recipeStep.setDescription(longDescArray[i]);
            recipeStep.setShortDescription(shortDescArray[i]);
            recipeStep.setVideoURL(videoUrlArray[i]);
            recipeStep.setThumbnailURL(imageArray[i]);
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
     *
     * @param cursorAfterMovingToCertainPos
     * @return corresponding recipe after extracting the data from cursorAfterMovingToCertainPos
     */
    public static Recipe getRecipeFromDbCursor(Cursor cursorAfterMovingToCertainPos,
                                               Context context) {
        if (cursorAfterMovingToCertainPos == null) return null;

        if (cursorAfterMovingToCertainPos.getPosition() < 0)
            throw new IllegalArgumentException("you have to move your cursor into a certain position before using this method");

        int recipeIdColIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_ID);
        int recipeNameIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_NAME);
        int recipeImageIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_IMAGE);
        int recipeIngredientsIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_INGREDIENTS);
        int stepsLongDescEncodeTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_LONG_DESC_ENCODED_TEXT);
        int stepsShortDescEncodedTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_SHORT_DESC_ENCODED_TEXT);
        int stepsVideosEncodedTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_VIDEOS_ENCODED_TEXT);
        int stepsImagesEncodedTextIndex = cursorAfterMovingToCertainPos.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_IMAGES_ENCODED_TEXT);

        int recipeId = cursorAfterMovingToCertainPos.getInt(recipeIdColIndex);
        String recipeName = cursorAfterMovingToCertainPos.getString(recipeNameIndex);
        String recipeImage = cursorAfterMovingToCertainPos.getString(recipeImageIndex);
        String recipeIngredientsText = cursorAfterMovingToCertainPos.getString(recipeIngredientsIndex);
        String stepsLongDescEncodedText = cursorAfterMovingToCertainPos.getString(stepsLongDescEncodeTextIndex);
        String stepsShortDescEncodedText = cursorAfterMovingToCertainPos.getString(stepsShortDescEncodedTextIndex);
        String stepsVideosEncodedText = cursorAfterMovingToCertainPos.getString(stepsVideosEncodedTextIndex);
        String stepsImagesEncodedText = cursorAfterMovingToCertainPos.getString(stepsImagesEncodedTextIndex);

        List<Recipe.RecipeStep> recipeSteps = StoringInDbUtil.getRecipeStepListFrom(
                stepsShortDescEncodedText, stepsLongDescEncodedText, stepsVideosEncodedText, stepsImagesEncodedText);

        List<Recipe.RecipeIngredient> ingredients =
                StoringInDbUtil.getIngredientsListFromIngredientsText(context, recipeIngredientsText);

        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setName(recipeName);
        recipe.setImageUrl(recipeImage);
        recipe.setIngredients(ingredients);
        recipe.setSteps(recipeSteps);

        return recipe;
    }

}
