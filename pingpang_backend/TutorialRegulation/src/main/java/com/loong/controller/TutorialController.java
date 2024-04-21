package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.Category;
import com.loong.entity.Tutorial;
import com.loong.entity.vo.TutorialVo;
import com.loong.service.IBasicInformationService;
import com.loong.service.ITutorialService;
import com.loong.util.FileUpUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-21
 */
@RestController
@RequestMapping("/tutorial")
public class TutorialController {

    @Autowired
    private ITutorialService tutorialService;

    @Autowired
    public IBasicInformationService basicInformationService;

    @Autowired
    private String tutorialPath;

    @GetMapping("/category")
    public R getCategories(){
        List<Category>categories=tutorialService.getCategories();
        return R.success(categories);
    }

    @GetMapping("/tutorial")
    public R getTutorial(long tutorialId){
        Tutorial tutorial = tutorialService.getByTutorialId(tutorialId);
        tutorial.setTutorialId(tutorial.getBasicInformation().getId());
        return R.success(tutorial);
    }

    @GetMapping("/{userId}")
    public R getTutorialsByUserId(@PathVariable long userId){
        List<TutorialVo>list=tutorialService.getTutorialsByUserId(userId);
        return R.success(list);
    }

    @PostMapping("/post")
    public R addTutorial(@RequestBody Tutorial tutorial){
        tutorialService.addTutorial(tutorial);
        return R.success(null,"发布成功");
    }

    @PostMapping("/upload")
    public R upload(long userId, MultipartFile file){
        String path=tutorialPath+"/"+userId+"/";
        String newFileName = FileUpUtil.upFile(file, path);
        return R.success(newFileName);
    }

    @GetMapping("/download")
    public R download(String fileName, long userId, HttpServletResponse response){
        String path=tutorialPath+"/"+userId+"/";
        FileUpUtil.downFile(response, path, fileName);
        return R.success(null);
    }
}
