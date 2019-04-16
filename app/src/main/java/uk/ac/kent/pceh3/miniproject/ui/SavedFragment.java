package uk.ac.kent.pceh3.miniproject.ui;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.network.SavedArticlesDB;

import static uk.ac.kent.pceh3.miniproject.network.SavedArticlesDB.SAVED_TABLE_NAME;


public class SavedFragment extends Fragment {
    private RecyclerView feedListView;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private SavedAdapter adapter;
    private String articleUrl;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    AlertDialog.Builder builder;
    private ImageView noImages;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        feedListView = (RecyclerView) view.findViewById(R.id.feed_list_view);
        noImages = view.findViewById(R.id.noImages);
        layoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        feedListView.setLayoutManager(layoutManager);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            feedListView.setLayoutManager(gridLayoutManager);


        } else {
            // In portrait
            feedListView.setLayoutManager(layoutManager);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.feed_list_view);
        DividerItemDecoration dividerVert = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerVert.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        feedListView.addItemDecoration(dividerVert);

        readFromDB();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getActivity() != null) {
            readFromDB();
        }
    }

    public void readFromDB() {

        SQLiteDatabase database = new SavedArticlesDB(getContext()).getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + SAVED_TABLE_NAME,null);

        if(cursor.getCount() <= 0){ //no articles saved yet
            noImages.setVisibility(View.VISIBLE);
        }
        else{
            noImages.setVisibility(View.GONE);
        }
        feedListView.setAdapter(new SavedAdapter(getContext(), cursor, listItemClickListener, listLongItemClickListener));
    }


    private View.OnClickListener listItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            TextView url = v.findViewById(R.id.url);
            articleUrl = url.getText().toString();

            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("articleUrl", articleUrl);

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

        }

    };

    private View.OnLongClickListener listLongItemClickListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(final View v)
        {

            builder = new AlertDialog.Builder(getContext());

            builder.setMessage("Do you want to remove this article from your saved list?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TextView url = v.findViewById(R.id.url);
                            articleUrl = url.getText().toString();
                            SQLiteDatabase database = new SavedArticlesDB(getContext()).getReadableDatabase();
                            database.delete(SavedArticlesDB.SAVED_TABLE_NAME, SavedArticlesDB.SAVED_COLUMN_URL +"=?", new String[] {articleUrl});
                            readFromDB();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Delete");
            alert.show();

            return true;
        }

    };


}
