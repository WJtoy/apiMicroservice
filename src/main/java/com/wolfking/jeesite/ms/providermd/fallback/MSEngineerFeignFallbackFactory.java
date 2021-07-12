package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDEngineer;
import com.kkl.kklplus.entity.md.dto.MDEngineerDto;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSEngineerFeignFallbackFactory implements FallbackFactory<MSEngineerFeign> {
    @Override
    public MSEngineerFeign create(Throwable throwable) {
        return new MSEngineerFeign() {


            @Override
            public MSResponse<Boolean> existIdNoAndAttachmentByIdForAPI(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateIdNo(MDEngineer engineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<String> getIdentity(Long id) {
               return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id获取安维对象信息
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDEngineer> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id从缓存中获取安维对象信息
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDEngineer> getByIdByFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id从缓存中获取安维对象信息(id,name)
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<String> getNameByIdByFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过安维人员id和网点id获取安维人员信息
             *
             * @param engineerId
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<MDEngineer> getEngineerFromCache(Long engineerId, Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过安维人员基本信息(API)
             *
             * @param engineerId
             * @return 姓名、电话、派单、完成、催单、投诉单
             */
            @Override
            public MSResponse<MDEngineer> getBaseInfoFromCache(Long engineerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过安维人员详细信息(API)
             *
             * @param servicePointId
             * @param engineerId
             * @return 姓名、电话、个人地址、服务区域、网点名称
             */
            @Override
            public MSResponse<MDEngineerDto> getDetailInfoFromCache(Long servicePointId, Long engineerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 按手机号返回安维id
             *
             * @param mobile
             * @param exceptId
             * @return
             */
            @Override
            public MSResponse<Long> getEngineerIdByMobile(String mobile, Long exceptId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id列表获取安维列表
             *
             * @param ids
             * @return
             */
            @Override
            public MSResponse<List<MDEngineer>> findEngineersByIds(List<Long> ids, List<String> fields) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取当前网点下所有的非当前人员的安维人员id列表
             *
             * @param engineerId
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<List<Long>> findSubEngineerIds(Long engineerId, Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 查询师傅列表
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<MSPage<MDEngineer>> findEngineerForKeFu(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取能否手机派单的安维id
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<MSPage<Long>> findAppFlagEngineer(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据安维名字或电话获取安维id列表
             *
             * @param mdEngineer
             * @return id
             */
            @Override
            public MSResponse<MSPage<Long>> findPagingIdWithNameOrPhone(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过网点id从缓存中获取安维列表
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<List<MDEngineer>> findEngineerByServicePointIdFromCache(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据安维服务区域id获取能手机接单的网点主账号安维人员列表信息
             *
             * @param areaId
             * @return
             */
            @Override
            public MSResponse<List<MDEngineer>> findEngineerListByServiceAreaId(Long areaId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页查询安维列表
             *
             * @param mdEngineerDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDEngineerDto>> findEngineerList(MDEngineerDto mdEngineerDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页查询安维列表  (专给API使用)
             *
             * @param mdEngineerDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDEngineerDto>> findEngineerListFromAPI(MDEngineerDto mdEngineerDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 检查网点下是否存在其他的主帐号
             *
             * @param servicePointId
             * @param exceptId
             * @return
             */
            @Override
            public MSResponse<Integer> checkMasterEngineer(Long servicePointId, Long exceptId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新安维人员信息
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<Integer> update(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新安维人员名字信息
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<Integer> updateEngineerName(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新安维人员数量及评分信息
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<Integer> updateEngineerByMap(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 升级网点
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<Integer> updateServicePointId(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 保存安维人员信息
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<Integer> save(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 重置安维负责人员主账号
             *
             * @param ids
             * @return
             */
            @Override
            public MSResponse<Integer> resetEngineerMasterFlag(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 软删除安维人员信息
             *
             * @param mdEngineer
             * @return
             */
            @Override
            public MSResponse<Integer> enableOrDisable(MDEngineer mdEngineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 新增个人地址修改接口(API)
             *
             * @param engineer
             * @return
             */
            @Override
            public MSResponse<Integer> updateAddress(MDEngineer engineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新师傅体温
             *
             * @param engineer
             * @return
             */
            @Override
            public MSResponse<Integer> updateTemperatureForAPI(MDEngineer engineer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
