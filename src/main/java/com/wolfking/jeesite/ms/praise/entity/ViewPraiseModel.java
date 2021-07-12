package com.wolfking.jeesite.ms.praise.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kkl.kklplus.entity.praise.Praise;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import lombok.Data;

import java.util.List;

@Data
public class ViewPraiseModel extends Praise {

    public static int VISIBILITY_FLAG_ALL = VisibilityFlagEnum.or(Sets.newHashSet(VisibilityFlagEnum.KEFU, VisibilityFlagEnum.CUSTOMER,VisibilityFlagEnum.SERVICE_POINT));

    /**
     * 客服可取消无费用且审核通过的超时标识(已超时)
     * */
    public static int HAS_OVERTIME_FLAG = 1;

    /**
     * str状态
     * */
    private String strStatus = "";

    /**
     * 厂商名称
     * */
    private String customerName = "";

    /**
     * 异常类型
     * */
    private String abnormalCategory = "";

    /**
     * 时效
     * */
    private String timelinessLabel = "";

    /**
     *  创建时间,申请时间 str
     * */
    private String applyTime = "";

    /**
     * 申请人
     * */
    private String applyName = "";

    /**
     *  审核时间,完成时间,驳回时间str
     * */
    private String strUpdateDate = "";

    /**
     *  审核人,驳回人
     * */
    private String strUpdateName = "";

    /**
     * 网点编号
     * */
    private String servicePointNo = "";

    /**
     * 网点电话
     * */
    private String servicePointPhone = "";


    /**
     * 用于判断显示时效的样式
     * */
    private double cutOffTimeliness;

    private List<PraiseLogModel> praiseLogModels = Lists.newArrayList();

    /**
     * 客服可取消无费用且审核通过的超时标识 0：未超时 1：已超时
     * */
    private int overtimeFlag = 0;




}
