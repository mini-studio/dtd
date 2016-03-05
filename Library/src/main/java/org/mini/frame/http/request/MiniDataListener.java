package org.mini.frame.http.request;

import com.dtdinc.dtd.core.exception.CEDataException;

/**
 * Created by Wuquancheng on 15/4/1.
 */
public interface MiniDataListener<T> {
    void onResponse(T data, CEDataException error);
}
