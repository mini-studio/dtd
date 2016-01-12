package org.mini.frame.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mini.R;

import org.mini.frame.toolkit.MiniDisplayUtil;
import org.mini.frame.toolkit.MiniSystemHelper;
import org.mini.frame.toolkit.MiniUtils;

/**
 * Created by hqh on 2015/7/8.
 * 个人资料 更多功能
 */
public class MiniAlertDialog implements View.OnClickListener{
    private Context context;
    private String mobile; //电话号码
    private String name; //名字
    private AlertDialog bottomDialog = null;

    public MiniAlertDialog(Context context,String mobile,String name) {
        this.context = context;
        this.mobile = mobile;
        this.name = name;
    }


    public MiniAlertDialog builder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        bottomDialog = builder.create();
        bottomDialog.show();
        bottomDialog.setCanceledOnTouchOutside(true);
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_person_detail_more,null);
        Window window = bottomDialog.getWindow();
        window.setLayout(MiniUtils.getScreenWidth(context), MiniDisplayUtil.dip2px(220, context));

        window.setContentView(layout);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.myStyle);

        RelativeLayout mSavePhone = (RelativeLayout) layout.findViewById(R.id.contacts_save_phone_dialog);
        RelativeLayout mCopyPhone = (RelativeLayout) layout.findViewById(R.id.contacts_copy_phone_dialog);
        RelativeLayout mCancel = (RelativeLayout) layout.findViewById(R.id.contacts_finish_phone_dialog);

        mSavePhone.setOnClickListener(this);
        mCopyPhone.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_save_phone_dialog:
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.dir/person");
                intent.setType("vnd.android.cursor.dir/contact");
                intent.setType("vnd.android.cursor.dir/raw_contact");
                intent.putExtra(Contacts.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                intent.putExtra(Contacts.Intents.Insert.PHONE, mobile);
                intent.putExtra(Contacts.Intents.Insert.NAME, name);
                context.startActivity(intent);
                bottomDialog.dismiss();
                break;
            case R.id.contacts_copy_phone_dialog:
                MiniSystemHelper.copyText(mobile, context);
                Toast.makeText(context, R.string.copy_phone_succeed, Toast.LENGTH_SHORT).show();
                bottomDialog.dismiss();
                break;
            case R.id.contacts_finish_phone_dialog:
                bottomDialog.dismiss();
                break;
        }
    }
}
