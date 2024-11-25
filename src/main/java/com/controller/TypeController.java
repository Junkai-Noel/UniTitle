package com.controller;

import com.pageUtils.PageParam;
import com.entity.Type;
import com.service.impl.TypeServiceImpl;
import com.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/portal")
public class TypeController {

    private final TypeServiceImpl typeServiceImpl;

    @Autowired
    public TypeController(TypeServiceImpl typeServiceImpl) {
        this.typeServiceImpl = typeServiceImpl;
    }

    /**
     * 获取全部Type数据
     * <p>直接使用IService层方法</p>
     * @return result.ok(List<E>) Result<?>
     */
    @GetMapping("/findAllTypes")
    public Result<?> findAllTypes() {
        List<Type> list = typeServiceImpl.list();
        return Result.ok(list);
    }

    @PostMapping("/findNewsPage")
    public Result<?> findNewsPage(@RequestBody PageParam pageParam) {
        return typeServiceImpl.findNewPage(pageParam);
    }

    @GetMapping("/showHeadlineDetails")
    public Result<?> showHeadlineDetail(int hId) {
        return typeServiceImpl.showHeadlineDetail(hId);
    }
}
