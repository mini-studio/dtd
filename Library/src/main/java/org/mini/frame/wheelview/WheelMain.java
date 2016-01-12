package org.mini.frame.wheelview;

import android.view.View;
import android.widget.ImageView;

import org.mini.frame.toolkit.MiniTimeUtil;

import com.mini.R;

import org.mini.frame.toolkit.MiniDisplayUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_minutes;
    private static int START_YEAR = 1000, END_YEAR = 2100;
    private int levelValue = 1;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }


    public WheelMain(View view) {
        super();
        this.view = view;
        setView(view);
    }

    public void initDateTimePicker(int year, int month, int day, int h) {
        initDateTimePicker(year, month, day, h, -1);
    }

    public void initDateTimePicker(int year, int month, int day, int h, int mi) {
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(day - 1);

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCyclic(true);// 可循环滚动
        wv_hours.setLabel("时");// 添加文字
        wv_hours.setCurrentItem(h);

        wv_minutes = (WheelView) view.findViewById(R.id.min);
        if (mi < 0) {
            wv_minutes.setVisibility(View.GONE);
        }
        else {
            wv_minutes.setVisibility(View.VISIBLE);
            wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));
            wv_minutes.setCyclic(true);// 可循环滚动
            wv_minutes.setLabel("分");// 添加文字
            wv_minutes.setCurrentItem(mi);
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize ;
        if (MiniDisplayUtil.getWindowWidth()>500){
            textSize = MiniDisplayUtil.dip2px(21);
        }else {
            textSize = MiniDisplayUtil.dip2px(15);
        }

        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_minutes.TEXT_SIZE = textSize;

    }


    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        StringBuffer sb = new StringBuffer();
        sb.append((year)).append("年")
                .append((month)).append("月")
                .append((day)).append("日  ")
                .append(hour).append(":");
        if (wv_minutes.getVisibility() == View.GONE) {
            sb.append("00");
        }
        else {
            sb.append(mi);
        }
        return sb.toString();
    }

    public String getSelectTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + START_YEAR)).append("年")
                .append((wv_month.getCurrentItem() + 1)).append("月")
                .append((wv_day.getCurrentItem() + 1)).append("日  ")
                .append(wv_hours.getCurrentItem()).append(":");
        if (wv_minutes.getVisibility() == View.GONE) {
            sb.append("00");
        }
        else {
            sb.append(wv_minutes.getCurrentItem());
        }
        return sb.toString();
    }

    public Date getSelectedDate() {
        try {
            return MiniTimeUtil.parseDateByStringTime(getSelectTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getTimeEffective() {
        Long currentTime = MiniTimeUtil.getTimeByStringTime(getCurrentTime());
        Long selectTime = MiniTimeUtil.getTimeByStringTime(getSelectTime());
        if (selectTime >= currentTime) {
            return true;
        } else {
            return false;
        }

    }
    public boolean getBirthTimeEffective() {
        Long currentTime = MiniTimeUtil.getTimeByStringTime(getCurrentTime());
        Long selectTime = MiniTimeUtil.getTimeByStringTime(getSelectTime());
        if (selectTime <= currentTime) {
            return true;
        } else {
            return false;
        }

    }



    /**
     * 作业分钟的选择器
     */
    public void initDateTimePicker() {
        wv_minutes = (WheelView) view.findViewById(R.id.view_wheel_view);
        wv_minutes.setVisibility(View.VISIBLE);
        wv_minutes.setAdapter(new NumericWheelAdapter(1, 300));
        wv_minutes.setCyclic(true);// 可循环滚动
        wv_minutes.setLabel("分钟");// 添加文字
        wv_minutes.setCurrentItem(29);
        int textSize = MiniDisplayUtil.dip2px(22);
        wv_minutes.TEXT_SIZE = textSize;

        final ImageView difficulty = (ImageView) view.findViewById(R.id.view_difficulty_image);
        final ImageView easy = (ImageView) view.findViewById(R.id.view_easy_image);
        final ImageView general = (ImageView) view.findViewById(R.id.view_general_image);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.view_difficulty_image:
                        levelValue = 3;
                        easy.setImageResource(R.drawable.home_work_easy_normal);
                        difficulty.setImageResource(R.drawable.home_work_difficulty_press);
                        general.setImageResource(R.drawable.home_work_general_normal);
                        break;

                    case R.id.view_easy_image:
                        levelValue = 1;
                        easy.setImageResource(R.drawable.home_work_easy_press);
                        difficulty.setImageResource(R.drawable.home_work_difficulty_normal);
                        general.setImageResource(R.drawable.home_work_general_normal);
                        break;

                    case R.id.view_general_image:
                        levelValue = 2;
                        easy.setImageResource(R.drawable.home_work_easy_normal);
                        difficulty.setImageResource(R.drawable.home_work_difficulty_normal);
                        general.setImageResource(R.drawable.home_work_general_press);
                        break;
                }
            }
        };
        difficulty.setOnClickListener(onClickListener);
        easy.setOnClickListener(onClickListener);
        general.setOnClickListener(onClickListener);


    }

    /**
     * 是否隐藏小时
     *
     * @param isVisibility
     */
    public void  setVisibilityHours(boolean isVisibility){
        if (isVisibility)
            wv_hours.setVisibility(View.GONE);
    }

    /**
     * 个人资料页面获取的生日日期
     *
     * @return
     */
    public String getSelectBirthdayDate() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                .append((wv_month.getCurrentItem() + 1)).append("-")
                .append((wv_day.getCurrentItem() + 1));
        return sb.toString();
    }


    /**
     * 获取难易度
     *
     * @return 1简单2一般3困难
     */
    public int getLevel() {
        return levelValue;
    }

    /**
     * 获取分钟数
     *
     * @return
     */
    public int getMinutes() {
        return wv_minutes.getCurrentItem()+1;
    }

}

