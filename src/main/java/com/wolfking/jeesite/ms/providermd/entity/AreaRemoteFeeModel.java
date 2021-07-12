package com.wolfking.jeesite.ms.providermd.entity;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.sys.entity.viewModel.AreaModel;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
public class AreaRemoteFeeModel {

    private Long cityId = 0L;
    private String parentName = "";
    private String areaName = "";
    private String fullName;
    private List<AreaModel> areaModelList = Lists.newArrayList();
    private List<SubAreaModel>  subAreaModels  = Lists.newArrayList();

    public int getSubAreaSize(){
        int count = 0;
        if(!ObjectUtils.isEmpty(areaModelList)){
            for(AreaModel areaModel:areaModelList){
                if(areaModel.getSubAreas().size()==0){
                    count = count+1;
                }else{
                    count = count + areaModel.getSubAreas().size();
                }
            }
        }
        return count;
    }
}
