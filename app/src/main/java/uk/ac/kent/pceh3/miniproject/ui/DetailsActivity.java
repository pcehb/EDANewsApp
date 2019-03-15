package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.model.Feed;


public class DetailsActivity extends AppCompatActivity {

    private TextView name;
    private TextView phoneNumber;
    private ImageView photo;
    private FeedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name=(TextView) findViewById(R.id.full_name);
        phoneNumber=(TextView) findViewById(R.id.phone_number);
        photo=(ImageView) findViewById(R.id.contact_photo);

        viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        viewModel.getSelectedFeed().observe(this, selectedObserver);
    }


    private final Observer<Integer> selectedObserver = new Observer<Integer>(){
        @Override
        public void onChanged(Integer position){
            System.out.println("Clicked");
            updateView(position);
        }
    };



    private  void updateView (int position){
        List<Articles> articles = viewModel.getFeedList().getValue();
        if (articles == null)
            return;
        Articles article = articles.get(position);

        name.setText(article.getTitle());
        phoneNumber.setText(article.getDescription());
        Picasso.get()
                .load(article.getImageUrl())
                .into(photo);
    }


}
