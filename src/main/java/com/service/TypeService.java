package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pageUtils.PageParam;
import com.entity.Type;
import com.utils.Result;

public interface TypeService extends IService<Type> {
    Result<?> findNewPage(PageParam pageParam);

    Result<?> showHeadlineDetail(int hId);
}
