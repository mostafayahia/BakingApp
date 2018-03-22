package nd801project.elmasry.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    private static final String RECIPE_INGREDIENTS = "INGREDIENTS:\n" +
            "2.0 CUP graham cracker crumbs\n" +
            "6.0 TBLSP unsalted butter, melted\n" +
            "0.5 CUP granulated sugar\n" +
            "1.5 TSP salt\n" +
            "5.0 TBLSP vanilla\n" +
            "1.0 K nutella or other chocolate-hazelnut spread\n" +
            "500.0 G mascapone cheese(room temperature)\n" +
            "1.0 CUP heavy cream(cold)\n" +
            "4.0 OZ cream cheese(softened)\n";

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickRecipeItem_openRecipeGeneralActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        // making sure the RecipeGeneralActivity display correct recipe data (like recipe ingredients)
        Espresso.onView(withRecyclerView(R.id.recipe_step_recycler_view).atPosition(0))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(
                        ViewMatchers.withText(RECIPE_INGREDIENTS))));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
