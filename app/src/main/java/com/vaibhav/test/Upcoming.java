package com.vaibhav.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Upcoming extends Fragment{

    String title,genre,backdrop_path,release_date;
    int id;
    View rootView;
    TableLayout stk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.upcoming, container, false);

        rootView = inflater.inflate(R.layout.upcoming, container, false);

        stk = (TableLayout) rootView.findViewById(R.id.table_up);
        TableRow tbrow = new TableRow(getActivity());
        tbrow.setMinimumHeight(30);
        tbrow.setBackgroundColor(Color.GRAY);
        TextView td1 = new TextView(getActivity());
        TextView td2 = new TextView(getActivity());
        td1.setText("Name");
        td1.setTextColor(Color.BLACK);
        td1.setTextSize(25);
        td1.setWidth(350);
        td1.setGravity(1);
        td2.setText("Release Date");
        td2.setTextColor(Color.BLACK);
        td2.setTextSize(25);
        td1.setGravity(1);
        tbrow.addView(td1);
        tbrow.addView(td2);
        stk.addView(tbrow);

        new HttpAsyncTask().execute("https://api.themoviedb.org/3/movie/upcoming?api_key=609afd2686e040b87bd26a0822c368af");

        return rootView;
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Not working";
        }
        catch (Exception e) {
            Log.w("Vai", "Exception in GET");
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                //Log.i("Vai","reached json vatiable");
                JSONArray results = json.getJSONArray("results");
                //Log.i("Vai","reached results vatiable");
                int n = results.length();
                //Log.i("Vai", String.valueOf(n));
                for(int i=0;i<n;i++) {
                    title = results.getJSONObject(i).getString("title");
                    id = Integer.parseInt(results.getJSONObject(i).getString("id"));
                    genre = results.getJSONObject(i).getString("genre_ids");
                    backdrop_path = results.getJSONObject(i).getString("backdrop_path");
                    release_date = results.getJSONObject(i).getString("release_date");

                    /*Log.w("Vai",title);
                    Log.w("Vai",String.valueOf(id));
                    Log.w("Vai",genre);
                    Log.w("Vai",release_date);*/

                    tableView (title,release_date,backdrop_path,id);



                }
            }
            catch (Exception e)
            {
                Log.w("Vai","Error in JSONObj");
            }
        }
    }

    private void tableView(String title, String release_date, String backdrop_path, int id) {
        stk = (TableLayout) rootView.findViewById(R.id.table_up);
        TableRow tbrow = new TableRow(getActivity());
        tbrow.setPadding(5, 5, 5, 0);
        TextView td1 = new TextView(getActivity());
        TextView td2 = new TextView(getActivity());
        td1.setText(title);
        td1.setTextColor(Color.BLACK);
        td1.setGravity(1);
        td1.setTextSize(20);
        td1.setWidth(350);
        td2.setText(release_date);
        td2.setTextColor(Color.BLACK);
        td2.setGravity(1);
        td2.setTextSize(20);
        //td2.setWidth(300);
        tbrow.addView(td1);
        tbrow.addView(td2);
        stk.addView(tbrow);
        final String tmp = id + "";
        tbrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                //TableRow row = (TableRow) v.getParent();
                detailsview(tmp);
            }
        });

    }
    public void detailsview(String id)
    {
        Intent i = new  Intent(this.getActivity(), DetailsActivity.class);
        i.putExtra("EXTRA_MESSAGE", id);
        startActivity(i);

    }
}
