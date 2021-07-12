package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.dao.CustomerFinanceDao;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerFinance;
import com.wolfking.jeesite.modules.md.entity.CustomerPrice;
import com.wolfking.jeesite.modules.md.entity.CustomerRequiredTagEnum;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.SystemService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerPriceService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created on 2017-04-12.
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CustomerService extends LongIDBaseService {

    @Resource
    private CustomerFinanceDao customerFinanceDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private MSCustomerService msCustomerService;

    @Autowired
    private MSCustomerPriceService msCustomerPriceService;


    public CustomerFinance getFinance(long id) {
        //切换为微服务
        CustomerFinance finance = customerFinanceDao.get(id);
        if (finance != null && finance.getPaymentType() != null && Integer.parseInt(finance.getPaymentType().getValue()) > 0) {
            String paymentTypeLabel = MSDictUtils.getDictLabel(finance.getPaymentType().getValue(), "PaymentType", "");
            finance.getPaymentType().setLabel(paymentTypeLabel);
        }
        return finance;
    }

    /**
     * 按用户id或者客户id查找客户列表
     *
     * @param paramMap
     * @return
     */
    public java.util.List<Customer> findListByUserIdOrCustomerId(java.util.Map<String, Object> paramMap) {
//        List<Customer> customerList = dao.findListByUserIdOrCustomerId(paramMap);
//        if (customerList != null && customerList.size() > 0) {
//            List<Long> salesList = customerList.stream().map(customer -> customer.getSales().getId()).collect(Collectors.toList());
//            Map<Long, User> userMap = MSUserUtils.getMapByUserIds(salesList);
//            for (Customer customer : customerList) {
//                User sales = userMap.get(customer.getId());
//                if (sales != null) {
//                    customer.getSales().setName(sales.getName());
//                    customer.getSales().setMobile(sales.getMobile());
//                    customer.getSales().setQq(sales.getQq());
//                }
//            }
//        }
//        return customerList;

        List<Customer> customerList = Lists.newArrayList();
        List<Long> customerIdList = systemService.findCustomerIdList(paramMap);
        if (customerIdList != null && !customerIdList.isEmpty()) {
            //String strIds = customerIdList.stream().map(Object::toString).collect(Collectors.joining(","));  //mark on 2020-3-17
            //customerList = msCustomerService.findBatchByIds(strIds);    //mark on 2020-3-17

            customerList = msCustomerService.findListByBatchIds(customerIdList);  //add on 2020-3-17

            if (customerList != null && customerList.size() > 0) {
                List<Long> salesList = customerList.stream().map(customer -> customer.getSales().getId()).collect(Collectors.toList());

                if (salesList != null && salesList.size() > 1) {
                    salesList = salesList.stream().distinct().collect(Collectors.toList());  // 去重复
                }
                Map<Long, User> userMap = MSUserUtils.getMapByUserIds(salesList);
                for (Customer customer : customerList) {
                    User sales = userMap.get(customer.getSales().getId());
                    if (sales != null) {
                        customer.getSales().setName(sales.getName());
                        customer.getSales().setMobile(sales.getMobile());
                        customer.getSales().setQq(sales.getQq());
                    }
                }
            }
        }
        return customerList;
    }

    /**
     * 根据客服id获取vip客户列表
     */
    public List<Customer> findVipListByKefu(Long kefuId) {
        List<Customer> customerList = null;
        List<Long> customerIdList = systemService.findVipCustomerIdListByKefu(kefuId);
        if (!CollectionUtils.isEmpty(customerIdList)) {
            //String strIds = customerIdList.stream().map(Object::toString).collect(Collectors.joining(","));  //mark on 2020-3-17
            //customerList = msCustomerService.findBatchByIds(strIds);   //mark on 2020-3-17
            customerList = msCustomerService.findListByBatchIds(customerIdList);  //add on 2020-3-17
        }
        return customerList == null ? Lists.newArrayList() : customerList;
    }

    /**
     * 从缓存读取客户信息
     * 只包含基本信息
     *
     * @param id
     * @return
     */
    public Customer getFromCache(long id) {
        // add on 2020-2-11
        Customer customerFromCache = msCustomerService.getFromCache(id);
        if (customerFromCache != null && customerFromCache.getSales() != null && customerFromCache.getSales().getId() != null) {
            User sales = MSUserUtils.get(customerFromCache.getSales().getId());
            if (sales != null) {
                customerFromCache.getSales().setName(sales.getName());
                customerFromCache.getSales().setMobile(sales.getMobile());
                customerFromCache.getSales().setQq(sales.getQq());
            }
            CustomerFinance customerFinance = getFinance(id);
            if (customerFinance != null) {
                customerFromCache.setFinance(customerFinance);
            }
        }
        // 读取跟单员信息
        if (customerFromCache != null && customerFromCache.getMerchandiser() != null && customerFromCache.getMerchandiser().getId() != null && customerFromCache.getMerchandiser().getId() > 0) {
            User merchandiser = MSUserUtils.get(customerFromCache.getMerchandiser().getId());
            customerFromCache.getMerchandiser().setName(merchandiser.getName());
            customerFromCache.getMerchandiser().setMobile(merchandiser.getMobile());
            customerFromCache.getMerchandiser().setQq(merchandiser.getQq());
        }
        // 为了防止调用的地方报错。
        if (customerFromCache == null) {
            customerFromCache = new Customer(id);
        }

        return customerFromCache;
        /*
        // mark on 2020-2-11 begin
        log.warn("1:{}",GsonUtils.toGsonString(customerFromCache));

        Customer customer = null;
        if (redisUtils.exists(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_CUSTOMER_ALL)) {
            customer = (Customer) redisUtils.zRangeOneByScore(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_CUSTOMER_ALL, id, id, Customer.class);
            if (customer == null) {
                customer = get(id);
//                if (customer != null) {  // mark on 2019-7-22
                if (customer != null && customer.getId() != null) { // add on 2019-7-22
                    redisUtils.zSetEX(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_CUSTOMER_ALL, customer, id, 0l);
                }
            }
            log.warn("2:{}",GsonUtils.toGsonString(customerFromCache));
            return customer;
        }
        //未装载客户列表
        loadAllCustomer();
        customer = (Customer) redisUtils.zRangeOneByScore(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_CUSTOMER_ALL, id, id, Customer.class);
        return customer;
        // mark on 2020-2-11 end
        */
    }

    /**
     * 从缓存读取客户信息
     * 按需读取，requiredTags = null，只读取客户基本信息
     */
    public Customer getFromCacheAsRequired(long id, Integer requiredTags) {
        // add on 2020-2-11
        Customer customer = msCustomerService.getFromCache(id);
        if (customer == null) {
            customer = new Customer(id);
            return customer;
        }
        if (requiredTags == null || requiredTags <= 0) {
            return customer;
        }
        if (CustomerRequiredTagEnum.FINANCE.hasTag(requiredTags)) {
            CustomerFinance customerFinance = getFinance(id);
            if (customerFinance != null) {
                customer.setFinance(customerFinance);
            }
        }
        if (CustomerRequiredTagEnum.SALE.hasTag(requiredTags)) {
            Long saleId = Optional.ofNullable(customer.getSales()).map(t -> t.getId()).orElse(0L);
            if (saleId > 0) {
                User sales = MSUserUtils.get(saleId);
                if (sales != null) {
                    customer.getSales().setName(sales.getName());
                    customer.getSales().setMobile(sales.getMobile());
                    customer.getSales().setQq(sales.getQq());
                }
            }
        }
        return customer;
    }

    /**
     * 读取某客户的价格清单
     * 先从缓存读取，缓存不存在从数据库读取，并更新缓存
     *
     * @param id 客户id
     * @return
     */
    public List<CustomerPrice> getPricesFromCache(Long id) {
        //调用微服务 add on 2019-11-6
        List<CustomerPrice> list = msCustomerPriceService.getPricesFromCache(id);
       /* if(listMD!=null && listMD.size()>0){
            return listMD;
        }*/
        //end
        // add on 比较微服务与web从缓存中读取的数据是否一致
       /* String strListFromMS = "";
        if(listMD !=null && listMD.size()>0){
            //Function<CustomerPrice, Integer> productSort = customerPrice->customerPrice.getProduct().getSort();
            listMD = listMD.stream().sorted(Comparator.comparing(CustomerPrice::getId)).collect(Collectors.toList());
            strListFromMS = GsonUtils.toGsonString(listMD);
        }
        List<CustomerPrice> list = getPricesFromCache(id,0);
        String strList = "";
        if(list !=null && list.size()>0){
            //Function<CustomerPrice, Integer> productSort = customerPrice->customerPrice.getProduct().getSort();
            list = list.stream().sorted(Comparator.comparing(CustomerPrice::getId)).collect(Collectors.toList());
            strList = GsonUtils.toGsonString(list);
        }
        if(strListFromMS.hashCode()!=strList.hashCode()){
            try {
                Customer customer = getFromCache(id);
                String customerName = "";
                if(customer !=null){
                    customerName = customer.getName();
                }
                log.error("客户:" + customerName + "从缓存取价格:微服务取的客户价格与web取得客户价格不一致,微服务客户价格:" + strListFromMS+ "web端客户价格:" + strList);
            }catch (Exception e){}
        }*/
        // end
        return list;
    }

}
