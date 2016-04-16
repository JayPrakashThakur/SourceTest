package com.appniche.sourcetest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JayPrakash on 16-04-2016.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    SourceAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new SourceAdapter(getActivity());

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        Log.d(LOG_TAG,"after list view set to adapter");
        return rootView;
    }

    @Override
    public void onStart() {
        new FetchDataTask().execute();
        super.onStart();
    }

    public class FetchDataTask extends AsyncTask<String, Void, SourceObject[]> {

        private final String LOG_TAG = FetchDataTask.class.getSimpleName();

        private SourceObject[] getSourceDataFromJson(String jsonStr)throws JSONException {

            JSONArray jsonArray = new JSONArray(jsonStr);
            SourceObject[] sourceObjects = new SourceObject[jsonArray.length()];

            for (int i=0; i<jsonArray.length();i++){
                sourceObjects[i] = new SourceObject(
                        jsonArray.getJSONObject(i).getJSONObject("commit").getJSONObject("author").getString("name"),
                        jsonArray.getJSONObject(i).getString("sha"),
                        jsonArray.getJSONObject(i).getJSONObject("commit").getString("message")
                );
            }
            return sourceObjects;
        }

        @Override
        protected SourceObject[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {
                String baseUrl = "https://api.github.com/repos/rails/rails/commits";

                URL url = new URL(baseUrl);
                Log.d(LOG_TAG,"URL IS "+url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                jsonStr = buffer.toString();
                Log.d(LOG_TAG,"JSON STRING "+jsonStr);
            }catch (IOException e){
                Log.e(LOG_TAG, "ERROR"+e);
                return null;
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getSourceDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(SourceObject[] strings) {
            adapter.setItems(strings);
            adapter.notifyDataSetChanged();
            super.onPostExecute(strings);
        }
    }
}
