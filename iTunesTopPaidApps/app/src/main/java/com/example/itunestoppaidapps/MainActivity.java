package com.example.itunestoppaidapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mrecyclerView;
    static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    ImageButton refresh;
    Switch aSwitch;
    static final ArrayList<App> newResult = new ArrayList<>();
    static final ArrayList<App> result = new ArrayList<>();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        aSwitch = (Switch) findViewById(R.id.switch1);
        refresh = (ImageButton) findViewById(R.id.ibtn_refresh);
        progressBar.setVisibility(View.INVISIBLE);
                if(isConnected()){
                    String urla = "https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json";
                    new DoAsynchTask().execute(urla);
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
                }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != connectivityManager.TYPE_WIFI && networkInfo.getType() != connectivityManager.TYPE_MOBILE)){
            return false;
        }
        return true;
    }
    private class DoAsynchTask extends AsyncTask<String, Void, ArrayList> {
        HttpURLConnection httpURLConnection = null;
        @Override
        protected ArrayList doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String json =  IOUtils.toString(httpURLConnection.getInputStream(),"UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONObject feed = root.getJSONObject("feed");
                    JSONArray entry = feed.getJSONArray("entry");

                    for(int i=0;i<entry.length();i++){
                        JSONObject entries = entry.getJSONObject(i);
                        App app = new App();
                        JSONObject apps = entries.getJSONObject("im:name");
                        app.name = apps.getString("label");
                        JSONArray image = entries.getJSONArray("im:image");
                        JSONObject selectedImage = image.getJSONObject(image.length()-1);
                        app.imageUrl = selectedImage.getString("label");
                        JSONObject price = entries.getJSONObject("im:price");
                        app.price = price.getString("label");

                        result.add(app);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {


            final ListView listView = (ListView) findViewById(R.id.listView);
            if(result.size()>0){
                //Log.i("result",result.toString());
                progressBar.setVisibility(View.INVISIBLE);
                final AppAdapter adapter= new AppAdapter(MainActivity.this,R.layout.app_item,result);
                listView.setAdapter(adapter);
                mrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mrecyclerView.setHasFixedSize(true);
                mlayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL, false);
                mrecyclerView.setLayoutManager(mlayoutManager);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        newResult.add((App) listView.getItemAtPosition(i));
                        result.remove(i);
                        mAdapter = new RecyclerAdapter(newResult);
                        mrecyclerView.setAdapter(mAdapter);
                        adapter.notifyDataSetChanged();

                    }
                });

                refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                });

                aSwitch.setChecked(false);
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b == true){
                            Collections.sort(result, App.priceSortDesc);
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Collections.sort(result, App.priceSortAesc);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
            }
            else{
                Log.i("result", "No Result");
            }
        }
    }
}