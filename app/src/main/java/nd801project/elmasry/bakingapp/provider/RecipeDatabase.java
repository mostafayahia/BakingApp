package nd801project.elmasry.bakingapp.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by yahia on 3/24/18.
 */

@Database(version = RecipeDatabase.VERSION)
public final class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(RecipeContract.class)
    public static final String RECIPES = "recipes";

}
