package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;


public class DetailsActivity extends AppCompatActivity {

    private TextView title;
    private String date;
    private TextView desc;
    private ImageView photo;
    private ProgressBar progressBar;
    private FeedViewModel viewModel;
    private String articleUrl;
    private Boolean isFABOpen = false;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab;

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
        final Intent intent = getIntent();
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, articleUrl);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
            }
        });
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent internet = new Intent(Intent.ACTION_VIEW);
                internet.setData(Uri.parse(articleUrl));
                startActivity(internet);
            }
        });

    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        ViewCompat.animate(fab)
                .rotation(45.0F)
                .withLayer()
                .setDuration(300L)
                .start();
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        ViewCompat.animate(fab)
                .rotation(0.0F)
                .withLayer()
                .setDuration(300L)
                .start();

    }

    public void updateData(Article data){
        title=(TextView) findViewById(R.id.article_title);
        desc=(TextView) findViewById(R.id.article_desc);
        photo=(ImageView) findViewById(R.id.toolbarImage);

        date = data.getDateTime();
        String[] dateFormattedPart = date.split("T"); // yyyy/mm/dd
        String[] dateSplit = dateFormattedPart[0].split("-");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int month = Integer.parseInt(dateSplit[1]);
        month = month -1;
        date = dateSplit[2].toString() + " " + months[month] + " " + dateSplit[0].toString();

        CollapsingToolbarLayout toolbar = findViewById(R.id.collapsingToolbar);

        toolbar.setTitle(date);

        title.setText(data.getTitle());
        desc.setText(Arrays.toString(data.getContent()).replaceAll("\\[|\\]", "")
                .replaceAll("\\u002E\\u002C ",".\n\n").replaceAll("\\u00a0", ""));

        Picasso.get()
                .load(data.getImageUrl())
                .placeholder(R.drawable.newspaper)
                .into(photo);

    }

}
