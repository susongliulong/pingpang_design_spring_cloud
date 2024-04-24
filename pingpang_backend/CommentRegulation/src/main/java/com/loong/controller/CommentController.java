package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.CommentVo;
import com.loong.entity.dto.CommentDto;
import com.loong.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-23
 */
@RestController
@RequestMapping("/comment")
public class CommentController {


    @Autowired
    private ICommentService commentService;


    @PostMapping("/post")
    public R publishComment(@RequestBody CommentDto comment){
        CommentVo commentVo = commentService.postComment(comment);
        return R.success(commentVo);
    }

    @GetMapping("/comments")
    public R comments(Long articleId){
        List<CommentVo>list=commentService.getComments(articleId);
        return R.success(list);
    }

    @GetMapping("/comments/pages")
    public R comments(Long articleId,Integer pageNumber){
        List<CommentVo>list=commentService.getComments(articleId,pageNumber);
        return R.success(list);
    }


    @RequestMapping("/comments/pages/{commentId}")
    public R comments(Long articleId,Integer pageNumber,@PathVariable Long commentId){
        List<CommentVo>list=commentService.getComments(articleId,pageNumber,commentId);
        return R.success(list);
    }



    @PostMapping("/replyOthers")
    public R reply(@RequestBody CommentDto commentDto){

        CommentVo commentVo=commentService.replyOthers(commentDto);
        return R.success(commentVo);
    }

    /**
     * 点赞操作
     * @param comment
     * @return
     */
    @PostMapping("/like")
    public R like(@RequestBody CommentVo comment){
        // 更新评论信息
        commentService.updateLikesOfComment(comment.getId(),comment.getLikes(),comment.getDislikes());
        return R.success("点赞成功");
    }
}
