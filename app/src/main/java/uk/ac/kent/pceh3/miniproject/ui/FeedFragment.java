package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.lang.reflect.Field;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;

public class FeedFragment extends Fragment {
    private RecyclerView feedListView;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private FeedAdapter adapter;
    private FeedViewModel viewModel;
    private ProgressBar progressBar;
    private String articleUrl;
    private RecyclerView recyclerView;
    private int page = 1;
    public String query = "";
    private String category = "";
    private SwipeRefreshLayout swipeContainer;
    private ImageView noImages;

    private final Observer<List<Articles>> feedsListObserver = new Observer<List<Articles>>(){
        @Override
        public void onChanged(List<Articles> Articles){
            adapter.updateData(Articles);
            swipeContainer.setRefreshing(false);
        }
    };

    private final Observer<List<Articles>> loadMoreObserver = new Observer<List<Articles>>(){
        @Override
        public void onChanged(List<Articles> Articles){
            adapter.addData(Articles);
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
            if (adapter.articles == null)
                return;

            Articles article = adapter.articles.get(position);
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

            System.out.println(position);

            viewModel.setSelectedFeed(position);
        }


    };

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        noImages = view.findViewById(R.id.noImages);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        //hide loading wheel to allow custom wheel
        try {
            Field f = swipeContainer.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView)f.get(swipeContainer);
            img.setAlpha(0.0f);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        adapter = new FeedAdapter(listItemClickListener);

        feedListView = (RecyclerView) view.findViewById(R.id.feed_list_view);
        DividerItemDecoration dividerVert = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerVert.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        feedListView.addItemDecoration(dividerVert);
        feedListView.setAdapter(adapter);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape

            feedListView.setLayoutManager(gridLayoutManager);

        } else {
            // In portrait
            feedListView.setLayoutManager(layoutManager);
        }

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(FeedViewModel.class);
        loadFeed(query, category);

        recyclerView = (RecyclerView) view.findViewById(R.id.feed_list_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    viewModel.getFeedList(page, query, category).observe((LifecycleOwner) getActivity(), loadMoreObserver);
                }
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                viewModel.getFeedList(page, query, category).observe((LifecycleOwner) getActivity(), feedsListObserver);
            }
        });


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getActivity() != null) {
            isNetworkConnected();
        }
    }

    public void loadFeed(String search, String searchedCategory){
        query = search;
        page = 1;
        category = searchedCategory;
        viewModel.getFeedList(page, query, category).observe((LifecycleOwner) getActivity(), feedsListObserver);
        viewModel.getNetworkStatus().observe((LifecycleOwner) getActivity(), networkStatusObserver);

        viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        viewModel.getSelectedFeed().observe(this, selectedObserver);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null){
            noImages.setVisibility(View.GONE);
        }
        else {
            noImages.setVisibility(View.VISIBLE);
        }

        return cm.getActiveNetworkInfo() != null;
    }

}
