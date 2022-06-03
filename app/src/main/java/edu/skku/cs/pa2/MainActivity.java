package edu.skku.cs.pa2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import edu.skku.cs.pa2.DataModel.UsersPost;
import edu.skku.cs.pa2.DataModel.UsersPostResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button signinButton;
    EditText username;

    public static final String EXT_USERNAME = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.userName);
        signinButton = findViewById(R.id.button_signin);

        signinButton.setOnClickListener(view -> {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://115.145.175.57:10099/users").newBuilder();
            String user = username.getText().toString();
            UsersPost usersPost = new UsersPost();
            usersPost.setUsername(user);

            Gson usernameGson = new Gson();
            String usernameJson = usernameGson.toJson(usersPost, UsersPost.class);

            String url = urlBuilder.build().toString();

            Request req = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), usernameJson))
                    .build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse = response.body().string();

                    Gson gson = new GsonBuilder().create();
                    final UsersPostResponse usersPostResponse = gson.fromJson(myResponse, UsersPostResponse.class);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(usersPostResponse.getSuccess().compareTo("false")==0) Toast.makeText(getApplicationContext(), usersPostResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                            else{
                                String user = username.getText().toString();

                                Intent intent = new Intent(MainActivity.this, MapSelectionActivity.class);
                                intent.putExtra(EXT_USERNAME, user);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
        });
    }

}