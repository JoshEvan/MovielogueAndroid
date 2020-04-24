package dicoding.joshua.com.movielogueconsumer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;
    private String photo;
    private String title;
    private String description;
    private String genres;
    private String score;
    private String duration;
    private String fav_state = "false"; // default value

    protected Movie(Parcel in) {
        photo = in.readString();
        title = in.readString();
        description = in.readString();
        genres = in.readString();
        score = in.readString();
        duration = in.readString();
        id = in.readString();
        fav_state = in.readString();
    }

    public Movie() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(genres);
        dest.writeString(score);
        dest.writeString(duration);
        dest.writeString(id);
        dest.writeString(fav_state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFav_state() {
        return fav_state;
    }

    public void setFav_state(String fav_state) {
        this.fav_state = fav_state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
