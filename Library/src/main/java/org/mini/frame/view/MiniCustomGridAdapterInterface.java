package org.mini.frame.view;

/**
 * Created by admin on 2015/6/12.
 */
public interface MiniCustomGridAdapterInterface {
    /**
     * Determines how to reorder items dragged from <code>originalPosition</code> to <code>newPosition</code>
     */
    void reorderItems(int originalPosition, int newPosition);

    /**
     * @return return columns number for GridView. Need for compatibility
     * (@link android.widget.GridView#getNumColumns() requires api 11)
     */
    int getColumnCount();
}
