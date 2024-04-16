package com.loong;

import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

public class DataTest {


    @Test
    public void test(){
        // 建立连接
        try {
            URL url = new URL("https://www.pingpangwang.com/forum-239-10.html");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 生成basic_information中和资讯相关的测试数据
     * 每一个话题下随机生成100条资讯
     */
    @Test
    public void test2() {

        // 正则表达式语法
        String titlePattern = "\\s{48}(.*)</a>$";// 匹配标题
        String categoryPattern="\\s+<a\\shref=\"forum\\.php\\?mod=forumdisplay&amp;fid=\\d+&amp;filter=typeid&amp;typeid=\\d+&amp;mobile=\\d+\"\\sclass=\"circle-name hot-post-circle\">(.*)</a>";
        String authorPattern="\\s+<span\\sclass=\"usr-name\">(.*)</span>";
        Pattern title = Pattern.compile(titlePattern);
        Pattern category = Pattern.compile(categoryPattern);
        Pattern author = Pattern.compile(authorPattern);

        // 建立连接
        try {
            // 读取1000页的赛事信息
            for(int i=1;i<=1000;i++){
                URL url = new URL(String.format("https://www.pingpangwang.com/forum-239-%d.html", i));
                HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){

                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test3(){
        System.out.println("                                                ".length());
    }
}
