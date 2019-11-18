package com.leyou.item.api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    /**
     * @Description: 根据分类id集合查询id名称集合
     * @Params: ids：分类id集合
     * @Author: WangQiang
     * @Date: 2019/11/16
     */
    @GetMapping
    public List<String> queryNamesByIds(@RequestParam("ids")List<Long> ids);
}
