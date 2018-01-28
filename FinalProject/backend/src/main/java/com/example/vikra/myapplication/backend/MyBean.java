package com.example.vikra.myapplication.backend;

import com.example.Joke;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private Joke joke;

    public  MyBean() {
        joke = new Joke();
    }

    public String getJoke() {

        return joke.getFreeJoke();
    }
}