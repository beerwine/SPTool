package sptool.dao;

import sptool.model.Advertisement;
import sptool.model.Category;

import java.util.List;

/**
 * Created by sergey on 6/6/16.
 */
public interface AdvertisementDao {
    public void save(Advertisement advertisement) throws Exception;
    public Advertisement getAdvertisementById(int id);
    public List<Advertisement> getListOfAdds(Category category, List<String> states);
    public void delete(int id);
}
