package sptool.dao;


import org.hibernate.Criteria;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.model.Statistic;
import sptool.util.Util;

import java.util.*;

/**
 * Created by sergey on 6/6/16.
 */
public class StatisticDaoImpl implements StatisticDao {
    @Override
    public void save(Statistic statistic, Advertisement advertisement){
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();


        Criteria criteria = session.createCriteria(Statistic.class, "st")
                .add(Restrictions.and(Restrictions.eq("st.add.id", advertisement.getId()),
                        Restrictions.eq("st.date", statistic.getDate())));

        Statistic aux = (Statistic) criteria.uniqueResult();

        if (aux != null)
        {
            aux.setClicks(statistic.getClicks());
            aux.setPaid(statistic.getPaid());
            session.update(aux);
        }else
        {
            session.persist(statistic);
        }

        tx.commit();
        session.close();

    }

    public JSONObject generalStatisticInPeriod(Advertisement ad, Date from, Date to)
    {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Statistic.class, "st").add(Restrictions.and(
                Restrictions.ge("st.date", from),
                Restrictions.le("st.date", to),
                Restrictions.eq("st.add.id", ad.getId())
        ));

        List<Statistic> listOfStaistic = criteria.list();


        int clicks = 0;
        int paid = 0;
        for (Statistic st:
             listOfStaistic) {

            clicks += st.getClicks();
            paid += st.getPaid();
        }

        JSONObject genStatistics = new JSONObject();
        genStatistics.put("totalPaid", new Integer(paid));
        genStatistics.put("totalClicked", new Integer(clicks));

        tx.commit();
        session.close();

        return genStatistics;
    }

    public JSONObject generalStatisticInPeriodFromCategory(Category category, Date from, Date to)
    {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Statistic.class, "st")
                .createAlias("st.add", "ad")
                .createAlias("ad.category", "ct")
                .add(Restrictions.and(
                        Restrictions.ge("st.date", from),
                        Restrictions.le("st.date", to),
                        Restrictions.eq("ct.id", category.getId())
                ));

        Set<Statistic> statistics = new HashSet<Statistic>(criteria.list());

        int paid = 0;
        int clicks = 0;

        for (Statistic st:
             statistics) {
            paid += st.getPaid();
            clicks += st.getClicks();
        }

        JSONObject genStatistic = new JSONObject();
        genStatistic.put("totalClicks",new Integer(clicks));
        genStatistic.put("totalPaid", new Integer(paid));


        tx.commit();
        session.close();

        return genStatistic;
    }

    public JSONArray complicateQuery(Date from, Date to)
    {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.getNamedQuery("@ALL_CATEGORIES");

        List<Category> categories = query.list();

        JSONArray complicateArray = new JSONArray();

        for (Category ct:
             categories) {

            List<Advertisement> adsWithMaxClicks = new ArrayList<Advertisement>();

            int max = -100;

            JSONObject item = new JSONObject();

            item.put("categoryID", ct.getId());
            item.put("categoryState", ct.getState());
            item.put("categoryName", ct.getName());

            JSONArray ads = new JSONArray();

            // Find max clicks in perion 'from - to' among all ads in category
            for (Advertisement ad:
                 ct.getAds()) {

                JSONObject thBestStatistic = generalStatisticInPeriod(ad, from, to);

                int auxMax = (Integer) thBestStatistic.get("totalClicked");

                // Collect ads
                if (auxMax == max)
                {
                    adsWithMaxClicks.add(ad);
                }

                if (auxMax > max)
                {
                    max = auxMax;
                    adsWithMaxClicks.clear();
                    adsWithMaxClicks.add(ad);
                }

            }

            for (Advertisement ad:
                 adsWithMaxClicks) {

                    JSONObject jAd = new JSONObject();

                    jAd.put("adID", ad.getId());
                    jAd.put("adName", ad.getName());
                    jAd.put("adState", ad.getState());
                    jAd.put("adClicks", max);

                    ads.add(jAd);
            }

            item.put("ads", ads);

            complicateArray.add(item);


        }

        tx.commit();
        session.close();

        return complicateArray;
    }
}

