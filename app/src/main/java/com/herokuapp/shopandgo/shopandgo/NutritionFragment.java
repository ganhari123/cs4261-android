package com.herokuapp.shopandgo.shopandgo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bukbukbukh on 4/19/17.
 */

public class NutritionFragment extends Fragment {

    public NutritionFragment() {

    }

    public static NutritionFragment newInstance(int sectionNumber) {
        NutritionFragment fragment = new NutritionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.nutrition_details, container, false);
        Button search = (Button) rootView.findViewById(R.id.nutrition_search);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(getActivity());
                EditText searchQuery = (EditText) rootView.findViewById(R.id.search_nutrition_item);
                String finalQuery = "";
                String query = searchQuery.getText().toString();
                for (int i = 0; i < searchQuery.getText().toString().length(); i++) {
                    if (query.charAt(i) == ' ') {
                        finalQuery = finalQuery + "%20";
                    } else {
                        finalQuery = finalQuery + String.valueOf(query.charAt(i));
                    }
                }
                Log.e("ERROR", finalQuery);
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://api.nutritionix.com/v1_1/search/" + finalQuery + "?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=115f541b&appKey=7aea0b380f1eedb023603cfa30de146f", new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        Log.e("ERROR", new String(bytes));
                        try {
                            JSONObject obj = new JSONObject(new String(bytes));
                            JSONArray arr = obj.getJSONArray("hits");
                            NutritionItem[] newArr = new NutritionItem[arr.length()];
                            for (int j = 0; j < arr.length(); j++) {
                                JSONObject item = arr.getJSONObject(j);
                                NutritionItem addItem = new NutritionItem();
                                if (!item.getJSONObject("fields").getString("item_id").equals("null")) {
                                    addItem.setItemID(item.getJSONObject("fields").getString("item_id"));
                                    Log.e("ID", item.getJSONObject("fields").getString("item_id"));
                                }
                                if (!item.getJSONObject("fields").getString("item_name").equals("null")) {
                                    addItem.setItemName(item.getJSONObject("fields").getString("item_name"));
                                    Log.e("item_name", item.getJSONObject("fields").getString("item_name"));
                                }

                                if (!item.getJSONObject("fields").getString("brand_name").equals("null")) {
                                    addItem.setBrandName(item.getJSONObject("fields").getString("brand_name"));
                                }

                                if (!item.getJSONObject("fields").getString("nf_calories").equals("null")) {
                                    addItem.setCalories(item.getJSONObject("fields").getDouble("nf_calories"));
                                    Log.e("item_name", item.getJSONObject("fields").getString("item_name"));
                                }

                                if (!item.getJSONObject("fields").getString("nf_total_fat").equals("null")) {
                                    addItem.setTotalFat(item.getJSONObject("fields").getDouble("nf_total_fat"));
                                }

                                if (!item.getJSONObject("fields").getString("nf_serving_size_qty").equals("null")) {
                                    addItem.setServingSize(item.getJSONObject("fields").getInt("nf_serving_size_qty"));
                                }
                                newArr[j] = addItem;
                            }

                            CustomAdapterNutritionResults adapter = new CustomAdapterNutritionResults(getActivity(), newArr);
                            ListView view = (ListView) rootView.findViewById(R.id.nutrition_list);
                            view.setAdapter(adapter);
                            ProgressDialogBox.hideBox(getActivity());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ProgressDialogBox.hideBox(getActivity());
                        }

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        ProgressDialogBox.hideBox(getActivity());

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });
        return rootView;
    }
}
