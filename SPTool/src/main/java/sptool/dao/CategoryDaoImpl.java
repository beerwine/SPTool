package sptool.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
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
    public void save(Category category) {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.save(category);
	//TODO Save only Category without any Ads
        for (Advertisement ad:
             category.getAdds()) {
            session.save(ad);
        }


        tx.commit();
        session.close();
    }

    /**
     * Get category by ID.
     * @param id
     * @return Category object
     */

    public Category getCategoryById(int id) {
        Session session = Util.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Category category = (Category) session.get(Category.class, id);

        session.update(category);

        tx.commit();
        session.close();
        return category;
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

        Category ct = (Category) session.get(Category.class, category.getId());

        if (ct != null)
        {
            ct.setName(category.getName());
            ct.setState(category.getState());
            session.update(ct);
        }

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

        if (category != null)
            session.delete(category);

        tx.commit();
        session.close();

    }
}
