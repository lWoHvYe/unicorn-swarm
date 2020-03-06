package com.lwohvye.springcloud.springcloudlwohvyeapi.common.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @PackageName: com.lwohvye.springcloud.springcloudlwohvyeapi.common.util
 * @ClassName: PageUtil
 * @Description: 分页相关工具类，当使用springboot-jpa时，提供相关的数据处理
 * 页面通过currentPage、pageSize、order进行相关参数的传递
 * @Author: Hongyan Wang
 * @Date: 2019/9/22 8:54
 */
@Getter
@Setter
public class PageUtil<T> implements Serializable {

    public static final long serialVersionUID = 1L;
    //  查询数据集合
    private List<T> pageData;
    //	页码
    private Integer currentPage = 1;
    //  每页记录数
    private Integer pageSize = 10;
    //  总记录数
    private Integer totalCount;
    //  总页数
    private Integer totalPages;
    //    排序列
    private String order;

//    private Pageable pageable;

    /**
     * @Description: 空参构造
     * @Param: []
     * @return:
     * @Author: Hongyan Wang
     * @Date: 2019/9/23 15:19
     */
    public PageUtil() {
    }


    private int obtPageCount() {
        if (this.totalCount % this.pageSize == 0) {
            return this.totalCount / this.pageSize;
        }
        return this.totalCount / this.pageSize + 1;
    }

    /**
     * @Description: 普通构造函数
     * @Param: [pageData, totalCount]
     * @return:
     * @Author: Hongyan Wang
     * @Date: 2019/9/23 15:20
     */
    public PageUtil(List<T> pageData, Integer totalCount) {
        this.pageData = pageData;
        this.totalCount = totalCount;
        this.totalPages = obtPageCount();
    }

    public void setPageEntity(Page<T> pageEntity) {
        /*
        接口Page继承了接口Slice
        总页数
        int getTotalPages()
        元素的总数
        long getTotalElements()
        返回当前页的索引（是第几页）
        int getNumber()
        返回作为List的页面内容
        List<T> getContent()
        返回当前在这个页上的元素的数量
        int getNumberOfElements()
        返回用于请求当前页的Pageable
        default Pageable    getPageable()
        返回页的大小。
        int getSize()
        返回页的排序参数。
        Sort getSort()
        页面是否有内容。
        boolean hasContent()
        是否有下一页。
        boolean hasNext()
        是否有上一页
        boolean hasPrevious()
        当前页是否是第一个
        boolean isFirst()
        当前页是否是最后一个
        boolean isLast()
        下一页的Pageable
        Pageable nextPageable()
        上一页的Pageable
        Pageable previousPageable()
         */
//        总页数
        totalPages = pageEntity.getTotalPages();
//        总记录数
        totalCount = Math.toIntExact(pageEntity.getTotalElements());
//        当前页
//        currentPage = pageEntity.getNumber();
//        每页记录数
//        pageSize = pageEntity.getSize();
//        查询结果集
        pageData = pageEntity.getContent();
    }

    public Pageable obtPageable() {
        Pageable pageable;
        if (StringUtils.isEmpty(order)) {
//           未传排序字段则使用默认排序
            pageable = PageRequest.of(currentPage - 1, pageSize);
        } else {
//        创建sort,使用静态方法创建
            var sort = Sort.by(Sort.Direction.DESC, order);
//        Sort.Order.by(order);
//        创建pageable,传入页码、每页记录数和排序字段
            pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        }
        return pageable;
    }

    //  是否是首页
    public boolean isFirst() {
        return (this.currentPage == 1) || (this.totalCount == 0);
    }

    //  是否是尾页
    public boolean isLast() {
        return (this.totalCount == 0) || (this.currentPage >= obtPageCount());
    }

    //  是否有下一页
    public boolean isHasNext() {
        return this.currentPage < obtPageCount();
    }

    //  是否有上一页
    public boolean isHasPrev() {
        return this.currentPage > 1;
    }

    //  下一页页码
    public Integer getNextPage() {
        if (this.currentPage >= obtPageCount()) {
            return obtPageCount();
        }
        return this.currentPage + 1;
    }

    //  上一页页码
    public Integer getPrevPage() {
        if (this.currentPage <= 1) {
            return 1;
        }
        return this.currentPage - 1;
    }
}
