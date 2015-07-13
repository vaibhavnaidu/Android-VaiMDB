package com.vaibhav.test;


import android.content.Intent;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.util.EntityUtils;
        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.URL;
        import java.net.URLConnection;

public class SearchActivity extends ActionBarActivity {


    EditText name;
    Button searchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_categories);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchbtn = (Button) findViewById(R.id.searchbtn);
        name = (EditText) findViewById(R.id.name);

        searchbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonclicked(v);
                    }
                }
        );
    }

    public void buttonclicked(View v) {

        String a = name.getText().toString();
        a = a.replaceAll(" ","+");
        String url = "https://api.themoviedb.org/3/search/movie?api_key=609afd2686e040b87bd26a0822c368af&query=" +a;

        //searchbtn = (Button) findViewById(R.id.searchbtn);
        /*Log.w("Vai", name.getText().toString());
        Log.w("Vai", year.getText().toString());
        Log.w("Vai", cast.getText().toString());
        Log.w("Vai", rating.getText().toString());*/

        Intent i = new Intent(this, SearchResults.class);
        i.putExtra("EXTRA_MESSAGE", url);
        startActivity(i);

    }
}
