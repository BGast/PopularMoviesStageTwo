package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstagetwo.model.Movies;
import com.example.android.popularmoviesstagetwo.utils.NetworkUtils;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private final Context mContext;
    private ArrayList<Movies> mMovie;
    private final ListItemClickListener mMovieClickListener;

    public RecyclerViewAdapter(Context mContext, ArrayList<Movies> mMovie, ListItemClickListener mMovieClickListener) {
        this.mContext = mContext;
        this.mMovie = mMovie;
        this.mMovieClickListener = mMovieClickListener;
    }

    public interface ListItemClickListener {
        void OnListItemClick(Movies movieItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_movie_poster, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovie == null ? 0 : mMovie.size();
    }

    public ArrayList<Movies> getMovieData() {
        return mMovie;
    }

    /**
     * Fills the movie list
     * @param movies of type ArrayList<Movies>
     */
    public void setMovieData(ArrayList<Movies> movies) {
        mMovie = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMovieListImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieListImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {   // Adds the movie posters to activity_main
            Log.d(TAG, "onBindViewHolder: called.");

            Movies movieItem = mMovie.get(position);
            mMovieListImageView = itemView.findViewById(R.id.iv_movie_poster);
            String bindMovie = NetworkUtils.buildPosterUrl(movieItem.getmMoviePoster());

            Glide.with(mContext)
                    .asBitmap()
                    .load(bindMovie)
                    .into(mMovieListImageView);
        }

        @Override
        public void onClick(View view) {    // Sets the onClick so you may see the movie details upon clicking a poster
            int clickPosition = getAdapterPosition();
            mMovieClickListener.OnListItemClick(mMovie.get(clickPosition));
        }
    }
}
