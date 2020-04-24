package dicoding.joshua.com.movielogueconsumer.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dicoding.joshua.com.movielogueconsumer.LoadFavoritesCallback;
import dicoding.joshua.com.movielogueconsumer.db.DatabaseContract;
import dicoding.joshua.com.movielogueconsumer.helper.MappingHelper;
import dicoding.joshua.com.movielogueconsumer.model.Movie;
import dicoding.joshua.com.movielogueconsumer.activity.MovieDetailActivity;
import dicoding.joshua.com.movielogueconsumer.R;
import dicoding.joshua.com.movielogueconsumer.adapter.MovieAdapter;
import dicoding.joshua.com.movielogueconsumer.R;
import dicoding.joshua.com.movielogueconsumer.activity.MovieDetailActivity;
import dicoding.joshua.com.movielogueconsumer.adapter.FavoriteAdapter;
import dicoding.joshua.com.movielogueconsumer.model.Movie;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteMovieFragment extends Fragment implements LoadFavoritesCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String EXTRA_STATE = "EXTRA_STATE";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView rvMovies;
    public ArrayList<Movie> movies;
    FavoriteAdapter listMovieAdapter= new FavoriteAdapter();
    private ProgressBar progressBar;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    public static FavoriteMovieFragment newInstance(int index) {
        FavoriteMovieFragment fragment = new FavoriteMovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovies = view.findViewById(R.id.rv_li);
        progressBar = view.findViewById(R.id.progressBarFavMov);

        showLoading(true);

        showRecyclerList();
        listMovieAdapter.notifyDataSetChanged();

        // CONTENT PROVIDER
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        MyDataObserver myObserver = new MyDataObserver(handler, getActivity());
        getContext().getContentResolver().registerContentObserver(DatabaseContract.FavoriteColumns.CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadFavoriteAsync(getActivity(), this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                listMovieAdapter.setFavMovies(list);
            }
        }
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size() > 0) {
            listMovieAdapter.setFavMovies(movies);
        } else {
            listMovieAdapter.setFavMovies(new ArrayList<Movie>());
            Toast.makeText(getActivity(),getResources().getString(R.string.no_data_fav_movie), Toast.LENGTH_SHORT).show();
        }
    }

    private static class LoadFavoriteAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadFavoriteAsync(Context context, LoadFavoritesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null, null, null);
            MappingHelper mh = new MappingHelper();
            return mh.mapCursorToArrayListMovie(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    public static class MyDataObserver extends ContentObserver {
        final Context context;
        public MyDataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoriteAsync(context, (LoadFavoritesCallback) context).execute();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.v("test", "oncreateview movie");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        return view;
    }

    private void showRecyclerList(){

        Log.v("coba", "showrv");
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        listMovieAdapter.notifyDataSetChanged();
        rvMovies.setAdapter(listMovieAdapter);

        listMovieAdapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data,int id) {
                System.out.println("test");
                Log.v("coba", "clicked"+data.getId());

                showSelectedMovie(data);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, data);
                intent.putExtra("pos",id);
                intent.putExtra("type","movie");

                startActivity(intent);
            }
        });
    }

    public void showLoading(Boolean state){
        if(state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }


    public void showSelectedMovie(Movie m){
        Log.v("coba", "TOAST"+m.getTitle());
        Toast.makeText(getActivity(),m.getTitle(), Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.v("coba","onattach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        listMovieAdapter.clearDataFav();
        new LoadFavoriteAsync(getContext(), this).execute();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
