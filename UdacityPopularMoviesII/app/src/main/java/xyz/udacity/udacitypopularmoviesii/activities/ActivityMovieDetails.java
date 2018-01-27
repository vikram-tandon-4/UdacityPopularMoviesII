package xyz.udacity.udacitypopularmoviesii.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import xyz.udacity.udacitypopularmoviesii.fragments.MovieDetailsFragment;
import xyz.udacity.udacitypopularmoviesii.models.Results;
import xyz.udacity.udacitypopularmoviesii.R;
import xyz.udacity.udacitypopularmoviesii.utils.Constants;

public class ActivityMovieDetails extends AppCompatActivity {
    protected Toolbar toolbar;
    private TextView toolBarHeaderText;
    Context mContext;
    private Results movieData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeaderText = (TextView) findViewById(R.id.tvTbTitle);
        mContext = this;
        setUpToolbar();

        if (getIntent() != null)
            movieData = (Results) getIntent().getSerializableExtra(Constants.MOVIEOBJECT);

        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_movie_details);
        if (movieDetailsFragment != null)
            movieDetailsFragment.changeData(movieData);
    }

    //setting toolbar
    private void setUpToolbar() {
        if (toolbar != null) {
            setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolBarHeaderText.setText(getString(R.string.movie_detail));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_details, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
        }
        return true;
    }
}
