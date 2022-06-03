package edu.skku.cs.pa2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import edu.skku.cs.pa2.DataModel.MapInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapSelectionActivity extends AppCompatActivity {
    private ArrayList<MapInfo> mapInfoList;

    private ListView mapsListview;
    private MapsListviewAdapter mapsListviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.EXT_USERNAME);

        TextView usernameTextview = findViewById(R.id.usernameTextview);
        usernameTextview.setText(username);

        mapInfoList = new ArrayList<MapInfo>();
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://115.145.175.57:10099/maps").newBuilder();

        String url = urlBuilder.build().toString();

        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myresponse = response.body().string();
                Gson gson = new Gson();
                final MapInfo[] mapInfos = gson.fromJson(myresponse, MapInfo[].class);
                for(int i = 0; i < mapInfos.length; i++) {
                    mapInfoList.add(mapInfos[i]);
                }
                MapSelectionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mapsListview = findViewById(R.id.mapsListview);
                        mapsListviewAdapter = new MapsListviewAdapter(getApplicationContext(), mapInfoList);
                        mapsListview.setAdapter(mapsListviewAdapter);
                    }
                });
            }
        });

    }

}