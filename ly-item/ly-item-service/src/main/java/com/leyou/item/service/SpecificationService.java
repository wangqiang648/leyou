package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * @Description: 根据分类id查询商品的规格组（比如手机--主体，基本信息，操作系统等）
     * @Params: [cid]商品的id
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    public List<SpecGroup> findSpecGroupsByCid(long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    /**
     * @Description:
     * @Params: gid (分组的id)
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    public List<SpecParam> findSpecParamsByGid(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return this.specParamMapper.select(specParam);
    }

    /**
     * @Description: 根据分类id增加规格
     * @Params: cid 分类id
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    public int addSpecGroupById(long cid, String groupName) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(groupName);
        return this.specGroupMapper.insert(specGroup);
    }

    /**
     * @Description: 根据分组的id查询商品的参数
     * @Params: gid (分组的id)
     * @Author: WangQiang
     * @Date: 2019/11/7
     */
    public int deleteSpecGroupById(long cid, String groupName) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(groupName);
        return this.specGroupMapper.delete(specGroup);
    }
}
