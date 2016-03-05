package com.dtdinc.dtd.activity.comm;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.core.api.data.City;
import com.dtdinc.dtd.core.kit.StringHelper;

import org.mini.frame.activity.base.MiniIntent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/11/29.
 */
public class MNCityPickerActivity extends MNActivityBase{
    private ExpandableListView listView;
    private CityDataSourceAdapter dataSourceAdapter;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_city_picker);
        this.setTitle("查看一下城市快件");
        this.initView();
    }

    private void initView() {
        listView = (ExpandableListView)this.findViewById(R.id.city_list_view);
        dataSourceAdapter = new CityDataSourceAdapter();
        listView.setAdapter(dataSourceAdapter);
        dataSourceAdapter.notifyDataSetChanged();
        for (int i = 0; i < dataSourceAdapter.key.size(); i++) {
            listView.expandGroup(i);
        }
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                City city = (City)v.getTag(R.integer.infoKey);
                onSelectCity(city);
                return true;
            }
        });
    }

    private void onSelectCity(City city) {
        if (city != null) {
            MiniIntent intent = new MiniIntent();
            intent.setObject(city);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class CityDataSourceAdapter extends BaseExpandableListAdapter {

        private List<String> cityList = City.cityList;
        private List<String> key = new ArrayList<String>();
        private Map<String, List<City>> map = new HashMap<>();

        public CityDataSourceAdapter() {
            for (int index = 0; index < cityList.size(); index++) {
                String city = cityList.get(index);
                String p = StringHelper.getPinYin(city.substring(0,1)).substring(0,1).toLowerCase();
                List<City> list = map.get(p);
                if (list == null) {
                    list = new ArrayList<City>();
                    map.put(p, list);
                    key.add(p);
                }
                City mnCity = new City();
                mnCity.setName(city);
                mnCity.setCode(String.valueOf(index));
                list.add(mnCity);
            }
            Collections.sort(key);
        }

        @Override
        public int getGroupCount() {
            return key.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            List<City> list = map.get(key.get(groupPosition));
            return list.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return key.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            List<City> list = map.get(key.get(groupPosition));
            return list.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.view_city_group, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.contact_group_name);
            titleView.setText(key.get(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.view_city_item, null);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.name);
            City mnCity = map.get(key.get(groupPosition)).get(childPosition);
            String city = mnCity.getName();
            convertView.setTag(R.integer.infoKey, mnCity);
            textView.setText(city);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
