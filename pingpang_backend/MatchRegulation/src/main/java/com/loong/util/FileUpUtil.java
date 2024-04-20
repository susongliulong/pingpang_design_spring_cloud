package com.loong.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Slf4j
public class FileUpUtil {

    public static String upFile(MultipartFile file, String path) {

        // 上传位置
        File f = new File(path);
        if (!f.exists())
            f.mkdirs();
        // 获得原始文件名
        String fileName = file.getOriginalFilename();

        // 新文件名
        String newFileName = UUID.randomUUID()+"."+fileName.split("\\.")[1];
        if (!file.isEmpty()) {
            try {
                FileOutputStream fos = new FileOutputStream(path
                        + newFileName);
                InputStream in = file.getInputStream();
                int b = 0;
                while ((b = in.read()) != -1) {
                    fos.write(b);
                }
                fos.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newFileName;
    }

    public static boolean downFile(HttpServletResponse response, String path, String filename) {
        // 得到要下载的文件名
        try {

            // 得到要下载的文件
            File file = new File(path +filename);

            // 如果文件不存在
            if (!file.exists()) {
                log.error("图片资源定位错误,您要下载的资源已被删除！！");
                return false;
            }

            // 设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(filename, "UTF-8"));
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(path + filename);
            // 创建输出流
            OutputStream out = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            // 循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                // 输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            // 关闭文件输入流
            in.close();
            // 关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
