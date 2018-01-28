package com.udacity.gradle.builditbigger;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Joke;
import com.example.vikra.myapplication.backend.myApi.MyApi;
import com.example.vikra.myapplication.backend.myApi.model.MyBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import xyz.clairvoyant.androidjokelibrary.activities.JokeAndroidActivity;


public class MainActivity extends AppCompatActivity {

    private String JOKE="joke";
    private Context mContext;
    private Dialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        Joke joke = new Joke();
        String jokeData=joke.getPaidJoke();
        if(!TextUtils.isEmpty(jokeData))
        {
           Toast.makeText(mContext, jokeData, Toast.LENGTH_SHORT).show();
        }
    }


    public void OpenJokeScreen(View view) {
        Joke joke = new Joke();
        String jokeData=joke.getPaidJoke();
        if(!TextUtils.isEmpty(jokeData)) {
            Intent intent = new Intent(MainActivity.this, JokeAndroidActivity.class);
            intent.putExtra(JOKE, jokeData);
            startActivity(intent);
        }

    }

    public void GetJokeFromBackgroundAndOpenJokeScreen(View view) {
        if(networkUp()) {
            // use the following for testing it on an emulator
//                new EndpointsAsyncTask().execute("http://10.0.2.2:8080/_ah/api/");
            new EndpointsAsyncTask().execute(getString(R.string.ip_url));
        }else{
            Toast.makeText(mContext, getString(R.string.no_internet_connectivity), Toast.LENGTH_LONG).show();
        }
    }

    class EndpointsAsyncTask extends AsyncTask<String, Void, String> {
        private  MyApi myApiService = null;
        private Context context;

        @Override
        protected String doInBackground(String ... params) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                   .setRootUrl(params[0])
  //                      .setRootUrl("http://172.16.0.134:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });

                myApiService = builder.build();
            }

            publishProgress(null);
            try {
                return myApiService.setJoke(new MyBean()).execute().getJoke();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mProgressDialog = Utils.showProgressDialog(mContext);
        }

        @Override
        protected void onPostExecute(String result) {
            Utils.cancelProgressDialog(mProgressDialog);
            Intent intent = new Intent(MainActivity.this, JokeAndroidActivity.class);
            intent.putExtra(JOKE, result);
            startActivity(intent);
        }
    }

    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
