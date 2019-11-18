package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /** 
    * @Description: 根据父节点id查询所有分类
    * @Params: 
    * @Author: WangQiang
    * @Date: 2019/11/16 
    */  
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam("pid") long pid){
        List<Category> categoryList = this.categoryService.queryCategoryById(pid);
        if(categoryList == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoryList);
    }

    /**
    * @Description: 根据分类id集合查询id名称集合
    * @Params: ids：分类id集合
    * @Author: WangQiang
    * @Date: 2019/11/16
    */
    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> categoryNames = this.categoryService.queryNamesByIds(ids);
        if(categoryNames == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoryNames);
    }
}
