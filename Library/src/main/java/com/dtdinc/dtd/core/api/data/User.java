package com.dtdinc.dtd.core.api.data;

import com.dtdinc.dtd.app.CEAppConfig;
import com.dtdinc.dtd.app.CESystem;

/**
 * Created by Wuquancheng on 15/10/25.
 *
 * 当前登录用户信息
 */
public class User {
    /** 用户名 */
    private String name;
    /** 用户id */
    private String uid;

    private String nickname;
    private String sid;
    private String phone;
    private String card;
    private String head;
    private String bankCard;

    private int lineCount = 0;
    private String linea;
    private String lineb;
    private String linec;

    /**
     * 紧急联系电话
     */
    private String emerPhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSid() {
        if (sid == null) {
            return "";
        }
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getHead() {
        if ("none".equals(head)) {
            return null;
        }
        return CEAppConfig.imageServer + head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public static User fromString(String string, String phone) {
        if ("password_or_phone_error".equals(string)) {
            return null;
        }
        else {
            String[] items = string.split("_");
            User user = new User();
            if (items.length >= 0) {
                user.sid = items[0];
            }
            if (items.length >= 2) {
                user.nickname = items[1];
            }
            if (items.length >= 3) {
                user.emerPhone = items[2];
            }
            if (items.length >= 4) {
                user.head = items[3];
            }
            if (items.length >= 5) {
                user.card = items[4];
            }
            if (items.length >= 6) {
                user.uid = items[5];
            }
            if (items.length >= 7) {
                user.bankCard = items[6];
            }
            if (items.length >= 8) {
                int lineCount = Integer.parseInt(items[7]);
                user.lineCount = lineCount;
                if (lineCount >= 1) {
                    user.linea = items[8];
                }
                if (lineCount >= 2) {
                    user.lineb = items[9];
                }
                if (lineCount >= 3) {
                    user.linec = items[10];
                }
            }
            user.phone = phone;
            return user;
        }
    }

    public String getEmerPhone() {
        if (emerPhone == null) {
            return this.phone;
        }
        return emerPhone;
    }

    public void setEmerPhone(String emerPhone) {
        this.emerPhone = emerPhone;
    }

    public void save() {
        CESystem.setCurrentUser(this);
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getLineb() {
        return lineb;
    }

    public void setLineb(String lineb) {
        this.lineb = lineb;
    }

    public String getLinec() {
        return linec;
    }

    public void setLinec(String linec) {
        this.linec = linec;
    }
}
