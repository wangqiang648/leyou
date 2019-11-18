package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("page")
     /**
     * @Description: 分页查询品牌
     * @Params: [page 当前页, rows 每页条数, sortBy 排序字段, desc 升序或降序, key 查找关键字]
     * @Author: WangQiang
     * @Date: 2019/10/30
     */
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", defaultValue = "false")Boolean desc,
            @RequestParam(value = "page", required = false)String key){
        PageResult<Brand> result =
                this.brandService.queryBrandByPageAndSort(page, rows, sortBy, desc, key);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    //新增商品
    /**
     * @Description: 增加品牌
     * @Params: [brand 品牌, cids 分类ids]
     * @Author: WangQiang
     * @Date: 2019/10/30
     */
    @PostMapping("addPage")
    public ResponseEntity<Void> saveBrand(@RequestBody Brand brand, @RequestParam("cids") List<Long> cids){
        if(brand == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.brandService.saveBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("test")
    public ResponseEntity<Void> test1(@RequestBody Category category){
        this.categoryService.saveCategory(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
    * @Description: 根据分类id查询品牌
    * @Params: [cid]
    * @Author: WangQiang
    * @Date: 2019/11/12
    */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")long cid){
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if(brands == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brands);
    }

    /** 
    * @Description: 根据品牌id查询商品品牌 
    * @Params: id:品牌id
    * @Author: WangQiang
    * @Date: 2019/11/16 
    */  
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") long id){
        Brand brand = this.brandService.queryBrandById(id);
        if(brand == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brand);
    }

}
