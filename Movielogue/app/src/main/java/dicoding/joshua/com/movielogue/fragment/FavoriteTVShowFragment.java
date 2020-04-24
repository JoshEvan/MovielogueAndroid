package dicoding.joshua.com.movielogue.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dicoding.joshua.com.movielogue.LoadFavoritesCallback;
import dicoding.joshua.com.movielogue.model.Movie;
import dicoding.joshua.com.movielogue.activity.MovieDetailActivity;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.adapter.FavoriteAdapter;
import dicoding.joshua.com.movielogue.db.FavoriteHelper;
import dicoding.joshua.com.movielogue.helper.MappingHelper;

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
 * {@link FavoriteTVShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteTVShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteTVShowFragment extends Fragment implements LoadFavoritesCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FavoriteHelper favoriteHelper;
    private ProgressBar progressBar;
    private RecyclerView rvTVShow;
    private ArrayList<Movie> favTVShow = new ArrayList<>();
    final FavoriteAdapter favTVShowAdapter= new FavoriteAdapter();
    private static final String EXTRA_STATE = "EXTRA_STATE_CONFIG_CHANGE";

    private OnFragmentInteractionListener mListener;

    public FavoriteTVShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteTVShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteTVShowFragment newInstance(String param1, String param2) {
        FavoriteTVShowFragment fragment = new FavoriteTVShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        progressBar = getActivity().findViewById(R.id.progressBarFavTVShow);
        favoriteHelper = FavoriteHelper.getInstance(getActivity());
        favoriteHelper.open();

        if(savedInstanceState == null || savedInstanceState.getParcelableArrayList(EXTRA_STATE)==null){
            new LoadFavoritesAsync(favoriteHelper,this);
        }else{
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            favTVShowAdapter.setFavMovies(list);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        favTVShowAdapter.clearDataFav();
        new LoadFavoritesAsync(favoriteHelper, this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);
    }

    private static class LoadFavoritesAsync extends AsyncTask<Void,Void, ArrayList<Movie>>{
        private final WeakReference<FavoriteHelper> weakFavoriteHelper;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadFavoritesAsync(FavoriteHelper favoriteHelper, LoadFavoritesCallback callback){
            weakFavoriteHelper = new WeakReference<>(favoriteHelper);
            weakCallback = new WeakReference<>(callback);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Cursor dataCursor = weakFavoriteHelper.get().queryAll();
            Log.v("coba","query all");
            MappingHelper mh = new MappingHelper();
            return mh.mapCursorToArrayListTVShow(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            Log.v("coba","post exec");
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    @Override
    public void preExecute() {
        progressBar = getActivity().findViewById(R.id.progressBarFavTVShow);
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if(movies.size() > 0){
            Log.v("coba","ada movies fav");
            favTVShowAdapter.setFavMovies(movies);
            favTVShow = favTVShowAdapter.getFavMovies();
            for(int i = 0;i< favTVShow.size();i++){
                Log.v("coba","isi data fav di post execute: "+favTVShow.get(i).getTitle());
            }
            showRecyclerList();
            favTVShowAdapter.notifyDataSetChanged();
            showLoading(false);
        }else{
            Toast.makeText(getActivity(),getResources().getString(R.string.no_data_fav_tvshow), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("coba","on view created fav movies");
        rvTVShow = view.findViewById(R.id.rv_li2);
        progressBar = view.findViewById(R.id.progressBarFavTVShow);

        new LoadFavoritesAsync(favoriteHelper, this).execute();
        showLoading(true);
        showRecyclerList();
        rvTVShow.setAdapter(favTVShowAdapter);
        favTVShowAdapter.notifyDataSetChanged();
    }


    private void showRecyclerList(){
        Log.v("coba", "showrv");
        rvTVShow.setHasFixedSize(false);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getActivity()));


        favTVShowAdapter.notifyDataSetChanged();
        Log.v("coba",favTVShowAdapter.getFavMovies().size()+"size adapter");

        favTVShowAdapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data,int id) {
                System.out.println("test");
                Log.v("coba", "clicked"+id);

                showSelectedMovie(data);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, favTVShow.get(id));
                intent.putExtra("pos",id);
                intent.putExtra("type","tvshow");

                onResume();
                startActivity(intent);
            }
        });
        showLoading(false);
    }

    public void showSelectedMovie(Movie m){
        Log.v("coba", "TOAST"+m.getTitle());
        Toast.makeText(getActivity(),m.getTitle(), Toast.LENGTH_SHORT).show();
    }
    public void showLoading(Boolean state){
        if(state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}