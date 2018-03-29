package nd801project.elmasry.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import nd801project.elmasry.bakingapp.R;
import nd801project.elmasry.bakingapp.model.Recipe;
import nd801project.elmasry.bakingapp.utilities.BakingAppUtil;
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
    private static final String PLAYER_POSITION_KEY = "player-position";
    private static final String PLAY_WHEN_READY_KEY = "play-when-ready";

    private long mPlayerPos;
    private boolean mPlayWhenReady = true; // true by default


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mPlayerView = rootView.findViewById(R.id.player_view);

        mRecipeStepInstructionTv = rootView.findViewById(R.id.step_instruction_text_view);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_STEP_KEY))
                mRecipeStep = savedInstanceState.getParcelable(RECIPE_STEP_KEY);
            if (savedInstanceState.containsKey(PLAYER_POSITION_KEY))
                mPlayerPos = savedInstanceState.getLong(PLAYER_POSITION_KEY);
            if (savedInstanceState.containsKey(PLAY_WHEN_READY_KEY))
                mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipeStep != null)
            outState.putParcelable(RECIPE_STEP_KEY, mRecipeStep);
        if (mExoPlayer != null)
            outState.putLong(PLAYER_POSITION_KEY, mExoPlayer.getCurrentPosition());
        if (mExoPlayer != null)
            outState.putBoolean(PLAY_WHEN_READY_KEY, mExoPlayer.getPlayWhenReady());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRecipeStep == null) return;
        if (mExoPlayer == null) {
            makeUpdates(mRecipeStep, mPlayerPos, mPlayWhenReady);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) releasePlayer();
    }

    /**
     * Update to the whole UI of this fragment & exoplayer according to the given recipeStep
     *
     * @param recipeStep
     */
    public void makeUpdates(Recipe.RecipeStep recipeStep) {
        makeUpdates(recipeStep, 0, true);
    }

    private void makeUpdates(Recipe.RecipeStep recipeStep, long playerPos, boolean playWhenReady) {
        mRecipeStep = recipeStep;
        String videoURL = mRecipeStep.getVideoURL();
        String thumbnailUrl = mRecipeStep.getThumbnailURL();
        updateUI(videoURL, thumbnailUrl);
        if (validVideoUrl(videoURL)) {
            if (mExoPlayer == null) initializePlayer();
            updatePlayer(videoURL);
            mExoPlayer.seekTo(playerPos);
            mExoPlayer.setPlayWhenReady(playWhenReady);
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
     * update the UI of this fragment according to video url & thumbnailUrl
     *
     * @param videoUrl
     */
    private void updateUI(String videoUrl, String thumbnailUrl) {
        if (!validVideoUrl(videoUrl)) {
            showNoVideoMessageView();
            if (videoUrl != null && videoUrl.trim().length() > 0 &&
                    !videoUrl.trim().toLowerCase().endsWith(VIDEO_FORMAT.toLowerCase()))
                Log.e(LOG_TAG, "Non mp4 video format isn't supported, the given one: " +
                        videoUrl);
        } else {
            hideNoVideoMessageView();
            if (!HelperUtil.isDeviceOnline(getContext()))
                showNoInternetNoVideoMessageView();
            else
                hideNoInternetNoVideoMessageView();
        }

        ImageView stepImageView = getView().findViewById(R.id.step_image);
        if (BakingAppUtil.validImageUrl(thumbnailUrl)) {

            Picasso.with(getContext())
                    .load(thumbnailUrl)
                    .error(R.drawable.error_in_loading_image)
                    .into(stepImageView);
        } else {
            // for testing
//            Picasso.with(getContext())
//                    .load(R.drawable.error_in_loading_image)
//                    .error(R.drawable.error_in_loading_image)
//                    .into(stepImageView);

            stepImageView.setVisibility(View.GONE);
            if (thumbnailUrl != null && thumbnailUrl.trim().length() > 0 &&
                    thumbnailUrl.trim().toLowerCase().endsWith("mp4"))
                Log.e(LOG_TAG, "it's mp4 file NOT image file");
        }
    }

    private ExtractorMediaSource getMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);
    }

    private void updatePlayer(String videoUrl) {
        ExtractorMediaSource mediaSource = getMediaSource(Uri.parse(videoUrl));
        mExoPlayer.prepare(mediaSource);
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
        mPlayerView.setPlayer(mExoPlayer);
    }

    private boolean validVideoUrl(String videoUrl) {
        return videoUrl != null && videoUrl.trim().length() > 0 &&
                videoUrl.trim().toLowerCase().endsWith(VIDEO_FORMAT.toLowerCase());
    }
}
