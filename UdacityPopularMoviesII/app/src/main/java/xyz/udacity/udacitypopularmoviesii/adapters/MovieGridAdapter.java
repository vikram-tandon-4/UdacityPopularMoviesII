package xyz.udacity.udacitypopularmoviesii.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import xyz.udacity.udacitypopularmoviesii.models.Results;
import xyz.udacity.udacitypopularmoviesii.R;


public class MovieGridAdapter extends BaseAdapter {
    private Context mContext;
    List<Results> movies;
    String BaseUrl = "http://image.tmdb.org/t/p/";
    String Imagequality = "w185/";


    // Constructor
    public MovieGridAdapter(Context c, List<Results> movies) {
        mContext = c;
        this.movies = movies;

    }

    public int getCount() {
        if (movies != null) {
            return movies.size();
        } else
            return 0;
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        View grid;
        if (convertView == null) {

            grid = new View(mContext);

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.grid_single, null);


        } else {

            grid = (View) convertView;
        }

        ImageView movie = (ImageView) grid.findViewById(R.id.movie);
//loading image with place holder
        Picasso.with(mContext)
                .load(BaseUrl + Imagequality + movies.get(position).getPoster_path())
                .placeholder(R.drawable.ic_movie_white_48dp)
                .error(R.drawable.ic_movie_white_48dp)
                .into(movie);

        return grid;
    }


}