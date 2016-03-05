package com.dtdinc.dtd.activity.profile;

import android.widget.EditText;
import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;

/**
 * Created by Wuquancheng on 15/10/25.
 * 取现，将账户中的余额取走
 */
public class DTDTakeCashActivity extends MNActivityBase {

    private TextView message_view;
    private EditText take_cash_text_view;
    private float total = 0;
    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_take_cash);
        this.setTitle("明细");
        String t = (String)getIntentObject();
        if (t == null) {
            t = "0";
        }
        total = Float.parseFloat(t);
        this.initView();

    }

    private void initView() {
        this.setNaivLeftBackAction();
        this.message_view = (TextView)findViewById(R.id.message_view);
        this.take_cash_text_view = (EditText)findViewById(R.id.take_cash_text_view);
        this.message_view.setText("当前最高可提现金额" + total + "元，申请提现后将在三个工作日内打入您预先设置的银行卡。");
        this.findViewById(R.id.take_cash_button, this);
    }

    @Action(R.id.take_cash_button)
    public void takeCash() {

        String takeAmount = this.take_cash_text_view.getText().toString();
        try {
            if (takeAmount == null) {
                showMessage("请输入提取的金额");
                return;
            }
            Float take = Float.parseFloat(takeAmount);
            if (take == null) {
                showMessage("请输入提取的金额");
                return;
            }
            if (take > total) {
                showMessage("请输入正确提现金额");
                return;
            }
        }
        catch (Exception e) {
            showMessage("请输入正确提现金额");
            return;
        }
        showWaiting();
        api.takeCash(takeAmount, new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                dismissWaiting();
                if ("get_ok".equals(data)) {
                    showMessage("恭喜您，提现成功");
                }
                else {
                    showMessage("提现失败请重试");
                }
            }
        });
    }

}
