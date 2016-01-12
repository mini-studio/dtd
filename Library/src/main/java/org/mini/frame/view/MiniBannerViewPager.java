package org.mini.frame.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.mini.frame.toolkit.MiniDisplayUtil;
import com.mini.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Wuquancheng on 15/4/11.
 */
public class MiniBannerViewPager extends ViewPager {

    public interface MiniBannerViewPagerAdapter {

        int bannerImageCount();

        String bannerImageUrlAtIndex(int index);

        String bannerTextAtIndex(int index);

        DisplayImageOptions bannerLoadOptionsAtIndex(int index);

        void bannerDidSelectAtIndex(int index);

    }

    private TextView textView;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private final int SCROLL_BORDER = 2, TIMER_UPDATE = 3;
    private MiniBannerViewPagerAdapter adapter;
    private View rootView;
    private Timer timer;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        switch (msg.what) {
            case TIMER_UPDATE: {
                int count = adapter.bannerImageCount();
                int index = getCurrentItem();
                index = (index+1)%(count+2);
                setCurrentItem(index);
                break;
            }
            case SCROLL_BORDER: {
                int count = adapter.bannerImageCount();
                int current = getCurrentItem();
                if (current == 0) {
                    setCurrentItem(count,false);
                }
                else if (current == count+1) {
                    setCurrentItem(1,false);
                }
                break;
            }
        }
        }
    };

    public MiniBannerViewPager(Context context) {
        super(context);
    }

    public MiniBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRootView(View view) {
        this.rootView = view;
        this.textView = (TextView)view.findViewById(R.id.banner_text);
        this.linearLayout= (LinearLayout) view.findViewById(R.id.banner_layout);
        this.relativeLayout=(RelativeLayout)view.findViewById(R.id.banner_relayout_layout);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setPagerAdapter(MiniBannerViewPagerAdapter adapter) {
        this.stopTimer();
        this.adapter = adapter;
        if (this.adapter != null) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
            super.setAdapter(viewPagerAdapter);
            super.setOnPageChangeListener(viewPagerAdapter);
            final int count = adapter.bannerImageCount();
            if (count > 1) {
                //设置展示的红点
                linearLayout.setVisibility(VISIBLE);
                linearLayout.removeAllViews();
                for (int i = 0; i <count ; i++) {
                    ImageView imageView=new ImageView(getContext());
                    imageView.setBackgroundResource(R.drawable.school_feed_point_normal);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin=5;
                    linearLayout.addView(imageView,layoutParams);
                }

                this.setCurrentItem(1);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(TIMER_UPDATE);
                    }
                },5000,5000);
            }else{
                linearLayout.setVisibility(GONE);
            }
        }
    }


    public void setVisibility(int visibility,int height) {
        if (visibility == View.GONE) {
            height = 0;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
        ViewGroup.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        rootView.setLayoutParams(params);
        params = relativeLayout.getLayoutParams();
        params.height = height == 0? 0 : MiniDisplayUtil.dip2px(20);
        this.relativeLayout.setLayoutParams(params);
        super.setVisibility(visibility);
    }


    public class ViewPagerAdapter extends PagerAdapter implements OnPageChangeListener {

        private Map<Integer,MiniImageView> cache = new HashMap<Integer,MiniImageView>();

        public String urlAtIndex(int index) {
            return adapter.bannerImageUrlAtIndex(index);
        }

        public void onPageSelected(int var1) {
            int count = adapter.bannerImageCount();
            if (count > 1) {
                handler.sendEmptyMessageDelayed(SCROLL_BORDER,500);
            }
            int index = indexForPosition(var1);
            for (int i = 0; i <linearLayout.getChildCount() ; i++) {
                ImageView imageView= (ImageView) linearLayout.getChildAt(i);
                if (i==index){
                    imageView.setBackgroundResource(R.drawable.school_feed_point_pressed);
                }else {
                    imageView.setBackgroundResource(R.drawable.school_feed_point_normal);
                }
            }
            String title = adapter.bannerTextAtIndex(index);
            textView.setText(title);
        }

        public  void onPageScrolled(int var1, float var2, int var3) {

        }

        public void onPageScrollStateChanged(int var1) {

        }

        public int getCount() {
            int count = 0;
            if (adapter != null) {
                count = adapter.bannerImageCount();
                if (count > 1) {
                    return count + 2;
                }
            }
            return count;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            int index = indexForPosition(position);
            int count = adapter.bannerImageCount();
            if (count==1){
                String title = adapter.bannerTextAtIndex(index);
                textView.setText(title);
            }
            MiniImageView imageView = cache.get(position);
            if (imageView == null) {
                imageView = new MiniImageView(container.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                cache.put(position, imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.bannerDidSelectAtIndex((Integer) ((MiniImageView) v).getInfo());
                    }
                });
            }
            Object parent = imageView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup)parent).removeView(imageView);
            }
            imageView.setInfo(index);
            container.addView(imageView);
            imageView.setImageUrl(urlAtIndex(index),MiniImageView.squareViewPagerImageOption());
            return imageView;
        }

        private int indexForPosition(int position) {
            int index = position;
            if (adapter.bannerImageCount() > 1) {
                if ( position == 0 ) {
                    index = adapter.bannerImageCount()-1;
                }
                else if ( position == adapter.bannerImageCount() + 1) {
                    index = 0;
                }
                else {
                    index = position - 1;
                }
            }
            return index;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            cache.remove(position);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==(arg1);
        }
    }
}
