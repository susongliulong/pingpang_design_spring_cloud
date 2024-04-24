package com.loong.service;

import com.loong.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.CommentVo;
import com.loong.entity.dto.CommentDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-04-23
 */
public interface ICommentService extends IService<Comment> {

    CommentVo postComment(CommentDto comment);

    List<CommentVo> getComments(Long articleId);

    CommentVo replyOthers(CommentDto commentDto);

    List<CommentVo> getComments(Long articleId, Integer pageNumber);

    List<CommentVo> getComments(Long articleId, Integer pageNumber, Long commentId);

    void updateLikesOfComment(Long id, Integer likes, Integer dislikes);
}
