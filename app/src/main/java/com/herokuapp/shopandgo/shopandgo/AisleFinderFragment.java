package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bukbukbukh on 4/19/17.
 */

public class AisleFinderFragment extends Fragment {

    String state;

    public AisleFinderFragment() {
        state = "";
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AisleFinderFragment newInstance(int sectionNumber) {
        AisleFinderFragment fragment = new AisleFinderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.super_market_by_zip, container, false);

        String[] states = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DC", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

        // State names in alphabetical order

        Arrays.sort(states);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.fifty_states);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, states);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        Button findSupermarkets = (Button) rootView.findViewById(R.id.find_supermarkets);
        findSupermarkets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(getActivity());
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                EditText zipcode = (EditText) rootView.findViewById(R.id.zipcode_query);
                String requestString = "http://www.SupermarketAPI.com/api.asmx/StoresByCityState?APIKEY=9699d4f2d0&SelectedCity=" + zipcode.getText().toString() + "&SelectedState=" + state;
                client.get( requestString, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.e("RecipeResult", new String(bytes));

                        try {
                            JSONObject object = XML.toJSONObject(new String(bytes));
                            Log.e("Error", object.toString());
                            JSONArray array = object.getJSONObject("ArrayOfStore").getJSONArray("Store");
                            SupermarketStore[] mainArray = new SupermarketStore[array.length()];
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject obj = array.getJSONObject(j);
                                mainArray[j] = new SupermarketStore();
                                if (!obj.getString("Storename").equals("null")) {
                                    mainArray[j].setStoreName(obj.getString("Storename"));
                                }

                                if (!obj.getString("Address").equals("null")) {
                                    mainArray[j].setAddress(obj.getString("Address"));
                                }

                                if (!obj.getString("City").equals("null")) {
                                    mainArray[j].setCity(obj.getString("City"));
                                }

                                if (!obj.getString("State").equals("null")) {
                                    mainArray[j].setState(obj.getString("State"));
                                }

                                if (!obj.getString("Zip").equals("null")) {
                                    mainArray[j].setZipcode(obj.getString("Zip"));
                                }

                                if (!obj.getString("StoreId").equals("null")) {
                                    mainArray[j].setStoreID(obj.getString("StoreId"));
                                }
                            }
                            CustomSuperMarketListAdapter adapter = new CustomSuperMarketListAdapter(getActivity(), mainArray);
                            ListView view = (ListView) rootView.findViewById(R.id.super_market_list);
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
        ListView view = (ListView) rootView.findViewById(R.id.super_market_list);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProgressDialogBox.createBox(getActivity());
                SupermarketStore item =  (SupermarketStore) (adapterView.getItemAtPosition(i));
                Gson gson = new Gson();
                String main = gson.toJson(item);
                Log.e("ERROR", main);
                Intent intent = new Intent(getActivity(), SuperMarketItemSearch.class);
                intent.putExtra("ListItem", main);
                ProgressDialogBox.hideBox(getActivity());
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }
}
