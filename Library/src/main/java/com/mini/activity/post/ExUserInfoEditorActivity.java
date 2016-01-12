package com.mini.activity.post;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.activity.comm.MNCityPickerActivity;
import com.mini.core.api.data.City;
import com.mini.core.model.PostUserInfo;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.toolkit.MiniTimePicker;
import org.mini.frame.toolkit.MiniTimeUtil;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class ExUserInfoEditorActivity extends MNActivityBase {

    private PostUserInfo userInfo;

    private TextView textViewCity;
    private TextView textViewAddress;
    private TextView textViewTime;

    private EditText editTextName;
    private EditText editTextMobile;

    private ExEditorActivity.EditorObject addressObject = new ExEditorActivity.EditorObject();

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_user_info_editor);
        Map<String,Object> map = (Map)this.getIntentObject();
        if (map != null) {
            String title = (String)map.get("title");
            this.setTitle(title);
            this.userInfo = (PostUserInfo)map.get("userInfo");
        }
        if (this.userInfo == null) {
            this.userInfo = new PostUserInfo();
        }
        this.initView();
        this.setNaviRightStringAction("保存");
    }

    public void initView() {
        this.textViewCity = (TextView)this.findViewById(R.id.text_view_city);
        this.textViewAddress = (TextView)this.findViewById(R.id.text_view_address);
        this.textViewTime = (TextView)this.findViewById(R.id.text_view_time);
        this.editTextName = (EditText)this.findViewById(R.id.edit_text_name);
        this.editTextMobile = (EditText)this.findViewById(R.id.edit_text_mobile);

        this.findViewById(R.id.layout_address, this);
        this.findViewById(R.id.layout_city, this);
        this.findViewById(R.id.layout_time, this);

        this.editTextName.setText(this.userInfo.getName());
        this.editTextMobile.setText(this.userInfo.getMobile());
        this.textViewCity.setText(this.userInfo.getCityName());
        this.textViewAddress.setText(this.userInfo.getAddress());
        this.textViewTime.setText(this.userInfo.getTime());

    }

    public void onNaviRightAction() {
        this.userInfo.setName(this.editTextName.getText().toString());
        this.userInfo.setMobile(this.editTextMobile.getText().toString());
        this.userInfo.setAddress(this.textViewAddress.getText().toString());
        this.userInfo.setTime(this.textViewTime.getText().toString());
        MiniIntent intent = new MiniIntent();
        intent.setObject(this.userInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Action(R.id.layout_address)
    public void pickAddress() {
        startActivityForResult(ExEditorActivity.class, this.addressObject, 300);
    }

    @ActivityResult(300)
    public void confirmAddress(int requestCode, int resultCode, Intent intent) {
        if (RESULT_OK == resultCode) {
            this.textViewAddress.setText(this.addressObject.getContent());
        }
    }


    @Action(R.id.layout_city)
    public void pickCity() {
        startActivityForResult(MNCityPickerActivity.class, null, 200);
    }

    @ActivityResult(200)
    public void selectCity(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            City object = (City)MiniIntent.getObjectFromIntent(intent);
            this.userInfo.setCity(object);
            this.textViewCity.setText(object.getName());
        }
    }

    @Action(R.id.layout_time)
    public void pickTime() {
        new MiniTimePicker(this, new MiniTimePicker.MiniTimePickerListener() {
            @Override
            public Date defaultData() {
                return null;
            }

            @Override
            public boolean didSelectDate(Date date) {
                userInfo.setDate(date);
                textViewTime.setText(userInfo.getTime());
                return true;
            }

            @Override
            public String format() {
                return "yyyyMMddHHmi";
            }
        }).show();
    }
}
