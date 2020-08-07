package com.jenjenfuture.projectuas_2201750761;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedMovieFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private TextView textView;
    private AdapterSavedMovie adapterSavedMovie;

    private List<Movie> movieList ;
    public SavedMovieFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.saved_movie_fragment,container,false);
        recyclerView = view.findViewById(R.id.saved_movie_fragment_recycler_view);
        textView = view.findViewById(R.id.saved_movie_fragment_textView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.addItemDecoration(new GridLayoutDecoration(2,12,true));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        DBMovie dbMovie = DBMovie.getInstance(getContext());
        movieList = dbMovie.getAllMovies();
        adapterSavedMovie = new AdapterSavedMovie(getContext(),movieList);

        recyclerView.setAdapter(adapterSavedMovie);
        setLayout();
    }

    public void setLayout(){
        if (adapterSavedMovie.getItemCount()>0)
            textView.setVisibility(View.GONE);
        else
            textView.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 2:
                adapterSavedMovie.deleteMovie(item.getGroupId());
                setLayout();
                Toast.makeText(getContext(),"Movie deleted",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);

    }
}
