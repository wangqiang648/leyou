package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;
    //条件查询商品
    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);

        //排序，通过example
        Example example = new Example(Brand.class);
        //先过滤
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("name", "%"+key+"%").
                    orEqualTo("letter", key.toUpperCase());
        }
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }

        //查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);

        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

   //增加品牌
    public void saveBrand(Brand brand, List<Long> cids) {
        //保存品牌
        brandMapper.insert(brand);
        //保存品牌商品中间表
        for(long cid : cids){
            brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }
    /**
    * @Description: 根据分类id查询品牌
    * @Params:
    * @Author: WangQiang
    * @Date: 2019/11/12
    */
    public List<Brand> queryBrandsByCid(long cid) {
        return this.brandMapper.queryBrandsByCid(cid);
    }

    /**
     * @Description: 根据品牌id查询商品品牌
     * @Params: id:品牌id
     * @Author: WangQiang
     * @Date: 2019/11/16
     */
    public Brand queryBrandById(long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
