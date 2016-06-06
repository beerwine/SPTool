import sptool.dao.*;
import sptool.model.Advertisement;

import sptool.model.Statistic;
import sptool.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by sergey on 6/6/16.
 */
public class Application {
    public static void main(String[] args) throws ParseException {

        StatisticDao dao = new StatisticDaoImpl();
        AdvertisementDao adDao = new AdvertisementDaoImpl();

        Advertisement ad = adDao.getAdvertisementById(2);

        Statistic st = new Statistic();
        st.setPaid(1000);
        st.setClicks(3000);

//        st.setDate(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = sdf.parse("2016-06-05");

        st.setDate(date);

        dao.save(st, ad);

        Util.getSessionFactory().close();


    }
}
