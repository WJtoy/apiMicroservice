package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.sd.utils.OrderNoAdapter;
import com.wolfking.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 问题反馈
 */
public class Feedback extends LongIDDataEntity<Feedback> {
    private static final long serialVersionUID = 1L;

    // Fields
    private Customer customer;
//    private String orderNo = "";//订单编号
    private String title = "问题反馈";//标题
    private String feedFrom = "";//投诉方
    private Integer replyFlag;//回复标示 0:已读  1：客服回复 2：厂商回复

    @JsonAdapter(OrderNoAdapter.class)
    private Order  order;//
    private Integer closeFlag;//关闭标识
    private Date closeDate;//关闭日期
    private User closeBy;//关闭人
    private String quarter = "";//数据库分片，与订单相同

//    private String attachment1;
    private Integer nextFloor = 1;
    private Integer attachmentCount = 0;//已上传图片数量,限制5张

    private List<FeedbackItem> items = Lists.newArrayList(); // 反馈列表

    // Constructors

    public Feedback() {
    }

    public Feedback(String title) {
        this();
        this.title = title;
    }

    public Feedback(Long id) {
        this();
        this.id = id;
    }

    public Feedback(Long id,String title) {
        this();
        this.id = id;
        this.title = title;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

//    @NotNull(message = "订单号不能为空")
//    public String getOrderNo() {
//        return orderNo;
//    }
//
//    public void setOrderNo(String orderNo) {
//        this.orderNo = orderNo;
//    }

    @Length(min = 1,max = 100,message = "标题长度不能超过50个汉字")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Length(min = 1,max = 20,message = "标题长度不能超过10个汉字")
    public String getFeedFrom() {
        return feedFrom;
    }

    public void setFeedFrom(String feedFrom) {
        this.feedFrom = feedFrom;
    }

    @Range(min = 0,max = 1,message = "回复标示只能在0-1之间")
    public Integer getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(Integer replyFlag) {
        this.replyFlag = replyFlag;
    }


    @Range(min = 0,max = 1,message = "关闭标示只能在0-1之间")
    public Integer getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(Integer closeFlag) {
        this.closeFlag = closeFlag;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public User getCloseBy() {
        return closeBy;
    }

    public void setCloseBy(User closeBy) {
        this.closeBy = closeBy;
    }

    public List<FeedbackItem> getItems() {
        return items;
    }

    public void setItems(List<FeedbackItem> items) {
        this.items = items;
    }

    public Integer getNextFloor() {
        return nextFloor;
    }

    public void setNextFloor(Integer nextFloor) {
        this.nextFloor = nextFloor;
    }

    public Integer getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    @NotNull(message = "订单不能为空")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
