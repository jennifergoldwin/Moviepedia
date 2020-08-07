package com.jenjenfuture.projectuas_2201750761;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchMovieFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private TextInputEditText searchMovie;
    private AdapterSearchMovie adapterSearchMovie;
    private ProgressDialog progressDialog;


    private static int START_PAGE =1;
    private int TOTAL_PAGE = 100;
    private int CURRENT_PAGE = START_PAGE;
    private String movie;

    public SearchMovieFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_movie_fragment,container,false);
        recyclerView = view.findViewById(R.id.search_movie_fragment_recycler_view);
        searchMovie = view.findViewById(R.id.search_movie_fragment_edit_text);

        recyclerView.setLayoutManager( new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridLayoutDecoration(2,12,true));

        searchMovie.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                movie = searchMovie.getText().toString().trim();
                if (movie.isEmpty()){
                    Toast.makeText(getContext(),"The field is empty",Toast.LENGTH_SHORT).show();
                    return false;
                }
                CURRENT_PAGE=START_PAGE;
                movieList.clear();
                showDialog();
                loadUrlMovie(movie,1);

            }
            return false;
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieList = new ArrayList<>();
        showDialog();
        loadUrlMovie("frozen",1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                adapterSearchMovie.addMovie(item.getGroupId());
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void showDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading movies...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private void loadUrlMovie(String searchMovie, int page) {
        if (!isConnected()){
            Toast.makeText(getContext(),"No internet connection",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        String Base_URL = "https://omdbapi.com/?apikey=67c7eb50" + "&page="+ page + "&type=movie&s=*"+searchMovie+"*";
        RequestQueue  requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Base_URL, null, response -> {
            try {
                if (response.getString("Response").equals("False")){
                    String falseText = response.getString("Error");

                    if (falseText.equals("Too many results")){
                        Toast.makeText(getActivity(),"Too many results",Toast.LENGTH_SHORT).show();
                    }
                    else if (falseText.equals("Movie not found")){
                        Toast.makeText(getActivity(),"Movie not found",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(),falseText,Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    return;
                }
                JSONArray jsonArray = response.getJSONArray("Search");
                TOTAL_PAGE = response.getInt("totalResults")/10;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(jsonObject.getString("Title"));
                    Log.d("123", jsonObject.getString("Title"));
                    movie.setImdbID(jsonObject.getString("imdbID"));
                    movie.setType(jsonObject.getString("Type"));
                    movie.setPoster(jsonObject.getString("Poster"));
                    movie.setYear(jsonObject.getString("Year"));

                    movieList.add(movie);
                }
                if (CURRENT_PAGE<=TOTAL_PAGE){
                    CURRENT_PAGE++;
                    loadUrlMovie(searchMovie,CURRENT_PAGE);
                }else {
                    progressDialog.dismiss();
                    adapterSearchMovie = new AdapterSearchMovie(getContext(),movieList);
                    recyclerView.setAdapter(adapterSearchMovie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getContext(),"Error loading Image",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
        requestQueue.add(jsonObjectRequest);
    }
}
