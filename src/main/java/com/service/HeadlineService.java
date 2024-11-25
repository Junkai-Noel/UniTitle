package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Headline;
import com.utils.Result;

public interface HeadlineService extends IService<Headline> {
    Result<?> publish(Headline headline);

    Result<?> findByHId(Integer hId);

    Result<?> updateHeadline(Headline headline);
}
