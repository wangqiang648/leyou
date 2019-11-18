package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
     /**
     * @Description: 分页查询商品
     * @Params: [page, rows, saleable：上架状态 , key：搜索关键字]
     * @Author: WangQiang
     * @Date: 2019/11/12
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key){
        PageResult<SpuBo> result = this.goodsService.querySpuByPage(page, rows, saleable, key);
        if(result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }

    /**
    * @Description: 增加商品
    * @Params: spuBo：spu的bo类
    * @Author: WangQiang
    * @Date: 2019/11/13
    */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        try {
            this.goodsService.saveGoods(spuBo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
    * @Description: 根据id查询商品详情
    * @Params: id:商品id
    * @Author: WangQiang
    * @Date: 2019/11/13
    */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id")long id){
        SpuDetail spuDetail = this.goodsService.querySpuDetailById(id);
        if(spuDetail == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spuDetail);
    }


    /**
    * @Description: 根据商品id查询sku及库存
    * @Params: id:商品id
    * @Author: WangQiang
    * @Date: 2019/11/13
    */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuById(@RequestParam long id){
        List<Sku> skus = this.goodsService.querySkyAndStockById(id);
        if(skus == null || skus.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * @Description: 根据spubo更新商品
     * @Params: spu的bo对象
     * @Author: WangQiang
     * @Date: 2019/11/13
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        try {
            this.goodsService.updateGoods(spuBo);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
