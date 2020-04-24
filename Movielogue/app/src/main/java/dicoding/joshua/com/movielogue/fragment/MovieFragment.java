package dicoding.joshua.com.movielogue.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dicoding.joshua.com.movielogue.model.MainViewModel;
import dicoding.joshua.com.movielogue.model.Movie;
import dicoding.joshua.com.movielogue.activity.MovieDetailActivity;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.adapter.MovieAdapter;
import dicoding.joshua.com.movielogue.db.FavoriteHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView rvMovies;
    public ArrayList<Movie> movies;
    MovieAdapter listMovieAdapter= new MovieAdapter();
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;
    private FavoriteHelper favoriteHelper;
    private Movie movie;

    public MovieFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(int index) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovies = view.findViewById(R.id.rv_li);
        progressBar = view.findViewById(R.id.progressBar);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        movies = mainViewModel.setListMovie();
        showLoading(true);

        mainViewModel.getListMovie().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if(movies != null){
                    listMovieAdapter.setData(movies);
                    showLoading(false);
                }
            }
        });

        showRecyclerList();
        listMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        favoriteHelper = FavoriteHelper.getInstance(getActivity());
        favoriteHelper.open();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.v("test", "oncreateview movie");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        return view;
    }

    private void showRecyclerList(){

        Log.v("coba", "showrv");
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        listMovieAdapter.notifyDataSetChanged();
        rvMovies.setAdapter(listMovieAdapter);

        listMovieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
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
        favoriteHelper.close();
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
