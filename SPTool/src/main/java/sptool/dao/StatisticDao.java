package sptool.dao;

import sptool.model.Advertisement;
import sptool.model.Statistic;

/**
 * Created by sergey on 6/6/16.
 */
public interface StatisticDao {
    public void save(Statistic statistic, Advertisement advertisement);
}
