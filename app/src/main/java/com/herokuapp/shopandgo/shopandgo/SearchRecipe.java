package com.herokuapp.shopandgo.shopandgo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ganhari123 on 4/8/2017.
 */

public class SearchRecipe extends Fragment {

    public SearchRecipe() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SearchRecipe newInstance(int sectionNumber) {
        SearchRecipe fragment = new SearchRecipe();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_recipe, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_title);
        EditText recipe_query = (EditText) rootView.findViewById(R.id.recipe_name);
        Button button = (Button) rootView.findViewById(R.id.search);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textView.setText("Search Recipe");
        return rootView;
    }
}
