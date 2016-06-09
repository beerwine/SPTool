package sptool.dao;

import org.json.simple.JSONObject;
import sptool.model.Category;

import java.util.List;

/**
 * Created by sergey on 6/5/16.
 * DAO class for Category
 */
public interface CategoryDao {
    public void save(Category category);
    public Category getCategoryById(int id);
    public List<Category> getAllCategories();
    public void updateCategory(Category category);
    public void deleteCategory(int id);
}
