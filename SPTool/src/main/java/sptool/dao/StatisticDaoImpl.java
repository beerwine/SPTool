package sptool.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import sptool.model.Advertisement;
import sptool.model.Statistic;
import sptool.util.Util;

import java.util.List;

/**
 * Created by sergey on 6/6/16.
 */
public class StatisticDaoImpl implements StatisticDao {
    @Override
    public void save(Statistic statistic, Advertisement advertisement) {
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
}
//        Criteria criteria = session.createCriteria(Advertisement.class, "ad").add(Restrictions.eq("ad.category.id", category.getId()));
