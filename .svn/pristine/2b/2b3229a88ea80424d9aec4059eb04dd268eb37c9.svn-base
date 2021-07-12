package com.wolfking.jeesite.modules.sys.entity.viewModel;

import com.wolfking.jeesite.common.config.redis.GsonIgnore;

public class FileUpload {
    private String id = "";
    private String fileName;
    private String url;
    private Integer status = 0; //上传结果 0:成功  1:失败
    @GsonIgnore
    private String origalName;

    public FileUpload(){}

    public FileUpload(String id){this.id = id;}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getOrigalName() {
        return origalName;
    }

    public void setOrigalName(String origalName) {
        this.origalName = origalName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
