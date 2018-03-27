package nd801project.elmasry.bakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by yahia on 3/24/18.
 */

public class RecipeContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_NAME = "recipeName";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_INGREDIENTS = "recipeIngredients";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_STEPS_LONG_DESC_ENCODED_TEXT = "recipeStepsLongDescEncodedText";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_STEPS_SHORT_DESC_ENCODED_TEXT = "recipeStepsShortDescEncodedText";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_STEPS_VIDEOS_ENCODED_TEXT = "recipeStepsVideosEncodedText";


}