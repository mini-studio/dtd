package com.dtdinc.dtd.activity.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dtdinc.dtd.core.api.data.User;
import com.mini.R;

import org.mini.frame.toolkit.MiniSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.dtdinc.dtd.app.CESystem.WHO;


/**
 * Created by Wuquancheng on 15/4/6.
 */
public class MNMainActivity extends Activity {

    private Handler handler = null;

    private ImageView mSplashImage = null;

    private ViewPager mViewPager;

    private List<View> imageViews = new ArrayList<View>(0);

    private boolean acted = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        mSplashImage = (ImageView) findViewById(R.id.splash_image);
        mSplashImage.setImageResource(R.drawable.default_image);
        mViewPager = (ViewPager)findViewById(R.id.splash_viewpager);
        handler = new Handler();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mSplashImage != null) {
            mSplashImage.setImageResource(0);
        }
    }

    private void dispatch() {
        int showSplash = MiniSharedPreferences.instance().getInt("first_run",0);
        if (showSplash == 0) {
            mSplashImage.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            initImageViews();
            mViewPager.setAdapter(new ViewPagerAdapter());
            mViewPager.setOnPageChangeListener(new SplashPageChangeListener());
        }
        else {
            if (acted) {
                return;
            }
            acted = true;
            User user = WHO();
            Intent intent;
            if (user == null) {
                //intent = new Intent(MNMainActivity.this, MNSigninActivity.class);
                intent = new Intent(MNMainActivity.this, MNMainTabActivity.class);
            } else {
                intent = new Intent(MNMainActivity.this, MNMainTabActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    public void onStart() {
        super.onStart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dispatch();
            }
        }, 2000);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initImageViews() {
        if (imageViews.size() > 0) {
            return;
        }
        int[] pics = new int[]{R.drawable.one, R.drawable.two, R.drawable.three};
        String[] colors = new String[] {"#0c1623","#038dc0","#e8b013"};
        for (int i = 0; i < pics.length; i++) {
            View page = LayoutInflater.from(this).inflate(R.layout.view_splash_pager, null);
            page.setBackgroundColor(Color.parseColor(colors[i]));
            ImageView pageBg = (ImageView) (page.findViewById(R.id.introduce_content_bg));
            pageBg.setBackgroundResource(pics[i]);
            if (i == pics.length - 1) {
                View view = page.findViewById(R.id.login_main_login_btn);
                view.setVisibility(View.VISIBLE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MiniSharedPreferences.instance().setInt("first_run",1);
                        dispatch();
                    }
                });
            }
            imageViews.add(page);
        }
    }

    class SplashPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

    class ViewPagerAdapter extends PagerAdapter {
        public ViewPagerAdapter() {
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);

        }
    }
}
