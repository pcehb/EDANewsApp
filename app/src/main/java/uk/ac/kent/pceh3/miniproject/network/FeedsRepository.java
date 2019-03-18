package uk.ac.kent.pceh3.miniproject.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.model.Feed;

/**
 * Created by pceh3 on 30/01/2019.
 */

public class FeedsRepository {
    private Retrofit retrofit;
    private FeedsService feedsService;

    private static final FeedsRepository ourInstance = new FeedsRepository();

    public static FeedsRepository getInstance() {
        return ourInstance;
    }


    public enum NetworkStatus {
        IDLE, LOADING, SUCCESS, ERROR
    }
    private MutableLiveData<NetworkStatus> networkStatus = new MutableLiveData<>();

    public LiveData<NetworkStatus> getNetworkStatus(){
        return networkStatus;
    }

    private FeedsRepository() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(FeedsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        feedsService = retrofit.create(FeedsService.class);
        networkStatus.setValue(NetworkStatus.IDLE);

    }

    // Retrieve list of feed articles
    public LiveData<List<Articles>> getFeedList() {
        final MutableLiveData<Feed> data = new MutableLiveData<>();
        final MutableLiveData<List<Articles>> articles = new MutableLiveData<>();
        // Get the HTTP call
        Call<Feed> call = feedsService.getAllFeeds();
        // Initiate network call in the background
        networkStatus.setValue(NetworkStatus.LOADING);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                articles.setValue(response.body().getArticles());
                networkStatus.setValue(NetworkStatus.IDLE);
            }
            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                System.out.println(t.getMessage());
                networkStatus.setValue(NetworkStatus.IDLE);
            // Handle failure
            }
        });

        return articles;
    }

    // Retrieve list of feed articles
    public LiveData<Article> getArticle(String url) {
        final MutableLiveData<Article> data = new MutableLiveData<>();
        // Get the HTTP call
        Call<Article> call = feedsService.getArticle(url);
        // Initiate network call in the background
        networkStatus.setValue(NetworkStatus.LOADING);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                data.setValue(response.body());
                networkStatus.setValue(NetworkStatus.IDLE);
            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                System.out.println(t.getMessage());
                networkStatus.setValue(NetworkStatus.IDLE);
                // Handle failure
            }
        });

        return data;
    }

}
