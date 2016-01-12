package org.mini.frame.photoview;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class MiniPhotoBucket implements Serializable {
    public int count = 0;
    public String bucketName;
    public List<MiniPhotoItem> imageList;

}
