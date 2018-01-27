package xyz.udacity.udacitypopularmoviesii.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.udacity.udacitypopularmoviesii.adapters.ReviewAdapter;

import xyz.udacity.udacitypopularmoviesii.interfaces.ApiInterface;
import xyz.udacity.udacitypopularmoviesii.models.Result;
import xyz.udacity.udacitypopularmoviesii.models.ReviewModel;
import xyz.udacity.udacitypopularmoviesii.R;
import xyz.udacity.udacitypopularmoviesii.utils.Constants;
import xyz.udacity.udacitypopularmoviesii.utils.SimpleDividerIncludingLastItemDecoration;


public class ReviewActivity extends AppCompatActivity {

    RecyclerView review_recyclerview;
    public static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    ProgressDialog progressDialog;
    Context mContext;
    int movieId = 0;
    ReviewAdapter mAdapter;
    TextView no_reviews, internet_connection;
    protected Toolbar toolbar;
    private TextView toolBarHeaderText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);
        mContext = this;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeaderText = (TextView) findViewById(R.id.tvTbTitle);
        setUpToolbar();

        review_recyclerview = (RecyclerView) findViewById(R.id.review_recyclerview);
        review_recyclerview.addItemDecoration(new SimpleDividerIncludingLastItemDecoration(getResources()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        review_recyclerview.setLayoutManager(mLayoutManager);
        review_recyclerview.setItemAnimator(new DefaultItemAnimator());
        no_reviews = (TextView) findViewById(R.id.no_reviews);
        internet_connection = (TextView) findViewById(R.id.internet_connection);

        if (getIntent().getIntExtra(Constants.ID, 0) != 0) {
            // custom url
            BASE_URL = "http://api.themoviedb.org/3/movie/" + getIntent().getIntExtra(Constants.ID, 0) + "/";
            movieId = getIntent().getIntExtra(Constants.ID, 0);
            if (isNetWorkAvailable(mContext)) {
                getMovieReview();
                internet_connection.setVisibility(View.GONE);
                no_reviews.setVisibility(View.GONE);
                review_recyclerview.setVisibility(View.VISIBLE);

            } else {
                internet_connection.setVisibility(View.VISIBLE);
                no_reviews.setVisibility(View.GONE);
                review_recyclerview.setVisibility(View.GONE);
            }
        }
    }


    public void getMovieReview() {

        ApiInterface apiService =
                ApiClientReview.getClient().create(ApiInterface.class);
        progressDialog = ProgressDialog.show(mContext, "", getString(R.string.plaese_wait), true);
        Call<ReviewModel> call = apiService.getMovieReview(Constants.API_KEY);
        Log.i("request", call.request().toString());
        call.enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                List<Result> reviewDetails = response.body().getResults();
                Log.i(getString(R.string.api_response), "Number of reviews received: " + reviewDetails.size());
                Log.i(getString(R.string.api_response), "Movie id: " + movieId);
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                if (reviewDetails.size() == 0) {
                    internet_connection.setVisibility(View.GONE);
                    no_reviews.setVisibility(View.VISIBLE);
                    review_recyclerview.setVisibility(View.GONE);

                } else {
                    // resetting adapter with fresh data
                    internet_connection.setVisibility(View.GONE);
                    no_reviews.setVisibility(View.GONE);
                    review_recyclerview.setVisibility(View.VISIBLE);
                    mAdapter = new ReviewAdapter(reviewDetails, mContext);
                    review_recyclerview.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
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

    public static class ApiClientReview {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setUpToolbar() {
        if (toolbar != null) {
            setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolBarHeaderText.setText(getString(R.string.reviews));
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
}
