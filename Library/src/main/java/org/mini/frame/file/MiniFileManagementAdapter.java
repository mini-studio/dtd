package org.mini.frame.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini.R;

import java.util.List;
import java.util.Map;

public class MiniFileManagementAdapter extends BaseAdapter {

  private LayoutInflater inflater;
  private List<Map<String, Object>> data;
  public static String fileIcon = "img";
  public static String fileName = "name";
  public static String filePath = "path";


  public MiniFileManagementAdapter() {

  }

  public void setContext(Context context) {
    inflater = LayoutInflater.from(context);
  }

  public void setData(List<Map<String, Object>> data) {
    this.data = data;
  }


  @Override
  public int getCount() {
    if (data != null) {
      return data.size();
    } else {
      return 0;
    }
  }

  @Override
  public Object getItem(int position) {
    if (data != null) {
      return data.get(position);
    } else {
      return null;
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.activity_file_management_item_adapter, null);
    }
    HolderView holderView = (HolderView) convertView.getTag();
    if (holderView == null) {
      holderView = new HolderView(convertView);
      convertView.setTag(holderView);
    }
    Map<String, Object> stringObjectMap = data.get(position);
    holderView.setData(stringObjectMap);
    return convertView;
  }


  private class HolderView {
    TextView tvFileName;// 文件名称
    ImageView ivFileIcon;// 文件图标

    public HolderView(View convertView) {
      tvFileName = (TextView) convertView.findViewById(R.id.file_management_item_tv_name);
      ivFileIcon = (ImageView) convertView.findViewById(R.id.file_management_item_iv_icon);
    }

    public void setData(Map<String, Object> stringObjectMap) {
      if (stringObjectMap.get(fileIcon) instanceof Integer) {
        ivFileIcon.setBackgroundResource((int) stringObjectMap.get(fileIcon));
      } else {
        ivFileIcon.setBackgroundResource(R.drawable.school_news_details_questionnaire_main_loading);
      }
      if (stringObjectMap.get(fileName) instanceof String) {
        tvFileName.setText((String) stringObjectMap.get(fileName));
      } else {
        tvFileName.setText("");
      }
    }

  }



}
