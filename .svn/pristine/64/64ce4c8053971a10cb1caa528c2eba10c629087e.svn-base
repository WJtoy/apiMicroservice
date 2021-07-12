package com.wolfking.jeesite.modules.api.service.sd;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineer;


import com.kkl.kklplus.entity.md.MDEngineerCert;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Auther wj
 * @Date 2021/4/9 15:12
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class AppIdentityService {

    @Autowired
    private MSEngineerFeign msEngineerFeign;




    public MSResponse<Integer> updateIdCard(Long id,Long userId, String identityCard, List<MDEngineerCert> list){

        MDEngineer mdEngineer = new MDEngineer();
        mdEngineer.setId(id);
        mdEngineer.setUpdateDate(new Date());
        mdEngineer.setUpdateById(userId);
        mdEngineer.setIdNo(identityCard);
        mdEngineer.setEngineerCerts(list);
        MSResponse<Integer> idNo = msEngineerFeign.updateIdNo(mdEngineer);
        return idNo;
    }


    public Integer getPast(Long id){
       MSResponse<Boolean> bool = msEngineerFeign.existIdNoAndAttachmentByIdForAPI(id);
       Integer result = 0;
       if (bool.getData()){
           result = 10;
       }else {
           result = 20;
       }
        return  result;
    }

    public boolean getPassPost(Long id){

        MSResponse<Boolean> bool = msEngineerFeign.existIdNoAndAttachmentByIdForAPI(id);
        return bool.getData();
    }


    public String getIdentity(Long id){
      MSResponse<String> identity = msEngineerFeign.getIdentity(id);
      if (identity.getData()!=null){
          return identity.getData();
      }
      return "";
    }




}
