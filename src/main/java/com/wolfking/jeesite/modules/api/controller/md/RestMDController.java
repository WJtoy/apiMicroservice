package com.wolfking.jeesite.modules.api.controller.md;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.kkl.kklplus.entity.md.MDAppFeedbackType;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.MaterialPriceConstrant;
import com.wolfking.jeesite.modules.api.entity.md.RestAppFeedbackType;
import com.wolfking.jeesite.modules.api.entity.md.RestGetHtmlContext;
import com.wolfking.jeesite.modules.api.entity.md.RestGetOptionList;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.sys.service.DictService;
import com.wolfking.jeesite.modules.utils.HtmlUtils;
import com.wolfking.jeesite.ms.providermd.service.MSAppFeedbackTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/md/")
public class RestMDController extends RestBaseController {
    @Autowired
    private DictService dictService;

    @Autowired
    private MSAppFeedbackTypeService msAppFeedbackTypeService;

    @RequestMapping(value = "getOptionList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getOptionList(@RequestBody RestGetOptionList getOptionList) throws Exception {
        return dictService.getDictListByType(getOptionList);
    }

    /**
     * 获取App异常/停滞类型
     * 二级结构
     */
    @RequestMapping(value = "getFeedbackTypeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getFeedbackTypeList() throws Exception {
        List<MDAppFeedbackType> list = msAppFeedbackTypeService.findAllList();
        Supplier<Stream<MDAppFeedbackType>> supplier = () -> list.stream();
        int size = Math.floorDiv(list.size(), 2) + 1;
        LinkedHashMap<Long, RestAppFeedbackType> tree = Maps.newLinkedHashMapWithExpectedSize(size);
        supplier.get().filter(t -> t.getParentId() == 0L && t.getIsEffect() == 1)
                .sorted(Comparator.comparing(MDAppFeedbackType::getSortBy))
                .forEach(t -> {
                    tree.put(t.getId(),
                            new RestAppFeedbackType()
                                    .setId(t.getId().intValue())
                                    .setParentId(t.getParentId().intValue())
                                    .setValue(t.getValue())
                                    .setLabel(t.getLabel())
                                    //.setHasChildren(t.getHasChildren())
                                    .setHasChildren(0)
                                    .setFeedbackType(t.getFeedbackType())
                                    .setChildren(Lists.newArrayList())
                    );
                });
        //children
        for (Map.Entry<Long, RestAppFeedbackType> entry : tree.entrySet()) {
            //if (entry.getValue().getHasChildren() == 1) {
            supplier.get().filter(c -> c.getParentId().intValue() == entry.getValue().getId() && c.getIsEffect() == 1)
                    .sorted(Comparator.comparing(MDAppFeedbackType::getSortBy))
                    .forEach(c -> {
                        entry.getValue().getChildren().add(
                                new RestAppFeedbackType()
                                        .setId(c.getId().intValue())
                                        .setParentId(c.getParentId().intValue())
                                        .setValue(c.getValue())
                                        .setLabel(c.getLabel())
                                        .setHasChildren(0)
                                        .setFeedbackType(c.getFeedbackType())
                        );
                        entry.getValue().setHasChildren(1);
                    });
            //}
        }
        list.clear();//清除列表中元素，确保list没有方法外使用，也没有作为引用参数
        return RestResultGenerator.success(tree.values());
    }


    @RequestMapping(value = "getHtmlContext", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getOptionList(@RequestBody RestGetHtmlContext params) throws Exception {
        if (params == null || params.getSystemId() == null || params.getSystemId() == 0
                || params.getType() == null || params.getType() == 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        String htmlContext = "";
        if (params.getSystemId() == 1 && params.getType() == 1) {
            htmlContext = MaterialPriceConstrant.KKL;
        } else if (params.getSystemId() == 2 && params.getType() == 1) {
            htmlContext = MaterialPriceConstrant.KKL_AC;
        }else if ((params.getSystemId() == 1 || params.getSystemId()==2) && params.getType()== 2){
            htmlContext = HtmlUtils.getHtmlString();
        }
        JsonObject jo = new JsonObject();
        jo.addProperty("htmlContext", htmlContext);
        return RestResultGenerator.success(jo);
    }

}
