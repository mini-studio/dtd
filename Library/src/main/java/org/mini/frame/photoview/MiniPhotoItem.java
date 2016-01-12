package org.mini.frame.photoview;

import java.io.Serializable;

/**
 * Created by Wuquancheng on 15/10/25.
 */
public class MiniPhotoItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
    public int position;
}
