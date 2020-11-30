package com.lwohvye.springcloud.springcloudlwohvyeapi.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.pooled.DbConfig;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeapi.common.util
 * @className StringUtil
 * @description 字符工具类
 * @date 2020/11/30 8:58
 */
public class StringUtil {

    private static final char SEPARATOR = '_';

    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    /**
     * @return java.lang.String
     * @description 下划线转驼峰  hello_world  -> HelloWorld
     * @params [str]
     * @author Hongyan Wang
     * @date 2020/9/15 10:09
     */
    public static String lineToHump(String str) {
        if (StrUtil.isEmpty(str))
            return "";
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine(String)})
     */
    public static String humpToLine2(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }


    /**
     * @return java.lang.String
     * @description 驼峰转下划线, 效率比上面高 HelloWorld -> hello_world
     * @params [str]
     * @author Hongyan Wang
     * @date 2020/9/15 10:10
     */
    public static String humpToLine(String str) {
        if (StrUtil.isEmpty(str))
            return "";
        str = str.substring(0, 1).toLowerCase() + str.substring(1);
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if ("127.0.0.1".equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 将List中map的key值命名方式格式化为驼峰
     *
     * @param
     * @return
     */
    public static List<Map<String, Object>> formatHumpNameForList(List<Map<String, String>> list) {
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        for (Map<String, String> o : list) {
            newList.add(formatHumpName(o));
        }
        return newList;
    }

    public static Map<String, Object> formatHumpName(Map<String, String> map) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String newKey = toFormatCol(key);
            newMap.put(newKey, entry.getValue());
        }
        return newMap;
    }

    public static String toFormatCol(String colName) {
        StringBuilder sb = new StringBuilder();
        String[] str = colName.toLowerCase().split("_");
        int i = 0;
        for (String s : str) {
            if (s.length() == 1) {
                s = s.toUpperCase();
            }
            i++;
            if (i == 1) {
                sb.append(s);
                continue;
            }
            if (s.length() > 0) {
                sb.append(s.substring(0, 1).toUpperCase());
                sb.append(s.substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * 获得当天是周几
     */
    public static String getWeekDay() {
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    // version: yyyyMMdd0000 时间戳+4位数字，递增
    public static String getNextVersion(String str, int length, Integer listIndex) {
        if (str.length() == 8) {
            return str + fillZero(length, listIndex.toString());
        }
        int i;
        try {
            i = Integer.parseInt(str.substring(8)) + listIndex - 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        i++;
        return str.substring(0, 8) + fillZero(length, Integer.toString(i));
    }

    private static String fillZero(int iTotalLength, String str) {
        int iLen = str.length();
        StringBuilder strBuilder = new StringBuilder(str);
        for (int i = 0; i < iTotalLength - iLen; i++) {
            strBuilder.insert(0, "0");
        }
        str = strBuilder.toString();
        return str;
    }

    public static List<Long> parseStrToArrLong(String str) {
        return StrUtil.isNotEmpty(str) ? Arrays.stream(str.split(",")).map(Long::parseLong).collect(Collectors.toList()) : new ArrayList<>();
    }

    public static List<Integer> parseStrToArrInteger(String str) {
        return StrUtil.isNotEmpty(str) ? Arrays.stream(str.split(",")).map(Integer::parseInt).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * @return java.lang.String
     * @description 将使用ISO-8859-1编码导致乱码的中文，使用UTF-8重新编码
     * 当前用于使用String存储数据库中blob类型属性时，解决返回数据乱码问题
     * @params [str]
     * @author Hongyan Wang
     * @date 2020/11/13 9:40
     */
    public static String convertToString(String str) {
        return StrUtil.isNotEmpty(str) ? new String(str.getBytes(StandardCharsets.ISO_8859_1)) : "";
    }

    /**
     * @description 获取字符串的二进制码。字符间使用_分隔。主要用到 Integer.toBinaryString(char)
     * @params [str]
     * @return java.lang.String
     * @author Hongyan Wang
     * @date 2020/11/30 10:56
     */
    public static String toBinary(String str) {
        String result = "";
        if (StrUtil.isEmpty(str))
            str = "lWoHvYe";
        char[] strChar = str.toCharArray();
        StringBuilder binaryStr = new StringBuilder();
        for (char c : strChar) {
//            转二进制  当把字符串转成char[]时，单个char对应的就是十进制的数值
            binaryStr.append(Integer.toBinaryString(c)).append("_");
        }
        if (binaryStr.length() > 0) {
//            移除末尾的 _
            result = binaryStr.substring(0, binaryStr.length() - 1);
        }
        return result;
    }

    private void StrConvert() {

        //    1.十进制转成二进制。当把字符串转成char[]时，单个char对应的就是十进制的数值
        String toBinaryString = Integer.toBinaryString(18); //将十进制数转成字符串，例如n=5 ，s = "101"

        //2.将字符串转成整形
        int integera = Integer.valueOf("1002");  //当然s只能是数字类的字符串

        //    或者
        int parseInt = Integer.parseInt("1002");

        //3.将整形转成字符串
        String str = String.valueOf(1025);  // 直接转成了


        //4.将整形转成十六进制的数
        String toHexStrings = Integer.toHexString(18);   //输出结果12

    }
}
