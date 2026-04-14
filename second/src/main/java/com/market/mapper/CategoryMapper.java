package com.market.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.market.pojo.Category;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM category")
    List<Category> listCategories();
    
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category getCategoryById(Integer id);

    @Insert("INSERT INTO category (name) VALUES (#{name})")
    void insertCategory(Category category);

    @Update("UPDATE category SET name = #{name} WHERE id = #{id}")
    void updateCategory(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(Integer id);
}
