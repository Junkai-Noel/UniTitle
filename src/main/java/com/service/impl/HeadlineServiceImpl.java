package com.service.impl;

import com.entity.HandledHeadline;
import com.entity.Headline;
import com.mapper.HeadlineMapper;
import com.service.HeadlineService;
import com.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>  implements HeadlineService {

    private final HeadlineMapper headlineMapper;

    @Autowired
    public HeadlineServiceImpl(HeadlineMapper headlineMapper) {
        this.headlineMapper = headlineMapper;
    }

    /**
     * 发布数据（headline表格中创建新记录）
     * @param headline Headline
     * @return result
     */
    @Override
    public Result<?> publish(@NotNull Headline headline) {
        headline.setPageViews(0);
        headline.setCreateTime(new Date().toInstant());
        headline.setUpdateTime(new Date().toInstant());
        headlineMapper.insert(headline);
        return Result.ok();
    }

    /**
     *  查询Headline对象，并获取信息
     * @param hId Integer
     * @return result
     */
    @Override
    public Result<?> findByHId(Integer hId) {
        Map<String,Object> headlineInner = headlineMapper.selectCertainRec(hId);
        Map<String,Object> headline = new HashMap<>();
        headline.put("headline",headlineInner);
        return Result.ok(headline);
    }

    /**
     * 更新Headline对象，并update到数据库
     * @param headline Headline
     * @return result
     */
    @Override
    public Result<?> updateHeadline(@NotNull Headline headline) {
        Headline databaseHeadline = headlineMapper.selectById(headline.getHId());
        databaseHeadline.setTitle(headline.getTitle());
        databaseHeadline.setArticle(headline.getArticle());
        databaseHeadline.setUpdateTime(new Date().toInstant());
        headlineMapper.updateById(databaseHeadline);
        return Result.ok();
    }
}
