package dicoding.joshua.com.movielogue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dicoding.joshua.com.movielogue.model.Movie;
import dicoding.joshua.com.movielogue.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> mData = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;
    private OnItemClickCallbackFav onItemClickCallbackFav;

    private Context context;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(Movie data, int id);
    }

    public interface OnItemClickCallbackFav{
        void onItemClickedFav(Movie data, int id);
    }

    public void setOnItemClickCallbackFav(OnItemClickCallbackFav onItemClickCallbackFav) {
        this.onItemClickCallbackFav = onItemClickCallbackFav;
    }

    public MovieAdapter(){

    }
    public MovieAdapter(Context context) {
        this.context = context;
    }
    public MovieAdapter(ArrayList<Movie> list){
        this.mData = list;
    }


    public void setData(ArrayList<Movie> movies){
        mData.clear();
        mData.addAll(movies);
        notifyDataSetChanged();
    }


    public void addItem(final Movie m){
        mData.add(m);
        notifyDataSetChanged();
    }

    public void updateItem(int pos, Movie m){
        this.mData.set(pos, m);
        notifyItemChanged(pos,m);
    }


    public void clearData(){
        mData.clear();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int pos) {
        Log.v("coba","lagi bind "+mData.get(pos).getTitle()+" di mov frag");
        holder.tvTitle.setText(mData.get(pos).getTitle());
        holder.tvScore.setText(mData.get(pos).getScore());
        Glide.with(holder.itemView.getContext())
                .load(mData.get(pos).getPhoto())
                .apply(new RequestOptions().override(75,100))
                .into(holder.imgPoster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mData.get(pos),pos);
            }
        });
    }

    public ArrayList<Movie> getmData() {
        return mData;
    }

    public void setmData(ArrayList<Movie> mData) {
        this.mData = mData;
    }

    public OnItemClickCallback getOnItemClickCallback() {
        return onItemClickCallback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.mData = movies;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvScore;
        ImageView imgPoster;

        public MovieViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvScore = itemView.findViewById(R.id.tv_score);
            imgPoster = itemView.findViewById(R.id.iv_profilePic);
        }

        @Override
        public void onClick(View view) {
            onItemClickCallback.onItemClicked(mData.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}
