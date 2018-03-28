package nd801project.elmasry.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nd801project.elmasry.bakingapp.test_utilities.HelperTestUtil;
import nd801project.elmasry.bakingapp.ui.MainActivity;
import nd801project.elmasry.bakingapp.ui.RecipeGeneralActivity;

/**
 * Created by yahia on 3/26/18.
 */

@RunWith(AndroidJUnit4.class)
public class IdlingResourceMainActivityTest {

    //=============================================================
    //               IMPORTANT:
    //  To test the idling resource, you should uninstall the app
    //  before running IdlingResouceMainActivityTest to force the
    //  app to fetching the data from the internet
    //=============================================================

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;
    private static final String RECIPE_NAME = "Nutella Pie";

    @Before
    public void registerIdlingResourceAndDeleteDb() {

        MainActivity mainActivity = mActivityTestRule.getActivity();

        mIdlingResource = mainActivity.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void makingSureViewsDisplayCorrectRecipeNames() {
        Espresso.onView(HelperTestUtil.withRecyclerView(R.id.recipes_recycler_view).atPosition(0))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(
                        ViewMatchers.withText(RECIPE_NAME))));
    }

    @Test
    public void clickRecipeItem_openRecipeGeneralActivity() {

        Intents.init();

        Espresso.onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        Intents.intended(IntentMatchers.hasComponent(RecipeGeneralActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
