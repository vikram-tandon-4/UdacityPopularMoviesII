package xyz.udacity.udacitypopularmoviesii.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.udacity.udacitypopularmoviesii.adapters.TrailerAdapter;
import xyz.udacity.udacitypopularmoviesii.utils.AppPreferences;
import xyz.udacity.udacitypopularmoviesii.interfaces.ApiInterface;
import xyz.udacity.udacitypopularmoviesii.models.Results;
import xyz.udacity.udacitypopularmoviesii.models.TrailerResponseModel;
import xyz.udacity.udacitypopularmoviesii.models.VideoResult;
import xyz.udacity.udacitypopularmoviesii.R;
import xyz.udacity.udacitypopularmoviesii.utils.Constants;
import xyz.udacity.udacitypopularmoviesii.utils.RecyclerItemClickListener;
import xyz.udacity.udacitypopularmoviesii.utils.SimpleDividerIncludingLastItemDecoration;
import xyz.udacity.udacitypopularmoviesii.activities.ReviewActivity;


public class MovieDetailsFragment extends Fragment {

    private Results movieData;
    private TextView overview, rating, year, movie_name, review, no_trailers, internet_connection, favorite, duration;
    Context mContext;
    String BaseUrl = "http://image.tmdb.org/t/p/";
    String Imagequality = "w185/";
    private ImageView movie_poster;

    ProgressDialog progressDialog;
    RecyclerView trailer_recyclerview;
    public static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    TrailerAdapter mAdapter;
    List<VideoResult> videoDetails;
    LinearLayout movie_details_fragment_layout;
    boolean isExisting = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);
        mContext = getActivity();
        movie_details_fragment_layout = (LinearLayout) view.findViewById(R.id.movie_details_fragment_layout);
        movie_poster = (ImageView) view.findViewById(R.id.movie_poster);
        overview = (TextView) view.findViewById(R.id.overview);
        rating = (TextView) view.findViewById(R.id.rating);
        year = (TextView) view.findViewById(R.id.year);
        movie_name = (TextView) view.findViewById(R.id.movie_name);
        review = (TextView) view.findViewById(R.id.review);
        favorite = (TextView) view.findViewById(R.id.favorite);
        //  duration=(TextView)view.findViewById(R.id.duration);
        internet_connection = (TextView) view.findViewById(R.id.internet_connection);
        no_trailers = (TextView) view.findViewById(R.id.no_trailers);


        trailer_recyclerview = (RecyclerView) view.findViewById(R.id.trailer_recyclerview);
        trailer_recyclerview.addItemDecoration(new SimpleDividerIncludingLastItemDecoration(getResources()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        trailer_recyclerview.setLayoutManager(mLayoutManager);
        trailer_recyclerview.setItemAnimator(new DefaultItemAnimator());

        trailer_recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (videoDetails != null) {
                            choiceDialog(position);
                        }
                    }
                })
        );


        return view;
    }

    public void changeData(final Results movieData) {
        movie_details_fragment_layout.setVisibility(View.VISIBLE);
        this.movieData = movieData;
        BASE_URL = "http://api.themoviedb.org/3/movie/" + movieData.getId() + "/";
        overview.setText(movieData.getOverview());
        String setRating = Double.toString(movieData.getVote_average()).substring(0, 3) + "/10";
        rating.setText(setRating);
        year.setText(movieData.getRelease_date().substring(0, 4));
        movie_name.setText(movieData.getTitle());
        //     duration.setText(""+movieData.getVote_average());

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppPreferences.getFavoriteList(mContext) != null && AppPreferences.getFavoriteList(mContext).size() > 0) {

                    ArrayList<Results> favoriteModel = AppPreferences.getFavoriteList(mContext);

                    // Avoiding redundant entries
                    for (int i = 0; i < favoriteModel.size(); i++) {
                        if (favoriteModel.get(i).getId() == movieData.getId()) {
                            isExisting = true;
                            break;
                        }
                    }
                    if (!isExisting) {
                        favoriteModel.add(movieData);
                        Toast.makeText(mContext, getString(R.string.added_to_fav), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, getString(R.string.already_added_to_fav), Toast.LENGTH_LONG).show();
                    }
                    isExisting = false;

                    AppPreferences.setFavoriteList(mContext, favoriteModel);
                } else {
                    ArrayList<Results> favoriteModel = new ArrayList<>();
                    favoriteModel.add(movieData);
                    AppPreferences.setFavoriteList(mContext, favoriteModel);
                    Toast.makeText(mContext, getString(R.string.added_to_fav), Toast.LENGTH_LONG).show();
                }


            }
        });

        // loading image using provided url
        Picasso.with(mContext)
                .load(BaseUrl + Imagequality + movieData.getPoster_path())
                .placeholder(R.drawable.ic_movie_white_48dp)
                .error(R.drawable.ic_movie_white_48dp)
                .into(movie_poster);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra(Constants.ID, movieData.getId());
                startActivity(intent);
            }
        });


        if (isNetWorkAvailable(mContext)) {
            getMovieTrailers();
            internet_connection.setVisibility(View.GONE);
            no_trailers.setVisibility(View.GONE);
            trailer_recyclerview.setVisibility(View.VISIBLE);

        } else {
            internet_connection.setVisibility(View.VISIBLE);
            no_trailers.setVisibility(View.GONE);
            trailer_recyclerview.setVisibility(View.GONE);
        }


    }

    //checking internet connection
    public static boolean isNetWorkAvailable(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (connMgr == null) {
                return false;
            } else if (connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isAvailable() && connMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public static class ApiClientTrailer {

        //setting url as per requirement
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit;
        }
    }


    public void getMovieTrailers() {

        ApiInterface apiService =
                ApiClientTrailer.getClient().create(ApiInterface.class);
        progressDialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.fetching_trailers), true);
        Call<TrailerResponseModel> call = apiService.getTrailers(Constants.API_KEY);
        Log.i("request", call.request().toString());
        call.enqueue(new Callback<TrailerResponseModel>() {
            @Override
            public void onResponse(Call<TrailerResponseModel> call, Response<TrailerResponseModel> response) {

                if(isAdded()) {//Return true if the fragment is currently added to its activity
                    videoDetails = response.body().getResults();

                    if (videoDetails.size() > 0)
                        Log.i(getString(R.string.api_response), "video id: " + videoDetails.get(0).getKey());
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    if (videoDetails.size() == 0) {
                        internet_connection.setVisibility(View.GONE);
                        no_trailers.setVisibility(View.VISIBLE);
                        trailer_recyclerview.setVisibility(View.GONE);

                    } else {
                        // resetting adapter with fresh data
                        Log.i(getString(R.string.api_response), "Number of videos received: " + videoDetails.size());
                        internet_connection.setVisibility(View.GONE);
                        no_trailers.setVisibility(View.GONE);
                        trailer_recyclerview.setVisibility(View.VISIBLE);
                        mAdapter = new TrailerAdapter(videoDetails, mContext);
                        trailer_recyclerview.setAdapter(mAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<TrailerResponseModel> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                Log.i(getString(R.string.api_response), t.toString());
            }
        });
    }

    public void watchYoutubeVideo(String id, boolean viewOnApp) {


        if (viewOnApp) {
            try {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v=" + id));
                startActivity(webIntent);
            }
        } else {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v=" + id));
            startActivity(webIntent);
        }

    }

    public void choiceDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.choose_trailer);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.web, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                watchYoutubeVideo(videoDetails.get(position).getKey(), false);
                dialog.cancel();
            }
        });

        builder.setPositiveButton(R.string.app, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                watchYoutubeVideo(videoDetails.get(position).getKey(), true);
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}

