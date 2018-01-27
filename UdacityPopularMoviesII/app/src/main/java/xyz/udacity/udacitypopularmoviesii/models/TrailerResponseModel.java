package xyz.udacity.udacitypopularmoviesii.models;

import java.io.Serializable;
import java.util.ArrayList;

public class TrailerResponseModel implements Serializable {

    private int id;
    private ArrayList<VideoResult> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<VideoResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<VideoResult> results) {
        this.results = results;
    }
}
