package com.wolfking.jeesite.modules.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URL;

/**
 * @Auther wj
 * @Date 2021/3/16 10:11
 */

@Slf4j
public class HtmlUtils {

    /**
     * 字符流
     *
     * @return
     */
    public static String getHtmlString() {
        StringBuffer sb = new StringBuffer();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("papers/mutualFund.html");
            inputStream = classPathResource.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
            log.error("HtmlUtils.getHtmlString：{}", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("HtmlUtils.getHtmlString：{}", e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("HtmlUtils.getHtmlString：{}", e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    //字节流
    public String getHtml() {
        URL url = this.getClass().getClassLoader().getResource("papers/mutualFund.html");
        StringBuffer sb = new StringBuffer();
        BufferedInputStream bis = null;
        try {
            File f = new File(url.toURI());
            FileInputStream fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            int len = 0;
            byte[] temp = new byte[1024];
            while ((len = bis.read(temp)) != -1) {
                sb.append(new String(temp, 0, len));
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {

        System.out.println(HtmlUtils.getHtmlString());

    }
}
