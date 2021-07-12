package com.wolfking.jeesite.ms.cc.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.cc.ReminderItem;
import com.kkl.kklplus.entity.cc.ReminderLog;
import com.kkl.kklplus.entity.cc.ReminderType;
import com.kkl.kklplus.entity.cc.vm.ReminderListModel;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * 催单项目视图模型
 * @date 2019-11-20 17:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderItemModel implements Serializable {

    private long id;
    //次序
    private int itemNo;
    private String createName;
    private String creatorTypeName;
    private long createAt;
    private String createRemark;
    private long processAt;
    private String processName;
    private String processRemark;
    private String processorTypeName;
    private double processTimeLiness;
    private double timeoutTimeLiness;
    private long timeoutAt;
    private int timeoutFlag;
    // 未回复距离现在的时效
    private double cutOffTimeLiness;
    private NameValuePair<Integer, String> reminderReason;
}
