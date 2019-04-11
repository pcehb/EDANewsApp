package uk.ac.kent.pceh3.miniproject.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    private SavedAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

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
        layoutManager = new LinearLayoutManager(getActivity());
        feedListView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        feedListView.addItemDecoration(divider);
        feedListView.setAdapter(adapter);


        recyclerView = (RecyclerView) view.findViewById(R.id.feed_list_view);

        readFromDB();

        return view;
    }

    private void readFromDB() {

        SQLiteDatabase database = new SavedArticlesDB(getContext()).getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + SAVED_TABLE_NAME,null);

        feedListView.setAdapter(new SavedAdapter(getContext(), cursor));
    }


    private View.OnClickListener listItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            System.out.println(position);

//            Articles article = adapter.cursor.get(position);
//            articleUrl = article.getArticleUrl();
//
//            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//            intent.putExtra("articleUrl", articleUrl);
//
//            startActivity(intent);
        }


    };

}
