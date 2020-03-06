package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * 验证码工具类
 * 当前只是图形验证码，后续添加数值运算型验证码，具体实现为生成0-100的两个数值，然后随机选一种运算符号，并算出运算结果
 *
 * @author Administrator
 */
@Slf4j
public class VerifyCodeUtils {

    //存入session的key
    public static final String VERIFY_CODE_SESSION_KEY = "VERIFY_CODE_SESSION_KEY";
    //验证码生成时间
    public static final String VERIFY_CODE_CREATE_MILLI = "VERIFY_CODE_CREATE_MILLI";
    //使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
    public static final String VERIFY_CODES = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private VerifyCodeUtils() {
    }

    /**
     * 使用系统默认字符源生成验证码
     *
     * @param verifySize 验证码长度
     * @return
     */
    public static HashMap<String, String> generateVerifyCode(int verifySize) {
        var map = new HashMap<String, String>();
        try {
            var random = SecureRandom.getInstanceStrong();
//        验证码的样式：算术型 字符型
            var verifyType = random.nextInt(2);

            switch (verifyType) {
                case 0:
                    map = generateNormStr(verifySize);
                    break;
                case 1:
                    map = generateOperStr();
                    break;
                default:
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("验证码生成失败，请重试!");
        }
        return map;
    }


    /**
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @description 生成验证码字符串及值，算术型
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/15 9:48
     */
    public static HashMap<String, String> generateOperStr() {
        var map = new HashMap<String, String>();
        //        定义数值索引，用于取用于运算的数值
        var index1 = random.nextInt(10);
        var index2 = random.nextInt(10);
//        定义运算符索引，用于确定运算符
        var index3 = random.nextInt(3);
//        定义样式数组，用于确定是数值还是汉字，值为0或1
        var index4 = random.nextInt(2);
//        然后定义两个数字数组，
        var str1 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        var str2 = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        var strs = new String[][]{str1, str2};
//        再定义下运算符数组
        var oper1 = new String[]{"+", "-", "x"};
        var oper2 = new String[]{"加", "减", "乘"};
        var opers = new String[][]{oper1, oper2};
//        最后定义结束符数组
        var end = new String[]{"=", "等"};

        var rStr = strs[index4];
        var rOper = opers[index4];
        var result = 0;
        var verifyCodeStr = rStr[index1] + rOper[index3] + rStr[index2] + end[index4];

        switch (index3) {
            case 0:
                result = index1 + index2;
                break;
            case 1:
                result = index1 - index2;
                break;
            case 2:
                result = index1 * index2;
                break;
            default:
                break;
        }

        map.put("verifyCodeStr", verifyCodeStr);
        map.put("result", String.valueOf(result));
        return map;
    }

    /**
     * @param verifySize
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @description 生成验证码字符串及值，普通型
     * @params [verifySize]
     * @author Hongyan Wang
     * @date 2019/10/15 9:59
     */
    public static HashMap<String, String> generateNormStr(int verifySize) {
        var map = new HashMap<String, String>();
        var codesLen = VERIFY_CODES.length();
        var verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(VERIFY_CODES.charAt(random.nextInt(codesLen - 1)));
        }
        map.put("verifyCodeStr", String.valueOf(verifyCode));
        map.put("result", String.valueOf(verifyCode));
        return map;
    }

    /**
     * 生成随机验证码文件,并返回验证码值
     *
     * @param w
     * @param h
     * @param outputFile
     * @param verifySize
     * @return
     */
    public static String outputVerifyImage(int w, int h, File outputFile, int verifySize) {
        var map = generateVerifyCode(verifySize);
        var verifyCode = map.get("verifyCodeStr");
        outputImage(w, h, outputFile, verifyCode);
        return verifyCode;
    }

    /**
     * 输出随机验证码图片流,并返回验证码值
     *
     * @param w
     * @param h
     * @param os
     * @param verifySize
     * @return
     * @throws IOException
     */
    public static String outputVerifyImage(int w, int h, OutputStream os, int verifySize) throws IOException {
        var map = generateVerifyCode(verifySize);
        var verifyCode = map.get("verifyCodeStr");
        outputImage(w, h, os, verifyCode);
        return verifyCode;
    }

    /**
     * 生成指定验证码图像文件
     *
     * @param w
     * @param h
     * @param outputFile
     * @param code
     */
    public static void outputImage(int w, int h, File outputFile, String code) {
        if (outputFile == null) {
            return;
        }
        var dir = outputFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            outputFile.createNewFile();
            var outputStream = new FileOutputStream(outputFile);
            outputImage(w, h, outputStream, code);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出指定验证码图片流
     *
     * @param w
     * @param h
     * @param os
     * @param code
     * @throws IOException
     */
    public static void outputImage(int w, int h, OutputStream os, String code) throws IOException {
        var verifySize = code.length();
        var image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        var g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        var colors = new Color[5];
        var colorSpaces = new Color[]{Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW};
        var fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[random.nextInt(colorSpaces.length)];
            fractions[i] = random.nextFloat();
        }
        Arrays.sort(fractions);

        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);

        var c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h - 4);

        //绘制干扰线
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        for (int i = 0; i < 30; i++) {
            var x = VerifyCodeUtils.random.nextInt(w - 1);
            var y = VerifyCodeUtils.random.nextInt(h - 1);
            var xl = VerifyCodeUtils.random.nextInt(6) + 1;
            var yl = VerifyCodeUtils.random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 添加噪点
        var yawpRate = 0.15f;// 噪声率
        var area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            var x = VerifyCodeUtils.random.nextInt(w);
            var y = VerifyCodeUtils.random.nextInt(h);
            var rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        shear(g2, w, h, c);// 使图片扭曲

        g2.setColor(getRandColor(100, 160));
        var fontSize = h - 8;
        var font = new Font("Algerian", Font.ITALIC, fontSize);
        g2.setFont(font);
        var chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            var affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * random.nextDouble() * (random.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize / 2, h / 2);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
        }

        g2.dispose();
        ImageIO.write(image, "jpg", os);
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        var r = fc + random.nextInt(bc - fc);
        var g = fc + random.nextInt(bc - fc);
        var b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        var rgb = getRandomRgb();
        var color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        var rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {

        var period = random.nextInt(2);

        var frames = 1;
        var phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            var d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                               + (6.2831853071795862D * (double) phase)
                                 / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }

    }

    private static void shearY(Graphics g, int w1, int h1, Color color) {

        var period = random.nextInt(40) + 10;

        var frames = 20;
        var phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                       * Math.sin((double) i / (double) period
                                  + (6.2831853071795862D * (double) phase)
                                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + h1, i, h1);

        }

    }
}
