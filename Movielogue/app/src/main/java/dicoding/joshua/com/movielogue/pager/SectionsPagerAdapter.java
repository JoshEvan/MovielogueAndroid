package dicoding.joshua.com.movielogue.pager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.fragment.FavoriteFragment;
import dicoding.joshua.com.movielogue.fragment.MovieFragment;
import dicoding.joshua.com.movielogue.fragment.TVShowFragment;

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
                fragment = new MovieFragment();
                break;
            case 1:
                fragment = new TVShowFragment();
                break;
            case 2:
                fragment = new FavoriteFragment();
                break;
            default:
                fragment = new MovieFragment();
                break;
        }
        return fragment;
    }
    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
    };
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mcontext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
