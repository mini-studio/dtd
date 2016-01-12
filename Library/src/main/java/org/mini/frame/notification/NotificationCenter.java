package org.mini.frame.notification;

import android.os.Handler;

import org.mini.frame.annotation.Notification;
import org.mini.frame.log.MiniLogger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class NotificationCenter {

    private static NotificationCenter notificationCenter = new NotificationCenter();

    private List<org.mini.frame.notification.Notification> notifications = new ArrayList<org.mini.frame.notification.Notification>();

    private Map<String,List<Object>> observers = new HashMap<String, List<Object>>();

    private Object lock = new Object();

    private Handler handler = new Handler();

    public static NotificationCenter defaultNotificationCenter() {
        return notificationCenter;
    }

    private List<Object> getObservers(String key) {
        synchronized (lock) {
            List<Object> lst = observers.get(key);
            if (lst == null) {
                lst = new ArrayList<Object>();
                observers.put(key, lst);
            }
            return lst;
        }
    }

    public void register(String key, Object observer) {
        synchronized (lock) {
            List<Object> lst = getObservers(key);
            if (!lst.contains(observer)) {
                lst.add(observer);
            }
        }
    }

    public void remove(Object object) {
        remove(object,null);
    }

    public synchronized void remove(Object object, String key) {
        synchronized (lock) {
            if (key == null) {
                List<String> emptyKeys = new ArrayList<String>();
                for (String k : observers.keySet()) {
                    List<Object> objects = observers.get(k);
                    if (objects != null && objects.size() > 0) {
                        if (objects.contains(object)) {
                            objects.remove(object);
                        }
                        if (objects.size() == 0) {
                            emptyKeys.add(k);
                        }
                    }
                }
                if (emptyKeys.size() > 0) {
                    for (String k : emptyKeys) {
                        observers.remove(k);
                    }
                }
            } else {
                List<Object> lst = getObservers(key);
                if (lst != null) {
                    lst.remove(object);
                }
                if (lst == null || lst.size() == 0) {
                    observers.remove(key);
                }
            }
        }
    }

    public void post(org.mini.frame.notification.Notification notification) {
        new NotificationDispatcher(notification).start();
    }

    public void post(String key) {
        org.mini.frame.notification.Notification notification = new org.mini.frame.notification.Notification();
        notification.setKey(key);
        new NotificationDispatcher(notification).start();
    }

    public void post(String key, Object object) {
        org.mini.frame.notification.Notification notification = new org.mini.frame.notification.Notification();
        notification.setKey(key);
        notification.setSource(object);
        new NotificationDispatcher(notification).start();
    }

    class NotificationDispatcher extends Thread {
        org.mini.frame.notification.Notification notification;
        public NotificationDispatcher(org.mini.frame.notification.Notification notification) {
            this.notification = notification;
        }

        public void run() {
            synchronized (lock) {
                try {
                    String key = notification.getKey();
                    List<Object> objects = getObservers(key);
                    if (objects != null && objects.size() > 0) {
                        for (final Object object : objects) {
                            final Method m = getNotificationMethod(object.getClass(), key);
                            if (m != null) {
                                if (!m.isAccessible()) {
                                    m.setAccessible(true);
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            m.invoke(object, notification);
                                        } catch (Exception e) {
                                            MiniLogger.get().e(e.getMessage(), e);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                catch (Exception e) {
                    MiniLogger.get().e(e);
                }
            }
        }

        Method getNotificationMethod(Class clazz, String key) {
            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null) {
                for (Method m : methods) {
                    Notification noti = m.getAnnotation(Notification.class);
                    if (noti != null && noti.value().equals(key)) {
                        return m;
                    }
                }
            }
            if (!clazz.getSuperclass().equals(Object.class)) {
                return getNotificationMethod(clazz.getSuperclass(),key);
            }
            return null;
        }
    }
}
