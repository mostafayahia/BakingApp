package nd801project.elmasry.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import nd801project.elmasry.bakingapp.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    public static final String EXTRA_RECIPE_STEP_LIST = "nd801project.elmasry.bakingapp.extra.RECIPE_STEP_LIST";
    public static final String EXTRA_RECIPE_STEP_INDEX = "nd801project.elmasry.bakingapp.extra.RECIPE_STEP_INDEX";
    public static final String EXTRA_RECIPE_NAME = "nd108project.elmasry.bakingapp.extra.RECIPE_NAME";

    private static final String STEP_INDEX_KEY = "step-index-key";

    private static final String VIDEO_FORMAT = "MP4";
    private static final int INVALID_STEP_INDEX = -1;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mRecipeStepInstructionTv;
    private int mStepIndex;
    private ArrayList<Recipe.RecipeStep> mRecipeSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // retrieving recipe step data from the activity started this activity
        mRecipeSteps = getIntent().getParcelableArrayListExtra(EXTRA_RECIPE_STEP_LIST);

        if (savedInstanceState == null) {
            // getting recipe object from the activity that started this activity
            mStepIndex = getIntent().getIntExtra(EXTRA_RECIPE_STEP_INDEX, INVALID_STEP_INDEX);
        } else if (savedInstanceState.containsKey(STEP_INDEX_KEY)) {
            mStepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
        }

        // if no recipe step info, there is nothing to do
        if (mStepIndex == INVALID_STEP_INDEX) {
            Toast.makeText(this, R.string.error_no_recipe_step_data, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        if (recipeName != null) setTitle(recipeName);

        Recipe.RecipeStep recipeStep = mRecipeSteps.get(mStepIndex);

        mPlayerView = findViewById(R.id.player_view);

        // getting video url
        String videoUrl = recipeStep.getVideoURL();
        updateUI(videoUrl);
        if (validVideoUrl(videoUrl)) {
            initializePlayer();
            updatePlayer(videoUrl);
        }

        // display recipe step instruction
        mRecipeStepInstructionTv = findViewById(R.id.step_instruction_text_view);
        mRecipeStepInstructionTv.setText(recipeStep.getDescription());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mStepIndex != INVALID_STEP_INDEX)
            outState.putInt(STEP_INDEX_KEY, mStepIndex);
    }

    private void updatePlayer(String videoUrl) {
        ExtractorMediaSource mediaSource = getMediaSource(Uri.parse(videoUrl));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        mPlayerView.setPlayer(mExoPlayer);
    }

    private boolean validVideoUrl(String videoUrl) {
        return videoUrl != null && videoUrl.length() > 0 &&
                videoUrl.toLowerCase().endsWith(VIDEO_FORMAT.toLowerCase());
    }

    /**
     * update the UI of this activity according to video url
     * @param videoUrl
     */
    private void updateUI(String videoUrl) {
        if (!validVideoUrl(videoUrl)) {
            showNoVideoMessageView();
            if (!videoUrl.toLowerCase().endsWith(VIDEO_FORMAT.toLowerCase()))
                Log.e(LOG_TAG, "Non mp4 video format isn't supported, the given one: " +
                        videoUrl);
        } else {
            hideNoVideoMessageView();
        }
    }

    private ExtractorMediaSource getMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(this, "BakingApp");
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(this, userAgent),
                new DefaultExtractorsFactory(), null, null);
        return mediaSource;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mExoPlayer != null) releasePlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mExoPlayer == null) {
            String videoURL = mRecipeSteps.get(mStepIndex).getVideoURL();
            updateUI(videoURL);
            if (validVideoUrl(videoURL)) {
                initializePlayer();
                updatePlayer(videoURL);
            }
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void showNoVideoMessageView() {
        findViewById(R.id.message_no_video_text_view).setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
    }

    private void hideNoVideoMessageView() {
        findViewById(R.id.message_no_video_text_view).setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    /**
     * Click handler for back button in this activity
     * @param view
     */
    public void clickBackHandler(View view) {
        if (mStepIndex <= 0) return;
        mStepIndex--;
        makeUpdates(mStepIndex);
    }

    /**
     * Update to the whole UI of this activity & exoplayer according to the given recipeStepIndex
     * @param recipeStepIndex
     */
    private void makeUpdates(int recipeStepIndex) {

        Recipe.RecipeStep recipeStep = mRecipeSteps.get(recipeStepIndex);

        String videoURL = recipeStep.getVideoURL();
        updateUI(videoURL);
        if (validVideoUrl(videoURL)) {
            if (mExoPlayer == null) initializePlayer();
            updatePlayer(videoURL);
        } else {
            releasePlayer();
        }

        String stepInstruction = recipeStep.getDescription();
        mRecipeStepInstructionTv.setText(stepInstruction);
    }


    /**
     * Click handler for Forward button in this activity
     * @param view
     */
    public void clickForwardHandler(View view) {
        if (mStepIndex >= mRecipeSteps.size() - 1) return;
        mStepIndex++;
        makeUpdates(mStepIndex);
    }
}
