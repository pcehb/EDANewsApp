package uk.ac.kent.pceh3.miniproject.ui;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.kent.pceh3.miniproject.model.Articles;
import uk.ac.kent.pceh3.miniproject.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TextView name;
    private TextView phoneNumber;
    private ImageView photo;
    private FeedViewModel viewModel;

    public DetailsFragment() {
        // Required empty public constructor
    }


    private final Observer<Integer> selectedObserver = new Observer<Integer>(){
        @Override
        public void onChanged(Integer position){
            updateView(position);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        name=(TextView) view.findViewById(R.id.article_title);
        phoneNumber=(TextView) view.findViewById(R.id.article_desc);
        photo=(ImageView) view.findViewById(R.id.contact_photo);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(FeedViewModel.class);
        viewModel.getSelectedFeed().observe((LifecycleOwner) getActivity(), selectedObserver);

        return view;
    }

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
