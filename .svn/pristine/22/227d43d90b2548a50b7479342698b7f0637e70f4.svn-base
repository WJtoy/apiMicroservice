package com.wolfking.jeesite.modules.sys.utils;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.kkl.kklplus.utils.SequenceIdUtils;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.sys.entity.Sequence;
import com.wolfking.jeesite.modules.sys.service.SequenceService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 单据编号工具类
 * Created on 2017-04-19.
 */
@Slf4j
public class SeqUtils {
    private static SequenceService seqService = SpringContextHolder.getBean(SequenceService.class);
    //private static final Striped<Lock> striped = Striped.lazyWeakLock(127);
    private static RedisUtilsLocal redisUtilsLocal = SpringContextHolder.getBean(RedisUtilsLocal.class);
    private static final String OrderNoKeyFormat = "SEQURENCE:%s:%s";
    private static final String ORDER_SEQ_CODE = "OrderNO";
    private static final Long OrderNoExpireSeconds = 172800L;//2天

    public static String NextSequenceNo(String code,int currTimes,int retryTimes){
        if(currTimes >= retryTimes){
            return "";
        }
        currTimes++;
        String no;
        try {
            no = NextSequenceNo(code);
            if (StringUtils.isBlank(no)) {
                return NextSequenceNo(code,currTimes,retryTimes);
            }
            return no;
        }catch (Exception e){
            log.error("生成单据:" + code + " 失败",e);
            return "";
        }
    }

    /**
     * 获取下一个单据编号
     * @param code
     * @return
     */
    //@Transactional()
    public static String NextSequenceNo(String code) {
        String returnSeqNo = "";

        if (StringUtils.isEmpty(code)) {
            return "";
        }
        Sequence seq = seqService.getByCode(code, false);
        if (seq == null) {
            return "";
        }
        StringBuffer seqNo = new StringBuffer(50);
        seqNo.append(MoreObjects.firstNonNull(seq.getPrefix(), ""));

        String KeyPostfix;
        if (StringUtils.isNoneBlank(seq.getDateFormat())) {
            KeyPostfix = DateUtils.formatDate(new Date(), seq.getDateFormat());
        } else {
            KeyPostfix = "DATENONE";
        }

        String seqKey = String.format(RedisConstant.SEQ_KEY, code, KeyPostfix);

        if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey)){
            returnSeqNo = (String) redisUtilsLocal.lPop(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, String.class);
        }

        if (returnSeqNo != null && returnSeqNo.length() > 0){
            return returnSeqNo;
        }

        //检查锁
        String lockKey = String.format(RedisConstant.LOCK_SEQ_KEY, code, KeyPostfix);
        if(redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey)){
            try {
                java.lang.Thread.sleep(1000);//1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey)){
                returnSeqNo = (String) redisUtilsLocal.lPop(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, String.class);
            }
            if (returnSeqNo == null || returnSeqNo.length() == 0) {
                throw new RuntimeException("获取序号失败");
            } else {
                return returnSeqNo;
            }
        }

        //加锁
        Boolean accepted = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 5);//30秒
        if (!accepted) {
            try {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey)){
                returnSeqNo = (String) redisUtilsLocal.lPop(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, String.class);
            }
            if (returnSeqNo == null || returnSeqNo.length() == 0) {
                throw new RuntimeException("获取序号失败");
            } else {
                return returnSeqNo;
            }
        }else{
            seq = seqService.getByCode(code, true);
            List<String> seqList = Lists.newArrayList();

            if (StringUtils.isNoneBlank(seq.getDateFormat())) {
                String now = DateUtils.formatDate(new Date(), seq.getDateFormat());
                if (now.equalsIgnoreCase(seq.getPreviousDate())) {
                    seq.setPreviousDigit(seq.getPreviousDigit() + 1);
                }else{
                    seq.setPreviousDigit(1);
                    seq.setPreviousDate(now);
                }
                seqNo.append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                        .append(now.replace("-", ""))
                        .append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                        .append(StringUtils.leftPad(""+seq.getPreviousDigit(),seq.getDigitBit(),"0"));
                returnSeqNo = seqNo.toString().replace("".concat(seq.getSeparator()).concat(seq.getSeparator()), seq.getSeparator());
                for (int index = 1; index < 200; index++){
                    seq.setPreviousDigit(seq.getPreviousDigit() + 1);
                    seqNo = new StringBuffer(50);
                    seqNo.append(MoreObjects.firstNonNull(seq.getPrefix(), ""));
                    seqNo.append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                            .append(now.replace("-", ""))
                            .append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                            .append(StringUtils.leftPad(""+seq.getPreviousDigit(),seq.getDigitBit(),"0"));
                    seqList.add(seqNo.toString().replace("".concat(seq.getSeparator()).concat(seq.getSeparator()), seq.getSeparator()));
                }

            } else {
                seq.setPreviousDigit(seq.getPreviousDigit() + 1);
                seqNo.append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                        .append(StringUtils.leftPad(""+seq.getPreviousDigit(),seq.getDigitBit(),"0"));
                returnSeqNo = seqNo.toString().replace("".concat(seq.getSeparator()).concat(seq.getSeparator()), seq.getSeparator());
                for (int index = 1; index < 200; index++){
                    seq.setPreviousDigit(seq.getPreviousDigit() + 1);
                    seqNo = new StringBuffer(50);
                    seqNo.append(MoreObjects.firstNonNull(seq.getPrefix(), ""));
                    seqNo.append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                            .append(StringUtils.leftPad(""+seq.getPreviousDigit(),seq.getDigitBit(),"0"));
                    seqList.add(seqNo.toString().replace("".concat(seq.getSeparator()).concat(seq.getSeparator()), seq.getSeparator()));
                }
            }
            seqService.updatePreviousDigit(seq);
            if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey)) {
                redisUtilsLocal.rPushAll(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, seqList, -1);
            }else {
                redisUtilsLocal.rPushAll(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, seqList, 24 * 60 * 60);
            }
            //释放锁
            redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
        }

        return returnSeqNo;
    }


    /**
     * 将取到的序号重新放入队列中，避免跳号的情况
     * @param code
     * @param date
     * @param sequenceNo
     */
    public static void reputSequenceNo(String code, Date date, String sequenceNo){

        if (StringUtils.isEmpty(code)) {
            return;
        }
        Sequence seq = seqService.getByCode(code, false);
        if (seq == null) {
            return;
        }

        String KeyPostfix;
        if (StringUtils.isNoneBlank(seq.getDateFormat())) {
            KeyPostfix = DateUtils.formatDate(date, seq.getDateFormat());
        } else {
            KeyPostfix = "DATENONE";
        }

        if (!KeyPostfix.equals("DATENONE")){
            String curPostFix = DateUtils.formatDate(new Date(), seq.getDateFormat());
        if (!curPostFix.equals(KeyPostfix)){
            return;
        }
    }

    String seqKey = String.format(RedisConstant.SEQ_KEY, code, KeyPostfix);

        if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey)){
        redisUtilsLocal.lPush(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, sequenceNo);
    }
    }

    //@Transactional()
    public static String NextOrderNo() {
        String returnSeqNo = new String("");
        String KeyPostfix = DateUtils.formatDate(new Date(), "yyyy-MM-dd");

        String seqKey = String.format(OrderNoKeyFormat, ORDER_SEQ_CODE, KeyPostfix);
        //先从缓存中取
        returnSeqNo = (String) redisUtilsLocal.sPop(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, String.class);
        if (!StringUtils.isBlank(returnSeqNo)) {
            return returnSeqNo;
        }
        //检查锁，防止多进程同时产生号码
        String lockKey = String.format(RedisConstant.LOCK_SEQ_KEY, ORDER_SEQ_CODE, KeyPostfix);
        boolean accepted = redisUtilsLocal.getLock(lockKey,ORDER_SEQ_CODE,30);
        //Boolean accepted = redisUtils.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 30);//30秒
        if (!accepted) {
            //其他进程在产生订单号，此处睡眠等待2秒，再取缓存
            try {
                java.lang.Thread.sleep(2000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

            returnSeqNo = (String) redisUtilsLocal.sPop(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, String.class);
            if (StringUtils.isBlank(returnSeqNo)) {
                throw new RuntimeException("无单号返回");
            } else {
                return returnSeqNo;
            }
        }
        //读取订单命名规则
        Sequence seq = seqService.getByCode(ORDER_SEQ_CODE, false);
        if (seq == null) {
            return returnSeqNo;
        }

        StringBuffer seqNo = new StringBuffer(50);
        seqNo.append(MoreObjects.firstNonNull(seq.getPrefix(), ""));
        //产生订单号
        seq = seqService.getByCode(ORDER_SEQ_CODE, true);
        Set<String> set = new HashSet();
        int maxTimes = 10000 * 10;//期望10万，实际要去除重号,测试后大概不到7万
        SequenceIdUtils sequence = new SequenceIdUtils(0,0);
        long id;
        if (StringUtils.isNoneBlank(seq.getDateFormat())) {
            String now = DateUtils.formatDate(new Date(), seq.getDateFormat());
            if (!now.equalsIgnoreCase(seq.getPreviousDate())) {
                seq.setPreviousDate(now);
            }
            seqNo.append(MoreObjects.firstNonNull(seq.getSeparator(), ""))
                    .append(now.replace("-", ""))
                    .append(MoreObjects.firstNonNull(seq.getSeparator(), ""));
        } else {
            seq.setPreviousDigit(seq.getPreviousDigit() + 1);
            seqNo.append(MoreObjects.firstNonNull(seq.getSeparator(), ""));
        }
        for (int i = 0; i < maxTimes; i++) {
            id = sequence.nextId();
            set.add(seqNo.toString().concat(StringUtils.right(String.valueOf(id),5)).toString().trim());
        }
        seqService.updatePreviousDigit(seq);
        //分批写入
        //此处必须定义为List<Object>,否则调用redisUtils.sAdd时，当初Object参数使用
        List<Object> list = set.stream().collect(Collectors.toList());
        System.out.println("total:" + list.size());
        List<List<Object>> subList = Lists.partition(list,5000);
        for(int i=0,size=subList.size();i<size;i++){
            List<Object> slist = subList.get(i);
            redisUtilsLocal.sAdd(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, slist, OrderNoExpireSeconds);//2天
            if(i<size-1) {
                try {
                    java.lang.Thread.sleep(300);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }

        //释放锁
        redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);

        returnSeqNo =  (String) redisUtilsLocal.sPop(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, String.class);
        if (StringUtils.isBlank(returnSeqNo)) {
            throw new RuntimeException("无单号返回");
        } else {
            return returnSeqNo;
        }
    }

    /**
     * 将取到的订单序重新放入队列中，避免取光重新产生的情况
     * @param date
     * @param orderNo
     */
    public static void reputOrderNo(Date date, String orderNo) {
        try {
            if(date == null){
                date = new Date();
            }
            String KeyPostfix = DateUtils.formatDate(date, "yyyy-MM-dd");
            String seqKey = String.format(OrderNoKeyFormat, ORDER_SEQ_CODE, KeyPostfix);
            if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey)) {
                redisUtilsLocal.sAdd(RedisConstant.RedisDBType.REDIS_SEQ_DB, seqKey, orderNo, -1);
            }else{
                System.out.println("no key");
            }
        }catch (Exception e){
            //LogUtils.saveLog("退回订单号失败","SeqUtils.reputOrderNo",orderNo,e,null);
            log.error("退回订单号失败:{}",orderNo);
        }
    }

    /**
     * 将取到的订单序重新放入队列中，避免取光重新产生的情况
     * @param orderNo
     */
    public static void reputOrderNo(String orderNo) {
        reputOrderNo(new Date(),orderNo);
    }

    public static long NextID(){
        Sequence seq = new Sequence();
        seq.setCode("MYCATSEQ_GLOBAL");
        return seqService.NextIDValue(seq);
    }

    public static long NextIDValue(TableName tableName)
    {
        String strSeq = "";
        switch (tableName)
        {
            case Order:
                strSeq = ORDER_SEQUENCE;
                break;
            case SdMaterialMaster:
                strSeq = SD_MATERIAL_MASTER_SEQUENCE;
                break;
            case SdMaterialItem:
                strSeq = SD_MATERIAL_ITEM_SEQUENCE;
                break;
            case SdMaterialAttachment:
                strSeq = SD_MATERIAL_ATTACHMENT_SEQUENCE;
                break;
            case CustomerCharge:
                strSeq = CUSTOMER_CHARGE_SEQUENCE;
                break;
            case CustomerCurrency:
                strSeq = CUSTOMER_CURRENCY_SEQUENCE;
                break;
            case EngineerCharge:
                strSeq = ENGINEER_CHARGE_SEQUENCE;
                break;
            case EngineerChargeMaster:
                strSeq = ENGINEER_CHARGE_MASTER_SEQUENCE;
                break;
            case EngineerCurrency:
                strSeq = ENGINEER_CURRENCY_SEQUENCE;
                break;
            case ServicePointWithdraw:
                strSeq = SERVICE_POINT_WITHDRAW_SEQUENCE;
                break;
            case ServicePointPayableMonthly:
                strSeq = SERVICE_POINT_PAYABLE_MONTHLY_SEQUENCE;
                break;
            case ServicePointPaidMonthly:
                strSeq = SERVICE_POINT_PAID_MONTHLY_SEQUENCE;
                break;
            case ServicePointDeducted:
                strSeq = SERVICE_POINT_DEDUCTED_SEQUENCE;
                break;
            case RptCustomerInfoMonthly:
                strSeq = RPT_CUSTOMER_INFO_MONTHLY_SEQUENCE;
                break;
            case RptCustomerPlanDaily:
                strSeq = RPT_CUSTOMER_PLAN_DAILY_SEQUENCE;
                break;
            case RptCustomerPlanMonthly:
                strSeq = RPT_CUSTOMER_PLAN_MONTHLY_SEQUENCE;
                break;
            case RptCustomerPlanChargeDaily:
                strSeq = RPT_CUSTOMER_PLAN_CHARGE_DAILY_SEQUENCE;
                break;
            case RptCustomerPlanChargeMonthly:
                strSeq = RPT_CUSTOMER_PLAN_CHARGE_MONTHLY_SEQUENCE;
                break;
            case RptEngineerInfoMonthly:
                strSeq = RPT_ENGINEER_INFO_MONTHLY_SEQUENCE;
                break;
            case RptHisOrderDetail:
                strSeq = RPT_HIS_ORDER_DETAIL_SEQUENCE;
                break;
            case RptKefuChargeDaily:
                strSeq = RPT_KEFU_CHARGE_DAILY_SEQUENCE;
                break;
            case RptKefuChargeMonthly:
                strSeq = RPT_KEFU_CHARGE_MONTHLY_SEQUENCE;
                break;
            case RptKefuDaily:
                strSeq = RPT_KEFU_DAILY_SEQUENCE;
                break;
            case MqOrderCharge:
                strSeq = MQ_ORDER_CHARGE_SEQUENCE;
                break;
            case MqOrderHistory:
                strSeq = MQ_ORDER_HISTORY_SEQUENCE;
                break;

        }
        if (StringUtils.isEmpty(strSeq))
        {
            return 0;
        }
        else {
            Sequence seq = new Sequence();
            seq.setCode(strSeq);
            return seqService.NextIDValue(seq);
        }
    }

    public static String  ORDER_SEQUENCE  = "MYCATSEQ_ORDER";
    public static String  SD_MATERIAL_MASTER_SEQUENCE  = "MYCATSEQ_MATMASTER";
    public static String  SD_MATERIAL_ITEM_SEQUENCE  = "MYCATSEQ_MATITEM";
    public static String  SD_MATERIAL_ATTACHMENT_SEQUENCE  = "MYCATSEQ_MATATTA";
    public static String  CUSTOMER_CHARGE_SEQUENCE = "MYCATSEQ_CUSTOMERCHARGE";
    public static String  CUSTOMER_CURRENCY_SEQUENCE = "MYCATSEQ_CUSTOMERCURRENCY";
    public static String  ENGINEER_CHARGE_SEQUENCE = "MYCATSEQ_ENGINEERCHARGE";
    public static String  ENGINEER_CHARGE_MASTER_SEQUENCE = "MYCATSEQ_ENGINEERCHARGEMASTER";
    public static String  ENGINEER_CURRENCY_SEQUENCE = "MYCATSEQ_ENGINEERCURRENCY";
    public static String  SERVICE_POINT_WITHDRAW_SEQUENCE = "MYCATSEQ_SERVICEPOINTWITHDRAW";
    public static String  SERVICE_POINT_PAYABLE_MONTHLY_SEQUENCE = "MYCATSEQ_SERVICEPOINTPAYABLEMONTHLY";
    public static String  SERVICE_POINT_PAID_MONTHLY_SEQUENCE = "MYCATSEQ_SERVICEPOINTPAIDMONTHLY";
    public static String  SERVICE_POINT_DEDUCTED_SEQUENCE = "MYCATSEQ_SERVICEPOINTDEDUCTED";
    public static String  RPT_CUSTOMER_INFO_MONTHLY_SEQUENCE = "MYCATSEQ_RPTCUSTINFOMONTHLY";
    public static String  RPT_CUSTOMER_PLAN_DAILY_SEQUENCE = "MYCATSEQ_RPTCUSTPLANDAILY";
    public static String  RPT_CUSTOMER_PLAN_MONTHLY_SEQUENCE = "MYCATSEQ_RPTCUSTPLANMONTHLY";
    public static String  RPT_CUSTOMER_PLAN_CHARGE_DAILY_SEQUENCE = "MYCATSEQ_RPTCUSTPLANCHARGEDAILY";
    public static String  RPT_CUSTOMER_PLAN_CHARGE_MONTHLY_SEQUENCE = "MYCATSEQ_RPTCUSTPLANCHARGEMONTHLY";
    public static String  RPT_ENGINEER_INFO_MONTHLY_SEQUENCE = "MYCATSEQ_RPTENGINFOMONTHLY";
    public static String  RPT_HIS_ORDER_DETAIL_SEQUENCE = "MYCATSEQ_RPTHISORDERDETAIL";
    public static String  RPT_KEFU_CHARGE_DAILY_SEQUENCE = "MYCATSEQ_RPTKEFUCHARGEDAILY";
    public static String  RPT_KEFU_CHARGE_MONTHLY_SEQUENCE = "MYCATSEQ_RPTKEFUCHARGEMONTHLY";
    public static String  RPT_KEFU_DAILY_SEQUENCE = "MYCATSEQ_RPTKEFUDAILY";
    public static String  MQ_ORDER_CHARGE_SEQUENCE = "MYCATSEQ_MQORDERCHARGE";
    public static String  MQ_ORDER_HISTORY_SEQUENCE = "MYCATSEQ_MQORDERHISTORY";


    public enum  TableName
    {
        Order,
        SdMaterialMaster,
        SdMaterialItem,
        SdMaterialAttachment,
        CustomerCharge,
        CustomerCurrency,
        EngineerCharge,
        EngineerChargeMaster,
        EngineerCurrency,
        ServicePointWithdraw,
        ServicePointPayableMonthly,
        ServicePointPaidMonthly,
        ServicePointDeducted,
        RptCustomerInfoMonthly,
        RptCustomerPlanDaily,
        RptCustomerPlanMonthly,
        RptCustomerPlanChargeDaily,
        RptCustomerPlanChargeMonthly,
        RptEngineerInfoMonthly,
        RptHisOrderDetail,
        RptKefuChargeDaily,
        RptKefuChargeMonthly,
        RptKefuDaily,
        MqOrderCharge,
        MqOrderHistory,

    }
}



