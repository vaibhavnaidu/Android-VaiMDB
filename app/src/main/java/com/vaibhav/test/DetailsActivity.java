package com.vaibhav.test;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
import java.net.URL;

public class DetailsActivity extends Activity {

    String title,backdrop_path,release_date,overview;
    TextView name,desc,rel;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_layout);

        imgLogo = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.mname);
        desc = (TextView) findViewById(R.id.details);
        rel = (TextView) findViewById(R.id.reldate);
        Intent i = getIntent();
        // Receiving the Data

        String id = i.getStringExtra("EXTRA_MESSAGE");
        Log.w("Vai", "id is this " + id);

        String url = "https://api.themoviedb.org/3/movie/"+id+"?api_key=609afd2686e040b87bd26a0822c368af";
        Log.w("Vai",  url);
        new HttpAsyncTask().execute(url);
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
                //JSONArray results = json.getJSONArray("adult");

                Log.w("Vai",json.getString("adult"));
                //int n = results.length();
                //Log.i("Vai", String.valueOf(n));
                    //String a = (results.getJSONObject(0).names()).toString();
                    title = json.getString("title");
                    backdrop_path = json.getString("backdrop_path");
                    release_date = json.getString("release_date");
                    overview = json.getString("overview");

                    //Log.w("Vai",genre);

                    String imageURL = "http://image.tmdb.org/t/p/w500"+backdrop_path;
                    new loadImageTask().execute(imageURL);

                    name.setText(title);
                    desc.setText(overview);
                    rel.setText(release_date);

            }
            catch (Exception e)
            {
                Log.w("Vai","Error in JSONObj");
            }
        }
    }

    public class loadImageTask extends AsyncTask<String, Void, Void>
    {
        Drawable imgLoad;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            imgLoad = LoadImageFromWeb(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            imgLogo.setBackgroundDrawable(imgLoad);


        }
    }

    public static Drawable LoadImageFromWeb(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
