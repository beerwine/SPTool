import sptool.dao.*;
import sptool.model.Advertisement;

import sptool.model.Category;
import sptool.model.Statistic;
import sptool.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by sergey on 6/6/16.
 */
public class Application {
    public static void main(String[] args) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date from = sdf.parse("2015-03-01");

        CategoryDao dao = new CategoryDaoImpl();

        Category category = dao.getCategoryById(2);


        StatisticDao sdao = new StatisticDaoImpl();



        Statistic statistic = sdao.generalStatisticInPeriodFromCategory(category, from, new Date());

        System.out.print(statistic.getClicks() + " " + statistic.getPaid());

        Util.getSessionFactory().close();


    }
}
