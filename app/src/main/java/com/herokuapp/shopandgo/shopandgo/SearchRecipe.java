package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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
        final View rootView = inflater.inflate(R.layout.search_recipe, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_title);
        final EditText recipe_query = (EditText) rootView.findViewById(R.id.recipe_name);
        Button button = (Button) rootView.findViewById(R.id.search);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogBox.createBox(getActivity());
                AsyncHttpClient client = new AsyncHttpClient();
                final String recipe = recipe_query.getText().toString();
                RequestParams params = new RequestParams();
                params.put("recipe", recipe);
                client.get("http://food2fork.com/api/search?key=125aec03ba4a0ffe5222a72a9783b3b6&q=" + recipe, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.e("RecipeResult", new String(bytes));
                        try {
                            JSONObject result = new JSONObject(new String(bytes));
                            JSONArray array = result.getJSONArray("recipes");
                            Log.e("LENGTH", String.valueOf(array.length()));
                            RecipeItem[] items = new RecipeItem[array.length()];
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject object = array.getJSONObject(j);
                                if (object == null) {
                                    Log.e("NULL", "NULL");
                                }
                                Log.e("ID", String.valueOf(object.getString("recipe_id")));
                                Log.e("TITLE", object.getString("title"));
                                Log.e("IMAGE_URL", object.getString("image_url"));

                                items[j] = new RecipeItem(object.getString("recipe_id"), object.getString("title"), object.getString("image_url"));
                            }
                            CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), items);
                            ListView view = (ListView) rootView.findViewById(R.id.recipe_search_results);
                            view.setAdapter(adapter);
                            ProgressDialogBox.hideBox(getActivity());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });

        ListView view = (ListView) rootView.findViewById(R.id.recipe_search_results);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProgressDialogBox.createBox(getActivity());
                RecipeItem item =  (RecipeItem) (adapterView.getItemAtPosition(i));
                Gson gson = new Gson();
                String main = gson.toJson(item);
                Log.e("ERROR", main);
                Intent intent = new Intent(getActivity(), RecipeViewPage.class);
                intent.putExtra("ListItem", main);
                getActivity().startActivity(intent);
            }
        });

        textView.setText("Search Recipe");
        return rootView;
    }
}
