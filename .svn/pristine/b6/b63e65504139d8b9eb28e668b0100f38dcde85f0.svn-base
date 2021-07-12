package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFee;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFeeExamplePicItem;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFeePraiseStandardItem;
import com.kkl.kklplus.entity.praise.CustomerPraisePaymentTypeEnum;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerPraiseFeeFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MSCustomerPraiseFeeService {
    @Autowired
    private MSCustomerPraiseFeeFeign msCustomerPraiseFeeFeign;

    private static final String DICT_DEFAULTPRAISEFEE = "DefaultPraiseFee";        // 缺省好评费
    private static final String DICT_PRAISEFEEEXAMPLEPIC = "PraiseFeeExamplePic";  //好评示例图片
    private static final String DICT_PRAISESTANDARD = "PraiseStandardType";         //好评标准

    /**
     * 根据客户ID查询客户好评费
     *
     * @param customerId 客户id
     * @return
     */
    public MDCustomerPraiseFee getByCustomerIdFromCacheForCP(Long customerId) {
        return MDUtils.getObjUnnecessaryConvertType(() -> msCustomerPraiseFeeFeign.getByCustomerIdFromCacheForCP(customerId));
    }

    /**
     * 根据客户ID查询客户好评费  2020-4-30
     *
     * @param customerId 客户id
     * @return
     */
    public MDCustomerPraiseFee getByCustomerIdFromCacheNewForCP(Long customerId) {
        MSResponse<MDCustomerPraiseFee> msResponse = msCustomerPraiseFeeFeign.getByCustomerIdFromCacheNewForCP(customerId);
        if (msResponse.getCode() >0) {
            return null;
        } else {
            MDCustomerPraiseFee customerPraiseFee = msResponse.getData();
            List<Dict> praiseStandardList = MSDictUtils.getDictList(DICT_PRAISESTANDARD);
            if (customerPraiseFee == null) {
                customerPraiseFee = new MDCustomerPraiseFee();
                customerPraiseFee.setCustomerId(customerId);
                customerPraiseFee.setPraiseFeeFlag(0);
                //修复客户未设置好评标准，此属性导致空指针问题 2020-07-22
                customerPraiseFee.setOnlineFlag(CustomerPraisePaymentTypeEnum.ONLINE.code);

                Dict pariseRequirementDict = MSDictUtils.getDictByValue("praiseRequirement", DICT_DEFAULTPRAISEFEE);
                customerPraiseFee.setPraiseRequirement(pariseRequirementDict.getRemarks());

                List<MDCustomerPraiseFeePraiseStandardItem> initCustomerPraiseFeePraiseStandardItems = Lists.newArrayList();
                List<MDCustomerPraiseFeePraiseStandardItem> customerPraiseFeePraiseStandardItems = mergeAllItems(initCustomerPraiseFeePraiseStandardItems, praiseStandardList);
                customerPraiseFee.setPraiseStandardItems(customerPraiseFeePraiseStandardItems);
            }
            customerPraiseFee.setExamplePicItems(mergeAllExamplePicItems(praiseStandardList));
            return customerPraiseFee;
        }
    }

    public List<MDCustomerPraiseFeePraiseStandardItem> mergeAllItems(List<MDCustomerPraiseFeePraiseStandardItem> items, List<Dict> dicts) {
        if (ObjectUtils.isEmpty(dicts)) {
            return Lists.newArrayList();
        }
        Map<String, MDCustomerPraiseFeePraiseStandardItem> map = Maps.newHashMap();
        if (!ObjectUtils.isEmpty(items)) {
            map = items.stream().collect(Collectors.toMap(MDCustomerPraiseFeePraiseStandardItem::getCode, Function.identity()));
        }
        List<MDCustomerPraiseFeePraiseStandardItem> returnItems = Lists.newArrayList();
        for (Dict dict : dicts) {
            MDCustomerPraiseFeePraiseStandardItem item = map.get(dict.getValue());
            if (item != null) {
                item.setName(dict.getLabel());
                item.setSort(dict.getSort());
            } else {
                item = new MDCustomerPraiseFeePraiseStandardItem();
                item.setCode(dict.getValue());
                item.setName(dict.getLabel());
                item.setSort(dict.getSort());
                item.setMustFlag(0);     // 是否必选初始化为不选,0-不选，1-选中
                item.setVisibleFlag(0);  // 是否显示初始化为不选,0-不选, 1-选中
                item.setFee(0.0D);
                item.setRemarks("");
            }
            returnItems.add(item);
        }
        return returnItems.stream().sorted(Comparator.comparing(r -> r.getSort())).collect(Collectors.toList());
    }

    /**
     * 归并所有的示例图片项次
     *
     * @return
     */
    public List<MDCustomerPraiseFeeExamplePicItem> mergeAllExamplePicItems(List<Dict> dicts) {
        //String strPrfix =
        List<MDCustomerPraiseFeeExamplePicItem> customerPraiseFeeExamplePicItems = Lists.newArrayList();
        //List<Dict> examplePicDictList = MSDictUtils.getDictList(DICT_PRAISEFEEEXAMPLEPIC);
        if (!ObjectUtils.isEmpty(dicts)) {

            for (Dict dict : dicts) {
                MDCustomerPraiseFeeExamplePicItem item = new MDCustomerPraiseFeeExamplePicItem();
                item.setCode(dict.getValue());
                item.setName(dict.getLabel());
                item.setSort(dict.getSort());
                item.setUrl(dict.getRemarks());

                customerPraiseFeeExamplePicItems.add(item);
            }
        }
        return customerPraiseFeeExamplePicItems.stream().sorted(Comparator.comparing(r -> r.getSort())).collect(Collectors.toList());
    }
}
