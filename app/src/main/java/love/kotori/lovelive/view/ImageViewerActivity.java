package love.kotori.lovelive.view;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;

import love.kotori.lovelive.fragment.ImageViewerFragment;
import love.kotori.lovelive.R;
import love.kotori.lovelive.model.Image;
import love.kotori.lovelive.util.SnackbarUtil;
import love.kotori.lovelive.widget.ExtendedViewPager;

import io.realm.Realm;
import io.realm.RealmResults;

public class ImageViewerActivity extends AppCompatActivity {

    Realm realm;
    private RealmResults<Image> images;
    private int currentIndex;
    private ExtendedViewPager mViewPager;
    private int type;
    private String url;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpager);
        // http://stackoverflow.com/questions/28144657/android-error-attempt-to-invoke-virtual-method-void-android-app-actionbar-on
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

        currentIndex = getIntent().getIntExtra("CURRENT_INDEX", 0);
        type = getIntent().getIntExtra("TYPE", 0);

        realm = Realm.getInstance(this);
        images = realm.where(Image.class).equalTo("type", type).findAll();

        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_viewer_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                File direct = new File(Environment.getExternalStorageDirectory()
                        + "/love.kotori.lovelive");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(title)
                        .setDescription("Something useful. No, really.")
                        .setDestinationInExternalPublicDir("/love.kotori.lovelive", title + ".jpg");
                mgr.enqueue(request);
                SnackbarUtil.showLong(this.mViewPager, "插画已保存至存储卡", "red");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // http://stackoverflow.com/questions/28525462/how-to-prevent-setdisplayhomeasupenabled-from-re-creating-the-parent-activity
    @Override
    public Intent getSupportParentActivityIntent() {
    /*String from = getIntent().getExtras().getString("from");
    Intent newIntent = null;
    if(from.equals("MAIN")){
        newIntent = new Intent(this, MainActivity.class);
    }else if(from.equals("FAV")){
        newIntent = new Intent(this, FavoriteActivity.class);
    }

    return newIntent;*/
        finish();
        return null;
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
            // http://stackoverflow.com/questions/15218904/android-fragmentpageradapter-getitem-method-called-twice-on-first-time
            //position = mViewPager.getCurrentItem();
            url = images.get(position).getUrl();
            title = images.get(position).getTitle();
            return ImageViewerFragment.newInstance(url, title);
        }


    }


}
