package love.kotori.lovelive.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import love.kotori.lovelive.fragment.ImageViewerFragment;
import love.kotori.lovelive.R;
import love.kotori.lovelive.model.Image;
import love.kotori.lovelive.widget.ExtendedViewPager;

import io.realm.Realm;
import io.realm.RealmResults;

public class ImageViewerActivity extends AppCompatActivity {

    Realm realm;
    private RealmResults<Image> images;
    private int currentIndex;
    private ExtendedViewPager mViewPager;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpager);
        mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

        currentIndex = getIntent().getIntExtra("CURRENT_INDEX", 0);
        type = getIntent().getIntExtra("TYPE", 0);

        realm = Realm.getInstance(this);
        images = realm.where(Image.class).equalTo("type", type).findAll();

        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(currentIndex);
    }

    public void onPause() {
        super.onPause();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImageViewerFragment.newInstance(
                    images.get(position).getUrl());
        }

    }


}
