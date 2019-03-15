package uk.ac.kent.pceh3.miniproject.network;

import retrofit2.Call;
import retrofit2.http.GET;
import uk.ac.kent.pceh3.miniproject.model.Feed;

/**
 * Created by pceh3 on 30/01/2019.
 */

public interface FeedsService {
    static final String BASE_URL = "http://www.ubicomp-kent.org/projects/newsfeed/";

    @GET("articles.cgi")
    Call<Feed> getAllFeeds();

}
