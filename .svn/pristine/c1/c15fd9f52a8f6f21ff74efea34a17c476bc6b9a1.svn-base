package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomer;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MSCustomerService {
    @Autowired
    private MSCustomerFeign msCustomerFeign;


    /**
     * 根据id获取单个客户信息
     * @param id
     * @return
     * id
     * name
     * salesId
     * paymentType
     */
    public Customer get(Long id) {
        return MDUtils.getById(id, Customer.class, msCustomerFeign::get);
    }

    /**
     * 从缓存中获取客户信息
     * @param id
     * @return
     */
    public Customer getFromCache(Long id) {
        return MDUtils.getById(id, Customer.class, msCustomerFeign::getFromCache);
    }

    /**
     * 根据ID获取客户信息
     * @param id
     * @return
     * id
     * code
     * name
     * salesId
     * remarks
     */
    public Customer getByIdToCustomer(Long id) {
        return MDUtils.getById(id, Customer.class, msCustomerFeign::getByIdToCustomer);
    }


    /**
     * 根据id获取customer列表
     * @param ids
     * @return
     *   id,code,name,salesId,contractDate
     */
    public List<Customer> findListByBatchIds(List<Long> ids) {
        // add on 2019-10-16
        List<Customer> customerList = Lists.newArrayList();
        if (ids != null && !ids.isEmpty()) {
            Lists.partition(ids, 100).forEach(longList -> {
                List<Customer> customersFromMS = MDUtils.findListNecessaryConvertType(Customer.class,()->msCustomerFeign.findByBatchIds(longList));
                if (customersFromMS != null && !customersFromMS.isEmpty()) {
                    customerList.addAll(customersFromMS);
                }
            });
        }
        return customerList;
    }

    /**
     * 获取所有customer
     * @return
     * id,
     * code,
     * name,
     * full_name,
     * vip,
     * salesman,
     * salesman_phone,
     * salesman_qq,
     * address,
     * zip_code,
     * master,
     * mobile,
     * phone,
     * fax,
     * email,
     * contract_date,
     * project_owner,
     * project_owner_phone,
     * project_owner_qq,
     * service_owner,
     * service_owner_phone,
     * service_owner_qq,
     * finance_owner,
     * finance_owner_phone,
     * finance_owner_qq,
     * technology_owner,
     * technology_owner_phone,
     * technology_owner_qq,
     * default_brand,
     * effect_flag,
     * logo,
     * attachment1,
     * attachment2,
     * attachment3,
     * attachment4,
     * isfrontshow,
     * sort,
     * description,
     * min_upload_number,
     * max_upload_number,
     * return_address,
     * order_approve_flag,
     * remarks,
     * sales_id,
     * short_message_flag,
     * time_liness_flag,
     * urgent_flag,
     * payment_type,
     * createById,
     * updateById,
     * create_date,
     * update_date
     */
    public List<Customer> findAll() {
        return MDUtils.findAllList(Customer.class, msCustomerFeign::findAll);
    }

    public List<NameValuePair<Long, String>> findBatchListByIds(List<Long> ids) {
        List<NameValuePair<Long, String>> customerList = Lists.newArrayListWithCapacity(ids.size());
        if (ids != null && !ids.isEmpty()) {
            Lists.partition(ids, 100).forEach(longList -> {
                List<NameValuePair<Long, String>> customersFromMS = msCustomerFeign.findBatchListByIds(longList).getData();
                if (customersFromMS != null && !customersFromMS.isEmpty()) {
                    customerList.addAll(customersFromMS);
                }
            });
        }
        return customerList;
    }

    /**
     * 获取所有客户列表
     * @return
     * id
     * code
     * name
     * contractDate
     * salesMan
     * paymentType
     */
    public List<Customer> findAllSpecifiedColumn() {
        return MDUtils.findAllList(Customer.class, msCustomerFeign::findAllSpecifiedColumn);
    }


    /**
     * 获取所有客户列表
     * @return
     * id
     * name
     */
    public List<Customer> findAllCustomerList() {
        List<Customer> customerList = Lists.newArrayList();
        customerList = MDUtils.findAllList(Customer.class, msCustomerFeign::findAllWithIdAndName);
        return customerList!=null && !customerList.isEmpty() ?customerList.stream().sorted(Comparator.comparing(Customer::getName)).collect(Collectors.toList()):Lists.newArrayList();
    }

    /**
     * 根据业务员id获取客户列表
     * @param salesId
     * @return
     * id
     * name
     */
    public List<Customer> findListBySalesId(Integer salesId) {
        List<Customer> customerList = Lists.newArrayList();
        customerList = MDUtils.findListByCustomCondition(salesId, Customer.class, msCustomerFeign::findListBySalesId);
        return customerList!=null && !customerList.isEmpty() ?customerList.stream().sorted(Comparator.comparing(Customer::getName)).collect(Collectors.toList()):Lists.newArrayList();
    }

    /**
     * 根据跟单员id获取客户列表  // add on 2019-11-22
     * @param merchandiserId
     * @return
     */
    public List<Customer> findListByMerchandiserId(Long merchandiserId) {
        List<Customer> customerList = MDUtils.findListByCustomCondition(merchandiserId, Customer.class, msCustomerFeign::findListByMerchandiserId);
        return customerList!=null && !customerList.isEmpty() ?customerList.stream().sorted(Comparator.comparing(Customer::getName)).collect(Collectors.toList()):Lists.newArrayList();
    }

    /**
     * 获取VIP客户列表
     * @return
     *   id,name
     */
    public List<Customer> findListByVipCustomer() {
        return MDUtils.findAllList(Customer.class, msCustomerFeign::findListByVipCustomer);
    }


}
