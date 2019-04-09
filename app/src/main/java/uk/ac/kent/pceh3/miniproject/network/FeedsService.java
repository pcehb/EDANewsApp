package uk.ac.kent.pceh3.miniproject.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.model.Feed;

/**
 * Created by pceh3 on 30/01/2019.
 */

public interface FeedsService {
    static final String BASE_URL = "http://www.ubicomp-kent.org/projects/newsfeed/";

    @GET("articles.cgi")
    Call<Feed> getAllFeeds(@Query("p") int page, @Query("q") String query, @Query("c") String category);

    @GET("article.cgi")
    Call<Article> getArticle(@Query("url") String articleUrl);

}
