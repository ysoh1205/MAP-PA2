package edu.skku.cs.pa2;

import static edu.skku.cs.pa2.MapsListviewAdapter.EXT_MAZENAME;
import static edu.skku.cs.pa2.MapsListviewAdapter.EXT_MAZESIZE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.skku.cs.pa2.DataModel.MazeGridviewAdapter;
import edu.skku.cs.pa2.DataModel.MazeResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MazeActivity extends AppCompatActivity {
    String mazeName;
    int mazeSize;
    int[][] mazeData2d;
    ArrayList<Integer> mazeData1d;

    GridView mazeGridview;
    MazeGridviewAdapter mazeGridviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        Intent intent = getIntent();
        mazeName = intent.getStringExtra(EXT_MAZENAME);
        mazeSize = Integer.parseInt(intent.getStringExtra(EXT_MAZESIZE));
        mazeGridview = findViewById(R.id.mazeGridview);
        mazeGridview.setNumColumns(mazeSize);

        mazeData1d = new ArrayList<Integer>();

        float cellSizetmp = (float)350 / mazeSize;

        final float cellSize = cellSizetmp;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://115.145.175.57:10099/maze/map").newBuilder();
        urlBuilder.addQueryParameter("name", mazeName);

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
                final MazeResponse mazeResponse = gson.fromJson(myresponse, MazeResponse.class);

                String data = new String(mazeResponse.getMaze());
                String rows[] = data.split("\n");
                int[][] mazeData2dtmp = new int[mazeSize][mazeSize];
                int[] mazeData1dtmp = new int[mazeSize*mazeSize];

                for(int i=1; i<mazeSize+1; i++) {
                    String[] temp = rows[i].split(" ");
                    for(int j=0; j<mazeSize; j++) {
                        mazeData2dtmp[i-1][j] = Integer.parseInt(temp[j]);
                    }
                }
                for(int i=0; i<mazeSize; i++) {
                    for(int j=0; j<mazeSize; j++) {
                        mazeData1d.add(mazeData2dtmp[i][j]);
                    }
                }
                MazeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mazeData2d = mazeData2dtmp;

                        mazeGridviewAdapter = new MazeGridviewAdapter(getApplicationContext(), mazeData1d, mazeSize);
                        int[] userCoordinate = {0, 0};
                        mazeGridviewAdapter.setUserCoordinate(userCoordinate);
                        int rotate = 0;
                        mazeGridviewAdapter.setUserDirection(rotate);
                        mazeGridview.setAdapter(mazeGridviewAdapter);

                        MazeStatus mazeStatus = new MazeStatus();
                        mazeStatus.setMazeData(mazeData2dtmp);
                        mazeStatus.generatePossibleWays();

                        Button leftButton = findViewById(R.id.button_Left);
                        Button upButton = findViewById(R.id.button_Up);
                        Button rightButton = findViewById(R.id.button_Right);
                        Button downButton = findViewById(R.id.button_Down);

                        Button hintButton = findViewById(R.id.button_Hint);

                        leftButton.setOnClickListener(view -> {
                            mazeGridviewAdapter.USER_DIRECTION = 270;
                            if(mazeGridviewAdapter.userCoordinate[1]!=0) {
                                if(mazeStatus.possibleWays[mazeGridviewAdapter.userCoordinate[0]][mazeGridviewAdapter.userCoordinate[1]].LEFT){
                                    mazeGridviewAdapter.userCoordinate[1]--;
                                    TextView turnTextview = (TextView) findViewById(R.id.turnTextview);
                                    int turn = Integer.parseInt(turnTextview.getText().toString().split(" ")[1]) + 1;
                                    turnTextview.setText("Turn: " + Integer.toString(turn));
                                }
                            }
                            mazeGridview.setAdapter(mazeGridviewAdapter);
                            if(mazeGridviewAdapter.userCoordinate[0]*mazeSize+mazeGridviewAdapter.userCoordinate[1] == mazeSize*mazeSize-1) {
                                Toast.makeText(getApplicationContext(), "Finish!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        rightButton.setOnClickListener(view -> {
                            mazeGridviewAdapter.USER_DIRECTION = 90;
                            if(mazeStatus.possibleWays[mazeGridviewAdapter.userCoordinate[0]][mazeGridviewAdapter.userCoordinate[1]].RIGHT){
                                if (mazeGridviewAdapter.userCoordinate[1] != mazeSize - 1) {
                                    mazeGridviewAdapter.userCoordinate[1]++;
                                    TextView turnTextview = (TextView) findViewById(R.id.turnTextview);
                                    int turn = Integer.parseInt(turnTextview.getText().toString().split(" ")[1]) + 1;
                                    turnTextview.setText("Turn: " + Integer.toString(turn));
                                }
                            }
                            mazeGridview.setAdapter(mazeGridviewAdapter);
                            if(mazeGridviewAdapter.userCoordinate[0]*mazeSize+mazeGridviewAdapter.userCoordinate[1] == mazeSize*mazeSize-1) {
                                Toast.makeText(getApplicationContext(), "Finish!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        upButton.setOnClickListener(view -> {
                            mazeGridviewAdapter.USER_DIRECTION = 0;
                            if(mazeGridviewAdapter.userCoordinate[0]!=0) {
                                if(mazeStatus.possibleWays[mazeGridviewAdapter.userCoordinate[0]][mazeGridviewAdapter.userCoordinate[1]].UP){
                                    mazeGridviewAdapter.userCoordinate[0]--;
                                    TextView turnTextview = (TextView) findViewById(R.id.turnTextview);
                                    int turn = Integer.parseInt(turnTextview.getText().toString().split(" ")[1]) + 1;
                                    turnTextview.setText("Turn: " + Integer.toString(turn));
                                }
                            }
                            mazeGridview.setAdapter(mazeGridviewAdapter);
                            if(mazeGridviewAdapter.userCoordinate[0]*mazeSize+mazeGridviewAdapter.userCoordinate[1] == mazeSize*mazeSize-1) {
                                Toast.makeText(getApplicationContext(), "Finish!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        downButton.setOnClickListener(view -> {
                            mazeGridviewAdapter.USER_DIRECTION = 180;
                            if(mazeGridviewAdapter.userCoordinate[0]!=mazeSize-1) {
                                if(mazeStatus.possibleWays[mazeGridviewAdapter.userCoordinate[0]][mazeGridviewAdapter.userCoordinate[1]].DOWN){
                                    mazeGridviewAdapter.userCoordinate[0]++;
                                    TextView turnTextview = (TextView) findViewById(R.id.turnTextview);
                                    int turn = Integer.parseInt(turnTextview.getText().toString().split(" ")[1]) + 1;
                                    turnTextview.setText("Turn: " + Integer.toString(turn));
                                }
                            }
                            mazeGridview.setAdapter(mazeGridviewAdapter);
                            if(mazeGridviewAdapter.userCoordinate[0]*mazeSize+mazeGridviewAdapter.userCoordinate[1] == mazeSize*mazeSize-1) {
                                Toast.makeText(getApplicationContext(), "Finish!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        hintButton.setOnClickListener(view -> {
                            if(mazeStatus.hintFlag){
                                int hint = mazeStatus.BFS(mazeGridviewAdapter.userCoordinate);
                                mazeGridviewAdapter.hintCoordinate = hint;
                                mazeStatus.hintFlag = false;
                            }
                            mazeGridview.setAdapter(mazeGridviewAdapter);
                        });

                    }
                });

            }
        });


    }
}