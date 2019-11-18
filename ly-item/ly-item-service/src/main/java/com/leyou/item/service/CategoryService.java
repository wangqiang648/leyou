package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryById(long pid) {
        Category t = new Category();
        t.setParentId(pid);
        return categoryMapper.select(t);
    }

    public void saveCategory(Category category) {
        categoryMapper.insert(category);
    }

    public List<String> queryNamesByIds(List<Long> ids){
        List<Category> categoryList = this.categoryMapper.selectByIdList(ids);
        List<String> categoryNameList= categoryList.stream().map(category ->
            category.getName()
        ).collect(Collectors.toList());
        return categoryNameList;
    }


}
