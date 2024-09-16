package com.example.examanfinalortegarizzo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RandomUserApi api = retrofit.create(RandomUserApi.class);

        api.getUsers(20).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body().results;
                    userAdapter = new UserAdapter(users,new UserAdapter.OnItemClickListener()  {
                        public void onItemClick(User user) {
                            Intent intent = new Intent(MainActivity.this,
                                    UserDetailsActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }
}

interface RandomUserApi {
    @GET("api/")
    Call<UserResponse> getUsers(@Query("results") int count);
}

class UserResponse {
    List<User> results;
}