package sptool.dao;

import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.model.Statistic;

import java.util.Date;

/**
 * Created by sergey on 6/6/16.
 */
public interface StatisticDao {
    public void save(Statistic statistic, Advertisement advertisement);
    public Statistic generalStatisticInPeriod(Advertisement ad, Date from, Date to);
    public Statistic generalStatisticInPeriodFromCategory(Category category, Date from, Date to);
}
