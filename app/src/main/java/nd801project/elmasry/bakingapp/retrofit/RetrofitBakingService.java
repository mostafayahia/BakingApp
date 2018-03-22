package nd801project.elmasry.bakingapp.retrofit;

import java.util.List;

import nd801project.elmasry.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by yahia on 3/15/18.
 */

public final class RetrofitBakingService {

    public interface BakingApi {

        @GET("topher/2017/May/59121517_baking/baking.json")
        Call<List<Recipe>> getRecipes();

    }
}
