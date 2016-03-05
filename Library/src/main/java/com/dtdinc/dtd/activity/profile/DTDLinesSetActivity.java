package com.dtdinc.dtd.activity.profile;

import android.content.Intent;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.activity.comm.MNCityPickerActivity;
import com.dtdinc.dtd.core.api.data.City;
import com.dtdinc.dtd.core.exception.CEDataException;
import com.dtdinc.dtd.core.model.LineInfo;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class DTDLinesSetActivity extends MNActivityBase {

    LineInfo info;

    private TextView from_view;
    private TextView to_view;

    private City fromCity;
    private City toCity;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_lines_set);
        this.info = (LineInfo)getIntentObject();
        this.setTitle(this.info.getName());
        this.initView();
        this.setNaviRightStringAction("保存");
    }

    private void initView() {
        this.from_view = (TextView)this.findViewById(R.id.from_view);
        this.to_view = (TextView)this.findViewById(R.id.to_view);
        this.findViewById(R.id.from_layout, this);
        this.findViewById(R.id.to_layout, this);
    }

    public void afterResume() {
        loadData();
    }

    private void loadData() {
    }

    /**
     * 设置出发城市
     */
    @Action(R.id.from_layout)
    public void actionForFromCity() {
        startActivityForResult(MNCityPickerActivity.class, null, 200);
    }

    @ActivityResult(200)
    public void onSelectFromCity(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            City city = (City)MiniIntent.getObjectFromIntent(intent);
            this.from_view.setText(city.getName());
            this.fromCity = city;
        }
    }

    /**
     * 设置到达城市
     */
    @Action(R.id.to_layout)
    public void actionForToCity() {
        startActivityForResult(MNCityPickerActivity.class, null, 300);
    }

    @ActivityResult(300)
    public void onSelectToCity(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            City city = (City)MiniIntent.getObjectFromIntent(intent);
            this.to_view.setText(city.getName());
            this.toCity = city;
        }
    }

    public void onNaviRightAction() {
        if (this.fromCity == null ) {
            showMessage("请选择出发城市");
            return;
        }
        if (this.toCity == null) {
            showMessage("请选择到达城市");
            return;
        }

        api.line(this.fromCity.getName(), this.toCity.getName(), new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                if ("ok".equals(data)) {
                    info.setFrom(fromCity);
                    info.setTo(toCity);
                    MiniIntent intent = new MiniIntent();
                    intent.setObject(info);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if ("already_set".equals(data)){
                    showMessage("已经设置过此线路");
                }
                else {
                    showMessage("请检查手机号");
                }
            }
        });
    }

}
