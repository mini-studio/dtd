package org.mini.frame.photoview;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mini.R;
import com.mini.activity.comm.MNActivityBase;
import com.mini.core.define.CEConst;

import org.mini.frame.notification.NotificationCenter;

import java.util.List;

/**
 * Created by hqh on 2015/7/13.
 * 自定义选择相册
 */
public class MiniCustomSelectPicActivity extends MNActivityBase implements AdapterView.OnItemClickListener {

    private int imageLimitCount; //图片的限制数量
    private MiniCustomSelectPicAlbumHelper helper;
    private List<MiniPhotoBucket> dataList;
    private ListView mSelectListView;
    private MiniCustomSelectPicAdapter mAdapter;

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_custom_select_pic);
        this.initData();
        this.initView();
        this.setTitle(R.string.select_album);
        this.setNaviRightTitleWithListener(getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        NotificationCenter.defaultNotificationCenter().register(CEConst.CE_SEND_IMAGES, this);
        imageLimitCount = getIntent().getIntExtra(CEConst.EXTRA_IMAGE_LIMIT_COUNT, 0);
        mSelectListView = (ListView) findViewById(R.id.custom_select_pic_list_view);
        mAdapter = new MiniCustomSelectPicAdapter();
        mAdapter.setContext(MiniCustomSelectPicActivity.this);
        mAdapter.setList(dataList);
        mAdapter.notifyDataSetChanged();
        mSelectListView.setAdapter(mAdapter);
        mSelectListView.setOnItemClickListener(this);

    }

    private void initData() {
        helper = MiniCustomSelectPicAlbumHelper.getHelper();
        helper.init(this);
        dataList = helper.getImagesBucketList(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MiniCustomSelectPicActivity.this,MiniCustomSelectPicDetailActivity.class);
        MiniPhotoBucket imageBucket = dataList.get(position);
        intent.putExtra(CEConst.EXTRA_IMAGE_NAME,imageBucket.bucketName);
        intent.putExtra(CEConst.EXTRA_IMAGE_LIMIT_COUNT,imageLimitCount);
        intent.putExtra(CEConst.EXTRA_IMAGE_LIST,imageBucket);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultNotificationCenter().remove(this);
    }

    @org.mini.frame.annotation.Notification(CEConst.CE_SEND_IMAGES)
    public void onReceivedSendSuccessNotification(org.mini.frame.notification.Notification notification) {
        finish();
    }
}
