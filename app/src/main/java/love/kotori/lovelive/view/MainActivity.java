package love.kotori.lovelive.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.astuetz.PagerSlidingTabStrip;

import love.kotori.lovelive.domain.Api;
import love.kotori.lovelive.fragment.PageFragment;
import love.kotori.lovelive.R;
import love.kotori.lovelive.util.SnackbarUtil;

public class MainActivity extends AppCompatActivity {

    private ViewPager mainViewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private long exitTime = 0;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout mContainer;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainViewPager = (ViewPager) findViewById(R.id.main_viewPager);
        //mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab_indicator);
        pagerSlidingTabStrip.setViewPager(mainViewPager);

        //指示条颜色
        pagerSlidingTabStrip.setIndicatorColor(R.color.blue_500);
        //指示条高度
        pagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));


        mainViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Api.actionBarColors[position])));
                Window window = getWindow();
                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // finally change the color
                window.setStatusBarColor(Color.parseColor(Api.statusBarColors[position]));
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"" + Api.titleColors[position] + "\">" + getString(R.string.app_name) + "</font>"));
            }
        });
    }


    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = Api.tags;

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            //if(fragments.size()<=position) fragments.add(PageFragment.newInstance(position));
            return PageFragment.newInstance(position);
            //return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.fab)
    public void fabOnClick() {
        PagerAdapter adapter = mainViewPager.getAdapter();
        PageFragment fragment = (PageFragment) adapter.instantiateItem(mainViewPager, mainViewPager.getCurrentItem());
        fragment.onRefresh();
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if (KeyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                SnackbarUtil.showLong(mContainer, getResources().getString(R.string.confirm_exit), "red");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(KeyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
