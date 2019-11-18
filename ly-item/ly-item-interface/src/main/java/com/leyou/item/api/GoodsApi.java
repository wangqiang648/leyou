package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {

    /**
     * @Description: 分页查询商品
     * @Params: [page, rows, saleable：上架状态 , key：搜索关键字]
     * @Author: WangQiang
     * @Date: 2019/11/12
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    /**
     * @Description: 根据id查询商品详情
     * @Params: id:商品id
     * @Author: WangQiang
     * @Date: 2019/11/13
     */
    @GetMapping("spu/detail/{id}")
    public SpuDetail querySpuDetailById(@PathVariable("id")long id);

    /**
     * @Description: 根据商品id查询sku及库存
     * @Params: id:商品id
     * @Author: WangQiang
     * @Date: 2019/11/13
     */
    @GetMapping("sku/list")
    public List<Sku> querySkuById(@RequestParam long id);

}
