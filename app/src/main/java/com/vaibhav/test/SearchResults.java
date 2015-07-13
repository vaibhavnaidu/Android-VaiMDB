package com.vaibhav.test;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SearchResults extends Activity {

    View rootView;
    String title,backdrop_path,release_date;
    TableLayout stk;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        Intent i = getIntent();
        String url = i.getStringExtra("EXTRA_MESSAGE");
        //Log.w("Vai", "id is this " + url);
        new HttpAsyncTask().execute(url);

        stk = (TableLayout) findViewById(R.id.search_r);
        TableRow tbrow = new TableRow(this);
        tbrow.setMinimumHeight(30);
        TextView td1 = new TextView(this);
        TextView td2 = new TextView(this);
        td1.setText("Name");
        td1.setTextColor(Color.BLACK);
        td1.setTextSize(25);
        td1.setWidth(350);
        td2.setText("Release Date");
        td2.setTextColor(Color.BLACK);
        td2.setTextSize(25);
        tbrow.addView(td1);
        tbrow.addView(td2);
        stk.addView(tbrow);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
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
                JSONArray results = json.getJSONArray("results");
                Log.w("Vai","jsonarray");
                int n = results.length();
                Log.i("Vai", String.valueOf(n));
                for(int i=0;i<n;i++) {
                    //String a = (results.getJSONObject(0).names()).toString();
                    title = results.getJSONObject(i).getString("title");
                    Log.w("Vai",title);
                    id = Integer.parseInt(results.getJSONObject(i).getString("id"));
                    backdrop_path = results.getJSONObject(i).getString("backdrop_path");
                    release_date = results.getJSONObject(i).getString("release_date");

                    //Log.w("Vai",genre);

                    /*String imageURL = "http://image.tmdb.org/t/p/w500"+backdrop_path;
                    new loadImageTask().execute(imageURL);*/

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
        stk = (TableLayout) findViewById(R.id.search_r);
        final TableRow tbrow = new TableRow(this);
        tbrow.setPadding(5,5,5,0);
        TextView td1 = new TextView(this);
        TextView td2 = new TextView(this);
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
        Intent i = new  Intent(this, DetailsActivity.class);
        i.putExtra("EXTRA_MESSAGE", id);
        startActivity(i);

    }
}
