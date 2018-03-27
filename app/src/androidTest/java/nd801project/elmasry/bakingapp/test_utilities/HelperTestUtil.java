package nd801project.elmasry.bakingapp.test_utilities;


/**
 * Created by yahia on 3/25/18.
 */

public class HelperTestUtil {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }


}
