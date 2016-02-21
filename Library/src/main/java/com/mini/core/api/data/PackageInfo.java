package com.mini.core.api.data;

/**
 * Created by Wuquancheng on 15/11/14.
 * 订单信息
 * id: "7",
 * source_city: "北京市",
 * source_time: "2015-10-03 16:00",
 * destination_city: "北京市",
 * arrive_time: "2015-10-04 18:00",
 * source_longitude: "116.41",
 * source_latitude: "39.90",
 * distance: "24479km"
 */
public class PackageInfo {

    private String activityTitle;

    //订单列表属性
    private String id;
    private String source_city;
    private String source_time;
    private String destination_city;
    private String arrive_time;
    private String source_longitude;
    private String source_latitude;
    private String location;

    //订单详情信息
    private String uid;
    private String phone;
    private String source_user;
    private String source_phone;
    private String source_address;
    private String destination_user;
    private String destination_phone;
    private String destination_address;
    private String name;
    private String weight;
    /**
     * 快件费用
     */
    private String price;
    private String status;
    private String key;
    private String order_number;
    private String create_time;
    private String real_arrive_time;
    private String finish_time;
    private String distance;
    private String incoming;

    /**
     * 接单人id
     */
    private String receive_uid;

    /**
     * 接单人电话
     */
    private String receive_phone;
    /**
     * 邮递方式
     */
    private String deliver_way;
    /**
     * 邮递信息
     */
    private String deliver_info;

    /**
     * 0 附近订单
     * 1 我的订单
     */
    private int from = 1;

    /**
     * 0 我接收的订单
     * 1 我发出的订单
     */
    private int orderType = 0;


    public String getSource_city() {
        return source_city;
    }

    public void setSource_city(String source_city) {
        this.source_city = source_city;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getSource_time() {
        return source_time;
    }

    public void setSource_time(String source_time) {
        this.source_time = source_time;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getSource_longitude() {
        return source_longitude;
    }

    public void setSource_longitude(String source_longitude) {
        this.source_longitude = source_longitude;
    }

    public String getSource_latitude() {
        return source_latitude;
    }

    public void setSource_latitude(String source_latitude) {
        this.source_latitude = source_latitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSource_user() {
        return source_user;
    }

    public void setSource_user(String source_user) {
        this.source_user = source_user;
    }

    public String getSource_phone() {
        return source_phone;
    }

    public void setSource_phone(String source_phone) {
        this.source_phone = source_phone;
    }

    public String getSource_address() {
        return source_address;
    }

    public void setSource_address(String source_address) {
        this.source_address = source_address;
    }

    public String getDestination_user() {
        return destination_user;
    }

    public void setDestination_user(String destination_user) {
        this.destination_user = destination_user;
    }

    public String getDestination_phone() {
        return destination_phone;
    }

    public void setDestination_phone(String destination_phone) {
        this.destination_phone = destination_phone;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceive_uid() {
        return receive_uid;
    }

    public void setReceive_uid(String receive_uid) {
        this.receive_uid = receive_uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getReal_arrive_time() {
        return real_arrive_time;
    }

    public void setReal_arrive_time(String real_arrive_time) {
        this.real_arrive_time = real_arrive_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming;
    }

    /**
     * 订单状态，0=提交未付费，1=发布已付费，2=已接单，3=已到货，4=已完成，5=已过期
     *
     * @return
     */
    public String getStatusDesc(int orderType) {
        if (orderType == 0 || orderType == 1) {//我接的单
            if ("1".equals(this.status)) {
                return "未接单";
            } else if ("2".equals(this.status)) {
                return "未付费";
            } else if ("3".equals(this.status)) {
                return "已付费";
            } else if ("4".equals(this.status)) {
                return "已完成";
            } else if ("5".equals(this.status)) {
                return "已过期";
            } else {
                return "";
            }
        } else {//我发布的单
            if ("2".equals(this.status)) {
                return "已接单";
            } else if ("0".equals(this.status)) {
                return "提交未付费";
            } else if ("1".equals(this.status)) {
                return "发布已付费";
            } else if ("3".equals(this.status)) {
                return "已到货";
            } else if ("4".equals(this.status)) {
                return "已完成";
            } else if ("5".equals(this.status)) {
                return "已过期";
            } else {
                return "";
            }
        }

    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getDeliver_way() {
        return deliver_way;
    }

    public void setDeliver_way(String deliver_way) {
        this.deliver_way = deliver_way;
    }

    public String getDeliver_info() {
        return deliver_info;
    }

    public void setDeliver_info(String deliver_info) {
        this.deliver_info = deliver_info;
    }

    public String getDeliverInfo() {
        return ("1".equals(this.deliver_way)?"火车":"飞机") + this.deliver_info;

    }

    public String getReceive_phone() {
        return receive_phone;
    }

    public void setReceive_phone(String receive_phone) {
        this.receive_phone = receive_phone;
    }
}