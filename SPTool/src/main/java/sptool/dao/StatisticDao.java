package sptool.dao;

import org.json.simple.JSONObject;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.model.Statistic;

import java.util.Date;

/**
 * Created by sergey on 6/6/16.
 */
public interface StatisticDao {
    public void save(Statistic statistic, Advertisement advertisement);
    public JSONObject generalStatisticInPeriod(Advertisement ad, Date from, Date to);
    public JSONObject generalStatisticInPeriodFromCategory(Category category, Date from, Date to);
    public void complicateQuery(Date from, Date to);
}
