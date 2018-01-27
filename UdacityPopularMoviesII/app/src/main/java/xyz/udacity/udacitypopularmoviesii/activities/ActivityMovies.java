package xyz.udacity.udacitypopularmoviesii.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import xyz.udacity.udacitypopularmoviesii.utils.AppPreferences;
import xyz.udacity.udacitypopularmoviesii.fragments.MovieDetailsFragment;
import xyz.udacity.udacitypopularmoviesii.fragments.MovieFragment;
import xyz.udacity.udacitypopularmoviesii.models.Results;
import xyz.udacity.udacitypopularmoviesii.R;

public class ActivityMovies extends AppCompatActivity implements MovieFragment.SendData {

    protected Toolbar toolbar;
    private TextView toolBarHeaderText;
    Context mContext;
    MovieFragment movieFragment;
    MovieDetailsFragment movieDetailsFragment;
    Boolean two_pane = false;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        mContext = this;
        fragmentManager = getFragmentManager();
        movieFragment = (MovieFragment) fragmentManager.findFragmentById(R.id.fragment_movie);

        movieFragment.setSendData(this);
        if (findViewById(R.id.fragment_movie_details) == null) {
            two_pane = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            two_pane = true;
            movieDetailsFragment = (MovieDetailsFragment) fragmentManager.findFragmentById(R.id.fragment_movie_details);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeaderText = (TextView) findViewById(R.id.tvTbTitle);
        setUpToolbar();

        if (isNetWorkAvailable(mContext))
            movieFragment.getTopRatedMovies();
        else
            openDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (isNetWorkAvailable(mContext)) {
            if (id == R.id.top_rated) {
                // Api call
                movieFragment.getTopRatedMovies();
            } else if (id == R.id.popular) {
                // Api call
                movieFragment.getPopularMovies();
            } else {
                movieFragment.setFavoriteMovies(AppPreferences.getFavoriteList(mContext));
            }
        } else
            // No internet connection Alert
            openDialog();


        return true;
        //   return super.onOptionsItemSelected(item);
    }

    public void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.dialog_text);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

    private void setUpToolbar() {
        if (toolbar != null) {
            setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolBarHeaderText.setText(getString(R.string.toolbar_screen_one));
        }
    }

    public Boolean getPaneMode() {
        return two_pane;
    }

    @Override
    public void movieDetails(Results movieData) {
        movieDetailsFragment.changeData(movieData);
    }
}
