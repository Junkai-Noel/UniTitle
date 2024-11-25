package com.controller;


import com.entity.Headline;
import com.security.JwtSecurityProperties;
import com.service.HeadlineService;
import com.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/headline")
public class HeadlineController {


    private final HeadlineService headlineService;
    private final JwtSecurityProperties jwtSecurityProperties;

    @Autowired
    public HeadlineController(JwtSecurityProperties jwtSecurityProperties, HeadlineService headlineService) {
        this.jwtSecurityProperties = jwtSecurityProperties;
        this.headlineService = headlineService;
    }

    /**
     * 实现步骤:
     *   1. token获取userId [无需校验,拦截器会校验]
     *   2. 封装headline数据
     *   3. 插入数据即可
     */
    @PostMapping("/publish")
    public Result<?> publish(@RequestBody Headline headline,
                             @RequestHeader String token) {
        headline.setPublisher(jwtSecurityProperties.getUIdFromToken(token));
        return headlineService.publish(headline);
    }

    /**
     *  查询Headline对象信息
     * @param hId Integer
     * @return result
     */
    @PostMapping("/findHeadlineByHId")
    public Result<?> findHeadLineById(Integer hId){
        return headlineService.findByHId(hId);
    }

    /**
     * 前端传参，更新记录
     * @param headline Headline
     * @return result
     */
    @PostMapping("/update")
    public Result<?> update(@RequestBody Headline headline){
        return headlineService.updateHeadline(headline);
    }

    /**
     * 前端传id，进行删除
     * @param hId Integer
     * @return result
     */
    @PostMapping("/removeByHId")
    public Result<?> removeByHId(Integer hId){
       if(headlineService.removeById(hId))
           return Result.ok();
       return null;
    }
}
