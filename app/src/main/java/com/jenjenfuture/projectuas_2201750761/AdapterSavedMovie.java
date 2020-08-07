package com.jenjenfuture.projectuas_2201750761;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSavedMovie extends RecyclerView.Adapter<AdapterSavedMovie.ViewHolder> {

    private List<Movie> movieList;
    private DBMovie dbMovie;
    private Context context;

    public AdapterSavedMovie (Context context,List<Movie> movies){
        dbMovie = DBMovie.getInstance(context);
        movieList = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSavedMovie.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_movie_template,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSavedMovie.ViewHolder holder, int position) {
//        Picasso.get().load(movieList.get(position).getPoster()).into(holder.movieImg);
        Glide.with(context)
                .load(movieList.get(position).getPoster())
                .placeholder(R.drawable.notfound)
                .into(holder.movieImg);

        holder.movieTitle.setText(movieList.get(position).getTitle());

        holder.linearLayout.setOnClickListener(v -> {
            Movie movie = movieList.get(position);
            Intent intent = new Intent(v.getContext(),MovieDetailActivity.class);
            intent.putExtra(AdapterSearchMovie.KEY_MOVIE_IMAGE,movie.getPoster());
            intent.putExtra(AdapterSearchMovie.KEY_MOVIE_TITLE,movie.getTitle());
            intent.putExtra(AdapterSearchMovie.KEY_IMBDID,movie.getImdbID());
            intent.putExtra(AdapterSearchMovie.KEY_YEAR,movie.getYear());
            intent.putExtra(AdapterSearchMovie.KEY_TYPE,movie.getType());
            v.getContext().startActivity(intent);
        });
    }

    public void deleteMovie(int position){
        dbMovie.deleteMovie(movieList.get(position).getImdbID());
        movieList.remove(movieList.get(position));
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
        ImageView movieImg;
        TextView movieTitle;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImg = itemView.findViewById(R.id.card_view_image_title);
            movieTitle = itemView.findViewById(R.id.card_view_movie_title);
            linearLayout = itemView.findViewById(R.id.card_view_linear_layout);
            linearLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),2,1,"Delete Movie");
        }

    }

}
