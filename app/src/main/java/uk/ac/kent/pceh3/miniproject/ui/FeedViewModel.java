package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;

/**
 * Created by pceh3 on 23/01/2019.
 */

public class FeedViewModel extends ViewModel {

    private LiveData<List<Articles>> feedList;


    public MutableLiveData<Integer> getSelectedFeed() {
        return selectedFeed;
    }

    public void setSelectedFeed(int position) {
        selectedFeed.setValue(position);
    }

    private MutableLiveData<Integer> selectedFeed= new MutableLiveData<Integer>();

    public LiveData<List<Articles>> getFeedList() {

        if (feedList == null) {
        // Load from server
            feedList = FeedsRepository.getInstance().getFeedList();
        }
        return feedList;
    }



    public LiveData<FeedsRepository.NetworkStatus> getNetworkStatus(){
        return FeedsRepository.getInstance().getNetworkStatus();
    }

}
