package com.dtdinc.dtd.activity.deliver;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.DTDPayActivity;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.activity.main.MNSigninActivity;
import com.dtdinc.dtd.activity.profile.DTDBecomeDispatcherActivity;
import com.dtdinc.dtd.activity.profile.DTDProfileActivity;
import com.dtdinc.dtd.core.api.data.PackageDetailWrapper;
import com.dtdinc.dtd.core.api.data.PackageInfo;
import com.dtdinc.dtd.core.api.data.User;
import com.dtdinc.dtd.core.api.engine.CEApi;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.annotation.ActivityResult;
import org.mini.frame.http.request.MiniDataListener;
import org.mini.frame.toolkit.MiniSystemHelper;
import org.mini.frame.view.MiniCustomDialog;
import org.mini.frame.view.MiniInputDialog;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/12/6.
 * 快件详情
 */
public class DTDPackageDetailActivity extends MNActivityBase {

    PackageInfo packageInfo;
    TextView message_view;
    View message_view_layout;
    EditText inputCodeView;

    int from = 1;

    private final static int REQ_LOGIN = 10000;

    private CEApi api = new CEApi();

    /**取件时间*/
    private TextView get_time_view;
    /**距离当前位置的距离*/
    private TextView get_dest_view;
    /**送件费*/
    private TextView income_view;
    /**发货人*/
    private TextView sender_info_name;
    /**发货人手机号码*/
    private TextView sender_info_mobile;
    /**发货人地址*/
    private TextView sender_info_address;
    /**收货人*/
    private TextView recipient_info_name;
    /**收货人手机号码*/
    private TextView recipient_info_mobile;
    /**收货人地址*/
    private TextView recipient_info_address;
    /**物品名称*/
    private TextView package_name;
    /**物品重量*/
    private TextView package_weight;
    /**送货时间*/
    private TextView send_time_view;
    /**订单号*/
    private TextView order_no;
    /**邮递方式*/
    private TextView deliver_info;

    private int orderType = 0;

    @Override
    protected void loadView() {
        super.setContentView(R.layout.activity_package_detail);
        this.packageInfo = (PackageInfo)getIntentObject();
        this.from = this.packageInfo.getFrom();
        this.orderType = this.packageInfo.getOrderType();
        this.setTitle("快件详情");
        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.button_contact, this);
        this.findViewById(R.id.button_task, this);
        this.findViewById(R.id.button_pay, this);
        this.get_time_view = (TextView)this.findViewById(R.id.get_time_view, this);
        this.get_dest_view = (TextView)this.findViewById(R.id.get_dest_view, this);
        this.income_view = (TextView)this.findViewById(R.id.income_view, this);
        this.sender_info_name = (TextView)this.findViewById(R.id.sender_info_name, this);
        this.sender_info_mobile = (TextView)this.findViewById(R.id.sender_info_mobile, this);
        this.sender_info_address =(TextView)this.findViewById(R.id.sender_info_address, this);
        this.recipient_info_name = (TextView)this.findViewById(R.id.recipient_info_name, this);
        this.recipient_info_mobile = (TextView)this.findViewById(R.id.recipient_info_mobile, this);
        this.recipient_info_address = (TextView)this.findViewById(R.id.recipient_info_address, this);
        this.package_name = (TextView)this.findViewById(R.id.package_name, this);
        this.package_weight = (TextView)this.findViewById(R.id.package_weight, this);
        this.send_time_view = (TextView)this.findViewById(R.id.send_time_view, this);

        this.message_view = (TextView)this.findViewById(R.id.message_view);
        this.message_view_layout = this.findViewById(R.id.message_view_layout);

        this.order_no = (TextView)this.findViewById(R.id.order_no);

        this.deliver_info = (TextView)this.findViewById(R.id.deliver_info);
        this.loadData();
    }

    private void receiveData(PackageDetailWrapper data) {
        if (data != null) {
            this.packageInfo = data.getOrderInfo();
            if (this.packageInfo != null) {
                this.get_time_view.setText(this.packageInfo.getSource_time());
                this.get_dest_view.setText(this.packageInfo.getDistance());
                if (this.orderType == 0) {
                    this.income_view.setText(this.packageInfo.getIncoming());
                }
                else {
                    this.income_view.setText(this.packageInfo.getPrice());
                }
                this.sender_info_name.setText("发件人：" + this.packageInfo.getSource_user());
                this.sender_info_mobile.setText(this.packageInfo.getPhone());
                this.sender_info_address.setText("发货地址：" +this.packageInfo.getSource_city() + this.packageInfo.getSource_address());

                this.recipient_info_name.setText("收件人：" + this.packageInfo.getDestination_user());
                this.recipient_info_mobile.setText(this.packageInfo.getDestination_phone());
                this.recipient_info_address.setText("收货地址：" + this.packageInfo.getDestination_city() + this.packageInfo.getDestination_address());

                this.package_weight.setText(this.packageInfo.getWeight());
                this.package_name.setText(this.packageInfo.getName());
                this.send_time_view.setText("送件时间:" + this.packageInfo.getArrive_time());
                //设置订单号
                this.order_no.setText(this.packageInfo.getOrder_number());
                //设置运送方式信息
                this.deliver_info.setText(this.packageInfo.getDeliverInfo());
                setMessage();
            }
        }
        else {
            showMessage("没有查到订单");
        }
    }

    private void loadData() {
        showWaiting();
        api.packageDetailInfo(this.packageInfo.getId(), new MiniDataListener<PackageDetailWrapper>() {
            @Override
            public void onResponse(PackageDetailWrapper data, CEDataException error) {
                dismissWaiting();
                receiveData(data);
            }
        });
    }

    private void setMessage() {
        //邮件的发送人
        String uid = this.packageInfo.getUid();
        User user = WHO();
        String currentUid = null;
        if (user != null) {
            currentUid = user.getUid();
        }
        String status = this.packageInfo.getStatus();
        String message;
        //从首页列表跳转过来、并且不是当前用户发送的快件
        if (user == null) {
            message = "提示：完成此笔订单，您将得到" + this.packageInfo.getIncoming()+"送件费";
            this.findViewById(R.id.status_ne_10).setVisibility(View.VISIBLE);
        }
        //邮件人为当前用户，则为发件
        else if (uid.equals(currentUid)) {
            if ("0".equals(status)) {
                message = "提示：为了保证您的快件能如期到达，请及时付款。";
                this.findViewById(R.id.status_e_0).setVisibility(View.VISIBLE);
            }
            else if ("1".equals(status)) { //订单已经生成，还没有人接单
                message = "提示：您的快件已生成，请耐心等待送件人联系您。";
                //this.findViewById(R.id.status_e_0).setVisibility(View.VISIBLE);
            }
            else if ("2".equals(status)) { //已接单，需付费
                message = "提示：请联系您的送件人后尽快付费";
                this.findViewById(R.id.status_e_2).setVisibility(View.VISIBLE);
                this.findViewById(R.id.button_contact_dispatcher_2, this);
                this.findViewById(R.id.button_pay_2, this);
            }
            else if ("3".equals(status)) { //已接单，已付费
                message = "提示：请联系送件人送件，只有在送件人将快件送达并且提交您给的送货码："+this.packageInfo.getKey()+" 后送件人才能完成订单并且收到送件费，在送件人未送达快件前请勿将送货码给对方";
                this.findViewById(R.id.status_e_3).setVisibility(View.VISIBLE);
            }
            else if ("4".equals(status)) {//已送达
                message = "提示：您的快件已经送到 "+this.packageInfo.getDestination_user()+" 手中，系统将在24小时内自动将送件费打入送件人账户，如有问题请及时联系客服。";
                this.findViewById(R.id.status_e_4_null).setVisibility(View.VISIBLE);
            }
            else {
                message = "提示：非常抱歉，由于您的快件在"+this.packageInfo.getArrive_time()+"前三小时没人接单，此快件已经过期，请您重新发送快件以便此时段送件人能看到订单。";
                this.findViewById(R.id.status_e_4_null).setVisibility(View.VISIBLE);
            }
        }
        //送件
        else {
            if ("1".equals(status)) {
                message = "提示：完成此笔订单，您将得到" + this.packageInfo.getIncoming()+"送件费";
                this.findViewById(R.id.status_ne_10).setVisibility(View.VISIBLE);
            }
            else if ("2".equals(status)) {
                message = "提示：目前订单为未付费状态，请尽快联系发件人付费，只有在发件人付费并且您成功把快件送达后您才能得到"+this.packageInfo.getIncoming()+"送件费";
                this.findViewById(R.id.status_ne_1).setVisibility(View.VISIBLE);
                this.findViewById(R.id.button_ne_complete, this);
            }
            else if ("3".equals(status)) {
                message = "提示：发件人已经付费，请尽快完成送件，完成送件并提交发件人给的送货码后才能收到送件费" + this.packageInfo.getIncoming()+"送件费";
                this.findViewById(R.id.status_ne_1).setVisibility(View.VISIBLE);
                this.findViewById(R.id.button_dispatch_code, this);
                this.inputCodeView = (EditText)this.findViewById(R.id.input_code_view);
            }
            else {
                message = "提示：您已经成功送达快件并获得"+this.packageInfo.getIncoming() + "快件费，如有问题请及时联系客服。";
                this.findViewById(R.id.status_ne_null).setVisibility(View.VISIBLE);
                this.findViewById(R.id.button_ne_contact_service, this);
            }
        }
        this.message_view.setText(message);
        this.message_view_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 联系送件人
     */
    @Action(R.id.button_contact_dispatcher)
    public void actionContactDispatcher() {
        MiniSystemHelper.call(packageInfo.getReceive_phone(), DTDPackageDetailActivity.this);
    }

    @Action(R.id.button_contact_dispatcher_2)
    public void actionContactDispatcher2() {
        MiniSystemHelper.call(packageInfo.getReceive_phone(), DTDPackageDetailActivity.this);
    }

    /**
     * 联系发件人
     */
    @Action(R.id.button_contact)
    public void actionContact() {
        MiniSystemHelper.call(packageInfo.getSource_phone(), DTDPackageDetailActivity.this);
    }

    /**
     * 支付费用
     */
    @Action(R.id.button_pay)
    public void actionPay() {
        startActivity(DTDPayActivity.class, packageInfo);
    }

    /**
     * 支付费用
     */
    @Action(R.id.button_pay_2)
    public void actionPay2() {
        startActivity(DTDPayActivity.class, packageInfo);
    }

    @ActivityResult(REQ_LOGIN)
    public void didLogin(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {

        }
    }

    @Action(R.id.button_contact_service)
    public void buttonContactService() {
        MiniSystemHelper.call(DTDProfileActivity.mobile, DTDPackageDetailActivity.this);
    }

    @Action(R.id.button_ne_contact_service)
    public void buttonNeContactService() {
        MiniSystemHelper.call(DTDProfileActivity.mobile, DTDPackageDetailActivity.this);
    }

    @Action(R.id.button_dispatch_code)
    public void buttonDispatchCode() {
        String code = this.inputCodeView.getText().toString();
        if (code == null || code.length() == 0) {
            showMessage("请输入收货码");
            return;
        }
        User user = WHO();
        api.postKey(user.getPhone(), packageInfo.getId(), code, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                if ("ok".equals(data)) {
                    showMessage("提交成功我们将在24小时内将" + packageInfo.getIncoming() + "快件费打入您的账号");
                    back();
                } else if ("order_error".equals(data)) {
                    showMessage("无效订单，请查看其他订单");
                } else if ("key_error".equals(data)) {
                    showMessage("收货码错误，请重新提交");
                } else {
                    showMessage("无效用户，请重新登录");
                }
            }
        });
    }

    /**
     * 完成订单
     */
    @Action(R.id.button_ne_complete)
    public void neComplete() {
        MiniCustomDialog.show(this, "提示", "您确定已经完成订单吗？", "取消", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = WHO();
                showWaiting();
                api.finishOrder(user.getPhone(), packageInfo.getId(), new MiniDataListener<String>() {
                    @Override
                    public void onResponse(String data, CEDataException error) {
                        dismissWaiting();
                        if (error != null) {
                            showError(error);
                        } else {
                            if ("ok".equals(data)) {
                                showMessage("提交成功，请填入收货码获取送件费");
                                back();
                            } else if ("order_error".equals(data)) {
                                showMessage("无效订单，请查看其他订单");
                            } else {
                                showMessage("无效用户，请重新登录");
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 我要接单
     */
    @Action(R.id.button_task)
    public void actionTask() {
        User user = WHO();
        if (user == null) {
            MiniCustomDialog.show(this, "提示", "请先登录后再接单", "取消", "好", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(DTDPackageDetailActivity.this, MNSigninActivity.class);
                    intent.putExtra(MNSigninActivity.TYPE,MNSigninActivity.TYPE_FOR_RESULT);
                    startActivityForResult(intent, REQ_LOGIN);
                }
            });
        }
        else {
            final String phone = user.getPhone();
            //用户选择快递方式
            MiniCustomDialog.show(this, "提示", "请选择送件方式", "火车", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final MiniInputDialog inputDialog = new MiniInputDialog(DTDPackageDetailActivity.this);
                    inputDialog.setTitle("请输入火车信息");
                    inputDialog.setConfirmButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = inputDialog.getText();
                            if (text.length() > 0) {
                                inputDialog.dismiss();
                                receiveOrder("1", text);
                            }
                        }
                    });
                    inputDialog.show();
                }
            }, "飞机", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final MiniInputDialog inputDialog = new MiniInputDialog(DTDPackageDetailActivity.this);
                    inputDialog.setTitle("请输入飞机信息");
                    inputDialog.setConfirmButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = inputDialog.getText();
                            if (text.length() > 0) {
                                inputDialog.dismiss();
                                receiveOrder("2", text);
                            }
                        }
                    });
                    inputDialog.show();
                }
            });

        }
    }

    public void receiveOrder(final String deliver_way, final String deliver_info) {
        User user = WHO();
        final String phone = user.getPhone();
        MiniCustomDialog.show(this, "提示", "您确定要完成此笔订单吗？", "取消", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showWaiting();
                api.getOrder(phone, packageInfo.getId(), deliver_way, deliver_info, new MiniDataListener<String>() {
                    @Override
                    public void onResponse(String data, CEDataException error) {
                        dismissWaiting();
                        resultGetOrder(data, error);
                    }
                });
            }
        });
    }

    public void resultGetOrder(String data, CEDataException error) {
        if (error != null) {
            showError(error);
        }
        if ("get_ok".equals(data)) {
            showMessage("接单成功, 请尽快与发件人联系");
        }
        else if ("card_error".equals(data)) {
            MiniCustomDialog.show(this, "提示", "请先上传正面手持身份证照片验证身份", "取消", "好", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(DTDBecomeDispatcherActivity.class);
                }
            });
        }
        else if ("could_not_get_yourself_order".equals(data)) {
            showMessage("您不能给自己送件");
        }
        else if ("order_error".equals(data)) {
            showMessage("无效订单，请查看其他订单");
        }
        else {
            showMessage("无效用户，请重新登录");
        }
    }

}
