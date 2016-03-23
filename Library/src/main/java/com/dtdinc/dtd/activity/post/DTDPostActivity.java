package com.dtdinc.dtd.activity.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.activity.comm.MNCityPickerActivity;
import com.dtdinc.dtd.app.CESystem;
import com.dtdinc.dtd.core.api.data.City;
import com.dtdinc.dtd.core.api.data.User;
import com.dtdinc.dtd.core.exception.CEDataException;
import com.dtdinc.dtd.core.model.PostPackageInfo;
import com.dtdinc.dtd.core.model.PostUserInfo;

import org.mini.frame.activity.base.MiniIntent;
import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.view.MiniActionSheetDialog;
import org.mini.frame.view.MiniCustomDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 发件页面
 */
public class DTDPostActivity extends MNActivityBase {

    private TextView packageNameTextView;
    private TextView packageWeightTextView;

    private PostPackageInfo postPackageInfo = new PostPackageInfo();

    private City city = null;

    private TextView senderInfoTitleView;
    private View senderInfoLayoutView;
    private TextView senderInfoNameView;
    private TextView senderInfoMobileView;
    private TextView senderInfoAddressView;

    private TextView recipientInfoTitleView;
    private View     recipientInfoLayoutView;
    private TextView recipientInfoNameView;
    private TextView recipientInfoMobileView;
    private TextView recipientInfoAddressView;
    private TextView add_image_text_view;
    private TextView package_price_text_view;



    private static final int REQ_CODE_FOR_CITY = 200;

    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_post);
        //this.setTitle("发件");
        this.setTitleMidImage(R.drawable.asf_03);
        city = CESystem.instance().getCurrentCity();
        this.setLeftTitleImage(R.drawable.location);
        this.setNaviLeftTitle(city.getName());
        this.initView();

    }

    private void initView() {
        this.findViewById(R.id.poster_info, this);
        this.findViewById(R.id.recipients_info, this);
        this.findViewById(R.id.package_name, this);
        this.findViewById(R.id.package_weight, this);
        this.findViewById(R.id.next_button, this);
        this.findViewById(R.id.add_image_layout, this);
        this.packageNameTextView = (TextView)this.findViewById(R.id.package_name_text_view);
        this.packageWeightTextView = (TextView)this.findViewById(R.id.package_weight_text_view);

        senderInfoTitleView = (TextView)this.findViewById(R.id.sender_info_title);
        senderInfoLayoutView = this.findViewById(R.id.sender_info_layout);
        senderInfoNameView = (TextView)this.findViewById(R.id.sender_info_name);
        senderInfoMobileView = (TextView)this.findViewById(R.id.sender_info_mobile);
        senderInfoAddressView = (TextView)this.findViewById(R.id.sender_info_address);

        recipientInfoTitleView = (TextView)this.findViewById(R.id.recipient_info_title);
        recipientInfoLayoutView = this.findViewById(R.id.recipient_info_layout);
        recipientInfoNameView = (TextView)this.findViewById(R.id.recipient_info_name);
        recipientInfoMobileView =(TextView)this.findViewById(R.id.recipient_info_mobile) ;
        recipientInfoAddressView =(TextView)this.findViewById(R.id.recipient_info_address) ;

        add_image_text_view = (TextView)this.findViewById(R.id.add_image_text_view);
        add_image_text_view.setText("未添加");
        this.package_price_text_view = (TextView)this.findViewById(R.id.package_price_text_view);
    }

    @Action(R.id.add_image_layout)
    public void actionAddImage() {
        this.pickImage(new MiniPickImageListener() {
            @Override
            public void onPickImage(String file) {
                postPackageInfo.setImagePath(file);
                add_image_text_view.setText("已添加");
            }
        });
    }

    @Action(R.id.poster_info)
    public void actionPostInfo(){
        Map map = new HashMap();
        map.put("title", "发件人信息");
        map.put("userInfo", this.postPackageInfo.getPosterInfo());
        startActivityForResult(DTDUserInfoEditorActivity.class, map, 300);
    }

    @ActivityResult(300)
    public void confirmPostInfo(int requestCode, int resultCode, Intent intent) {
        if (RESULT_OK == resultCode) {
            PostUserInfo postUserInfo = (PostUserInfo)MiniIntent.getObjectFromIntent(intent);
            if (postUserInfo != null && !postUserInfo.isEmpty()) {
                this.postPackageInfo.setPosterInfo(postUserInfo);
                this.senderInfoLayoutView.setVisibility(View.VISIBLE);
                this.senderInfoTitleView.setVisibility(View.GONE);
                this.senderInfoNameView.setText("发货人:" + postUserInfo.getName());
                this.senderInfoMobileView.setText(postUserInfo.getMobile());
                this.senderInfoAddressView.setText("发货地址:" + postUserInfo.getFullAddress());
            }
            else {
                this.senderInfoLayoutView.setVisibility(View.GONE);
                this.senderInfoTitleView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Action(R.id.recipients_info)
    public void actionRecipientsInfo(){
        Map map = new HashMap();
        map.put("title", "收件人信息");
        map.put("userInfo", this.postPackageInfo.getRecipientsInfo());
        startActivityForResult(DTDUserInfoEditorActivity.class, map, 500);
    }

    @ActivityResult(500)
    public void confirmRecipientsInfo(int requestCode, int resultCode, Intent intent) {
        if (RESULT_OK == resultCode) {
            PostUserInfo postUserInfo = (PostUserInfo)MiniIntent.getObjectFromIntent(intent);
            this.postPackageInfo.setRecipientsInfo((PostUserInfo)MiniIntent.getObjectFromIntent(intent));
            if (postUserInfo != null && !postUserInfo.isEmpty()) {
                this.postPackageInfo.setRecipientsInfo(postUserInfo);
                this.recipientInfoLayoutView.setVisibility(View.VISIBLE);
                this.recipientInfoTitleView.setVisibility(View.GONE);
                this.recipientInfoNameView.setText("收货人:" + postUserInfo.getName());
                this.recipientInfoMobileView.setText(postUserInfo.getMobile());
                this.recipientInfoAddressView.setText("收货地址:" + postUserInfo.getFullAddress());
            }
            else {
                this.recipientInfoLayoutView.setVisibility(View.GONE);
                this.recipientInfoTitleView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Action(R.id.package_name)
    public void actionPackageName(){
        final List<String> subjects = Arrays.asList(new String[]{"文件","服饰","食品","日用品","数码产品","其他"});
        new MiniActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true)
                .addSheetItem(subjects, new MiniActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        packageNameTextView.setText(subjects.get(which));
                        postPackageInfo.setPackageName(subjects.get(which));
                    }
                }).show();
    }

    @Action(R.id.package_weight)
    public void actionPackageWeight(){
        final List<String> subjects = Arrays.asList(new String[]{"1kg","2kg","3kg","4kg","5kg"});
        new MiniActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true)
                .addSheetItem(subjects, new MiniActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        String weight = subjects.get(which);
                        setWeight(weight);
                    }
                }).show();
    }

    private void setWeight(String weight) {
        packageWeightTextView.setText(weight);
        postPackageInfo.setPackageWeight(weight);
        postPackageInfo.setPrice(null);
        api.getPrice(weight, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                if (error == null) {
                    package_price_text_view.setText(data);
                    postPackageInfo.setPrice(data);
                }
            }
        });
    }

    protected void onNaviLeftButtonAction() {
        startActivityForResult(MNCityPickerActivity.class, null, REQ_CODE_FOR_CITY);
    }

    @ActivityResult(REQ_CODE_FOR_CITY)
    public void onPickCity(int requestCode, int resultCode, Intent intent) {
        Object object = MiniIntent.getObjectFromIntent(intent);
        if (object != null && object instanceof City) {
            this.setCity((City)object);
        }
    }

    private void setCity(City city) {
        this.city = city;
        this.resetNaviLeftTitle(city.getName());
        this.postPackageInfo.getPosterInfo().setCity(city);
        this.postPackageInfo.getRecipientsInfo().setCity(city);
    }

    /**
     * 清除数据
     * 当订单完成创建后，应该清除数据
     */
    private void clearData() {
        this.postPackageInfo = new PostPackageInfo();
        this.senderInfoLayoutView.setVisibility(View.GONE);
        this.senderInfoTitleView.setVisibility(View.VISIBLE);
        this.recipientInfoLayoutView.setVisibility(View.GONE);
        this.recipientInfoTitleView.setVisibility(View.VISIBLE);
        this.packageWeightTextView.setText("");
        this.packageNameTextView.setText("");
        this.package_price_text_view.setText("");
    }

    /**
     * 创建发件订单
     */
    @Action(R.id.next_button)
    private void publishOrder() {
        if (postPackageInfo == null || !postPackageInfo.isCompletive()) {
            showMessage("请将订单信息填写完整");
            return;
        }
        MiniCustomDialog.show(this, "提示", "此笔订单需要支付" + this.postPackageInfo.getPrice() + ", 您确定要发送吗?", "取消", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                User user = WHO();
                api.publishOrder(user.getPhone(), postPackageInfo, new MiniDataListener<String>() {
                    @Override
                    public void onResponse(String data, CEDataException error) {
                        if (error == null) {
                            if (!"db_error".equals(data) && !"phone_error".equals(data) && !"session_error".equals(data)) {
                                showMessage("发单成功，请耐心等待接单");
                                clearData();
                            } else {
                                showMessage("发单失败请重试");
                            }
                        } else {
                            showError(error);
                        }
                    }
                });
            }
        });
    }
}
