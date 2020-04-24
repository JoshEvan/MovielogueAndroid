package dicoding.joshua.com.movielogueconsumer.pager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import dicoding.joshua.com.movielogueconsumer.R;
import dicoding.joshua.com.movielogueconsumer.fragment.FavoriteMovieFragment;
import dicoding.joshua.com.movielogueconsumer.fragment.FavoriteTVShowFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mcontext;
    public SectionsPagerAdapter(Context context, FragmentManager fm){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mcontext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new FavoriteMovieFragment();

                break;
            case 1:
                fragment = new FavoriteTVShowFragment();
                break;
            default:
                fragment = new FavoriteMovieFragment();
                break;
        }
        return fragment;
    }
    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
    };
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mcontext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
