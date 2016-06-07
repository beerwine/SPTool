package sptool.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sergey on 6/6/16.
 */
public class AdvertisementDaoImpl implements AdvertisementDao {
    @Override
    public void save(Advertisement advertisement) throws Exception{
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try
        {
            session.save(advertisement);
        }finally {
            tx.commit();
            session.close();
        }


    }

    @Override
    public Advertisement getAdvertisementById(int id) {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Advertisement ad = (Advertisement) session.get(Advertisement.class, id);

        tx.commit();
        session.close();

        return ad;
    }

    @Override
    public List<Advertisement> getListOfAdds(Category category, List<String> states) {

        if (states.size() == 0 || category == null)
            return new ArrayList<Advertisement>();

        Session session = Util.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Advertisement.class, "ad").add(Restrictions.eq("ad.category.id", category.getId()));

        List<Advertisement> aux = criteria.list();
        List<Advertisement> listOfAds = new ArrayList<Advertisement>();

        for (Advertisement ad:
             aux) {
            if (states.contains(ad.getState()))
                listOfAds.add(ad);
        }


        tx.commit();
        session.close();

        return listOfAds;


    }

    @Override
    public void delete(int id) {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Advertisement ad = (Advertisement) session.get(Advertisement.class, id);

        if (ad != null)
            session.delete(ad);

        tx.commit();
        session.close();

    }
}
