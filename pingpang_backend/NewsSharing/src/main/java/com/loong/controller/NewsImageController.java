package com.loong.controller;

import com.loong.common.R;
import com.loong.util.FileUpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/newsImage")
@RestController
public class NewsImageController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private String articleImgPath;

    @PostMapping("/upload")
    public R upload(@RequestBody MultipartFile file, HttpServletRequest httpServletRequest) {
//        Object userId = httpServletRequest.getSession().getServletContext().getAttribute("userId");
        Object userId = httpSession.getAttribute("userId");
        if (userId != null) {
            String path = articleImgPath+"/news/" + userId+"/";
            String news = FileUpUtil.upFile(file, path);
            return R.success(news);
        }
        return R.error("图片上传失败");
    }

    @GetMapping("/download")
    public R download(String fileName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        FileUpUtil.downFile(httpServletResponse,articleImgPath+"/news/"+httpSession.getAttribute("userId").toString()+"/",fileName);
        return R.success(fileName);
    }

    @PostMapping("/authorOfNews/{userId}")
    public R authorOfNews(@PathVariable long userId, HttpServletRequest httpServletRequest) {
        httpSession=httpServletRequest.getSession();
        httpSession.setAttribute("userId",userId);
        return R.success(null);
    }


}
