package uk.ac.kent.pceh3.miniproject.ui;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import uk.ac.kent.pceh3.miniproject.R;
public class SearchPopup extends Fragment implements FragmentManager.OnBackStackChangedListener {
    public EditText searchText;

    public SearchPopup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getFragmentManager().addOnBackStackChangedListener(this);

        final View view = inflater.inflate(R.layout.search_popup, container, false);
        searchText = (EditText) view.findViewById(R.id.searchText);


        return view;
    }


    @Override
    public void onBackStackChanged() {

    }
}
