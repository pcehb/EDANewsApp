package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Article;
import uk.ac.kent.pceh3.miniproject.network.FeedsRepository;
import uk.ac.kent.pceh3.miniproject.network.SavedArticlesDB;

import static uk.ac.kent.pceh3.miniproject.network.SavedArticlesDB.SAVED_COLUMN_URL;
import static uk.ac.kent.pceh3.miniproject.network.SavedArticlesDB.SAVED_TABLE_NAME;


public class DetailsActivity extends AppCompatActivity {

    private TextView title;
    private String date;
    private TextView desc;
    private ImageView photo;
    private TextView categories;
    private ProgressBar progressBar;
    private FeedViewModel viewModel;
    private String articleUrl;
    private Boolean isFABOpen = false;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab;
    private android.support.v7.widget.Toolbar toolbar;

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

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        getWindow().setEnterTransition(new Explode());

        final Intent intent = getIntent();
        articleUrl = intent.getStringExtra("articleUrl");

        setContentView(R.layout.activity_details);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        isNetworkConnected();

        // show all fabs
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
        // share article
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
        // save article
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sqldb = new SavedArticlesDB(getApplicationContext()).getReadableDatabase();

                Cursor cursor = sqldb.rawQuery("select * from " + SAVED_TABLE_NAME + " WHERE " + SAVED_COLUMN_URL + " =?", new String[] {articleUrl});
                if(cursor.getCount() <= 0){ //article isn't saved yet
                    cursor.close();
                    SQLiteDatabase database = new SavedArticlesDB(getApplicationContext()).getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(SavedArticlesDB.SAVED_COLUMN_URL, articleUrl);
                    values.put(SavedArticlesDB.SAVED_COLUMN_TITLE, title.getText().toString());
                    values.put(SavedArticlesDB.SAVED_COLUMN_DESC, desc.getText().toString());
                    Bitmap image = ((BitmapDrawable)photo.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    values.put(SavedArticlesDB.SAVED_COLUMN_PHOTO, imageInByte);
                    values.put(SavedArticlesDB.SAVED_COLUMN_DATE, date);
                    values.put(SavedArticlesDB.SAVED_COLUMN_CAT, categories.getText().toString());

                    database.insert(SavedArticlesDB.SAVED_TABLE_NAME, null, values);

                    Toast.makeText(getApplicationContext(), "Article saved", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Article already saved", Toast.LENGTH_LONG).show();
                    cursor.close();
                }

            }
        });
        // open article in web
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
        categories=(TextView) findViewById(R.id.categories);
        toolbar = findViewById(R.id.toolbar);

        date = data.getDateTime();
        String[] dateFormattedPart = date.split("T"); // yyyy/mm/dd
        String[] dateSplit = dateFormattedPart[0].split("-");
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        int month = Integer.parseInt(dateSplit[1]);
        month = month -1;
        date = dateSplit[2].toString() + " " + months[month] + " " + dateSplit[0].toString();

        toolbar.setTitle(date);

        title.setText(data.getTitle());
        desc.setText(Arrays.toString(data.getContent()).replaceAll("\\[|\\]", "")
                .replaceAll("\\u002E\\u002C ",".\n\n").replaceAll("\\u00a0", ""));
        categories.setText("Categories: " + Arrays.toString(data.getCategories()).replaceAll("\\[|\\]", ""));

        Picasso.get()
                .load(data.getImageUrl())
                .placeholder(R.drawable.newspaper)
                .into(photo);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null){
            System.out.println("CONNECTED");
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
        else {
            System.out.println("NOT CONNECTED");
            title=(TextView) findViewById(R.id.article_title);
            desc=(TextView) findViewById(R.id.article_desc);
            photo=(ImageView) findViewById(R.id.toolbarImage);
            categories=(TextView) findViewById(R.id.categories);
            toolbar = findViewById(R.id.toolbar);
            SQLiteDatabase database = new SavedArticlesDB(getApplicationContext()).getReadableDatabase();

            Cursor cursor = database.query(SavedArticlesDB.SAVED_TABLE_NAME,null,
                    SavedArticlesDB.SAVED_COLUMN_URL + " like ?",
                    new String[]{articleUrl},null,null,null
            );


            if (cursor != null)
            {
                cursor.moveToFirst();
                String titleText = cursor.getString(2);
                title.setText(titleText);
                String descText = cursor.getString(3);
                desc.setText(descText);
                byte[] imgByte = cursor.getBlob(4);
                Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
                photo.setImageBitmap(bmp);
                String dateText = cursor.getString(5);
                toolbar.setTitle(dateText);
                String catText = cursor.getString(6);
                categories.setText(catText);
            }

        }

        return cm.getActiveNetworkInfo() != null;
    }

}
