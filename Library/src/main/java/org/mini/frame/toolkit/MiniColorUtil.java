package org.mini.frame.toolkit;

import android.graphics.Color;

import java.util.Random;

public class MiniColorUtil {

  public static int getColor(int position) {
    String colors[] =
        {"#48da9b", "#ffb45c", "#4ee0fa", "#ff888e", "#fee55a", "#f68cff", "#a6f058", "#ff8a61", "#af9fff", "#5ea2ff",
            "#ff83bd", "#33d6d0", "#b38c7f", "#ffb9d0", "#ffcd62", "#d28bfe", "#a69fff", "#4fd4ff"};
    if (position >= colors.length) {
      Random random = new Random();
      return Color.parseColor(colors[(random.nextInt(colors.length))]);
    } else {
      return Color.parseColor(colors[position]);
    }
  }
}
