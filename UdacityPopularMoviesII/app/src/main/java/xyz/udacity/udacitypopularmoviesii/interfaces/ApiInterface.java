package xyz.udacity.udacitypopularmoviesii.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.udacity.udacitypopularmoviesii.models.MovieListResponseBean;
import xyz.udacity.udacitypopularmoviesii.models.ReviewModel;
import xyz.udacity.udacitypopularmoviesii.models.TrailerResponseModel;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MovieListResponseBean> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieListResponseBean> getPopularMovies(@Query("api_key") String apiKey);

    @GET("reviews")
    Call<ReviewModel> getMovieReview(@Query("api_key") String apiKey);

    @GET("videos")
    Call<TrailerResponseModel> getTrailers(@Query("api_key") String apiKey);

}