package com.viselvis.restfultutorialandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.viselvis.restfultutorialandroid.R;
import com.viselvis.restfultutorialandroid.adapter.MoviesAdapter;
import com.viselvis.restfultutorialandroid.model.Movie;
import com.viselvis.restfultutorialandroid.model.MovieResponse;
import com.viselvis.restfultutorialandroid.rest.MovieApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        connectAndGetApiData();
    }

    // creates an instance of retrofit
    private void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieApiService movieApiService = retrofit.create(MovieApiService.class);

            Call<MovieResponse> call = movieApiService.getTopRatedMovies(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> movies = response.body().getResults();

                    recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, MainActivity.this));
                    Log.d(TAG, "onResponse: Number of movies received: " + movies.size());
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.toString());
                }
            });

        }
    }
}