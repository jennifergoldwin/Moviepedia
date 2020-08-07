package com.jenjenfuture.projectuas_2201750761;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView movie_img;
    private TextView movie_title;
    private TextView movie_year;
    private TextView movie_imdbID;

    private Button deleteMovie;
    private Button saveMovie;

    private DBMovie dbMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Movie Detail");

        dbMovie = DBMovie.getInstance(this);

        movie_img = findViewById(R.id.movie_detail_activity_img);
        movie_title = findViewById(R.id.movie_detail_activity_title);
        movie_imdbID = findViewById(R.id.movie_detail_activity_imdbID);
        movie_year = findViewById(R.id.movie_detail_activity_year);

        deleteMovie = findViewById(R.id.movie_detail_activity_button_delete);
        saveMovie = findViewById(R.id.movie_detail_activity_button_save);

        Intent intent = getIntent();

        String imdbID = intent.getStringExtra(AdapterSearchMovie.KEY_IMBDID);
        String title =  intent.getStringExtra(AdapterSearchMovie.KEY_MOVIE_TITLE);
        String year = intent.getStringExtra(AdapterSearchMovie.KEY_YEAR);
        String photo = intent.getStringExtra(AdapterSearchMovie.KEY_MOVIE_IMAGE);
        String type = intent.getStringExtra(AdapterSearchMovie.KEY_TYPE);

//        Picasso.get().load(photo).into(movie_img);
        Glide.with(getApplicationContext())
                .load(photo)
                .placeholder(R.drawable.notfound)
                .into(movie_img);

        movie_title.setText(title);
        movie_year.setText(year);
        movie_imdbID.setText(imdbID);

        if (dbMovie.checkMovie(imdbID)){
            saveMovie.setVisibility(View.GONE);

            deleteMovie.setOnClickListener(v -> {
                dbMovie.deleteMovie(imdbID);
                Toast.makeText(getApplicationContext(),"Movie deleted",Toast.LENGTH_SHORT).show();
                finish();
            });
        }
        else{
            deleteMovie.setVisibility(View.GONE);
            saveMovie.setOnClickListener(v -> {
                Movie movie = new Movie();
                movie.setTitle(title);
                movie.setType(type);
                movie.setYear(year);
                movie.setImdbID(imdbID);
                movie.setPoster(photo);
                dbMovie.insertMovie(movie);
                Toast.makeText(getApplicationContext(),"Movie added",Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}