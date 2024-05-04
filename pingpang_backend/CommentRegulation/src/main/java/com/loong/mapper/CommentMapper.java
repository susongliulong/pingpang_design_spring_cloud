package com.loong.mapper;

import com.loong.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.entity.CommentVo;
import com.loong.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-23
 */
public interface CommentMapper extends BaseMapper<Comment> {

    void insertArticleComment(Long commentId, Long articleId);

    List<CommentVo> getComments(Long articleId);

    @Select("select * from user where id = #{userId}")
    User getUser(Long userId);

    void insertSubComment(Long mainCommentId, Long subCommentId);

    List<CommentVo> getCommentsByPage(Long articleId, Integer pageNumber);

    Integer getsubCommentsNumber(Long id);

    List<CommentVo>getSubComments(Long commentId, Integer pageNumber);

    List<CommentVo> getCommentsByArticleId(Long articleId);

    List<CommentVo> getSubCommentsByPage(Long commentId, Integer pageStart);

    @Update("update comment set likes = #{likes} ,dislikes=#{dislikes} where id = #{id}")
    void updateLikesOfComment(Long id, Integer likes, Integer dislikes);

    @Delete("delete from article_comment where comment_id=#{id}")
    void deleteArticleComment(Long id);

    @Delete("delete from comment_sub_comment where comment_sub_comment.comment_id=#{id}")
    void deleteSubComment(Long id);
}
