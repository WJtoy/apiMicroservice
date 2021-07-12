package com.wolfking.jeesite.modules.api.controller.md;

import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.RestCommonIds;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.md.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/")
public class RestProductController extends RestBaseController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "getProductMaterialList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getProductMaterialList(@RequestBody RestCommonIds commonIds) throws Exception {
        return productService.getProductMaterialList(commonIds);
    }
}
