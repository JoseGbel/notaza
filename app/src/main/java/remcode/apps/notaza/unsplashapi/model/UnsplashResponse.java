package remcode.apps.notaza.unsplashapi.model;


import java.util.ArrayList;

public class UnsplashResponse {

    private ArrayList<UnsplashPic> results;

    private String url;

    public String getUrl() {
        return url;
    }

    public ArrayList<UnsplashPic> getResults() {
        return results;
    }

    public void downloadPicture(String url){

    }

    public void setResults(ArrayList<UnsplashPic> results) {
        this.results = results;
    }
}
