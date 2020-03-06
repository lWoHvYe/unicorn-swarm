package com.lwohvye.springcloud.springcloudlwohvyeapi.common.util;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springboot.dubbointerface.common.util
 * @className ListUtil
 * @description 使用Stream分割list为多个list
 * @date 2020/1/15 15:48
 */

public class ListUtil<T> {

    //按每n个一组分割
    private Integer maxNumber;
    //    结果
    @Getter
    private List<List<T>> resultList;
    //    切割的List
    private List<T> subList;

    public ListUtil(Integer maxNumber, List<T> subList) {
        this.maxNumber = maxNumber;
        this.subList = subList;
        int limit = countStep(subList.size());
        subList(limit);
    }

    /**
     * 计算切分次数
     */
    private Integer countStep(Integer size) {
        return (size + maxNumber - 1) / maxNumber;
    }

    private void subList(int limit) {
//        这里需要先对resultList初始化
        resultList = new ArrayList<>();
        //方法一：使用流遍历操作，获取指定索引范围数据，放入结果集中
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> resultList.add(subList.stream().skip(i * maxNumber).limit(maxNumber).collect(Collectors.toList())));
        System.out.println(resultList);
    }

    private void subList1(int limit) {
        //方法二：获取分割后的集合，将指定索引范围内数据创建集合，放入结果集
        resultList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> subList.stream().skip(a * maxNumber).limit(maxNumber).parallel().collect(Collectors.toList())).collect(Collectors.toList());
        System.out.println(resultList);
    }

    private void subList3(int limit){
//        使用google guava对List进行分割
        resultList = Lists.partition(subList,limit);
    }
}
