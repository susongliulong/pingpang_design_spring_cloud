package com.loong;

import com.loong.service.INewsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NewsSharingApplicationTest
{

    @Autowired
    private INewsService iNewsService;
    @Test
    public void test(){

        iNewsService.recommendArticles().forEach(System.out::println);
    }
}
