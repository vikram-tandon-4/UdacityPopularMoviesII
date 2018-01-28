package xyz.clairvoyant.androidjokelibrary.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xyz.clairvoyant.androidjokelibrary.R;


public class JokeAndroidActivity extends AppCompatActivity{

    private String JOKE="joke";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_joke_library);

        if(getIntent().getStringExtra(JOKE)!= null && getIntent().getStringExtra(JOKE).length()>0)
        {
            TextView tvJoke = (TextView)findViewById(R.id.tvJoke);
            tvJoke.setText(getIntent().getStringExtra(JOKE));
        }
    }
}
