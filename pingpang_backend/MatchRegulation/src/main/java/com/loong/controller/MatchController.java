package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.dto.MatchDTO;
import com.loong.entity.dto.SearchMessage;
import com.loong.entity.vo.MatchVo;
import com.loong.service.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private IMatchService matchService;


    @PostMapping("/matchMessage/{pageNumber}")
    public R matchMessage(@PathVariable Integer pageNumber, @RequestBody SearchMessage searchMessage){
        List<MatchVo>matches=matchService.matchMessage(pageNumber,searchMessage);
        return R.success(matches);
    }

    @PostMapping("pages")
    public R pages(@RequestBody SearchMessage searchMessage){
        Long pages=matchService.pages(searchMessage);
        return R.success(pages);
    }

    @GetMapping("/matchMessage/{matchId}")
    public R matchMessage(@PathVariable long matchId){

        String content=matchService.getMatchMessage(matchId);
        return R.success(content);
    }

    @PostMapping("/post")
    public R postMatch(@RequestBody MatchDTO matchDTO){

        // 更新赛事数据
        matchService.postMatch(matchDTO);
        return R.success(null,"发布成功");
    }
}
