package dicoding.joshua.com.movielogueconsumer;

import java.util.ArrayList;

import dicoding.joshua.com.movielogueconsumer.model.Movie;

public interface LoadFavoritesCallback {
    void preExecute();
    void postExecute(ArrayList<Movie> movies);
}