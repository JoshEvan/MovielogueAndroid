package dicoding.joshua.com.movielogue.adapter;

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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    // FAV SECTION

    ////////////////////////////////////////////////////
    private static ArrayList<Movie> favMovies = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public interface OnItemClickCallback{
        void onItemClicked(Movie data, int id);
    }

    public OnItemClickCallback getOnItemClickCallback() {
        return onItemClickCallback;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }



    public void setFavMovies(ArrayList<Movie> favMovies) {
        if(favMovies.size() > 0) this.favMovies.clear();
        this.favMovies.addAll(favMovies);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getFavMovies() {
        if(favMovies.size()== 0)
            setFavMovies(favMovies);
        return favMovies;
    }

    public void addItemmDataFav(final Movie m){
        this.favMovies.add(m);
        notifyDataSetChanged();
    }


    public void removeItemmDataFav(int pos){
        this.favMovies.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos,favMovies.size());
    }

    public boolean removeItemmDataFavByID(String id){
        int pos = -1;
        for(int i = 0;i<favMovies.size();i++) {
            if(favMovies.get(i).getId().equals(id)){
                pos = i;
                break;
            }
        }
        if(pos!=-1){
            Log.v("coba","SUCCESS REMOVE fav "+id);
            this.favMovies.remove(pos);
            this.notifyItemRemoved(pos);
            this.notifyItemRangeChanged(pos,favMovies.size());
            return true;
        }else
            Log.v("coba","FAIL REMOVE fav "+id);
        return false;
    }

    public void clearDataFav(){
        favMovies.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie,viewGroup,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, int i) {
        holder.tvTitle.setText(favMovies.get(i).getTitle());
        Log.v("coba","set adapter "+favMovies.get(i).getTitle());
        holder.tvScore.setText(favMovies.get(i).getScore());
        Glide.with(holder.itemView.getContext())
                .load(favMovies.get(i).getPhoto())
                .apply(new RequestOptions().override(75,100))
                .into(holder.imgPoster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(favMovies.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return favMovies.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvScore;
        ImageView imgPoster;

        public FavoriteViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvScore = itemView.findViewById(R.id.tv_score);
            imgPoster = itemView.findViewById(R.id.iv_profilePic);
        }

    }
}
