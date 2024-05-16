package com.loong;

import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTest {



    @Test
    public void generateSchools() throws Exception{

        URL url = new URL("https://www.gdut.edu.cn/jgsz/jxjg.htm");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        String collegePattern="target=\"_blank\">(.{2,13})学院</a>";
        Pattern pattern = Pattern.compile(collegePattern);
        List<String> colleges = new ArrayList<>();
        while((line = bufferedReader.readLine())!=null){
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()){
                colleges.add(matcher.group(1)+"学院");
            }
        }
        PrintWriter printWriter = new PrintWriter(new FileOutputStream("D:\\10.软件工程课程\\软件工程大四上\\毕业论文\\pingpang_backend\\MatchRegulation\\src\\test\\resources\\school_colleges.txt"));
        // 获取中国大学相关数据，写入文件colleges
        URL url2 = new URL("https://www.dxsbb.com/news/list_88.html");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
        httpURLConnection.setRequestMethod("GET");
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream));
        String line2;
        String schoolPattern="<span class=\"c1\">(.{4,15})</span>";
        Pattern pattern2 = Pattern.compile(schoolPattern);
        while((line2 = bufferedReader2.readLine())!=null){
            Matcher matcher = pattern2.matcher(line2);
            while(matcher.find()){
                colleges.forEach(
                        college->{
                            printWriter.write(matcher.group(1)+" "+college+"\n");
                            printWriter.flush();
                        }
                );
            }
        }
    }

    @Test
    public void generateColleges() throws Exception{

        URL url = new URL("https://www.gdut.edu.cn/jgsz/jxjg.htm");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        String collegePattern="target=\"_blank\">(.{2,13})学院</a>";
        Pattern pattern = Pattern.compile(collegePattern);
        while((line = bufferedReader.readLine())!=null){
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()){

            }
        }
    }

}
