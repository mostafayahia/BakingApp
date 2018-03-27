package nd801project.elmasry.bakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by yahia on 3/24/18.
 */

@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class)
public final class RecipeProvider {

    public static final String AUTHORITY = "nd801project.elmasry.bakingapp.provider.provider";

    @TableEndpoint(table = RecipeDatabase.RECIPES)
    public static class Recipes {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipes");

    }

}
