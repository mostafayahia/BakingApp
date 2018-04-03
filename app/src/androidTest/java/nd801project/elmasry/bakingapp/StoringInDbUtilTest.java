package nd801project.elmasry.bakingapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.StoringInDbUtil;

import static org.junit.Assert.*;

/*
 * we are making these tests of this class here in androidTest because there are classes used here in
 * native android classes (like Recipe extends android.os.Parcelable)
 */
@RunWith(AndroidJUnit4.class)
public class StoringInDbUtilTest {

    @Test
    public void testGetRecipeIngredientsText() {
        final String LINE_BREAK = "\n";
        final String RECIPE_INGREDIENTS = "INGREDIENTS:" + LINE_BREAK +
                "2.0 CUP Graham Cracker crumbs" + LINE_BREAK +
                "6.0 TBLSP unsalted butter, melted";

        Recipe.RecipeIngredient recipeIngredient1 = new Recipe.RecipeIngredient();
        recipeIngredient1.setQuantity(2.0);
        recipeIngredient1.setMeasure("CUP");
        recipeIngredient1.setIngredient("Graham Cracker crumbs");

        Recipe.RecipeIngredient recipeIngredient2 = new Recipe.RecipeIngredient();
        recipeIngredient2.setQuantity(6.0);
        recipeIngredient2.setMeasure("TBLSP");
        recipeIngredient2.setIngredient("unsalted butter, melted");

        List<Recipe.RecipeIngredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(recipeIngredient1);
        recipeIngredients.add(recipeIngredient2);

        String output = StoringInDbUtil.getRecipeIngredientsText(InstrumentationRegistry.getTargetContext(),
                recipeIngredients);

        assertTrue("the output should be: " + RECIPE_INGREDIENTS.replace("\n", "\\n") +
                        "\nbut we get: " + output.replace("\n", "\\n"),
                output.equals(RECIPE_INGREDIENTS));

    }

}

