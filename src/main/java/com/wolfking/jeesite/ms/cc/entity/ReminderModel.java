package com.wolfking.jeesite.ms.cc.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.cc.Reminder;
import com.kkl.kklplus.entity.cc.ReminderLog;
import com.kkl.kklplus.entity.cc.ReminderType;
import com.kkl.kklplus.entity.cc.vm.ReminderListModel;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * 催单单据视图模型
 * @date 2019-07-11 14:56
 */
@Data
@NoArgsConstructor
public class ReminderModel extends ReminderListModel {

    public static int VISIBILITY_FLAG_ALL = VisibilityFlagEnum.or(Sets.newHashSet(VisibilityFlagEnum.KEFU, VisibilityFlagEnum.CUSTOMER));
    //最后项目id
    //private long itemId;
    //催单类型名称
    private String reminderTypeName;
    public String getReminderTypeName(){
        ReminderType type = ReminderType.fromCode(this.getReminderType());
        if(type != null){
            return type.getMsg();
        }
        return StringUtils.EMPTY;
    }

    //数据来源
    private String dataSourceName;
    //status
    private String statusName;
    //客户
    @JsonAdapter(CustomerSimpleAdapter.class)
    private Customer customer;
    //催单创建时的网点
    @JsonAdapter(ServicePointSimpleAdapter.class)
    private ServicePoint servicePoint;
    //街道
    @JsonAdapter(AreaSimpleAdapter.class)
    private Area subArea;
    //创建者
    @JsonAdapter(UserSimpleAdapter.class)
    private User creator;

    //催单日期
    private String createDate;

    //处理日期
    private String processDate;
    //完成日期
    private String completeDate;

    //完成用时
    private String completeTime;

    //订单完成日期
    private String orderCloseDate;
    public String getOrderCloseDate(){
        if(this.getOrderCloseAt() <=0){
            return StringUtils.EMPTY;
        }
        return DateFormatUtils.format(this.getOrderCloseAt(), "yyyy-MM-dd HH:mm:ss");
    }

    //品类名称，用于显示
    private String productCategoryName;

    //辅助属性，用于判断是新建还是修改(从列表中点击弹窗)
    // 催单确认：1-跟单确认 2-跟单驳回
    private int action;

    private String orderStatusName;
    public String getOrderStatusName(){
        switch(this.getOrderStatus()){
            case 80:
                this.orderStatusName = "客评";
                break;
            case 90:
                this.orderStatusName = "已退单";
                break;
            case 100:
                this.orderStatusName = "已取消";
            break;
            default:
                this.orderStatusName = "处理中";
                break;
        }
        return this.orderStatusName;
    }

    private List<ReminderItemModel> items = Lists.newArrayList();

    private List<ReminderLog> logs = Lists.newArrayList();
    //处理方法
    // 1:跟单确认 2:跟单驳回
    private int actionCode;

}
