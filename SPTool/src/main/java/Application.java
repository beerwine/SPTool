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


        StatisticDao dao = new StatisticDaoImpl();

        dao.complicateQuery();

        Util.getSessionFactory().close();


    }
}
