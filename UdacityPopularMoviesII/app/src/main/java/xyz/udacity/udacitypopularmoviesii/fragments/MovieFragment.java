package xyz.udacity.udacitypopularmoviesii.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.udacity.udacitypopularmoviesii.adapters.MovieGridAdapter;
import xyz.udacity.udacitypopularmoviesii.utils.ApiClient;
import xyz.udacity.udacitypopularmoviesii.utils.Constants;
import xyz.udacity.udacitypopularmoviesii.interfaces.ApiInterface;
import xyz.udacity.udacitypopularmoviesii.models.MovieListResponseBean;
import xyz.udacity.udacitypopularmoviesii.models.Results;
import xyz.udacity.udacitypopularmoviesii.R;
import xyz.udacity.udacitypopularmoviesii.activities.ActivityMovieDetails;
import xyz.udacity.udacitypopularmoviesii.activities.ActivityMovies;


public class MovieFragment extends Fragment {

    GridView gridview;
    Results movieData;
    TextView no_movies;
    ProgressDialog progressDialog;
    SendData sendData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_fragment, container, false);

        gridview = (GridView) view.findViewById(R.id.gridview);
        no_movies = (TextView) view.findViewById(R.id.no_movies);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                movieData = (Results) adapterView.getItemAtPosition(i);
// sending movie data to details screen

                if (((ActivityMovies) getActivity()).getPaneMode()) {
                    sendData.movieDetails(movieData);
                } else {
                    Intent intent = new Intent(getActivity(), ActivityMovieDetails.class);
                    intent.putExtra(Constants.MOVIEOBJECT, movieData);
                    startActivity(intent);
                }
            }
        });

        return view;
    }


    public void getTopRatedMovies() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.plaese_wait), true);
        Call<MovieListResponseBean> call = apiService.getTopRatedMovies(Constants.API_KEY);
        call.enqueue(new Callback<MovieListResponseBean>() {
            @Override
            public void onResponse(Call<MovieListResponseBean> call, Response<MovieListResponseBean> response) {

                if(response.body()!=null){


                if(isAdded()) {//Return true if the fragment is currently added to its activity
                    List<Results> movies = response.body().getResults();

                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    if (movies == null || movies.size() == 0) {
                        no_movies.setVisibility(View.VISIBLE);
                    } else {
                        Log.i(getString(R.string.api_response), "Number of movies received: " + movies.size());
                        // resetting adapter with fresh data
                        gridview.setAdapter(new MovieGridAdapter(getActivity(), movies));
                        no_movies.setVisibility(View.GONE);
                    }
                }
                }else{
                    Log.i("Response","body is null");
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    no_movies.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MovieListResponseBean> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                Log.i("API RESPONSE", t.toString());
            }
        });
    }


    public void getPopularMovies() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        progressDialog = ProgressDialog.show(getActivity(), "", "Please Wait...", true);

        Call<MovieListResponseBean> call = apiService.getPopularMovies(Constants.API_KEY);
        call.enqueue(new Callback<MovieListResponseBean>() {
            @Override
            public void onResponse(Call<MovieListResponseBean> call, Response<MovieListResponseBean> response) {

                if(isAdded()) {//Return true if the fragment is currently added to its activity

                    List<Results> movies = response.body().getResults();
                    Log.e(getString(R.string.api_response), getString(R.string.number_of_movies) + movies.size());
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    if (movies == null || movies.size() == 0) {
                        no_movies.setVisibility(View.VISIBLE);
                    } else {
                        // resetting adapter with fresh data
                        gridview.setAdapter(new MovieGridAdapter(getActivity(), movies));
                        no_movies.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieListResponseBean> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                Log.e(getString(R.string.api_response), t.toString());
            }
        });
    }

    // setting local data in gridview
    public void setFavoriteMovies(List<Results> movies) {
        if (movies==null || movies.size() == 0) {
            no_movies.setVisibility(View.VISIBLE);
        } else {
            gridview.setAdapter(new MovieGridAdapter(getActivity(), movies));
            no_movies.setVisibility(View.GONE);
        }
    }

    public interface SendData {
        public void movieDetails(Results movieData);
    }

    public void setSendData(SendData sendData) {
        this.sendData = sendData;
    }
}
