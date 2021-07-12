/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.controller.test;

import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 测试
 *
 * @author Ryan
 * @version 2017-11-7
 */
@RestController
@RequestMapping("/api/test/")
@Slf4j
public class RestTestController extends RestBaseController {

    /**
     * 管理登录
     */
    @RequestMapping(value = "test",produces="application/json;charset=UTF-8")
    public RestResult<Object> test(HttpServletRequest request, HttpServletResponse response) {
//        Engineer engineer = (Engineer)request.getAttribute("engineer");
//        if(engineer != null){
//            System.out.println("id:" + engineer.getId() + ",name:" + engineer.getName());
//        }
        return RestResultGenerator.success("OK");
    }


    /**
     * 管理登录
     */
    @RequestMapping(value = "testupload",consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> testUpload(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest muti = (MultipartHttpServletRequest) request;
        //System.out.println(muti.getMultiFileMap().size());
        log.info("{}",muti.getMultiFileMap().size());

        MultiValueMap<String, MultipartFile> map = muti.getMultiFileMap();

        for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {

            List<MultipartFile> list = entry.getValue();
            for (MultipartFile multipartFile : list) {
                try {
                    multipartFile.transferTo(new File("D:/t/"
                            + multipartFile.getOriginalFilename()));
                } catch (IllegalStateException | IOException e) {
                }
            }
        }

        return RestResultGenerator.success(null);
    }
     
}
