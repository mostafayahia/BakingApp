package nd801project.elmasry.bakingapp.utilities;

/**
 * Created by yahia on 3/28/18.
 */

public class BakingAppUtil {

    /**
     * image url valid only if it's not null and not empty string and also doesn't end with mp4 (not mp4 file)
     * @param imageUrl
     * @return true if image url is not null and not empty string and doesn't end with mp4
     */
    public static boolean validImageUrl(String imageUrl) {
        return imageUrl != null && imageUrl.trim().length() > 0 &&
                !imageUrl.trim().toLowerCase().endsWith("mp4");
    }

}
