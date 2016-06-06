import sptool.dao.AdvertisementDao;
import sptool.dao.AdvertisementDaoImpl;
import sptool.dao.CategoryDao;
import sptool.dao.CategoryDaoImpl;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 6/6/16.
 */
public class Application {
    public static void main(String[] args)
    {

        AdvertisementDao dao = new AdvertisementDaoImpl();
        dao.delete(1);
        Util.getSessionFactory().close();


    }
}
