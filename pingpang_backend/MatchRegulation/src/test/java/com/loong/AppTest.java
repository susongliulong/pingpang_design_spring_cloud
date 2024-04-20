package com.loong;

import com.loong.entity.Match;
import com.loong.entity.User;
import com.loong.entity.UserAbility;
import com.loong.mapper.MatchMapper;
import com.loong.mapper.SignUpMapper;
import com.loong.mapper.UserAbilityMapper;
import com.loong.mapper.UserMapper;
import com.loong.service.IMatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableTransactionManagement
public class AppTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAbilityMapper userAbilityMapper;

    @Autowired
    private SignUpMapper signUpMapper;

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private IMatchService matchService;

    /**
     * 生成赛事管理所需要的测试数据
     */
    @Test
    public void test() throws Exception {
        /*
         查询user表，获得每一个用户user
            更新user的能力值user_ability，各个能力值的取值范围在1到10，其中积分值的取值范围在20到100不等
            更新user发布的赛事信息match表
            更新user报名的赛事信息sign_up表
         */
        List<User> users = userMapper.selectList(null);
        Random random = new Random();
        UserAbility userAbility = new UserAbility();
        // 更新user_ability表
        users.forEach(
                user -> {
                    userAbility.setUserId(user.getId());
                    userAbility.setReceive(random.nextInt(10) + 1);
                    userAbility.setServe(random.nextInt(10) + 1);
                    userAbility.setAssault(random.nextInt(10) + 1);
                    userAbility.setDefend(random.nextInt(10) + 1);
                    userAbility.setScore(random.nextInt(81) + 20);
                    userAbilityMapper.insert(userAbility);
                }
        );

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\10.软件工程课程\\软件工程大四上\\毕业论文\\pingpang_backend\\MatchRegulation\\src\\test\\resources\\school_colleges.txt")));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] strings = line.split("\\s+");
            if (strings.length >= 2) {
                users.forEach(user -> {
                    Match match = new Match();
                    match.setAwards(random.nextInt(9) + 1);
                    match.setMaxNumber(random.nextInt(30) + 20);
                    match.setMinPoints(random.nextInt(6) + 2);
                    match.setPlayerNumber(match.getMaxNumber() / 2);
                    match.setContact(user.getTelephone());
                    match.setUserId(user.getId());
                    match.setMatchStartTime(LocalDateTime.of(2024, (int) (user.getId() % 12 + 1), (int) (user.getId() % 28 + 1), (int) (user.getId() % 24), (int) (user.getId() % 60)));
                    match.setAddress(strings[0] + strings[1]);
                    match.setDescription(strings[0] + strings[1] + "将举办一年一度新生杯赛事");
                    matchMapper.insert(match);
                });
            }
        }
    }

    @Test
    public void test2(){
        matchService.matchMessage(10).forEach(System.out::println);
    }

    @Test
    public void test3() throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\10.软件工程课程\\软件工程大四上\\毕业论文\\pingpang_backend\\MatchRegulation\\src\\test\\resources\\match.txt")));
        String line;
        StringBuffer stringBuffer=new StringBuffer();
        while((line = bufferedReader.readLine())!=null){
            stringBuffer.append(line);
        }
        List<Match> list = matchService.list();
        list.forEach(
                match -> {
                    matchService.addMatchMessage(match.getMatchId(),stringBuffer.toString());
                }
        );
    }

}
