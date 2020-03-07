package com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.MpCustomEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MasterMpCustomMapper {
    int deleteByPrimaryKey(Integer customId);

    int insert(MpCustomEntity record);

    int insertSelective(MpCustomEntity record);

    int updateByPrimaryKeySelective(MpCustomEntity record);

    int updateByPrimaryKey(MpCustomEntity record);

}