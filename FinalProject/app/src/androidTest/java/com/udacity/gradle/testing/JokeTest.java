package com.udacity.gradle.testing;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import com.udacity.gradle.builditbigger.EndpointsAsyncTask;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JokeTest  extends AndroidTestCase {

    @Test
    public void checkJokeIsEmpty() {

        String result = null;
        EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask("http://192.168.1.4:8080/_ah/api/");
        endpointsAsyncTask.execute();
        try {
            // retrieving string when background task is over
            result = endpointsAsyncTask.get();
            // test to check if the string is null
            assertNotNull(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



