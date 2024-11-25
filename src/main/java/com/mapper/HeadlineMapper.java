package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.entity.HandledHeadline;
import com.entity.Headline;
import com.pageUtils.PageData;
import com.pageUtils.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


public interface HeadlineMapper extends BaseMapper<Headline> {
    IPage<PageData> selectPageMap(IPage<?> page, @Param("pageParam") PageParam param);
    HandledHeadline selectDetailHandledHeadline( Integer hId);
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String, Object> selectCertainRec(Integer hId);
}