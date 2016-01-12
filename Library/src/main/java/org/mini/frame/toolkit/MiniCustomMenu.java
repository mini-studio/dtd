package org.mini.frame.toolkit;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mini.R;

/**
 * Created by admin on 2015/6/17.
 */
public class MiniCustomMenu extends Dialog {
    public MiniCustomMenu(Context context) {
        super(context);
    }

    public MiniCustomMenu(Context context, int theme) {
        super(context, theme);
    }


    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
        private Context context;
        private String title;
        private MiniCustomMenu dialog;
        private onItemClickListener mListener;
        private Integer ids[];
        private String[] names;

        public Builder(Context context) {
            this.context = context;
        }

        public void setItemListener(onItemClickListener listener) {
            this.mListener = listener;
        }

        /**
         * 使用资源设置对话框标题信息
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 使用字符串设置对话框标题信息
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public void setItems(Integer[] ids, String[] names) {
            this.ids = ids;
            this.names = names;
        }

        /**
         * 创建自定义的对话框
         */
        public MiniCustomMenu create(Boolean... flag) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义的对话框主题

            dialog = new MiniCustomMenu(context, R.style.customMenu);
            dialog.setCanceledOnTouchOutside(true);
            View layout = inflater.inflate(R.layout.view_custom_menu, null);

            View customView = layout.findViewById(R.id.custom_menu_view);
                customView.setBackgroundColor(context.getResources().getColor(R.color.common_green_name));

            // 设置对话框标题
            TextView customTitle = (TextView) layout.findViewById(R.id.custom_menu_title);
                customTitle.setTextColor(context.getResources().getColor(R.color.common_green_name));
            customTitle.setText(title);

            MiniRoundCornerListView listView = (MiniRoundCornerListView) layout.findViewById(R.id.custom_menu_list);
            listView.setRoundCorner(2);
            listView.setPressColor(context.getResources().getColor(R.color.top_list_item_press));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListener != null && ids != null) {
                        mListener.onItemClick(ids[position]);
                    }
                }
            });
            ListAdapter adapter = new ListAdapter();
            listView.setAdapter(adapter);

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            lp.width = MiniUtils.getScreenWidth(context) - MiniUtils.getScreenWidth(context) / 6;

            dialogWindow.setAttributes(lp);
            dialog.setContentView(layout);

            return dialog;
        }

        class ListAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                if (ids != null) {
                    return ids.length;
                } else {
                    return 0;
                }
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_menu_item, null);
                TextView textView = (TextView) convertView.findViewById(R.id.custom_menu_list_item_text);
                if (names != null) {
                    String value = names[position];
                    textView.setText(value + "");
                }
                return convertView;
            }
        }

    }

    public interface onItemClickListener {
        public void onItemClick(int id);
    }

}
