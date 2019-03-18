package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;


public class DetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView desc;
    private ImageView photo;
    private ProgressBar progressBar;
    private FeedViewModel viewModel;
    private String articleUrl;

    private final Observer<Article> articleObserver = new Observer<Article>(){
        @Override
        public void onChanged(Article Article){
            updateData(Article);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        articleUrl = intent.getStringExtra("articleUrl");

        setContentView(R.layout.activity_details);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);

        viewModel.getArticle(articleUrl).observe(this, articleObserver);
        viewModel.getNetworkStatus().observe(this, networkStatusObserver);


        Article data = viewModel.getArticle(articleUrl).getValue();
        if (data != null){
            updateData(data);
        }
        else{

        }
    }

    public void updateData(Article data){
        title=(TextView) findViewById(R.id.article_title);
        desc=(TextView) findViewById(R.id.article_desc);
        photo=(ImageView) findViewById(R.id.toolbarImage);

        title.setText(data.getTitle());
        desc.setText(Arrays.toString(data.getContent()).replaceAll("\\[|\\]", "")
                .replaceAll("\\u002E\\u002C ",".\n\n").replaceAll("\\u00a0", ""));

        Picasso.get()
                .load(data.getImageUrl())
                .placeholder(R.drawable.newspaper)
                .into(photo);

    }

}
