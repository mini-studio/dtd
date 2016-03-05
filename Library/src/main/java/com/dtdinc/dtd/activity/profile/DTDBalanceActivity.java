package com.dtdinc.dtd.activity.profile;

import android.widget.TextView;

import com.mini.R;
import com.dtdinc.dtd.activity.comm.MNActivityBase;
import com.dtdinc.dtd.core.exception.CEDataException;

import org.mini.frame.annotation.Action;
import org.mini.frame.http.request.MiniDataListener;

import static com.dtdinc.dtd.app.CESystem.WHO;

/**
 * Created by Wuquancheng on 15/10/25.
 * 我的账户余额
 */
public class DTDBalanceActivity extends MNActivityBase {

    private TextView text_balance_view;
    private String money;
    @Override
    protected void loadView() {
        this.setContentView(R.layout.activity_balance);
        this.setTitle("我的余额");
        this.initView();
        this.loadData();
    }

    private void initView() {
        this.setNaivLeftBackAction();
        this.text_balance_view = (TextView)findViewById(R.id.text_balance_view);
        this.findViewById(R.id.take_cash_button, this);
    }

    private void loadData() {
        api.balanceInfo(WHO().getPhone(), new MiniDataListener<String>() {
            @Override
            public void onResponse(String data, CEDataException error) {
                if (error == null) {
                    money = data;
                    text_balance_view.setText(data);
                }
            }
        });
    }

    @Action(R.id.take_cash_button)
    public void takeCashButton() {
        startActivity(DTDTakeCashActivity.class,money);
    }
}
