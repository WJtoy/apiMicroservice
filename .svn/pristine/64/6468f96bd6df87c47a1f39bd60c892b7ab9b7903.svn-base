package com.wolfking.jeesite.ms.mapper.md;
import com.kkl.kklplus.entity.md.MDProductMaterial;
import com.wolfking.jeesite.modules.md.entity.ProductMaterial;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductMaterialMapper extends CustomMapper<MDProductMaterial, ProductMaterial> {
    @Override
    public void mapAtoB(MDProductMaterial a, ProductMaterial b, MappingContext context) {
        b.setProductId(a.getProductId());
        b.setMaterialId(a.getMaterialId());
    }

    @Override
    public void mapBtoA(ProductMaterial b, MDProductMaterial a, MappingContext context) {
        a.setProductId(b.getProductId());
        a.setMaterialId(b.getMaterialId());
    }
}
