package dicoding.joshua.com.movielogueconsumer.model;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private static final String API_KEY = "398c533923c1a37945d066d3400ffd54";
    private static String searchKeyword = "";

    int sdhSiap = 0;
    final ArrayList<String> titles = new ArrayList<>();
    private MutableLiveData<ArrayList<Movie>> listMovie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> listTVShows = new MutableLiveData<>();
    private ArrayList<Movie> listMovieTemp = new ArrayList<>();


    public static String getSearchKeyword() {
        return searchKeyword;
    }

    public static void setSearchKeyword(String s) {
        searchKeyword = s;
    }

    public MutableLiveData<ArrayList<Movie>> getListTVShows() {
        return listTVShows;
    }

    public static String convertDurationFormat(String dur){
        String res = "";
        int temp = Integer.parseInt(dur);
        int minute = temp%60;
        int hour = 0;
        while(temp >= 60){
            temp/=60;hour++;
        }

        res = String.format("%02d:%02d",hour,minute);
        return res;
    }

    public ArrayList<Movie> setListTVShows() {
        final AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<Movie> lisItems = new ArrayList<>();

        if(searchKeyword.equals("")){
            final String url = "https://api.themoviedb.org/3/discover/tv?api_key="+getApiKey()+"&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&include_null_first_air_dates=false";

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String result = new String(responseBody);
                        final JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for(int i = 0;i<list.length();i++){
                            JSONObject movie = list.getJSONObject(i);
                            final Movie currMov = new Movie();
                            currMov.setId(movie.getInt("id")+"");
                            currMov.setTitle(movie.getString("original_name"));
                            currMov.setScore(movie.getString("vote_average"));
                            currMov.setDescription(movie.getString("overview"));

                            String POSTER_SIZE = "w185";
                            String POSTER_FILENAME = movie.getString("poster_path");
                            String urlPhoto = "https://image.tmdb.org/t/p/"+POSTER_SIZE+"/"+POSTER_FILENAME+"";

                            currMov.setPhoto(urlPhoto);

                            int currID = movie.getInt("id");
                            String urlDetail = "https://api.themoviedb.org/3/tv/"+currID+"?api_key="+getApiKey()+"&language=en-US";
                            client.get(urlDetail, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String resDetail = new String(responseBody);
                                        JSONObject responseObjectDetail = new JSONObject(resDetail);
                                        JSONArray listGenre = responseObjectDetail.getJSONArray("genres");

                                        String currGenres = "";
                                        for(int j = 0;j< listGenre.length(); j++){
                                            if(j>0) currGenres+=" - ";
                                            JSONObject itemGenres = listGenre.getJSONObject(j);
                                            currGenres+=itemGenres.getString("name");
                                        }
                                        JSONArray listDuration = responseObjectDetail.getJSONArray("episode_run_time");

                                        String currMovDuration = "";
                                        for(int j = 0;j< listDuration.length(); j++){
                                            if(j>0) currMovDuration+=", ";
                                            String itemDuration = listDuration.get(j).toString();
                                            Log.v("coba",itemDuration);
                                            currMovDuration+=itemDuration;
                                        }
                                        currMov.setDuration(convertDurationFormat(currMovDuration));
                                        currMov.setGenres(currGenres);

                                    }catch (Exception e){
                                        Log.d("Exception DetailAPI", e.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Log.d("onFailure DetailAPI", error.getMessage());
                                }
                            });

                            lisItems.add(currMov);
                        }
                        listTVShows.postValue(lisItems);

                    }catch(Exception e){
                        Log.d("Exception DiscoverAPI", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure DiscoverAPI", error.getMessage());
                }
            });
        }else{
            final String url = "https://api.themoviedb.org/3/search/multi?api_key="+getApiKey()+"&language=en-US&query="+searchKeyword+"&include_adult=false";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String result = new String(responseBody);
                        final JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for(int i = 0;i<list.length();i++){
                            JSONObject movie = list.getJSONObject(i);
                            if(!movie.getString("media_type").equals("tv")) continue;
                            final Movie currMov = new Movie();
                            currMov.setId(movie.getInt("id")+"");
                            currMov.setTitle(movie.getString("original_name"));
                            currMov.setScore(movie.getString("vote_average"));
                            currMov.setDescription(movie.getString("overview"));

                            String POSTER_SIZE = "w185";
                            String POSTER_FILENAME = movie.getString("poster_path");
                            String urlPhoto = "https://image.tmdb.org/t/p/"+POSTER_SIZE+"/"+POSTER_FILENAME+"";

                            currMov.setPhoto(urlPhoto);

                            int currID = movie.getInt("id");
                            String urlDetail = "https://api.themoviedb.org/3/tv/"+currID+"?api_key="+getApiKey()+"&language=en-US";
                            client.get(urlDetail, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String resDetail = new String(responseBody);
                                        JSONObject responseObjectDetail = new JSONObject(resDetail);
                                        JSONArray listGenre = responseObjectDetail.getJSONArray("genres");

                                        String currGenres = "";
                                        for(int j = 0;j< listGenre.length(); j++){
                                            if(j>0) currGenres+=" - ";
                                            JSONObject itemGenres = listGenre.getJSONObject(j);
                                            currGenres+=itemGenres.getString("name");
                                        }
                                        JSONArray listDuration = responseObjectDetail.getJSONArray("episode_run_time");

                                        String currMovDuration = "";
                                        for(int j = 0;j< listDuration.length(); j++){
                                            if(j>0) currMovDuration+=", ";
                                            String itemDuration = listDuration.get(j).toString();
                                            Log.v("coba",itemDuration);
                                            currMovDuration+=itemDuration;
                                        }
                                        currMov.setDuration(convertDurationFormat(currMovDuration));
                                        currMov.setGenres(currGenres);

                                    }catch (Exception e){
                                        Log.d("Exception DetailAPI", e.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Log.d("onFailure DetailAPI", error.getMessage());
                                }
                            });

                            lisItems.add(currMov);
                        }
                        listTVShows.postValue(lisItems);

                    }catch(Exception e){
                        Log.d("Exception DiscoverAPI", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure DiscoverAPI", error.getMessage());
                }
            });
        }

        return lisItems;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public LiveData<ArrayList<Movie>> getListMovie() {
        return listMovie;
    }

    public ArrayList<Movie> setListMovie() {
        // REQUEST API
        final AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<Movie> lisItems = new ArrayList<>();

        if(searchKeyword.equals("")){
            final String url = "https://api.themoviedb.org/3/discover/movie?api_key="+getApiKey()+"&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String result = new String(responseBody);
                        final JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for(int i = 0;i<list.length();i++){
                            JSONObject movie = list.getJSONObject(i);
                            final Movie currMov = new Movie();
                            currMov.setId(movie.getInt("id")+"");
                            Log.v("coba","movie id"+movie.getInt("id")+movie.getString("title"));
                            currMov.setTitle(movie.getString("title"));
                            currMov.setScore(movie.getString("vote_average"));
                            currMov.setDescription(movie.getString("overview"));

                            String POSTER_SIZE = "w185";
                            String POSTER_FILENAME = movie.getString("poster_path");
                            String urlPhoto = "https://image.tmdb.org/t/p/"+POSTER_SIZE+"/"+POSTER_FILENAME+"";

                            currMov.setPhoto(urlPhoto);

                            int currID = movie.getInt("id");
                            String urlDetail = "https://api.themoviedb.org/3/movie/"+currID+"?api_key="+API_KEY+"&language=en-US";
                            client.get(urlDetail, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String resDetail = new String(responseBody);
                                        JSONObject responseObjectDetail = new JSONObject(resDetail);
                                        JSONArray listGenre = responseObjectDetail.getJSONArray("genres");

                                        String currGenres = "";
                                        for(int j = 0;j< listGenre.length(); j++){
                                            if(j>0) currGenres+=" - ";
                                            JSONObject itemGenres = listGenre.getJSONObject(j);
                                            currGenres+=itemGenres.getString("name");
                                        }
                                        String currMovDuration = responseObjectDetail.getString("runtime");
                                        currMov.setDuration(convertDurationFormat(currMovDuration));
                                        currMov.setGenres(currGenres);

                                    }catch (Exception e){
                                        Log.d("Exception DetailAPI", e.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Log.d("onFailure DetailAPI", error.getMessage());
                                }
                            });
                            listMovieTemp.add(currMov);
                            lisItems.add(currMov);
                        }
                        listMovie.postValue(lisItems);

                    }catch(Exception e){
                        Log.d("Exception DiscoverAPI", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure DiscoverAPI", error.getMessage());
                }
            });
        }else{
            final String url = "https://api.themoviedb.org/3/search/multi?api_key="+getApiKey()+"&language=en-US&query="+searchKeyword+"&include_adult=false";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String result = new String(responseBody);
                        final JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for(int i = 0;i<list.length();i++){
                            JSONObject movie = list.getJSONObject(i);
                            if(!movie.getString("media_type").equals("movie")) continue;
                            final Movie currMov = new Movie();
                            currMov.setId(movie.getInt("id")+"");
                            Log.v("coba","movie id"+movie.getInt("id")+movie.getString("title"));
                            currMov.setTitle(movie.getString("title"));
                            currMov.setScore(movie.getString("vote_average"));
                            currMov.setDescription(movie.getString("overview"));

                            String POSTER_SIZE = "w185";
                            String POSTER_FILENAME = movie.getString("poster_path");
                            String urlPhoto = "https://image.tmdb.org/t/p/"+POSTER_SIZE+"/"+POSTER_FILENAME+"";

                            currMov.setPhoto(urlPhoto);

                            int currID = movie.getInt("id");
                            String urlDetail = "https://api.themoviedb.org/3/movie/"+currID+"?api_key="+API_KEY+"&language=en-US";
                            client.get(urlDetail, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String resDetail = new String(responseBody);
                                        JSONObject responseObjectDetail = new JSONObject(resDetail);
                                        JSONArray listGenre = responseObjectDetail.getJSONArray("genres");

                                        String currGenres = "";
                                        for(int j = 0;j< listGenre.length(); j++){
                                            if(j>0) currGenres+=" - ";
                                            JSONObject itemGenres = listGenre.getJSONObject(j);
                                            currGenres+=itemGenres.getString("name");
                                        }
                                        String currMovDuration = responseObjectDetail.getString("runtime");
                                        currMov.setDuration(convertDurationFormat(currMovDuration));
                                        currMov.setGenres(currGenres);

                                    }catch (Exception e){
                                        Log.d("Exception DetailAPI", e.getMessage());
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Log.d("onFailure DetailAPI", error.getMessage());
                                }
                            });
                            listMovieTemp.add(currMov);
                            lisItems.add(currMov);
                        }
                        listMovie.postValue(lisItems);

                    }catch(Exception e){
                        Log.d("Exception DiscoverAPI", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure DiscoverAPI", error.getMessage());
                }
            });

        }
        return lisItems;
    }



    public interface MyJSONCallback{
        public void doneDownload(boolean success, ArrayList<String> titles);
    }
    public void getReleaseToday(final MyJSONCallback myJSONCallback){
        titles.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(d);
        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+API_KEY+"&primary_release_date.gte="+date+"&primary_release_date.lte="+date;

        Log.v("cobarelease","getReleaseToday:"+url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String res = new String(responseBody);
                Log.v("cobarelease",res);
                try {
                    JSONObject respObj = new JSONObject(res);
                    JSONArray list = respObj.getJSONArray("results");
                    Log.v("cobarelease",list.length()+"LIST LENGTH");
                    Log.v("cobarelease",list+"");
                    for(int i = 0;i<list.length();i++){
                        JSONObject movie = list.getJSONObject(i);
                        String currTitle = movie.getString("title");
                        String message = currTitle;

                        Log.v("cobarelease",message+"");
                        titles.add(message);
                    }

                    myJSONCallback.doneDownload(true, titles);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("cobarelease","fail ambil data");
            }

        });
        Log.v("cobarelease","RETURN LIST TITLE");
//        return titles;
    }

    public ArrayList<String> getTitlesRelease(){
        return titles;
    }

    public Movie reqDetailById(final int currID){

        final Movie currMov2 = new Movie();
        final AsyncHttpClient client = new AsyncHttpClient();
        final String urlDetail = "https://api.themoviedb.org/3/movie/"+currID+"?api_key="+API_KEY+"&language=en-US";
        client.get(urlDetail, new AsyncHttpResponseHandler() {
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String resDetail = new String(responseBody);
                    JSONObject responseObjectDetail = new JSONObject(resDetail);
                    Log.v("coba",responseObjectDetail+"");
                    Log.v("coba",responseObjectDetail.getString("title"));
                    Log.v("coba","woi");
                    currMov2.setId(responseObjectDetail.getString("id"));
                    currMov2.setTitle(responseObjectDetail.getString("title"));
                    currMov2.setScore(responseObjectDetail.getString("vote_average"));
                    currMov2.setDescription(responseObjectDetail.getString("overview"));

                    String POSTER_SIZE = "w185";
                    String POSTER_FILENAME = responseObjectDetail.getString("poster_path");
                    String urlPhoto = "https://image.tmdb.org/t/p/"+POSTER_SIZE+"/"+POSTER_FILENAME+"";

                    currMov2.setPhoto(urlPhoto);

                    JSONArray listGenre = responseObjectDetail.getJSONArray("genres");

                    String currGenres = "";
                    for(int j = 0;j< listGenre.length(); j++){
                        if(j>0) currGenres+=" - ";
                        JSONObject itemGenres = listGenre.getJSONObject(j);
                        currGenres+=itemGenres.getString("name");
                    }
                    String currMovDuration = responseObjectDetail.getString("runtime");
                    currMov2.setDuration(convertDurationFormat(currMovDuration));
                    currMov2.setGenres(currGenres);
                    Log.v("coba","end of request");
                }catch (Exception e){
                    Log.d("Exception DetailAPI", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure DetailAPI", error.getMessage());
            }
        });
        Log.v("coba","plis bisa "+ currMov2.getTitle());
        return currMov2;
    }

    public void setParticularMovieFavState(int pos, String state){
        listMovieTemp.get(pos).setFav_state(state);
        listMovie.postValue(listMovieTemp);
    }


}
