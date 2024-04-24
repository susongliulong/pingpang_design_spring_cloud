package com.loong;

import com.loong.entity.CommentVo;
import com.loong.service.ICommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest
{
    @Autowired
    private ICommentService commentService;

    @Test
    public void test(){
        List<CommentVo> comments = commentService.getComments(1780961472154107906L);
        comments.forEach(System.out::println);
    }
}
