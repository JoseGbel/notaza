package my.apps.skillstracker.unsplashapi.model;


import java.util.ArrayList;

public class PictureResponse {

    public ArrayList<UnsplashPic> getResults() {
        return results;
    }

    public void setResults(ArrayList<UnsplashPic> results) {
        this.results = results;
    }

    private ArrayList<UnsplashPic> results;
}
