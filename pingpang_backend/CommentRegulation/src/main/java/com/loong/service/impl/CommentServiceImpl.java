package com.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.Comment;
import com.loong.entity.CommentVo;
import com.loong.entity.dto.CommentDto;
import com.loong.mapper.CommentMapper;
import com.loong.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-23
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentVo postComment(CommentDto commentDto) {

        // 在comment表中插入数据
        Comment comment = new Comment();
        comment.setPublishTime(LocalDateTime.now());
        comment.setUserId(commentDto.getUserId());
        comment.setContent(commentDto.getContent());
        commentMapper.insert(comment);
        // 在article_comment表中插入数据
        commentMapper.insertArticleComment(comment.getId(),commentDto.getArticleId());

        CommentVo commentVo = new CommentVo();
        commentVo.setUser(commentMapper.getUser(comment.getUserId()));
        commentVo.setId(comment.getId());
        commentVo.setUserId(commentDto.getUserId());
        commentVo.setPublishTime(comment.getPublishTime());
        commentVo.setContent(comment.getContent());
        commentVo.setDislikes(0);
        commentVo.setLikes(0);
        commentVo.setComments(new ArrayList<CommentVo>());
        return commentVo;
    }

    @Override
    public List<CommentVo> getComments(Long articleId) {
        List<CommentVo>list=commentMapper.getComments(articleId);
        list.forEach(
                commentVo -> {
                    commentVo.setNumber(commentVo.getComments().size());
                    commentVo.getComments().forEach(
                            element -> {
                                element.setUser(commentMapper.getUser(element.getUserId()));
                            }
                    );
                }
        );
        return list;
    }

    /**
     * 回复其他人的评论
     * @param commentDto
     * @return
     */
    @Override
    @Transactional
    public CommentVo replyOthers(CommentDto commentDto) {
        CommentVo commentVo = new CommentVo();
        // 在comment表插入数据
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUserId(commentDto.getUserId());
        comment.setReplyToId(commentDto.getReplyToId());
        // 设置名称
        comment.setReplyToName(commentMapper.getUser(comment.getReplyToId()).getName());
        comment.setPublishTime(LocalDateTime.now());
        commentMapper.insert(comment);
        // 在comment_sub_comment表插入数据
        commentMapper.insertSubComment(commentDto.getCommentId(),comment.getId());
        // 最后将插入的数据返回
        commentVo.setUser(commentMapper.getUser(comment.getUserId()));
        commentVo.setComments(null);
        commentVo.setPublishTime(comment.getPublishTime());
        commentVo.setReplyToId(commentDto.getReplyToId());
        commentVo.setReplyToName(commentDto.getReplyToName());
        commentVo.setContent(comment.getContent());
        commentVo.setLikes(0);
        commentVo.setDislikes(0);
        commentVo.setComments(new ArrayList<CommentVo>());
        return commentVo;
    }

    @Override
    public List<CommentVo> getComments(Long articleId, Integer pageNumber) {

        Integer pageStart=(pageNumber-1)*5;
        List<CommentVo>list=commentMapper.getCommentsByArticleId(articleId);
        list.forEach(
                commentVo -> {
                    commentVo.setNumber(commentMapper.getsubCommentsNumber(commentVo.getId()));
                    commentVo.setUser(commentMapper.getUser(commentVo.getUserId()));
                    commentVo.setComments(commentMapper.getSubCommentsByPage(commentVo.getId(),pageStart));
                    commentVo.getComments().forEach(
                            element -> {
                                element.setUser(commentMapper.getUser(element.getUserId()));
                            }
                    );
                }
        );
        return list;
    }

    @Override
    public List<CommentVo> getComments(Long articleId, Integer pageNumber, Long commentId) {
        Integer pageStart=(pageNumber-1)*5;
        List<CommentVo>list=commentMapper.getSubCommentsByPage(commentId,pageStart);
        list.forEach(
                commentVo -> {
                    commentVo.setUser(commentMapper.getUser(commentVo.getUserId()));
                }
        );
        return list;
    }

    @Override
    public void updateLikesOfComment(Long id, Integer likes, Integer dislikes) {
        commentMapper.updateLikesOfComment(id,likes,dislikes);
    }
}
