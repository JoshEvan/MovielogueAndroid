package dicoding.joshua.com.movielogue;

import java.util.ArrayList;

import dicoding.joshua.com.movielogue.model.Movie;

public interface LoadFavoritesCallback{
    void preExecute();
    void postExecute(ArrayList<Movie> movies);
}