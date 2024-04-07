package com.loong.controller;



import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@RestController
@RequestMapping("/code")
public class CheckCodeController {

    @GetMapping("/get")
    public void generateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 200;
        int height = 50;

        // 创建一个图像缓冲区
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics2D g2d = bufferedImage.createGraphics();

        // 设置背景颜色
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        // 生成随机字符
        String captchaText = generateRandomString(5); // 生成包含5个字符的随机字符串

        // 将验证码文本存储在Session中，以便稍后验证
        ServletContext servletContext = request.getSession().getServletContext();
        servletContext.setAttribute("captcha", captchaText);

        // 设置字体和字体大小
        g2d.setFont(new Font("SansSerif", Font.BOLD, 24));

        // 设置文本颜色
        g2d.setColor(Color.black);

        // 在图像上绘制验证码文本
        g2d.drawString(captchaText, 50, 30);

        // 添加一些噪音，使验证码更难以识别
        addNoise(g2d, width, height);

        // 设置响应内容类型
        response.setContentType("image/png");

        // 获取响应的输出流
        OutputStream os = response.getOutputStream();

        // 将图像写入响应
        ImageIO.write(bufferedImage, "png", os);

        // 关闭输出流
        os.close();
    }

    @GetMapping("/check")
    public boolean checkCode(String checkCode, HttpServletRequest request){

        String captcha = (String)request.getSession().getServletContext().getAttribute("captcha");
        return checkCode.equals(captcha);
    }





    // 生成随机字符串
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    // 添加噪音
    private void addNoise(Graphics2D g2d, int width, int height) {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int size = random.nextInt(5);
            g2d.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            g2d.fillRect(x, y, size, size);
        }
    }


}
