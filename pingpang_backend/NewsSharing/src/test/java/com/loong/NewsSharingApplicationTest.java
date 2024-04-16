package com.loong;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.entity.BasicInformation;
import com.loong.entity.Interest;
import com.loong.entity.News;
import com.loong.entity.User;
import com.loong.mapper.BasicInformationMapper;
import com.loong.mapper.UserMapper;
import com.loong.service.IBasicInformationService;
import com.loong.service.IInterestService;
import com.loong.service.INewsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
@RunWith(SpringRunner.class)
public class NewsSharingApplicationTest {

    @Autowired
    private INewsService iNewsService;

    @Autowired
    private IInterestService iInterestService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IBasicInformationService iBasicInformationService;

    @Autowired
    private BasicInformationMapper basicInformationMapper;


    @Test
    public void test() {
        iNewsService.recommendArticles(0).forEach(System.out::println);
    }

    @Test
    public void test3() {
        basicInformationMapper.getAllTopics().forEach(System.out::println);
    }

    /**
     * 讲category_title_author.txt中的数据插入到数据库中的表user,interest以及basic_information
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        // 读取配置title_category_author.txt内容，生成测试的数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\10.软件工程课程\\软件工程大四上\\毕业论文\\pingpang_backend\\NewsSharing\\src\\test\\resources\\category_title_author.txt")));
        String line;
        int i = 1;
        Random random = new Random();
        while ((line = bufferedReader.readLine()) != null) {
            String[] strings = line.split("\t");
            if(strings.length<3)continue;
            if(i==10000)break;
            // 更新interest表
            Interest interest = iInterestService.getOne(new LambdaQueryWrapper<Interest>().eq(Interest::getName, strings[0]));
            if (interest == null) {
                Interest o = new Interest();
                o.setName(strings[0]);
                iInterestService.save(o);
            }
            interest = iInterestService.getOne(new LambdaQueryWrapper<Interest>().eq(Interest::getName, strings[0]));
            // 设置作者信息
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getName, strings[2]);
            User user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setName(strings[2]);
                user.setPassword("123456");
                //电话号码生成
                if (i < 10) {
                    user.setTelephone("1734563" + "000" + i);
                } else if (i < 100) {
                    user.setTelephone("1734563" + "00" + i);
                } else if (i < 1000) {
                    user.setTelephone("1734563" + "0" + i);
                } else if(i<10000){
                    user.setTelephone("1734563" + i);
                }
                user.setAvatar("https://img.pingpangwang.com/uc_server/data/avatar/000/11/36/84_avatar_middle.jpg");
                user.setPlayYears(random.nextInt(60));
                user.setLastLoginTime(LocalDateTime.of(2024, i % 12 + 1, i % 28 + 1, i % 24, i % 60, i % 60));
                userMapper.insert(user);
            }
            // 更新basic_information表
            BasicInformation basicInformation = new BasicInformation();
            basicInformation.setTitle(strings[1]);
            basicInformation.setAuthorId(user.getId());
            basicInformation.setCollects(random.nextInt(10000));
            basicInformation.setComments(random.nextInt(10000));
            basicInformation.setPageView(random.nextInt(10000));
            basicInformation.setLikes(random.nextInt(10000));
            basicInformation.setPublishTime(LocalDateTime.of(2024, i * 3 % 12 + 1, i * 3 % 28 + 1, i * 3 % 24, i % 60, i % 60));
            basicInformation.setState(1);
            basicInformation.setCategoryId(interest.getId());
            iBasicInformationService.save(basicInformation);
            i++;
        }
    }

    /**
     * 在乒乓网上面爬取测试数据到category_title_author.txt
     */
    @Test
    public void test2() {
        // 正则表达式语法
        String titlePattern = "\\s{48}(.*)</a>$";// 匹配标题
        String categoryPattern = "\\s+<a\\shref=\"forum\\.php\\?mod=forumdisplay&amp;fid=\\d+&amp;filter=typeid&amp;typeid=\\d+&amp;mobile=\\d+\"\\sclass=\"circle-name hot-post-circle\">(.*)</a>";
        String authorPattern = "\\s+<span\\sclass=\"usr-name\">(.*)</span>";
        Pattern title = Pattern.compile(titlePattern);
        Pattern category = Pattern.compile(categoryPattern);
        Pattern author = Pattern.compile(authorPattern);
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\10.软件工程课程\\软件工程大四上\\毕业论文\\pingpang_backend\\NewsSharing\\src\\test\\resources\\category_title_author.txt");
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            // 读取1000页的赛事信息
            for (int i = 1; i <= 1000; i++) {
                URL url = new URL(String.format("https://www.pingpangwang.com/forum-239-%d.html", i));
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    Matcher matcher = title.matcher(line);
                    if (matcher.find()) {
                        printWriter.write(matcher.group(1) + "\t");
                    }
                    Matcher categoryMatcher = category.matcher(line);
                    if (categoryMatcher.find()) {
                        printWriter.write(categoryMatcher.group(1) + "\t");
                    }
                    Matcher authorMatcher = author.matcher(line);
                    if (authorMatcher.find()) {
                        printWriter.write(authorMatcher.group(1) + "\n");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test4() throws Exception{
        List<BasicInformation> list = iBasicInformationService.list();
        // 读配置文件内容
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/resources/news_content.txt")));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        list.forEach(
                basicInformation -> {
                    News news = new News();
                    news.setNewsId(basicInformation.getId());
                    news.setCategoryId(basicInformation.getCategoryId());
                    news.setContent(stringBuilder.toString());
                    iNewsService.save(news);
                }
        );
    }

}
