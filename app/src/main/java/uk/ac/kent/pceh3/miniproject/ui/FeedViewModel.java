package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;

/**
 * Created by pceh3 on 23/01/2019.
 */

public class FeedViewModel extends ViewModel {

    private LiveData<List<Articles>> feedList;

    private MutableLiveData<Integer> selectedFeed= new MutableLiveData<Integer>();

    private LiveData<Article> articleLiveData;

    public MutableLiveData<Integer> getSelectedFeed() {
        return selectedFeed;
    }

    public void setSelectedFeed(int position) {
        selectedFeed.setValue(position);
    }

    public LiveData<List<Articles>> getFeedList(int page, String query){
        feedList = FeedsRepository.getInstance().getFeedList(page, query);
        return feedList;
    }


    public LiveData<Article> getArticle(String url) {
        if (articleLiveData == null) {
            // Load from server
            articleLiveData = FeedsRepository.getInstance().getArticle(url);
        }
        return articleLiveData;
    }


    public LiveData<FeedsRepository.NetworkStatus> getNetworkStatus(){
        return FeedsRepository.getInstance().getNetworkStatus();
    }

}
