package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.CustomerMaterial;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.mapper.MaterialMapper;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerMaterialService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户配件Service
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CustomerMaterialService extends LongIDBaseService {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MSCustomerMaterialService msCustomerMaterialService;


    /**
     * 根据客户Id 产品Id,配件Id获取去缓存
     */
    public CustomerMaterial getFromCache(long customerId, long productId, Long mateiralId) {
        //调用微服务 2019-9-20 start
        CustomerMaterial customerMaterial = msCustomerMaterialService.getCustomerMaterialByCustomerAndProductAndMaterial(customerId, productId, mateiralId);
        if (customerMaterial != null) {
            return customerMaterial;
        }
        return null;
        //调用微服务 end
        /*
        // mark on 2020-1-10
        String key = String.format(RedisConstant.MD_CUSTOMER_MATERIAL,customerId,productId);
        customerMaterial = redisUtils.hGet(RedisConstant.RedisDBType.REDIS_MD_DB,key,mateiralId.toString(),CustomerMaterial.class);
        if(customerMaterial ==null){
            customerMaterial =dao.getByCustomerAndProductAndMaterial(customerId,productId,mateiralId);
            if(customerMaterial !=null){
                redisUtils.hmSet(RedisConstant.RedisDBType.REDIS_MD_DB,key,customerMaterial.getMaterial().getId().toString(),customerMaterial,0L);
            }
        }
        return customerMaterial;
        */
    }

    /**
     * 根据客户Id + 产品Id 读取配件设定列表
     */
    public List<CustomerMaterial> getListFromCache(long customerId, long productId) {
        //调用微服务 2019-9-23 start
        List<CustomerMaterial> customerMaterials = msCustomerMaterialService.findListByCustomerAndProduct(customerId, productId);
        if (customerMaterials != null && customerMaterials.size() > 0) {
            return customerMaterials;
        }
        return Lists.newArrayList();
        /*
        //mark on 2020-1-11 begin
        String key = String.format(RedisConstant.MD_CUSTOMER_MATERIAL,customerId,productId);
        List<CustomerMaterial> list = Lists.newArrayList();
        @SuppressWarnings("unchecked")
        Map<String, byte[]> maps = redisUtils.hGetAll(RedisConstant.RedisDBType.REDIS_MD_DB,key);
        if (maps != null && maps.size() > 0) {
            CustomerMaterial material;
            for (Map.Entry<String, byte[]> entry : maps.entrySet()) {
                material = (CustomerMaterial) redisUtils.gsonRedisSerializer.fromJson(StringUtils.toString(entry.getValue()), CustomerMaterial.class);
                list.add(material);
            }
        }else{
            list =dao.getListByCustomerAndProduct(customerId,productId);
            if(!ObjectUtils.isEmpty(list)){
                Map<String,Object> map = Maps.newHashMap();
                for(CustomerMaterial entity:list){
                    map.put(entity.getMaterial().getId().toString(),entity);
                }
                redisUtils.hmSetAll(RedisConstant.RedisDBType.REDIS_MD_DB,key,map,0L);
            }else{
                list = Lists.newArrayList();
            }
        }
        return list;
        //mark on 2020-1-11 end
        */
    }


    /**
     * 读取配件返件标识及价格
     * 1.先读取客户配置
     * 2.1没有设定，读取配件设定
     *
     * @param customerId
     * @param productId
     * @param materialId
     * @return Material(isReturn, price)
     */
    public Material getMaterialInfoOfCustomer(long customerId, long productId, long materialId) {
        CustomerMaterial customerMaterial = getFromCache(customerId, productId, materialId);
        Material material = materialService.getFromCache(materialId);
        if (material == null) {
            return material;
        }
        if (customerMaterial != null) {
            material.setIsReturn(customerMaterial.getIsReturn());
            material.setPrice(customerMaterial.getPrice());
        }
        return material;
    }


    /**
     * 根据客户Id + 产品Id 读取配件设定列表
     * 1.先读取客户配置
     * 2.再读取配件设定，以1为准
     */
    public Map<Long, Material> getMapFromCache(long customerId, long productId) {
        Map<Long, Material> maps = Maps.newHashMap();
        //1.先读取客户配置
        List<CustomerMaterial> list = getListFromCache(customerId, productId);
        if (!ObjectUtils.isEmpty(list)) {
            MaterialMapper mapper = Mappers.getMapper(MaterialMapper.class);
            maps.putAll(list.stream().collect(Collectors.toMap(CustomerMaterial::getMaterialId, item -> mapper.customerToMaterial(item))));
        }
        //2.get material
        List<Material> materials = materialService.getMaterialListByProductId(productId);
        Material material;
        if (!ObjectUtils.isEmpty(materials)) {
            materials.stream().forEach(t -> {
                if (maps.containsKey(t.getId())) {
                    Material m = maps.get(t.getId());
                    if (StringUtils.isBlank(m.getName())) {
                        m.setName(t.getName());
                    }
                } else {
                    maps.put(t.getId(), t);
                }
            });
        }
        return maps;
    }

}
