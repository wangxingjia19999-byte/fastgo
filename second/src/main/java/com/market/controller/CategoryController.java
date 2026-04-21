package com.market.controller;

import com.market.mapper.CategoryMapper;
import com.market.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping
    public List<Category> list() {
        return categoryMapper.listCategories();
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable Integer id) {
        return categoryMapper.getCategoryById(id);
    }

    @PostMapping
    public void create(@RequestBody Category category) {
        categoryMapper.insertCategory(category);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody Category category) {
        category.setId(id);
        categoryMapper.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryMapper.deleteCategory(id);
    }
}
