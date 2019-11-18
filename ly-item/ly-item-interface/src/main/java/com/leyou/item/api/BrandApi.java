package com.leyou.item.api;
import com.leyou.item.pojo.Brand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("brand")
public interface BrandApi {

    /**
     * @Description: 根据品牌id查询商品品牌
     * @Params: id:品牌id
     * @Author: WangQiang
     * @Date: 2019/11/16
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") long id);

}

