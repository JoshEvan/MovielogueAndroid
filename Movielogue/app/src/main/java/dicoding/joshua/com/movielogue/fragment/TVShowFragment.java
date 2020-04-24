package dicoding.joshua.com.movielogue.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
 * {@link TVShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TVShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TVShowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rvTVShow;
    private ArrayList<Movie> list = new ArrayList<>();
    public ArrayList<Movie> tvshows;
    MovieAdapter listTVShowAdapter= new MovieAdapter(list);
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;

    public TVShowFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TVShowFragment newInstance(int index) {
        TVShowFragment fragment = new TVShowFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, index);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tvshow, container, false);
        rvTVShow = view.findViewById(R.id.rv_li_tvs);
        progressBar = view.findViewById(R.id.progressBar);
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        tvshows = mainViewModel.setListTVShows();
        showLoading(true);

        mainViewModel.getListTVShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if(movies != null){
                    listTVShowAdapter.setData(movies);
                    showLoading(false);
                }
            }
        });
        showRecyclerList();
        return view;
    }

    public void showLoading(Boolean state){
        if(state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }


    private void showRecyclerList(){

        Log.v("coba", "showrv");
        rvTVShow.setLayoutManager(new LinearLayoutManager(getActivity()));

        listTVShowAdapter.notifyDataSetChanged();
        rvTVShow.setAdapter(listTVShowAdapter);

        listTVShowAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data,int id) {
                System.out.println("test");
                Log.v("coba", "clicked"+id);

                showSelectedTVShows(data);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, tvshows.get(id));
                intent.putExtra("type","tvshow");
                startActivity(intent);
            }
        });

    }

    public void showSelectedTVShows(Movie m){
        Toast.makeText(getActivity(), m.getTitle(), Toast.LENGTH_LONG).show();
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
