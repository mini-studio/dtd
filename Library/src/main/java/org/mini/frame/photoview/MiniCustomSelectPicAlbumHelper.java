package org.mini.frame.photoview;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 选择多个图片图片选择专辑缩略图
 * 画册数量每个专辑的照片
 *
 * @author Gassion
 */
public class MiniCustomSelectPicAlbumHelper {
    Context context;
    ContentResolver cr;
    /**
     * The thumbnail list
     */
    HashMap<String, String> thumbnailList = new HashMap<String, String>();

    /**
     * album list
     */
    List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    HashMap<String, MiniPhotoBucket> bucketList = new HashMap<String, MiniPhotoBucket>();

    private static MiniCustomSelectPicAlbumHelper instance;

    private MiniCustomSelectPicAlbumHelper() {
    }

    public static MiniCustomSelectPicAlbumHelper getHelper() {
        if (instance == null) {
            instance = new MiniCustomSelectPicAlbumHelper();
        }
        return instance;
    }

    /**
     * init
     *
     * @param context
     */
    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    /**
     * get Thumbnail
     */
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getThumbnailColumnData(cursor);
    }

    /**
     * Thumbnail images from the database
     *
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur == null)
            return;

        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * Get the original image
     */
    void getAlbum() {
        String[] projection = {Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST,
                Albums.NUMBER_OF_SONGS};
        Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getAlbumColumnData(cursor);

    }

    /**
     * The original image from the local database
     *
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;

            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);

            } while (cur.moveToNext());

        }
    }

    /**
     * Whether to create a photo collections
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * Get photo collections
     */
    void buildImagesBucketList() {
        getThumbnail();

        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME,
                Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cur.moveToFirst()) {
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            do {
                String _id = cur.getString(photoIDIndex);
                String path = cur.getString(photoPathIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);

                MiniPhotoBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new MiniPhotoBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<MiniPhotoItem>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                MiniPhotoItem imageItem = new MiniPhotoItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.thumbnailPath = thumbnailList.get(_id);
                bucket.imageList.add(imageItem);
            } while (cur.moveToNext());
        }

        hasBuildImagesBucketList = true;
    }

    public List<MiniPhotoBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<MiniPhotoBucket> tmpList = new ArrayList<MiniPhotoBucket>();
        Iterator<Entry<String, MiniPhotoBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, MiniPhotoBucket> entry = (Entry<String, MiniPhotoBucket>) itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    String getOriginalImagePath(String image_id) {
        String path = null;
        String[] projection = {Media._ID, Media.DATA};
        Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));

        }
        return path;
    }

}
