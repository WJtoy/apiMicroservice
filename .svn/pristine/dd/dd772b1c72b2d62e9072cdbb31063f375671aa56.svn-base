package com.wolfking.jeesite.ms.cc.entity;

import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import lombok.Data;

@Data
public class AbnormalFormModel extends AbnormalForm {

   /**
    * 来源名称
    **/
  private String channelName;

    /**
     * 异常单类型名称
     **/
  private String fromTypeName;

    /**
     * 子异常类型名称
     **/
  private String subTypeName;

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

    /**
     * 品类名称
     **/
    private String productCategoryName;

    /**
     * 客服
     **/
    @JsonAdapter(UserSimpleAdapter.class)
    private User kefu;


    /**
     * 反馈时间(用于显示)
     **/
    private String strCreateDate;

    /**
     * 处理人名称(用于显示)
     **/
    private String closeByName;


    /**
     * 处理时间(用于显示)
     **/
    private String closeDate;

    /**
     * 反馈时效
     **/
    private String feedBackTimeliness;

    /**
     * 用于判断显示时效的样式
     * */
    private double cutOffTimeliness;



}
