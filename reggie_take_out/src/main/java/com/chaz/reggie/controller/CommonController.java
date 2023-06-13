package com.chaz.reggie.controller;

import com.chaz.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author chaz
 * @time 2023-06-05 20:40:56
 * @description TODO
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * @description 文件上传
     * @author chaz
     * @date 20:43 2023/6/5
     * @param file 
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){ //此处形参名称不能随意命名，必须要和 前端请求表单中的name=“file” 的name一致
        log.info(file.toString());
        //原始文件名
        String originalFileName=file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件重名导致的文件覆盖
        String fileName = UUID.randomUUID().toString()+suffix;

        //创建一个目录
        File dir= new File(basePath);
        if (!dir.exists()) {
            //目录不存在时创建目录
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * @description 文件下载
     * @author chaz
     * @date 10:02 2023/6/6
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        log.info("正在下载图片---- name：{}，response：{}",name,response.toString());
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写会浏览器，在浏览器显示图片
            ServletOutputStream outputStream=response.getOutputStream();

            response.setContentType("image/jpeg");

            int len=0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
