package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomer;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerFeignFallbackFactory;
import org.slf4j.MDC;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerFeignFallbackFactory.class)
public interface MSCustomerFeign {
    /**
     * 根据ID获取客户信息
     * @param id
     * @return
     * id
     * name
     * salesId
     * paymentType
     */
    @GetMapping("/customer/get/{id}")
    MSResponse<MDCustomer> get(@PathVariable("id") Long id );

    /**
     * 根据ID从缓存中获取客户信息(输出字段与Web中从缓存获取客户信息相同)
     * @param id
     * @return
     * id,Name,SalesId,Master,Phone,ContractDate
     * MinUploadNumber,MaxUploadNumber,ReturnAddress,
     * PaymentType,Remarks,DefaultBrand,EffectFlag
     * ShortMessageFlag,TimeLinessFlag, UrgentFlag,tVipFlag
     */
    @GetMapping("/customer/getFromCache/{id}")
    MSResponse<MDCustomer> getFromCache(@PathVariable("id") Long id);

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
    @GetMapping("/customer/getByIdToCustomer/{id}")
    MSResponse<MDCustomer> getByIdToCustomer(@PathVariable("id") Long id );


    /**
     * 通过id获取客户信息
     * @param id
     * @return
     * id,
     * phone,
     * logoId,
     * code,
     * name,
     * fullName,
     * master,
     * sales,
     * zipcode,
     * email,
     * address,
     * fax,
     * contractDate,
     * remarks,
     * minUploadNumber,
     * maxUploadNumber,
     * returnAddress,
     * effectFlag,
     * shortMessageFlag,
     * timeLinessFlag,
     * urgentFlag,
     * projectOwner,
     * projectOwnerPhone,
     * projectOwnerQq,
     * serviceOwner,
     * serviceOwnerPhone,
     * serviceOwnerQq,
     * financeOwner,
     * financeOwnerPhone,
     * financeOwnerQq,
     * technologyOwner,
     * technologyOwnerPhone,
     * technologyOwnerQq,
     * useDefaultPrice,
     * attachment1Id,
     * attachment2Id,
     * attachment3Id,
     * attachment4Id,
     */
    @GetMapping("/customer/getByIdToCustomerSpecifiedColumn/{id}")
    MSResponse<MDCustomer> getByIdToCustomerSpecifiedColumn(@PathVariable("id") Long id);

    /**
     * 根据Code获取单个客户ID
     * @param code
     * @return
     * id
     */
    @GetMapping("/customer/getCustomerIdByCode/{code}")
    MSResponse<Long> getCustomerIdByCode(@PathVariable("code") String code);

    /**
     * 根据id获取customer信息列表
     * @param ids
     * @return
     * id
     * code
     * name
     */
    @GetMapping("/customer/findBatchByIds/{ids}")
    MSResponse<List<MDCustomer>> findBatchByIds(@PathVariable("ids") String ids);

    /**
     * 根据id列表获取客户列表信息
     * @param ids
     * @return
     * id,code,name,paymenttype,salesid,contractDate
     */
    @PostMapping("/customer/findCustomersWithIds")
    MSResponse<List<MDCustomer>>  findCustomersWithIds(@RequestBody List<Long> ids);

    /**
     * 根据id列表获取customer信息列表
     * @param ids
     * @return
     * id
     * code
     * name
     */
    @PostMapping("/customer/findByBatchIds/")
    MSResponse<List<MDCustomer>> findByBatchIds(@RequestBody List<Long> ids);

    /**
     * 根据id列表获取customer的id与name 两个字段的列表  2020-3-17
     * @param ids
     * @return
     */
    @PostMapping("/customer/findIdAndNameListByIds")
    MSResponse<List<MDCustomer>> findIdAndNameListByIds(@RequestBody List<Long> ids);

    /**
     * 查找所有的非贵宾客户  2020-3-17
     * @return
     */
    @GetMapping("/customer/findNoVIPList")
    MSResponse<List<MDCustomer>> findNoVIPList();

    /**
     * 获取客户列表
     * @return
     */
    @GetMapping("/customer/findAll")
    MSResponse<List<MDCustomer>> findAll();

    /**
     * 批量获取客户id/name
     * @param ids
     * @return
     */
    @PostMapping("/customer/findBatchListByIds")
    MSResponse<List<NameValuePair<Long, String>>> findBatchListByIds(@RequestBody List<Long> ids);

    /**
     * 获取所有客户列表
     * id
     * code
     * name
     * contractDate
     * salesMan
     * paymentType
     * @return
     */
    @GetMapping("/customer/findAllSpecifiedColumn")
    MSResponse<List<MDCustomer>> findAllSpecifiedColumn();

    /**
     *  分页获取客户列表
      * @param customer
     * @return
     * id,
     * code,
     * name,
     * master,
     * phone,
     * email,
     * technologyOwner,
     * technologyOwnerPhone,
     * defaultBrand,
     * effectFlag,
     * shortMessageFlag,
     * remarks
     */
    @PostMapping("customer/findCustomerList")
    MSResponse<MSPage<MDCustomer>> findCustomerList(@RequestBody MDCustomer customer);

    /**
     * 获取所有客户ID以及名称
     * @return
     */
    @GetMapping("/customer/findAllWithIdAndName")
    MSResponse<List<MDCustomer>> findAllWithIdAndName();

    /**
     * 根据salesId获取客户列表
     * @param salesId
     * @return
     */
    @GetMapping("/customer/findListBySalesId/{salesId}")
    MSResponse<List<MDCustomer>> findListBySalesId(@PathVariable("salesId") Integer salesId );

    /**
     * 根据跟单员id获取客户列表
     * @param merchandiserId
     * @return
     * id,name
     */
    @GetMapping("/customer/findListByMerchandiserId")
    MSResponse<List<MDCustomer>> findListByMerchandiserId(@RequestParam("merchandiserId") Long merchandiserId );


    /**
     * 条件查询全部客户&指定栏位
     * @param customer
     * @return
     */
    @PostMapping("/customer/findAllByIdAndPaymentType")
    MSResponse<List<MDCustomer>> findAllByIdAndPaymentType(@RequestBody MDCustomer customer);

    /**
     * 根据paymentType查询id列表
     * @param paymentType
     * @return
     * id
     */
    @GetMapping("/customer/findCustomerIdByPaymentType/{paymentType}")
    MSResponse<List<Long>> findCustomerIdByPaymentType(@PathVariable("paymentType") Integer paymentType);

    /**
     * 获取vip客户列表 //add on 2019-12-9
     * @return
     * id,name
     */
    @GetMapping("/customer/findListByVipCustomer")
    MSResponse<List<MDCustomer>> findListByVipCustomer();

    /**
     * 保存客户
     * @param customer
     * @return
     */
    @PostMapping(value="/customer/saveCustomer")
    MSResponse<Integer> saveCustomer(@RequestBody MDCustomer customer);

    /**
     * 更新客户信息
     * @param customer
     * @return
     */
    @PutMapping("/customer/updateCustomer")
    MSResponse<Integer> updateCustomer(@RequestBody MDCustomer customer);

    /**
     * 更新客户业务员信息
     * @param customer
     * @return
     */
    @PutMapping("/customer/updateSales")
    MSResponse<Integer> updateSales(@RequestBody MDCustomer customer);

    /**
     * 更新客户跟单员信息
     * @param customer
     * @return
     */
    @PutMapping("/customer/updateMerchandiserId")
    MSResponse<Integer> updateMerchandiserId(@RequestBody MDCustomer customer);

    /**
     * 删除客户信息
     * @param mdCustomer
     * @return
     */
    @DeleteMapping("/customer/removeCustomer")
    MSResponse<Integer> removeCustomer(@RequestBody MDCustomer mdCustomer);
}
