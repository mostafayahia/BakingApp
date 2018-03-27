package nd801project.elmasry.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.HelperUtil;

/**
 * Created by yahia on 3/20/18.
 */

public class RecipeDetailFragment extends Fragment {

    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mRecipeStepInstructionTv;
    private Recipe.RecipeStep mRecipeStep;

    private static final String VIDEO_FORMAT = "MP4";
    private static final String RECIPE_STEP_KEY = "recipe-step";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mPlayerView = rootView.findViewById(R.id.player_view);

        mRecipeStepInstructionTv = rootView.findViewById(R.id.step_instruction_text_view);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_STEP_KEY)) {
            mRecipeStep = savedInstanceState.getParcelable(RECIPE_STEP_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipeStep != null)
            outState.putParcelable(RECIPE_STEP_KEY, mRecipeStep);
    }

    /**
     * Update to the whole UI of this fragment & exoplayer according to the given recipeStep
     * @param recipeStep
     */
    public void makeUpdates(Recipe.RecipeStep recipeStep) {
        mRecipeStep = recipeStep;
        String videoURL = mRecipeStep.getVideoURL();
        updateUI(videoURL);
        if (validVideoUrl(videoURL)) {
            if (mExoPlayer == null) initializePlayer();
            updatePlayer(videoURL);
        } else {
            if (mExoPlayer != null) releasePlayer();
        }

        String stepInstruction = mRecipeStep.getDescription();
        mRecipeStepInstructionTv.setText(stepInstruction);
    }


    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void showNoVideoMessageView() {
        getView().findViewById(R.id.message_no_video_text_view).setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
        getView().findViewById(R.id.message_no_internet_no_video_text_view).setVisibility(View.GONE);
    }

    private void hideNoVideoMessageView() {
        getView().findViewById(R.id.message_no_video_text_view).setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void showNoInternetNoVideoMessageView() {
        getView().findViewById(R.id.message_no_internet_no_video_text_view).setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
    }

    private void hideNoInternetNoVideoMessageView() {
        getView().findViewById(R.id.message_no_internet_no_video_text_view).setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
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
            if (!HelperUtil.isDeviceOnline(getContext()))
                showNoInternetNoVideoMessageView();
            else
                hideNoInternetNoVideoMessageView();
        }
    }

    private ExtractorMediaSource getMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) releasePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRecipeStep == null) return;
        if (mExoPlayer == null) {
            makeUpdates(mRecipeStep);
        }
    }

    private void updatePlayer(String videoUrl) {
        ExtractorMediaSource mediaSource = getMediaSource(Uri.parse(videoUrl));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
        mPlayerView.setPlayer(mExoPlayer);
    }

    private boolean validVideoUrl(String videoUrl) {
        return videoUrl != null && videoUrl.length() > 0 &&
                videoUrl.toLowerCase().endsWith(VIDEO_FORMAT.toLowerCase());
    }
}
