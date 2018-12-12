package com.example.dell.moviestage2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dell.moviestage2.Model.Movie;
import com.example.dell.moviestage2.MovieDetailActivity;
import com.example.dell.moviestage2.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    int total_types;

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, ArrayList<Movie> movieArrayLists){
        this.context = context;
        this.movieArrayList = movieArrayLists;
        total_types = movieArrayList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View rootView;
        switch (viewType){
            case Movie.Main_View_Type:
                rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
                return new MainViewHolder(rootView);
             case Movie.Trailer_View_Type:
                 rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item2, viewGroup, false);
                 return new TrailerViewHolder(rootView);
              case Movie.Review_View_Type:
                  rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item3, viewGroup, false);
                  return new ReviewHolder(rootView);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        final Movie result = movieArrayList.get(i);
        if(result != null){
            switch (result.getViewType()){
                case Movie.Main_View_Type:
                    String url = "http://image.tmdb.org/t/p/w185/";
                    Picasso.with(context).load(url+result.getMovie_poster()).into(((MainViewHolder) viewHolder).poster);
                    ((MainViewHolder) viewHolder).poster.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MovieDetailActivity.class);
                            intent.putExtra("result", result);
                            context.startActivity(intent);
                        }
                    });

                    break;

                 case Movie.Trailer_View_Type:
                     ((TrailerViewHolder) viewHolder).trailerTv.setText(result.getTrailerName());
                     ((TrailerViewHolder) viewHolder).trailerTv.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             String url = buildUrl();
                             Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                             context.startActivity(intent);
                         }

                         private String buildUrl() {
                             Uri.Builder builder = new Uri.Builder();

                             builder.scheme(MovieDetailActivity.URL_SCHEME).authority(MovieDetailActivity.BASE_URL).path(MovieDetailActivity.PATH2);
                             builder.appendQueryParameter("v", result.getKey());
                             return builder.toString();
                         }
                     });
                     break;

                  case Movie.Review_View_Type:
                      ((ReviewHolder) viewHolder).author.setText(result.getName());
                      ((ReviewHolder) viewHolder).content.setText(result.getKey());

            }
        }

    }

    @Override
    public int getItemViewType(int position) {

        switch (movieArrayList.get(position).getViewType()) {
            case 0:
                return Movie.Main_View_Type;
            case 1:
                return Movie.Trailer_View_Type;
            case 2:
                return Movie.Review_View_Type;
            default:
                return -1;
        }
    }
    public void setMovie(List<Movie> movie){
        this.movieArrayList = (ArrayList<Movie>) movie;
        notifyDataSetChanged();
    }


    public class MainViewHolder extends RecyclerView.ViewHolder  {

        @BindView(R.id.imageView)
        ImageView poster;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.content)
        TextView content;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textView)
        TextView trailerTv;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }
}
