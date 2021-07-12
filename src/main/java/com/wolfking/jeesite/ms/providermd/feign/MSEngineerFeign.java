package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDEngineer;
import com.kkl.kklplus.entity.md.dto.MDEngineerDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSEngineerFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSEngineerFeignFallbackFactory.class)
public interface MSEngineerFeign {




    @GetMapping("/engineer/existIdNoAndAttachmentByIdForAPI/{id}")
    MSResponse<Boolean> existIdNoAndAttachmentByIdForAPI(@PathVariable("id") Long id);

    @PutMapping("/engineer/updateIdNoAndAttachmentForAPI")
    MSResponse<Integer> updateIdNo(MDEngineer engineer);

    /**
     *  根据师傅id获取身份证
     */
    @GetMapping("/engineer/getIdNoByIdFromCacheForAPI/{id}")
    MSResponse<String> getIdentity(@PathVariable("id") Long id);

    /**
     * 根据id获取安维对象信息
     * @param id
     * @return
     */
    @GetMapping("/engineer/getById/{id}")
    MSResponse<MDEngineer> getById(@PathVariable("id") Long id);

    /**
     * 根据id从缓存中获取安维对象信息
     * @param id
     * @return
     */
    @GetMapping("/engineer/getByIdFromCache/{id}")
    MSResponse<MDEngineer> getByIdByFromCache(@PathVariable("id") Long id);


    /**
     * 根据id从缓存中获取安维对象信息(id,name)
     * @param id
     * @return
     */
    @GetMapping("/engineer/getNameByIdFromCache/{id}")
    MSResponse<String> getNameByIdByFromCache(@PathVariable("id") Long id);

    /**
     * 通过安维人员id和网点id获取安维人员信息
     * @param engineerId
     * @param servicePointId
     * @return
     */
    @GetMapping("/engineer/getEngineerFromCache")
    MSResponse<MDEngineer> getEngineerFromCache(@RequestParam("engineerId") Long engineerId, @RequestParam("servicePointId") Long servicePointId);

    /**
     * 通过安维人员基本信息(API)
     * @param engineerId
     * @return
     * 姓名、电话、派单、完成、催单、投诉单
     */
    @GetMapping("/engineer/getBaseInfoFromCache")
    MSResponse<MDEngineer> getBaseInfoFromCache(@RequestParam("engineerId") Long engineerId);

    /**
     * 通过安维人员详细信息(API)
     * @param engineerId
     * @return
     * 姓名、电话、个人地址、服务区域、网点名称
     */
    @GetMapping("/engineer/getDetailInfoFromCache")
    MSResponse<MDEngineerDto> getDetailInfoFromCache(@RequestParam("servicePointId") Long servicePointId, @RequestParam("engineerId") Long engineerId);


    /**
     * 按手机号返回安维id
     * @param mobile
     * @param exceptId
     * @return
     */
    @GetMapping("/engineer/getEngineerIdByMobile")
    MSResponse<Long> getEngineerIdByMobile(@RequestParam("mobile") String mobile, @RequestParam("exceptId") Long exceptId);

    /**
     * 根据id列表获取安维列表
     * @param ids
     * @return
     */
    @PostMapping("/engineer/findEngineersByIds")
    MSResponse<List<MDEngineer>>  findEngineersByIds(@RequestBody List<Long> ids, @RequestParam("fields")List<String> fields);

    /**
     * 获取当前网点下所有的非当前人员的安维人员id列表
     * @param engineerId
     * @param servicePointId
     * @return
     */
    @GetMapping("/engineer/findSubEngineerIds")
    MSResponse<List<Long>>  findSubEngineerIds(@RequestParam("engineerId") Long engineerId, @RequestParam("servicePointId") Long servicePointId);


    /**
     * 查询师傅列表
     * @param mdEngineer
     * @return
     */
    @PostMapping("/engineer/findEngineerForKeFu")
    MSResponse<MSPage<MDEngineer>> findEngineerForKeFu(@RequestBody MDEngineer mdEngineer);

    /**
     * 分页获取能否手机派单的安维id
     * @param mdEngineer
     * @return
     */
    @PostMapping("/engineer/findAppFlagEngineer")
    MSResponse<MSPage<Long>> findAppFlagEngineer(@RequestBody MDEngineer mdEngineer);

    /**
     * 根据安维名字或电话获取安维id列表
     * @param mdEngineer
     * @return
     * id
     */
    @PostMapping("/engineer/findPagingIdWithNameOrPhone")
    MSResponse<MSPage<Long>> findPagingIdWithNameOrPhone(@RequestBody MDEngineer mdEngineer);


    /**
     * 通过网点id从缓存中获取安维列表
     * @param servicePointId
     * @return
     */
    @GetMapping("/engineer/findEngineerByServicePointIdFromCache")
    MSResponse<List<MDEngineer>> findEngineerByServicePointIdFromCache(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 根据安维服务区域id获取能手机接单的网点主账号安维人员列表信息
     * @param areaId
     * @return
     */
    @GetMapping("/engineer/findEngineerListByServiceAreaId")
    MSResponse<List<MDEngineer>> findEngineerListByServiceAreaId(@RequestParam("areaId") Long areaId);

    /**
     * 分页查询安维列表
     * @param mdEngineerDto
     * @return
     */
    @PostMapping("/engineer/findEngineerList")
    MSResponse<MSPage<MDEngineerDto>> findEngineerList(@RequestBody MDEngineerDto mdEngineerDto);

    /**
     * 分页查询安维列表  (专给API使用)
     * @param mdEngineerDto
     * @return
     */
    @PostMapping("/engineer/findEngineerListFromAPI")
    MSResponse<MSPage<MDEngineerDto>> findEngineerListFromAPI(@RequestBody MDEngineerDto mdEngineerDto);

    /**
     * 检查网点下是否存在其他的主帐号
     * @param servicePointId
     * @param exceptId
     * @return
     */
    @GetMapping("/engineer/checkMasterEngineer")
    MSResponse<Integer> checkMasterEngineer(@RequestParam("servicePointId") Long servicePointId, @RequestParam("exceptId") Long exceptId);
    /**
     * 更新安维人员信息
     * @param mdEngineer
     * @return
     */
    @PutMapping("/engineer/update")
    MSResponse<Integer> update(@RequestBody MDEngineer mdEngineer);

    /**
     * 更新安维人员名字信息
     * @param mdEngineer
     * @return
     */
    @PutMapping("/engineer/updateEngineerName")
    MSResponse<Integer> updateEngineerName(@RequestBody MDEngineer mdEngineer);

    /**
     * 更新安维人员数量及评分信息
     * @param mdEngineer
     * @return
     */
    @PutMapping("/engineer/updateEngineerByMap")
    MSResponse<Integer> updateEngineerByMap(@RequestBody MDEngineer mdEngineer);

    /**
     * 升级网点
     * @param mdEngineer
     * @return
     */
    @PutMapping("/engineer/updateServicePointId")
    MSResponse<Integer> updateServicePointId(@RequestBody MDEngineer mdEngineer);


    /**
     * 重置安维负责人员主账号
     * @param ids
     * @return
     */
    @PutMapping("/engineer/resetEngineerMasterFlag")
    MSResponse<Integer> resetEngineerMasterFlag(@RequestBody List<Long> ids);

    /**
     * 保存安维人员信息
     * @param mdEngineer
     * @return
     */
    @PostMapping("/engineer/save")
    MSResponse<Integer> save(@RequestBody MDEngineer mdEngineer);

    /**
     * 软删除安维人员信息
     * @param mdEngineer
     * @return
     */
    @DeleteMapping("/engineer/enableOrDisable")
    MSResponse<Integer> enableOrDisable(@RequestBody MDEngineer mdEngineer);

    /**
     * 新增个人地址修改接口(API)
     * @param engineer
     * @return
     */
    @PutMapping("/engineer/updateAddress")
    MSResponse<Integer> updateAddress(@RequestBody MDEngineer engineer);

    /**
     * 更新师傅体温
     * @param engineer
     * @return
     */
    @PutMapping("/engineer/updateTemperatureForAPI")
    MSResponse<Integer> updateTemperatureForAPI(@RequestBody MDEngineer engineer);

}
