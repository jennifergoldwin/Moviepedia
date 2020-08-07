package com.jenjenfuture.projectuas_2201750761;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSearchMovie extends RecyclerView.Adapter<AdapterSearchMovie.ViewHolder> {


    public static final String KEY_MOVIE_IMAGE = "movie_image";
    public static final String KEY_IMBDID = "imbdid";
    public static final String KEY_MOVIE_TITLE = "movie_title";
    public static final String KEY_YEAR = "year";
    public static final String KEY_TYPE = "type";

    private Context context;
    private List<Movie> movieList;
    private DBMovie dbMovie;

    public AdapterSearchMovie(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        dbMovie = DBMovie.getInstance(context);
    }

    @NonNull
    @Override
    public AdapterSearchMovie.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_movie_template,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchMovie.ViewHolder holder, int position) {
        Glide.with(context)
                .load(movieList.get(position).getPoster())
                .placeholder(R.drawable.notfound)
                .into(holder.movieImg);
//        Picasso.get().load(movieList.get(position).getPoster()).into(holder.movieImg);
        holder.movieTitle.setText(movieList.get(position).getTitle());

        holder.linearLayout.setOnClickListener(v -> {
            Movie movie = movieList.get(position);
            Intent intent = new Intent(v.getContext(),MovieDetailActivity.class);
            intent.putExtra(KEY_MOVIE_IMAGE,movie.getPoster());
            intent.putExtra(KEY_MOVIE_TITLE,movie.getTitle());
            intent.putExtra(KEY_IMBDID,movie.getImdbID());
            intent.putExtra(KEY_YEAR,movie.getYear());
            intent.putExtra(KEY_TYPE,movie.getType());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

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
            menu.add(this.getAdapterPosition(),1,0,"Saved Movie");
        }
    }

    public void addMovie (int pos){
        if (dbMovie.checkMovie(movieList.get(pos).getImdbID())){
            Toast.makeText(context,"Failed, This movie has been added",Toast.LENGTH_SHORT).show();
        }
        else {
            dbMovie.insertMovie(movieList.get(pos));
            notifyDataSetChanged();
            Toast.makeText(context,"Movie added",Toast.LENGTH_SHORT).show();
        }
    }
}
