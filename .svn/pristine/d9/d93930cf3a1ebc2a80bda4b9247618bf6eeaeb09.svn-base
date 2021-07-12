package com.wolfking.jeesite.modules.sd.utils;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhoucy
 * @date 2018/8/27 9:59
 **/
public class OrderPicUtils {

    /**
     * 工单图片的默认扩展名
     */
    public static final String ORDER_PIC_DEFAULT_EXT_NAME = ".jpg";
    private static final String PIC_UPLOADS_DIR = "uploads";

    /**
     * 删除磁盘文件
     */
    public static boolean deleteFile(String filePath) {
        try {
            boolean result = false;
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                result = file.delete();
            }
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getOrderPicHostDir() {
//        return Global.getConfig("userfiles.host") + "/";
        return ApiPropertiesUtils.getUserFiles().getHost() + "/";
    }

    public static String getPicUrl(String picRelativePath) {
//        String hostPath = Global.getConfig("userfiles.host");
        String hostPath = ApiPropertiesUtils.getUserFiles().getHost();
        if (StringUtils.endsWith(hostPath, "/")) {
            hostPath = hostPath.substring(0, hostPath.length() - 1);
        }
        if (StringUtils.startsWith(picRelativePath, "/")) {
            picRelativePath = picRelativePath.substring(1);
        }
        if (StringUtils.endsWith(hostPath, PIC_UPLOADS_DIR) && StringUtils.startsWith(picRelativePath, PIC_UPLOADS_DIR)) {
            picRelativePath = picRelativePath.substring(PIC_UPLOADS_DIR.length());
        }
        return hostPath + picRelativePath;
    }

    public static String getOrderPicMasterDir(HttpServletRequest request) {
        String masterPath = Global.getUploadfilesDir();
        if (StringUtils.isBlank(masterPath) && request != null) {
            masterPath = request.getServletContext().getRealPath("") + "/uploads/";
        }
        return masterPath;
    }

    /**
     * 保存工单图片，返回值：{aElement: 是否成功, bElement: 文件路径}
     */
    public static TwoTuple<Boolean, String> saveFile(HttpServletRequest request, MultipartFile picFile) {
        boolean successFlag = false;
        StringBuilder subPath = new StringBuilder();
        String masterPath = OrderPicUtils.getOrderPicMasterDir(request);
        if (!StringUtils.isBlank(masterPath)) {
            subPath.append(DateUtils.getYear()).append("/")
                    .append(DateUtils.getMonth()).append("/")
                    .append(DateUtils.getDay()).append("/");
            try {
                File dir = new File(masterPath + subPath.toString());
                boolean isExists = true;
                if (!dir.exists()) {
                    isExists = dir.mkdirs();
                }
                if (isExists) {
                    String originalFilename = picFile.getOriginalFilename();
                    String extName = OrderPicUtils.ORDER_PIC_DEFAULT_EXT_NAME;
                    if (StringUtils.isNotBlank(originalFilename)) {
                        int index = originalFilename.lastIndexOf(".");
                        if (index >= 0) {
                            extName = originalFilename.substring(index);
                        }
                        subPath.append(UUID.randomUUID().toString()).append(extName);
                        picFile.transferTo(new File(masterPath + subPath));
                        successFlag = true;
                    }
                }
            } catch (Exception e) {
                successFlag = false;
            }
        }
        return new TwoTuple<>(successFlag, subPath.toString());
    }

    private static String savePicFile(MultipartFile picFile, String mainDir, String subDir) throws IOException {
        String filePath = "";
        String originalFilename = picFile.getOriginalFilename();
        String extName = OrderPicUtils.ORDER_PIC_DEFAULT_EXT_NAME;
        if (StringUtils.isNotBlank(originalFilename)) {
            int index = originalFilename.lastIndexOf(".");
            if (index >= 0) {
                extName = originalFilename.substring(index);
            }
            filePath = subDir + UUID.randomUUID().toString() + extName;
            picFile.transferTo(new File(mainDir + filePath));
        }
        return filePath;
    }

    public static TwoTuple<Boolean, List<String>> saveImageFiles(HttpServletRequest request, MultipartFile[] picFiles) {
        boolean successFlag = false;
        List<String> filePaths = Lists.newArrayList();
        String masterDir = OrderPicUtils.getOrderPicMasterDir(request);
        if (!StringUtils.isBlank(masterDir)) {
            String subDir = DateUtils.getYear() + "/" + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
            try {
                File dir = new File(masterDir + subDir);
                boolean isExists = true;
                if (!dir.exists()) {
                    isExists = dir.mkdirs();
                }
                if (isExists) {
                    for (MultipartFile file : picFiles) {
                        String filePath = savePicFile(file, masterDir, subDir);
                        if (StringUtils.isNotBlank(filePath)) {
                            filePaths.add(filePath);
                        }
                    }
                    successFlag = true;
                }
            } catch (Exception e) {
                successFlag = false;
            }
        }
        return new TwoTuple<>(successFlag, filePaths);
    }

    public static TwoTuple<Boolean, List<String>> saveImageFilesForMDServicePoint(HttpServletRequest request, MultipartFile[] picFiles) {
        boolean successFlag = false;
        List<String> filePaths = Lists.newArrayList();
        String masterDir = OrderPicUtils.getOrderPicMasterDir(request);
        if (!StringUtils.isBlank(masterDir)) {
            String subDir =  "servicePoint/"+DateUtils.getYear() + "/" + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
            try {
                File dir = new File(masterDir + subDir);
                boolean isExists = true;
                if (!dir.exists()) {
                    isExists = dir.mkdirs();
                }
                if (isExists) {
                    for (MultipartFile file : picFiles) {
                        String filePath = savePicFile(file, masterDir, subDir);
                        if (StringUtils.isNotBlank(filePath)) {
                            filePaths.add(filePath);
                        }
                    }
                    successFlag = true;
                }
            } catch (Exception e) {
                successFlag = false;
            }
        }
        return new TwoTuple<>(successFlag, filePaths);
    }

    public static String getPraisePicUrl(String picRelativePath) {
//        String hostPath = Global.getConfig("userfiles.host");
        String hostPath = ApiPropertiesUtils.getUserFiles().getHost();
        if (StringUtils.endsWith(hostPath, "/")) {
            hostPath = hostPath.substring(0, hostPath.length() - 1);
        }
        if (StringUtils.startsWith(picRelativePath, "/")) {
            picRelativePath = picRelativePath.substring(1);
        }
        if (StringUtils.endsWith(hostPath, PIC_UPLOADS_DIR) && StringUtils.startsWith(picRelativePath, PIC_UPLOADS_DIR)) {
            picRelativePath = picRelativePath.substring(PIC_UPLOADS_DIR.length());
        }
        return hostPath + "/" + picRelativePath;
    }

    public static String getPraiseExamplePicUrl(String picRelativePath) {
        if (StringUtils.isBlank(picRelativePath)) {
            return "";
        }
//        String hostPath = Global.getConfig("userfiles.host");
        String hostPath = ApiPropertiesUtils.getUserFiles().getHost();
        if (StringUtils.endsWith(hostPath, "/")) {
            hostPath = hostPath.substring(0, hostPath.length() - 1);
        }
        if (StringUtils.endsWith(hostPath, PIC_UPLOADS_DIR)) {
            hostPath = hostPath.substring(0, hostPath.length() - PIC_UPLOADS_DIR.length());
        }
        if (StringUtils.endsWith(hostPath, "/")) {
            hostPath = hostPath.substring(0, hostPath.length() - 1);
        }
        if (StringUtils.startsWith(picRelativePath, "/")) {
            picRelativePath = picRelativePath.substring(1);
        }
        return hostPath + "/" + picRelativePath;
    }

}
