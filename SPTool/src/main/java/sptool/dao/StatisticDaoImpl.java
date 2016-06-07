package sptool.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;

import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.model.Statistic;
import sptool.util.Util;

import java.util.Date;
import java.util.List;

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
            session.save(statistic);
        }

        tx.commit();
        session.close();

    }

    public Statistic generalStatisticInPeriod(Advertisement ad, Date from, Date to)
    {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Statistic.class, "st").add(Restrictions.and(
                Restrictions.ge("st.date", from),
                Restrictions.le("st.date", to),
                Restrictions.eq("st.add.id", ad.getId())
        ));

        List<Statistic> listOfStaistic = criteria.list();

        Statistic statistic = new Statistic();

        statistic.setAdd(ad);
        statistic.setDate(new Date());

        int clicks = 0;
        int paid = 0;
        for (Statistic st:
             listOfStaistic) {

            clicks += st.getClicks();
            paid += st.getPaid();
        }

        statistic.setClicks(clicks);
        statistic.setPaid(paid);

        tx.commit();
        session.close();
        return statistic;
    }

    public Statistic generalStatisticInPeriodFromCategory(Category category, Date from, Date to)
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

        List<Statistic> statistics = criteria.list();

        Statistic statistic = new Statistic();

        int paid = 0;
        int clicks = 0;

        for (Statistic st:
             statistics) {
            paid += st.getPaid();
            clicks += st.getClicks();
        }

        statistic.setDate(new Date());
        statistic.setClicks(clicks);
        statistic.setPaid(paid);

        tx.commit();
        session.close();

        return statistic;
    }

    public void complicateQuery(Date from, Date to)
    {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Statistic.class)
                .setProjection(Projections.projectionList()
                .add(Projections.groupProperty("add.id"))
                .add(Projections.max("clicks")));

        //Get max from every advertisement
        DetachedCriteria subCriteria = DetachedCriteria.forClass(Statistic.class)
                .setProjection(Projections.projectionList()
                .add(Projections.groupProperty("add.id"))
                .add(Projections.max("clicks")));

        //Get statistics in period
        DetachedCriteria anotherCriteria = DetachedCriteria.forClass(Statistic.class)
                .add(Restrictions.and(
                        Restrictions.ge("date", from),
                        Restrictions.le("date", to)
                ));


        tx.commit();
        session.close();
    }
}