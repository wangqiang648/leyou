package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    //分页查询商品信息
    public PageResult<SpuBo> querySpuByPage(
            Integer page, Integer rows, Boolean saleable, String key) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //过滤搜索关键字
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title", "%"+key+"%");
        }
        //过滤上架信息
        if(saleable != null){
            criteria.orEqualTo("saleable", saleable);
        }
        //分页
        PageHelper.startPage(page, rows);

        Page<Spu> pageInfo = (Page<Spu>)this.spuMapper.selectByExample(example);
        //将spu转化成spubo
        List<SpuBo> spuBos = pageInfo.getResult().stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            //设置商品的名称--根据brand_id去查找商品名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            //设置分类的名称
            List<String> categoryNameList = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(),
                    spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(categoryNameList, "/"));

            return spuBo;
        }).collect(Collectors.toList());

        //返回结果
        return new PageResult<>(pageInfo.getTotal(), spuBos);
    }

    /**
    * @Description: 增加商品的方法
    * @Params:
    * @Author: WangQiang
    * @Date: 2019/11/12
    */
    @Transactional
    public void saveGoods(SpuBo spu) {
        //保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insertSelective(spu);
        //保存spu详情
        spu.getSpuDetail().setSpuId(spu.getId());
        this.spuDetailMapper.insertSelective(spu.getSpuDetail());
        //保存sku和库存信息
        this.saveSkuAndStock(spu);
    }

    /**
     * @Description: 根据id查询商品详情
     * @Params: id:商品id
     * @Author: WangQiang
     * @Date: 2019/11/13
     */
    public SpuDetail querySpuDetailById(long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * @Description: 根据商品id查询sku及库存
     * @Params: id:商品id
     * @Author: WangQiang
     * @Date: 2019/11/13
     */
    public List<Sku> querySkyAndStockById(long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = this.skuMapper.select(sku);
        for(Sku sku1 : skus){
            Stock stock = this.stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        }
        return skus;
    }


    /**
     * @Description: 根据spubo更新商品
     * @Params: spu的bo对象
     * @Author: WangQiang
     * @Date: 2019/11/13
     */
    public void updateGoods(SpuBo spuBo) {
        //判断sku是否为空
        List<Sku> skus = this.querySkyAndStockById(spuBo.getId());
        if(skus != null || skus.size() !=0){
            //有sku先删除及库存，然后在添加sku及库存
            for(Sku sku : skus){
                //现根据sku的id去删除库存，不能直接设置为0
                this.stockMapper.deleteByPrimaryKey(sku.getId());
                this.skuMapper.delete(sku);
            }
        }
        //更新sku
        this.saveSkuAndStock(spuBo);
        //更新spu
        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        //更新spuDetail
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

    }

    //保存sku和库存
    private void saveSkuAndStock(SpuBo spuBo){
        for(Sku sku : spuBo.getSkus()){
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        }
    }
}
