package com.wolfking.jeesite.modules.api.service.fi;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.api.dao.AppMyWalletDao;
import com.wolfking.jeesite.modules.api.entity.common.IntegerDoubleTuple;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.fi.mywallet.*;
import com.wolfking.jeesite.modules.api.util.FIEnums;
import com.wolfking.jeesite.modules.fi.dao.EngineerCurrencyDepositDao;
import com.wolfking.jeesite.modules.fi.entity.*;

import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServicePointFinance;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.md.utils.ProductUtils;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 我的钱包
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppMyWalletService extends LongIDBaseService {

    @Resource
    private AppMyWalletDao appMyWalletDao;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private EngineerCurrencyDepositDao engineerCurrencyDepositDao;

    /**
     * 获取网点余额信息
     */
    public AppGetServicePointBalanceResponse getServicePointBalance(Long servicePointId) {
        AppGetServicePointBalanceResponse response = new AppGetServicePointBalanceResponse();
        ServicePointFinance finance = appMyWalletDao.getServicePointBalance(servicePointId);

        if (finance != null) {
            response.setBalance(finance.getBalance());
            response.setPayable(finance.getBalance() + finance.getTotalAmount() - finance.getPlatformFee() );
            response.setPaid(finance.getTotalAmount() - finance.getPlatformFee());
            response.setInfoFee(finance.getInfoFee());
            response.setTaxFee(finance.getTaxFee());
            response.setCompletedCharge( response.getPayable()
                    - response.getInfoFee()
                    - response.getTaxFee()
                   );
        }
        EngineerCurrency firstCurrency = appMyWalletDao.getFirstCurrencyCreateDate(servicePointId);
        if (firstCurrency != null && firstCurrency.getCreateDate() != null) {
            response.setMonthCount(DateUtils.getDateDiffMonth(firstCurrency.getCreateDate(), new Date()) + 1);
        }
        return response;
    }


    /**
     * 获取网点余额信息(新)
     */
    public AppGetServicePointBalanceResponse getServicePointBalanceNew(Long servicePointId) {
        AppGetServicePointBalanceResponse response = new AppGetServicePointBalanceResponse();
        ServicePointFinance finance = appMyWalletDao.getServicePointBalance(servicePointId);

        if (finance != null) {
            response.setBalance(finance.getBalance()); //余额
            response.setPayable(finance.getBalance()
                    + finance.getTotalAmount()
                    - finance.getPlatformFee()
            );   //入账
            response.setPaid(finance.getTotalAmount()- finance.getPlatformFee());  //已提现
            response.setInfoFee(finance.getInfoFee());  //平台费
            response.setTaxFee(finance.getTaxFee());    //扣点
            response.setCompletedCharge( response.getPayable()
                    +(finance.getDeposit()-finance.getDepositRecharge())
            );     //完工
            response.setDeposit(0-(finance.getDeposit()-finance.getDepositRecharge())); //转质保金
        }
        EngineerCurrency firstCurrency = appMyWalletDao.getFirstCurrencyCreateDate(servicePointId);
        if (firstCurrency != null && firstCurrency.getCreateDate() != null) {
            response.setMonthCount(DateUtils.getDateDiffMonth(firstCurrency.getCreateDate(), new Date()) + 1);
        }
        return response;
    }

    /**
     * 获取网点完工单金额明细
     */
    public AppGetServicePointCompletedChargeListResponse getServicePointCompletedChargeList(Long servicePointId, Integer yearIndex, Integer monthIndex,
                                                                                            Page<EngineerChargeMaster> page) {
        Date beginCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex, 1));
        Date endCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex + 1, 1));
        String quarter = QuarterUtils.getSeasonQuarter(beginCreateDate);
        IntegerDoubleTuple tuple = appMyWalletDao.getServicePointCompletedChargeSummary(quarter, servicePointId, beginCreateDate, endCreateDate);
        Integer writeOffQty = appMyWalletDao.getServicePointWriteOffQty(quarter, servicePointId, beginCreateDate, endCreateDate);
        List<EngineerChargeMaster> chargeList = appMyWalletDao.getServicePointCompletedChargeList(quarter, servicePointId, beginCreateDate, endCreateDate, page);
        List<AppGetServicePointCompletedChargeListResponse.CompletedChargeItem> list = Lists.newArrayList();
        AppGetServicePointCompletedChargeListResponse.CompletedChargeItem chargeItem;
        String transactionTypeLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_COMPLETED_ORDER + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        for (EngineerChargeMaster item : chargeList) {
            chargeItem = new AppGetServicePointCompletedChargeListResponse.CompletedChargeItem();
            chargeItem.setItemId(item.getId());
            chargeItem.setOrderId(item.getOrderId());
            chargeItem.setQuarter(item.getQuarter());
            chargeItem.setCurrencyNo(item.getOrderNo());
            chargeItem.getTransactionType().setLabel(transactionTypeLabel);
            chargeItem.setCreateDate(item.getCreateDate().getTime());
            double amount = item.getServiceCharge()
                    + item.getExpressCharge()
                    + item.getMaterialCharge()
                    + item.getTravelCharge()
                    + item.getOtherCharge()
                    + item.getCustomerTimeLinessCharge()
                    + item.getTimeLinessCharge()
                    + item.getUrgentCharge()
                    + item.getPraiseFee()
                    + item.getInsuranceCharge();
            chargeItem.setAmount(amount);
            list.add(chargeItem);
        }
        Page<AppGetServicePointCompletedChargeListResponse.CompletedChargeItem> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        if (!list.isEmpty()) {
            rtnPage.setList(list);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }

        AppGetServicePointCompletedChargeListResponse response = new AppGetServicePointCompletedChargeListResponse();
        response.setYearIndex(yearIndex);
        response.setMonthIndex(monthIndex);
        response.setPageNo(rtnPage.getPageNo());
        response.setPageSize(rtnPage.getPageSize());
        response.setRowCount(rtnPage.getCount());
        response.setPageCount(rtnPage.getPageCount());
        response.setList(rtnPage.getList());
        if (tuple != null) {
            response.setCompletedCharge(tuple.getBElement() == null ? 0.0 : tuple.getBElement());
            response.setCompletedQty(tuple.getAElement() == null ? 0 : tuple.getAElement());
        }
        response.setWriteOffQty(writeOffQty == null ? 0 : writeOffQty);
        return response;
    }


    /**
     * 获取网点完工单金额明细(新)
     */
    public AppGetServicePointCompletedChargeListResponse getServicePointCompletedChargeListNew(Long servicePointId, Integer yearIndex, Integer monthIndex,
                                                                                            Page<EngineerChargeMaster> page) {
        Date beginCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex, 1));
        Date endCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex + 1, 1));
        String quarter = QuarterUtils.getSeasonQuarter(beginCreateDate);
        IntegerDoubleTuple tuple = appMyWalletDao.getServicePointCompletedChargeFeeSummary(quarter, servicePointId, beginCreateDate, endCreateDate);
        Integer writeOffQty = appMyWalletDao.getServicePointWriteOffQty(quarter, servicePointId, beginCreateDate, endCreateDate);

        Double taxFeeSummary = appMyWalletDao.getServicePointTaxFeeSummary(quarter, servicePointId, beginCreateDate, endCreateDate);
        Double infoFeeSummary = appMyWalletDao.getServicePointInfoFeeSummary(quarter, servicePointId, beginCreateDate, endCreateDate);

        List<EngineerChargeMaster> chargeList = appMyWalletDao.getServicePointCompletedChargeList(quarter, servicePointId, beginCreateDate, endCreateDate, page);
        List<AppGetServicePointCompletedChargeListResponse.CompletedChargeItem> list = Lists.newArrayList();
        AppGetServicePointCompletedChargeListResponse.CompletedChargeItem chargeItem;
        String transactionTypeLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_COMPLETED_ORDER + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        for (EngineerChargeMaster item : chargeList) {
            chargeItem = new AppGetServicePointCompletedChargeListResponse.CompletedChargeItem();
            chargeItem.setItemId(item.getId());
            chargeItem.setOrderId(item.getOrderId());
            chargeItem.setQuarter(item.getQuarter());
            chargeItem.setCurrencyNo(item.getOrderNo());
            chargeItem.getTransactionType().setLabel(transactionTypeLabel);
            chargeItem.setCreateDate(item.getCreateDate().getTime());
            double amount = item.getServiceCharge()
                    + item.getExpressCharge()
                    + item.getMaterialCharge()
                    + item.getTravelCharge()
                    + item.getOtherCharge()
                    + item.getCustomerTimeLinessCharge()
                    + item.getTimeLinessCharge()
                    + item.getUrgentCharge()
                    + item.getPraiseFee()
                    + item.getInsuranceCharge();
            chargeItem.setAmount(amount);
            list.add(chargeItem);
        }
        Page<AppGetServicePointCompletedChargeListResponse.CompletedChargeItem> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        if (!list.isEmpty()) {
            rtnPage.setList(list);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        AppGetServicePointCompletedChargeListResponse response = new AppGetServicePointCompletedChargeListResponse();
        response.setYearIndex(yearIndex);
        response.setMonthIndex(monthIndex);
        response.setPageNo(rtnPage.getPageNo());
        response.setPageSize(rtnPage.getPageSize());
        response.setRowCount(rtnPage.getCount());
        response.setPageCount(rtnPage.getPageCount());
        response.setList(rtnPage.getList());
        response.setTaxFee(taxFeeSummary == null ? 0.0 : 0-taxFeeSummary);  //扣点月份
        response.setInfoFee(infoFeeSummary == null ? 0.0 : 0-infoFeeSummary);    //平台管理费月份
        if (tuple != null) {
            response.setCompletedCharge(tuple.getBElement() == null ? 0.0 : tuple.getBElement());
            response.setCompletedQty(tuple.getAElement() == null ? 0 : tuple.getAElement());
        }
        response.setWriteOffQty(writeOffQty == null ? 0 : writeOffQty);
        return response;
    }

    /**
     * 获取网点退补单金额明细
     */
    public AppGetServicePointWriteOffChargeListResponse getServicePointWriteOffChargeList(Long servicePointId, Integer yearIndex, Integer monthIndex,
                                                                                          Page<EngineerCharge> page) {
        Date beginCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex, 1));
        Date endCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex + 1, 1));
        String quarter = QuarterUtils.getSeasonQuarter(beginCreateDate);
        IntegerDoubleTuple tuple = appMyWalletDao.getServicePointWriteOffChargeSummary(quarter, servicePointId, beginCreateDate, endCreateDate);
        Integer completedQty = appMyWalletDao.getServicePointCompletedQty(quarter, servicePointId, beginCreateDate, endCreateDate);
        List<EngineerCharge> chargeList = appMyWalletDao.getServicePointWriteOffChargeList(quarter, servicePointId, beginCreateDate, endCreateDate, page);
        String transactionTypeInLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_WRITE_OFF_IN + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        String transactionTypeOutLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_WRITE_OFF_OUT + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        List<AppGetServicePointWriteOffChargeListResponse.WriteOffChargeItem> list = Lists.newArrayList();
        AppGetServicePointWriteOffChargeListResponse.WriteOffChargeItem chargeItem;
        for (EngineerCharge item : chargeList) {
            chargeItem = new AppGetServicePointWriteOffChargeListResponse.WriteOffChargeItem();
            chargeItem.setItemId(item.getId());
            chargeItem.setOrderId(item.getOrderId());
            chargeItem.setQuarter(item.getQuarter());
            chargeItem.setCurrencyNo(item.getOrderNo());
            chargeItem.setCreateDate(item.getCreateDate().getTime());
            double amount = item.getServiceCharge() + item.getExpressCharge() + item.getMaterialCharge() + item.getTravelCharge() + item.getOtherCharge();
            chargeItem.setAmount(amount);
            chargeItem.getTransactionType().setLabel(amount > 0 ? transactionTypeInLabel : transactionTypeOutLabel);
            list.add(chargeItem);
        }
        Page<AppGetServicePointWriteOffChargeListResponse.WriteOffChargeItem> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        if (!list.isEmpty()) {
            rtnPage.setList(list);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }

        AppGetServicePointWriteOffChargeListResponse response = new AppGetServicePointWriteOffChargeListResponse();
        response.setYearIndex(yearIndex);
        response.setMonthIndex(monthIndex);
        response.setPageNo(rtnPage.getPageNo());
        response.setPageSize(rtnPage.getPageSize());
        response.setRowCount(rtnPage.getCount());
        response.setPageCount(rtnPage.getPageCount());
        response.setList(rtnPage.getList());
        if (tuple != null) {
            response.setWriteOffCharge(tuple.getBElement() == null ? 0.0 : tuple.getBElement());
            response.setWriteOffQty(tuple.getAElement() == null ? 0 : tuple.getAElement());
        }
        response.setCompletedQty(completedQty == null ? 0 : completedQty);
        return response;
    }

    /**
     * 获取网点提现明细
     */
    public AppGetServicePointWithdrawListResponse getServicePointWithdrawList(Long servicePointId, Integer yearIndex, Integer monthIndex,
                                                                              Page<EngineerCurrency> page) {
        Date beginCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex, 1));
        Date endCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex + 1, 1));
        String quarter = QuarterUtils.getSeasonQuarter(beginCreateDate);
        Double totalWithdrawCharge = appMyWalletDao.getServicePointWithdrawTotalCharge(quarter, servicePointId, beginCreateDate, endCreateDate);
        List<EngineerCurrency> withdrawList = appMyWalletDao.getServicePointWithdrawList(quarter, servicePointId, beginCreateDate, endCreateDate, page);
        Map<String, Dict> paymentTypeMap = MSDictUtils.getDictMap(Dict.DICT_TYPE_PAYMENT_TYPE);
        String transactionTypeLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_WITHDRAW + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        List<AppGetServicePointWithdrawListResponse.WithdrawItem> list = Lists.newArrayList();
        AppGetServicePointWithdrawListResponse.WithdrawItem withdrawItem;
        for (EngineerCurrency item : withdrawList) {
            withdrawItem = new AppGetServicePointWithdrawListResponse.WithdrawItem();
            withdrawItem.setItemId(item.getId());
            withdrawItem.setCurrencyNo(item.getCurrencyNo());
            withdrawItem.getTransactionType().setLabel(transactionTypeLabel);
            withdrawItem.setCreateDate(item.getCreateDate().getTime());
            Dict paymentTypeDict = paymentTypeMap.get(item.getPaymentType().toString());
            if (paymentTypeDict != null) {
                withdrawItem.getPaymentType().setLabel(paymentTypeDict.getLabel());
            }
            withdrawItem.setAmount(-item.getAmount());
            withdrawItem.setRemarks(item.getRemarks());
            list.add(withdrawItem);
        }
        Page<AppGetServicePointWithdrawListResponse.WithdrawItem> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        if (!list.isEmpty()) {
            rtnPage.setList(list);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }

        AppGetServicePointWithdrawListResponse response = new AppGetServicePointWithdrawListResponse();
        response.setYearIndex(yearIndex);
        response.setMonthIndex(monthIndex);
        response.setPageNo(rtnPage.getPageNo());
        response.setPageSize(rtnPage.getPageSize());
        response.setRowCount(rtnPage.getCount());
        response.setPageCount(rtnPage.getPageCount());
        response.setList(rtnPage.getList());
        response.setWithdrawCharge(totalWithdrawCharge == null ? 0.0 : totalWithdrawCharge);
        return response;
    }

    /**
     * 获取网点完工账单项详情
     */
    public AppGetServicePointCompletedChargeDetailResponse getServicePointCompletedChargeDetail(String quarter, Long engineerChargeMasterId) {
        EngineerChargeMaster master = appMyWalletDao.getServicePointCompletedChargeDetail(quarter, engineerChargeMasterId);
        if (master == null) {
            throw new RestAppException("读取账单明细失败");
        }
        String orderQuarter = QuarterUtils.getOrderQuarterFromNo(master.getOrderNo());
        OrderCondition condition = appMyWalletDao.getOrderInfo(orderQuarter, master.getOrderId());
        if (condition == null) {
            throw new RestAppException("读取账单明细的工单信息失败");
        }
        List<OrderDetail> details = appMyWalletDao.getOrderDetailList(orderQuarter, master.getOrderId());

        String transactionTypeLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_COMPLETED_ORDER + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        AppGetServicePointCompletedChargeDetailResponse response = new AppGetServicePointCompletedChargeDetailResponse();
        response.getTransactionType().setLabel(transactionTypeLabel);
        double amount = master.getServiceCharge() + master.getExpressCharge() + master.getMaterialCharge() + master.getTravelCharge() + master.getOtherCharge()
                + master.getCustomerTimeLinessCharge() + master.getTimeLinessCharge() + master.getUrgentCharge() + master.getPraiseFee()
                + master.getInsuranceCharge();
        response.setAmount(amount);

        AppGetServicePointCompletedChargeDetailResponse.ChargeDetail chargeDetail = new AppGetServicePointCompletedChargeDetailResponse.ChargeDetail();
        chargeDetail.setServiceCharge(master.getServiceCharge());
        chargeDetail.setExpressCharge(master.getExpressCharge());
        chargeDetail.setMaterialCharge(master.getMaterialCharge());
        chargeDetail.setTravelCharge(master.getTravelCharge());
        chargeDetail.setOtherCharge(master.getOtherCharge());
        chargeDetail.setCustomerTimeLinessCharge(master.getCustomerTimeLinessCharge());
        chargeDetail.setTimeLinessCharge(master.getTimeLinessCharge());
        chargeDetail.setUrgentCharge(master.getUrgentCharge());
        chargeDetail.setPraiseFee(master.getPraiseFee());
        chargeDetail.setInsuranceCharge(master.getInsuranceCharge());
        response.setChargeDetail(chargeDetail);

        AppGetServicePointCompletedChargeDetailResponse.OrderInfo orderInfo = new AppGetServicePointCompletedChargeDetailResponse.OrderInfo();
        orderInfo.setOrderNo(master.getOrderNo());
        orderInfo.setUserName(condition.getUserName());
        orderInfo.setServicePhone(condition.getServicePhone());
        orderInfo.setServiceAddress(condition.getAreaName() + condition.getServiceAddress());
        orderInfo.setChargeDate(master.getCreateDate().getTime());
        if (!details.isEmpty()) {
            AppGetServicePointCompletedChargeDetailResponse.OrderInfo.ServiceItem serviceItem;
            Map<Long, Product> productMap = ProductUtils.getAllProductMap();
            Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
            Product product;
            ServiceType serviceType;
            for (OrderDetail detail : details) {
                serviceItem = new AppGetServicePointCompletedChargeDetailResponse.OrderInfo.ServiceItem();
                serviceItem.setServiceTimes(detail.getServiceTimes());
                serviceItem.setQty(detail.getQty());
                product = productMap.get(detail.getProductId());
                if (product != null) {
                    serviceItem.setProductName(product.getName());
                }
                serviceType = serviceTypeMap.get(detail.getServiceType().getId());
                if (serviceType != null) {
                    serviceItem.setServiceTypeName(serviceType.getName());
                }
                orderInfo.getServices().add(serviceItem);
            }
        }
        response.setOrderInfo(orderInfo);

        return response;
    }

    /**
     * 获取网点退补账单项详情
     */
    public AppGetServicePointWriteOffChargeDetailResponse getServicePointWriteChargeDetail(String quarter, Long engineerChargeId) {
        EngineerCharge engineerCharge = appMyWalletDao.getServicePointWriteOffChargeDetail(quarter, engineerChargeId);
        if (engineerCharge == null) {
            throw new RestAppException("读取账单明细失败");
        }
        String orderQuarter = QuarterUtils.getOrderQuarterFromNo(engineerCharge.getOrderNo());
        OrderCondition condition = appMyWalletDao.getOrderInfo(orderQuarter, engineerCharge.getOrderId());
        if (condition == null) {
            condition = appMyWalletDao.getOrderInfo(null, engineerCharge.getOrderId());
        }
        if (condition == null) {
            throw new RestAppException("读取账单明细的工单信息失败");
        }
        List<OrderDetail> details = appMyWalletDao.getOrderDetailList(orderQuarter, engineerCharge.getOrderId());

        String transactionTypeInLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_WRITE_OFF_IN + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        String transactionTypeOutLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_WRITE_OFF_OUT + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        AppGetServicePointWriteOffChargeDetailResponse response = new AppGetServicePointWriteOffChargeDetailResponse();
        double amount = engineerCharge.getServiceCharge() + engineerCharge.getExpressCharge() + engineerCharge.getMaterialCharge() + engineerCharge.getTravelCharge() + engineerCharge.getOtherCharge();
        response.setAmount(amount);
        response.getTransactionType().setLabel(amount > 0 ? transactionTypeInLabel : transactionTypeOutLabel);

        AppGetServicePointWriteOffChargeDetailResponse.ChargeDetail chargeDetail = new AppGetServicePointWriteOffChargeDetailResponse.ChargeDetail();
        chargeDetail.setServiceCharge(engineerCharge.getServiceCharge());
        chargeDetail.setExpressCharge(engineerCharge.getExpressCharge());
        chargeDetail.setMaterialCharge(engineerCharge.getMaterialCharge());
        chargeDetail.setTravelCharge(engineerCharge.getTravelCharge());
        chargeDetail.setOtherCharge(engineerCharge.getOtherCharge());
        chargeDetail.setCreateDate(engineerCharge.getCreateDate().getTime());
        chargeDetail.setRemarks(engineerCharge.getRemarks());
        response.setChargeDetail(chargeDetail);

        AppGetServicePointWriteOffChargeDetailResponse.OrderInfo orderInfo = new AppGetServicePointWriteOffChargeDetailResponse.OrderInfo();
        orderInfo.setOrderNo(condition.getOrderNo());
        orderInfo.setUserName(condition.getUserName());
        orderInfo.setServicePhone(condition.getServicePhone());
        orderInfo.setServiceAddress(condition.getAreaName() + condition.getServiceAddress());
        if (!details.isEmpty()) {
            AppGetServicePointWriteOffChargeDetailResponse.OrderInfo.ServiceItem serviceItem;
            Map<Long, Product> productMap = ProductUtils.getAllProductMap();
            Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
            Product product;
            ServiceType serviceType;
            for (OrderDetail detail : details) {
                serviceItem = new AppGetServicePointWriteOffChargeDetailResponse.OrderInfo.ServiceItem();
                serviceItem.setServiceTimes(detail.getServiceTimes());
                serviceItem.setQty(detail.getQty());
                product = productMap.get(detail.getProductId());
                if (product != null) {
                    serviceItem.setProductName(product.getName());
                }
                serviceType = serviceTypeMap.get(detail.getServiceType().getId());
                if (serviceType != null) {
                    serviceItem.setServiceTypeName(serviceType.getName());
                }
                orderInfo.getServices().add(serviceItem);
            }
        }
        response.setOrderInfo(orderInfo);

        return response;
    }


    /**
     * 获取质保金列表
     */
    public AppGetServicePointDepositListResponse getServicePointDepositList(Long servicePointId, Integer yearIndex, Integer monthIndex,
                                                                              Page<EngineerCurrencyDeposit> page) {
        Date beginCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex, 1));
        Date endCreateDate = DateUtils.getStartOfDay(DateUtils.getDate(yearIndex, monthIndex + 1, 1));

        String quarter = QuarterUtils.getSeasonQuarter(beginCreateDate);
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH )+1;
        int year = cal.get(Calendar.YEAR);
        Double recharge = 0.0;
        Double completeCharge = 0.0;
        List<EngineerCurrencyDeposit> depositList = Lists.newArrayList();
        if (yearIndex<=year && monthIndex <= month){
            recharge = engineerCurrencyDepositDao.getServicePointDepositRechargeTotalMonth(quarter, servicePointId, beginCreateDate, endCreateDate);
            completeCharge = engineerCurrencyDepositDao.getServicePointDepositCompleteTotalMonth(quarter, servicePointId, beginCreateDate, endCreateDate);
            depositList = engineerCurrencyDepositDao.getDepositList(quarter, servicePointId, beginCreateDate, endCreateDate, page);
        }
        ServicePointDeposit servicePointDeposit = engineerCurrencyDepositDao.getServicePointDeposit(servicePointId);

        String transactionTypeRechargeLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_DEPOSIT_RECHARGE + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        String transactionTypeCompleteLabel = MSDictUtils.getDictLabel(AppWalletConstrant.TRANSACTION_TYPE_DEPOSIT_COMPLETE + "", Dict.DICT_TYPE_APP_WALLET_TRANSACTION_TYPE, "");
        List<AppGetServicePointDepositListResponse.DepositItem> list = Lists.newArrayList();
        AppGetServicePointDepositListResponse.DepositItem depositItem;
        for (EngineerCurrencyDeposit item : depositList) {
            depositItem = new AppGetServicePointDepositListResponse.DepositItem();
            depositItem.setItemId(item.getId());
            if (item.getActionType() == FIEnums.DepositActionTypeENum.OFFLINE_RECHARGE.getValue()){  //10线下充值
                depositItem.getTransactionType().setValue(String.valueOf(AppWalletConstrant.TRANSACTION_TYPE_DEPOSIT_RECHARGE));
                depositItem.getTransactionType().setLabel(transactionTypeRechargeLabel);
            }else if (item.getActionType() == FIEnums.DepositActionTypeENum.ORDER_DEDUCTION.getValue()){ //20 完工
                depositItem.getTransactionType().setValue(String.valueOf(AppWalletConstrant.TRANSACTION_TYPE_DEPOSIT_COMPLETE));
                depositItem.getTransactionType().setLabel(transactionTypeCompleteLabel);
            }

            if (item.getPaymentType() == FIEnums.DepositPaymentTypeENum.CASH.getValue()){
                depositItem.getPaymentType().setValue(String.valueOf(FIEnums.DepositPaymentTypeENum.CASH.getValue()));
                depositItem.getPaymentType().setLabel(FIEnums.DepositPaymentTypeENum.CASH.getName());
            }else if (item.getPaymentType() == FIEnums.DepositPaymentTypeENum.UNIONPAY.getValue()){
                depositItem.getPaymentType().setValue(String.valueOf(FIEnums.DepositPaymentTypeENum.UNIONPAY.getValue()));
                depositItem.getPaymentType().setLabel(FIEnums.DepositPaymentTypeENum.UNIONPAY.getName());
            }else if (item.getPaymentType() == FIEnums.DepositPaymentTypeENum.ALIPAY.getValue()){
                depositItem.getPaymentType().setValue(String.valueOf(FIEnums.DepositPaymentTypeENum.ALIPAY.getValue()));
                depositItem.getPaymentType().setLabel(FIEnums.DepositPaymentTypeENum.ALIPAY.getName());
            }else if (item.getPaymentType() == FIEnums.DepositPaymentTypeENum.WEIXIN.getValue()){
                depositItem.getPaymentType().setValue(String.valueOf(FIEnums.DepositPaymentTypeENum.WEIXIN.getValue()));
                depositItem.getPaymentType().setLabel(FIEnums.DepositPaymentTypeENum.WEIXIN.getName());
            }else {
                depositItem.getPaymentType().setValue(item.getPaymentType().toString());
                depositItem.getPaymentType().setLabel("");
            }

            depositItem.setCurrencyNo(item.getCurrencyNo());
            depositItem.setCreateDate(item.getCreateDate().getTime());
            depositItem.setAmount(item.getAmount());
            depositItem.setRemarks(item.getRemarks());
            list.add(depositItem);
        }
        Page<AppGetServicePointDepositListResponse.DepositItem> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        if (!list.isEmpty()) {
            rtnPage.setList(list);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        AppGetServicePointDepositListResponse response = new AppGetServicePointDepositListResponse();
        response.setYearIndex(yearIndex);
        response.setMonthIndex(monthIndex);
        response.setPageNo(rtnPage.getPageNo());
        response.setPageSize(rtnPage.getPageSize());
        response.setRowCount(rtnPage.getCount());
        response.setPageCount(rtnPage.getPageCount());
        response.setList(rtnPage.getList());
        response.setRechargeDeposit(recharge == null ? 0 : recharge);
        response.setOrderDeposit(completeCharge == null ? 0 : completeCharge);
        response.setDeposit(response.getRechargeDeposit() + response.getOrderDeposit());
        response.setTotalOrderDeposit(
                (servicePointDeposit.getDeposit()==null?0:servicePointDeposit.getDeposit())-
                (servicePointDeposit.getDepositRecharge()==null?0:servicePointDeposit.getDepositRecharge())
        );
        response.setTotalRechargeDeposit(servicePointDeposit.getDepositRecharge() == null ? 0 : servicePointDeposit.getDepositRecharge());
        response.setTotalDeposit(response.getTotalOrderDeposit() + response.getTotalRechargeDeposit());
        return response;
    }


}
