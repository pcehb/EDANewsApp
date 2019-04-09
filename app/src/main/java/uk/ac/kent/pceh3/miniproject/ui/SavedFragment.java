package uk.ac.kent.pceh3.miniproject.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uk.ac.kent.pceh3.miniproject.R;
import uk.ac.kent.pceh3.miniproject.model.Articles;


public class SavedFragment extends Fragment {
    public List<Articles> articles = new ArrayList<>();

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        String filename = "hello_file";
        StringBuffer stringBuffer = new StringBuffer();

        try {
            FileInputStream fis = getContext().openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String strLine = null;

            while ((strLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(strLine + "\n");
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Displaying data on the toast
        Toast.makeText(getContext(), stringBuffer.toString(),Toast.LENGTH_LONG).show();


        return view;
    }
}
