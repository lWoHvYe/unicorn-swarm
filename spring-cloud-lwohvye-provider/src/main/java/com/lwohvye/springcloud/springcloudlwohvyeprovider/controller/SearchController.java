package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.SearchEntity;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/search")
@Scope("prototype")
public class SearchController {
    //solr服务地址
    private static String solrUrl = "http://127.0.0.1:8080/solr/solr_core";
    private static HttpSolrClient solrServer = new HttpSolrClient.Builder(solrUrl).build();


    /**
     * 根据关键字及类别搜索
     *
     * @param searchKey 关键字
     * @param pages
     * @return
     */
    @GetMapping("/solrSearch")
//    @ResponseBody
//    solr搜索
    public Object solrSearch(String searchKey, String pages, String limits) {
//        JSONObject json = new JSONObject();
        try {
            int currentPage = 1;
            int limit = 10;
//            未传curPage默认第一页
            if (!StringUtils.isEmpty(pages))
                currentPage = Integer.parseInt(pages);
            if (!StringUtils.isEmpty(limits))
                limit = Integer.parseInt(limits);
            if (!StringUtils.isEmpty(searchKey)) {
                searchKey = formatWords(searchKey);
            } else {
                searchKey = "*";
            }
            SolrQuery params = new SolrQuery();
            params.setQuery("name:" + searchKey);
//              开启edismax方式进行自定义权重算法
            params.set("defType", "edismax");
//              对于top为true的，加上指定的评分，
//              在生成索引时，需保证需置顶的记录的top值为布尔true
            params.set("bf", "if(top,10,0)");
//              frange函数设置查询范围
            params.addFilterQuery("{!frange l=1}query($q)");
            params.setFields("gid,name,rectype,geom");
            params.set("start", (currentPage - 1) * limit); //起始行
            params.set("rows", limit); //行
            QueryResponse query = solrServer.query(params);
            SolrDocumentList result = query.getResults();
            long numFound = result.getNumFound();
//            指定list初始长度，避免频繁扩容问题 new ArrayList<>(初始长度)
//            也可直接创建ArrayList，然后使用其ensureCapacity(欲放入数据的长度)，来避免频繁扩容，这种更适合于list中已有数据的
//            情况，否则不如第一种
            ArrayList<SearchEntity> list = new ArrayList<>();
            list.ensureCapacity(result.size());
            for (SolrDocument entries : result) {
                SearchEntity entity = new SearchEntity();
                entity.setId((Integer) entries.get("gid"));
                entity.setName((String) entries.get("name"));
                entity.setType((String) entries.get("rectype"));
                entity.setGeom((String) entries.get("geom"));
                list.add(entity);
            }
            PageUtil<SearchEntity> pageUtil = new PageUtil<>();
//            设置结果集
            pageUtil.setPageData(list);
//            设置当前页
            pageUtil.setCurrentPage(currentPage);
//            设置每页记录数
            pageUtil.setPageSize(limit);
//            设置总记录数
            pageUtil.setTotalCount(Long.valueOf(numFound).intValue());
//            将结果拼接为geoJson
//            json.put("list", list);
//            json.put("numFound", numFound);
//            json.put("curPage", currentPage);
//            json.put("totalPage", numFound / limit + 1);

            return pageUtil;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return json.toJSONString();
        return "success";
    }

    /**
     * 查询关键字格式化
     *
     * @param keyWord
     * @return
     * @throws UnsupportedEncodingException
     */
    public String formatWords(String keyWord) throws UnsupportedEncodingException {

//          将url中%替换为%25 防止解析报错
        keyWord = URLDecoder.decode(keyWord.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "UTF-8");
//          去除输入项中的可见符号
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(keyWord);
//          去除空字符串
        keyWord = m.replaceAll("").trim();
//          返回格式化字符串
        return keyWord;
    }

}
