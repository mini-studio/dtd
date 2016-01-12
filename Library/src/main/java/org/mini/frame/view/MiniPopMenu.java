package org.mini.frame.view;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mini.R;


/**
 * Created by Wuquancheng on 15/5/1.
 */
public class MiniPopMenu{

    public interface MiniPopMenuAdapter {
        int getCount();
        String getItem(int position);
        int selectedIndex();
        void onDismiss();
        void didSelectAtIndex(int index);
    }

    private PopupWindow mPopupWindow;
    private ListView listView;
    private Context context;
    private View parent;

    public MiniPopMenuAdapter menuAdapter;

    public MiniPopMenu(Context context, View parent) {
        this.context = context;
        this.parent = parent;
    }

    public void setMenuAdapter(MiniPopMenuAdapter menuAdapter) {
        this.menuAdapter = menuAdapter;
    }

    public void show() {
        if (mPopupWindow == null) {
            LayoutInflater lay = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view  = lay.inflate(R.layout.popuwindow_class, null);
            listView = (ListView) view.findViewById(R.id.pop_listView);
            listView.setAdapter(new ListViewAdapter(context));
            mPopupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

            mPopupWindow.setAnimationStyle(R.style.popupAnimationClass);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            ColorDrawable dw = new ColorDrawable(0000000000);
            mPopupWindow.setBackgroundDrawable(dw);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    menuAdapter.onDismiss();
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    menuAdapter.didSelectAtIndex(position);
                    mPopupWindow.dismiss();
                }
            });
        }
        mPopupWindow.showAsDropDown(parent, 0, 0);
        mPopupWindow.update();
    }



    class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ListViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return menuAdapter.getCount();
        }

        @Override
        public String getItem(int position) {
            return menuAdapter.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                //convertView = inflater.inflate(R.layout.class_list, null);
            }
            HolderView holderView = (HolderView) convertView.getTag();
            if (holderView == null) {
                holderView = new HolderView(convertView);
                convertView.setTag(holderView);
            }
            String object = getItem(position);
            if (object != null) {
                holderView.setData(object, position == menuAdapter.selectedIndex());
            }
            return convertView;
        }

        private class HolderView {
            TextView className;
            ImageView accessory;

            public HolderView(View convertView) {
                //className = (TextView) convertView.findViewById(R.id.class_list_name);
                //accessory = (ImageView) convertView.findViewById(R.id.class_list_img);
            }

            public void setData(String object, boolean selected) {
                className.setText(object);
                if (selected) {
                    accessory.setVisibility(View.VISIBLE);
                } else {
                    accessory.setVisibility(View.GONE);
                }
            }
        }
    }

}
