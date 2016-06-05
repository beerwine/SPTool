import org.hibernate.Session;
import org.hibernate.Transaction;
import sptool.dao.CategoryDao;
import sptool.dao.CategoryDaoImpl;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sergey on 6/5/16.
 */
public class App {
    public static void main(String[] args)
    {
        CategoryDao dao = new CategoryDaoImpl();

        Category category = new Category();
        category.setName("newName");
        category.setId(9);
        category.setState("ST");
        category.setState("Pending");

        dao.updateCategory(category);

        Util.getSessionFactory().close();
    }
}
