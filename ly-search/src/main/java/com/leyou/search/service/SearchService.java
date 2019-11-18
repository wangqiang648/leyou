package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    //用于skus属性的反序列化
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    /**
    * @Description: 根据spu拿到Goods对象
    * @Params: spu：商品的spu
    * @Author: WangQiang
    * @Date: 2019/11/17
    */
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        //查询spu中没有的属性
        //查询brandName
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //查询CategoryName，所有的分类
        List<String> categoryName = this.categoryClient.queryNamesByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //查询价格，所有sku的价格都需要，先把所有的sku查询出来
        List<Long> prices = new ArrayList<>();
        List<Sku> skus = this.goodsClient.querySkuById(spu.getId());
        //储存sku对象的集合，Goods中将集合反序列化为string
        List<Map<String, Object>> skuList = new ArrayList<>();
        for(Sku sku : skus){
            //把价格添加到集合
            prices.add(sku.getPrice());
            //添加sku中有用的属性到skuList集合
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isNotBlank(sku.getImages()) ?
                    StringUtils.split(sku.getImages(), ",")[0] : "");
            skuList.add(skuMap);
        }

        //查询规格参数
        //查询出所有的规格参数
        List<SpecParam> specPrams = this.specificationClient.findSpecPrams(
                null, spu.getCid3(), true, null);
        //查询规格参数的值，先获取SpuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spu.getId());
        //设置通用规格参数
        Map<Long, Object> genericSpecMap = MAPPER.readValue(
                spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>(){});
        //设置特殊规格参数
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(
                spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {});
        // 定义map接收规格参数{规格参数名，规格参数值}
        Map<String, Object> paramMap = new HashMap<>();
        specPrams.forEach(specParam -> {
            if(specParam.getGeneric()){
                String value = genericSpecMap.get(specParam.getId()).toString();
                if(specParam.getNumeric()){
                    // 如果是数值的话，判断该数值落在那个区间
                    value = chooseSegment(value, specParam);
                }
                paramMap.put(specParam.getName(), value);
            }else{
                paramMap.put(specParam.getName(), specialSpecMap.get(specParam.getId()));
            }
        });

        // 设置参数
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + " "
                + brand.getName() + " " + StringUtils.join(categoryName, " "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuList));
        goods.setSpecs(paramMap);

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
