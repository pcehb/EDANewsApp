package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.model.Feed;
import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;

public class FeedFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private RecyclerView feedListView;
    private LinearLayoutManager layoutManager;
    private FeedAdapter adapter;
    private FeedViewModel viewModel;
    private ProgressBar progressBar;
    private String articleUrl;

    private final Observer<List<Articles>> feedsListObserver = new Observer<List<Articles>>(){
        @Override
        public void onChanged(List<Articles> Articles){
            adapter.updateData(Articles);
        }
    };

    private final Observer<FeedsRepository.NetworkStatus> networkStatusObserver = new Observer<FeedsRepository.NetworkStatus>(){
        @Override
        public void onChanged(FeedsRepository.NetworkStatus networkStatus){
            if(networkStatus == FeedsRepository.NetworkStatus.LOADING)
                progressBar.setVisibility(View.VISIBLE);
            else progressBar.setVisibility(View.GONE);
        }
    };

    private final Observer<Integer> selectedObserver = new Observer<Integer>(){
        @Override
        public void onChanged(Integer position){
            List<Articles> articles = viewModel.getFeedList().getValue();
            if (articles == null)
                return;

            Articles article = articles.get(position);
            articleUrl = article.getArticleUrl();

            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("articleUrl", articleUrl);

            startActivity(intent);
        }
    };

    private View.OnClickListener listItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            List<Articles> list = viewModel.getFeedList().getValue();
            Articles articles = list.get(position);
            viewModel.setSelectedFeed(position);
        }


    };

    public FeedFragment() {
        // Required empty public constructor
    }


    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        feedListView = (RecyclerView) view.findViewById(R.id.feed_list_view);
        layoutManager = new LinearLayoutManager(getActivity());
        feedListView.setLayoutManager(layoutManager);

        adapter = new FeedAdapter(listItemClickListener);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        feedListView.addItemDecoration(divider);
        feedListView.setAdapter(adapter);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(FeedViewModel.class);

        viewModel.getFeedList().observe((LifecycleOwner) getActivity(), feedsListObserver);
        viewModel.getNetworkStatus().observe((LifecycleOwner) getActivity(), networkStatusObserver);

        viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        viewModel.getSelectedFeed().observe(this, selectedObserver);

        List<Articles> data = viewModel.getFeedList().getValue();
        if (data != null){
            adapter.updateData(data);
        }
        else{

        }

        return view;
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only if there are entries in the back stack
        boolean canGoBack = getFragmentManager().getBackStackEntryCount()>0;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

}
