package sptool.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.util.Util;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sergey on 6/5/16.
 * Implementation of CategoryDao
 */


public class CategoryDaoImpl implements CategoryDao {

    /**
     * Save new Category
     */
    public void save(Category category)  {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.persist(category);

        tx.commit();
        session.close();

    }

    /**
     * Get category by ID.
     * @param id
     * @return Category object
     */

    public JSONObject getCategoryById(int id) {
        Session session = Util.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Category category = (Category) session.get(Category.class, id);

        JSONObject jsonCategory = null;

        if (category != null)
        {
            jsonCategory = new JSONObject();

            jsonCategory.put("id", new Integer(category.getId()));

            jsonCategory.put("name", category.getName());

            jsonCategory.put("state", category.getState());

            JSONArray ads = new JSONArray();

            for (Advertisement ad:
                 category.getAds()) {

                JSONObject advertisement = new JSONObject();

                advertisement.put("id", new Integer(ad.getId()));
                advertisement.put("state", ad.getState());
                advertisement.put("name", ad.getName());
                advertisement.put("pucture", ad.getPictureUrl());
                advertisement.put("url", ad.getLinkUrl());

                ads.add(advertisement);
            }
            jsonCategory.put("ads", ads);

        }


        tx.commit();
        session.close();
        return jsonCategory;
    }

    /**
     * Return all Categories
     * @return
     */
    public List<Category> getAllCategories() {
        Session session = Util.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Query query = session.getNamedQuery("@ALL_CATEGORIES");

        List<Category> categories = query.list();

        tx.commit();
        session.close();

        return categories;
    }

    /**
     * Update category.
     * @param category
     */
    public void updateCategory(Category category) {

        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Category category1 = (Category) session.get(Category.class, category.getId());

        if (!category.getState().equals(category1.getState()))
        {
            category1.setState(category.getState());
            for (Advertisement ad:
                 category1.getAds()) {
                ad.setState(category1.getState());
            }
        }

        category1.setName(category.getName());

        session.update(category1);

        tx.commit();
        session.close();

    }

    /**
     * Delete category with id and all advertisements.
     * @param id
     */
    public void deleteCategory(int id) {

        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Category category = (Category) session.get(Category.class, id);

        session.delete(category);

        tx.commit();
        session.close();

    }
}
