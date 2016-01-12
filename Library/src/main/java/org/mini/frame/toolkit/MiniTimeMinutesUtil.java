package org.mini.frame.toolkit;

/**
 * Created by gassion on 15/6/15.
 */
public class MiniTimeMinutesUtil {


    private static int perHour = 60;

    public static int getTimeMinutes(int costTime) {
        int hour = costTime / perHour;
        int minute = costTime % perHour;
        return hour * perHour + minute;
    }
}
