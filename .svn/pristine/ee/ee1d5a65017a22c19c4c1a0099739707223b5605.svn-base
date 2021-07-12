package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.UrgentCustomer;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.modules.md.entity.viewModel.AreaUrgentModel;
import com.wolfking.jeesite.modules.md.entity.viewModel.UrgentChargeModel;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerUrgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UrgentCustomerService extends LongIDBaseService {
    @Autowired
    private AreaService areaService;

    @Autowired
    private UrgentLevelService urgentLevelService;

    @Autowired
    private MSCustomerUrgentService msCustomerUrgentService;

    /**
     * 客户id获得列表
     *
     * @param customerId
     * @return
     */
    public List<AreaUrgentModel> findListByCustomerId(Long customerId) {
        Map<Long, Area> areaMaps = areaService.findMapByType(Area.TYPE_VALUE_PROVINCE);
        List<AreaUrgentModel> list = Lists.newArrayList();
        Map<Long, UrgentLevel> urgentLevels = urgentLevelService.findAllMap();
        //查找客户下的区域
        List<UrgentChargeModel> urgentChargeModels = findListByCustomerIdFromMS(customerId);     // add on 2019-8-1 调用微服务获取数据

        if (urgentChargeModels != null && urgentChargeModels.size() > 0) {
            Map<Long, List<UrgentChargeModel>> groups = urgentChargeModels.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getAreaId(),
                            Collectors.mapping(p -> p,
                                    Collectors.toList()
                            )
                    ));
            Set<Map.Entry<Long, List<UrgentChargeModel>>> entrySet = groups.entrySet();
            Iterator<Map.Entry<Long, List<UrgentChargeModel>>> iter = entrySet.iterator();
            Map.Entry<Long, List<UrgentChargeModel>> entry;
            AreaUrgentModel areaUrgentModel;
            List<UrgentChargeModel> items;
            while (iter.hasNext()) {
                entry = iter.next();
                if (areaMaps.containsKey(entry.getKey())) {
                    areaUrgentModel = new AreaUrgentModel();
                    areaUrgentModel.setArea(areaMaps.get(entry.getKey()));
                    items = entry.getValue();
                    items.stream().forEach(t -> {
                        t.setUrgentLevel(urgentLevels.get(t.getUrgentLevel().getId()));
                    });
                    areaUrgentModel.setList(items);
                    list.add(areaUrgentModel);
                }
            }
        }

        return list;
    }

    /**
     * 根据customerId从微服务中获取加急费用等级列表  // add on 2019-8-1
     *
     * @param customerId
     * @return
     */
    private List<UrgentChargeModel> findListByCustomerIdFromMS(Long customerId) {
        List<UrgentChargeModel> urgentChargeModelList = Lists.newArrayList();

        UrgentCustomer urgentCustomer = new UrgentCustomer();
        urgentCustomer.setCustomer(new Customer(customerId));
        List<UrgentCustomer> urgentCustomerList = msCustomerUrgentService.findListByCustomerId(urgentCustomer);
        if (urgentCustomerList != null && !urgentCustomerList.isEmpty()) {
            urgentCustomerList.stream().sorted(Comparator.comparing(urgentCustomerEntity -> urgentCustomerEntity.getUrgentLevel().getId())).forEach(urgentCustomerEntity -> {
                UrgentChargeModel urgentChargeModelEntity = new UrgentChargeModel();
                urgentChargeModelEntity.setUrgentLevel(urgentCustomerEntity.getUrgentLevel());
                urgentChargeModelEntity.setAreaId(urgentCustomerEntity.getArea().getId());
                urgentChargeModelEntity.setChargeIn(urgentCustomerEntity.getChargeIn());
                urgentChargeModelEntity.setChargeOut(urgentCustomerEntity.getChargeOut());

                urgentChargeModelList.add(urgentChargeModelEntity);
            });
        }
        return urgentChargeModelList;
    }

    /**
     * 通过客户 和 省ID获取 加急信息
     *
     * @param customerId
     * @param areaId
     * @return
     */
    public AreaUrgentModel getAreaUrgentModel(Long customerId, Long areaId) {
        AreaUrgentModel areaUrgentModel = null;
        List<AreaUrgentModel> list = findListByCustomerId(customerId);
        if (list != null && list.size() > 0) {
            areaUrgentModel = list.stream().filter(t -> t.getArea().getId().equals(areaId)).findFirst().orElse(null);
        }
        return areaUrgentModel;
    }

}
