package com.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.HandledHeadline;
import com.entity.Headline;
import com.pageUtils.PageData;
import com.pageUtils.PageInfo;
import com.pageUtils.PageParam;
import com.entity.Type;
import com.mapper.HeadlineMapper;
import com.mapper.TypeMapper;
import com.service.TypeService;
import com.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>  implements TypeService {

    private final HeadlineMapper headlineMapper;

    @Autowired
    public TypeServiceImpl(HeadlineMapper headlineMapper) {
        this.headlineMapper = headlineMapper;
    }

    /**
     * 获取前端传来的分页参数(封装在pageParam中），根据参数进行分页
     * <p>结果需要进行多次封装：</p>
     * <p>result{
     *    xxx
     *    xxx
     *    data:{
     *         pageInfo:
     *         ...
     *         ...
     *         pageData{
     *             []
     *             []
     *             ...
     *         }
     *     }
     * }
     * </p>
     * <p>可用Map进行数据的封装。但使用Map封装可读性差，所以将需要封装的数据都单独做一个类，存放在pageUtils包中</p>
     * @param pageParam pageData实体类
     * @return result
     */
    @Override
    public Result<?> findNewPage(PageParam pageParam) {
         /*
            设置分页参数
         */

        //setPage用于设置分页参数，页数和几行数据
        IPage<Headline> setPage = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());

        //resultPage用于接收mapper方法返回的结果对象。结果对象封装为IPage接口（data中的pageData
        IPage<PageData> resultPage = headlineMapper.selectPageMap(setPage, pageParam);

        //将pageInfo中的字段封装成对象，将resultPage中的数据装入对象中
        PageInfo info = new PageInfo();
        info.setPageData(resultPage.getRecords());
        info.setPageSize(resultPage.getSize());
        info.setPageNum(resultPage.getCurrent());
        info.setTotalPage(resultPage.getPages());
        info.setTotalSize(resultPage.getTotal());

        //将上面装载好的pageInfo对象再装载到Map中
        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("pageInfo",info);
        // 响应JSON
        return Result.ok(pageInfoMap);
    }


    /**
     * 根据hId查询表格数据，需要多表查询，所以使用自定义mapper方法，随后将结果封装到Map中
     * @param hId int
     * @return result
     */
    @Override
    public Result<?> showHeadlineDetail(int hId) {
        HandledHeadline handledHeadline = headlineMapper.selectDetailHandledHeadline(hId);
        Headline headline = new Headline();
        headline.setHId(hId);
        headline.setPageViews((int) (handledHeadline.getPageViews()+1));
        headline.setVersion(headlineMapper.selectById(hId).getVersion());
        headlineMapper.updateById(headline);
        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("headline",handledHeadline);
        return Result.ok(pageInfoMap);
    }
}
