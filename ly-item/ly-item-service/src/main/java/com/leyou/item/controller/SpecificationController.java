package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;


     /**
     * @Description: 根据分类id查询商品的规格组（比如手机--主体，基本信息，操作系统等）
     * @Params: [cid]分类的id
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    @GetMapping("group/{cid}")
    public ResponseEntity<List<SpecGroup>> findSpecGroupsByCid(@PathVariable("cid") long cid){
        List<SpecGroup> specGroupList =  this.specificationService.findSpecGroupsByCid(cid);
        if(specGroupList == null || specGroupList.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specGroupList);
    }

     /**
     * @Description: 根据分类id增加规格
     * @Params: cid 分类id
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    @GetMapping("group/add")
    public ResponseEntity<Void> addSpecGroupById(@RequestParam("cid") long cid,
                                                 @RequestParam("groupName") String groupName){
        int result = this.specificationService.addSpecGroupById(cid, groupName);
        if(result == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * @Description: 根据分类id和name删除规格
     * @Params: cid 分类id|groupName 分类的name
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    @GetMapping("group/delete")
    public ResponseEntity<Void> deleteSpecGroupById(@RequestParam("cid") long cid,
                                                 @RequestParam("groupName") String groupName){
        int result = this.specificationService.deleteSpecGroupById(cid, groupName);
        if(result == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
    * @Description: 根据分组的id查询商品的参数
    * @Params: gid (分组的id)
    * @Author: WangQiang
    * @Date: 2019/11/7
    */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> findSpecPrams(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic){
        List<SpecParam> specParamList = this.specificationService.
                findSpecParamsByGid(gid, cid, searching, generic);
        if(specParamList == null || specParamList.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specParamList);
    }
}
