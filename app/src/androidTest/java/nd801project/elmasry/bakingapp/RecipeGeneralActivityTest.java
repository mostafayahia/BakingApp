package nd801project.elmasry.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import nd801project.elmasry.bakingapp.test_utilities.HelperTestUtil;
import nd801project.elmasry.bakingapp.ui.MainActivity;



@RunWith(AndroidJUnit4.class)
public class RecipeGeneralActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActviTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private static final String LINE_BREAK = "\n";

    private static final String RECIPE_INGREDIENTS = "INGREDIENTS:" + LINE_BREAK +
            "2.0 CUP Graham Cracker crumbs" + LINE_BREAK +
            "6.0 TBLSP unsalted butter, melted" + LINE_BREAK +
            "0.5 CUP granulated sugar" + LINE_BREAK +
            "1.5 TSP salt" + LINE_BREAK +
            "5.0 TBLSP vanilla" + LINE_BREAK +
            "1.0 K Nutella or other chocolate-hazelnut spread" + LINE_BREAK +
            "500.0 G Mascapone Cheese(room temperature)" + LINE_BREAK +
            "1.0 CUP heavy cream(cold)" + LINE_BREAK +
            "4.0 OZ cream cheese(softened)";

    private static final String RECIPE_STEP = "Prep the cookie crust.";

    @Test
    public void makingSureViewsDisplayCorrectRecipeIngredientsAndSteps() {

        Espresso.onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        Espresso.onView(HelperTestUtil.withRecyclerView(R.id.recipe_step_recycler_view).atPosition(0))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(
                        ViewMatchers.withText(RECIPE_INGREDIENTS))));

        Espresso.onView(HelperTestUtil.withRecyclerView(R.id.recipe_step_recycler_view).atPosition(3))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(
                        ViewMatchers.withText(RECIPE_STEP))));

    }
}
