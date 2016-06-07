package sptool.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sptool.dao.*;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.model.Statistic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sergey on 6/5/16.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    //-----------------------Retrieve category with certain ID--------------------------------------------------------//
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category>  getCategory(@PathVariable("id") int id)
    {
        CategoryDao dao = new CategoryDaoImpl();

        Category category = dao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }

    //-----------------------Retrieve all categories------------------------------------------------------------------//
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> listAllCategories()
    {
        CategoryDao dao = new CategoryDaoImpl();

        List<Category> categories = dao.getAllCategories();

        if (categories.size() == 0)
            return new ResponseEntity<List<Category>>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    //-----------------------Create new category----------------------------------------------------------------------//
    @RequestMapping(value = "/category", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<Void> createCategory(@RequestBody Category category)
    {

        CategoryDao dao = new CategoryDaoImpl();

        Category category1 = dao.getCategoryById(category.getId());

        if (category1 == null)
        {
            try
            {
                dao.save(category);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

            }
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }

        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    //---------------------------Update category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCategory(@PathVariable("id") int id, @RequestBody Category category)
    {
        CategoryDao dao = new CategoryDaoImpl();

        Category aux = dao.getCategoryById(id);

        if (aux != null)
        {
            aux.setState(category.getState());
            aux.setName(category.getName());
            dao.updateCategory(aux);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    //---------------------------Remove category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeCategory(@PathVariable("id") int id)
    {
        CategoryDao dao = new CategoryDaoImpl();


        Category category = dao.getCategoryById(id);

        if (category != null)
        {
            dao.deleteCategory(id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    //---------------------------------Create advertisement-----------------------------------------------------------//
    @RequestMapping(value = "/category/{id}/advertisement", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<Void> createAd(@PathVariable("id") int id, @RequestBody Advertisement ad)
    {
        CategoryDao cdao = new CategoryDaoImpl();
        Category category = cdao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        ad.setCategory(category);

        try
        {
            AdvertisementDao adao = new AdvertisementDaoImpl();
            adao.save(ad);
        } catch (Exception e) {
            e.printStackTrace();
            new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    //-----------------------Retrieve advertisement with certain ID---------------------------------------------------//
    @RequestMapping(value = "/advertisement/{id}", method = RequestMethod.GET)
    public ResponseEntity<Advertisement> getAdById(@PathVariable("id") int id)
    {
        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<Advertisement>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Advertisement>(ad, HttpStatus.OK);
    }


    //-----------------------Retrieve advertisement from category-----------------------------------------------------//
    @RequestMapping(value = "/category/{id}/advertisement", method = RequestMethod.GET)
    public ResponseEntity<List<Advertisement>> getAdsFromCategory(@PathVariable("id") int id, @RequestParam(value = "states") List<String> states)
    {

        CategoryDao dao = new CategoryDaoImpl();
        Category category = dao.getCategoryById(id);
        if (category == null)
        {
            return new ResponseEntity<List<Advertisement>>(HttpStatus.NOT_FOUND);
        }

        AdvertisementDao adao = new AdvertisementDaoImpl();

        List<Advertisement> ads = adao.getListOfAdds(category, states);

        return new ResponseEntity<List<Advertisement>>(ads, HttpStatus.OK);
    }

    //-----------------------------------Remove ad with ID------------------------------------------------------------//
    @RequestMapping(value = "/advertisement/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeAd(@PathVariable("id") int id)
    {
        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        dao.delete(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //------------------------------------------Create statistic about ad---------------------------------------------//
    @RequestMapping(value = "/advertisement/{id}/statistic", method = RequestMethod.POST)
    public ResponseEntity<Void> createStatistic(@PathVariable("id") int id, @RequestBody(required = false) Statistic statistic)
    {
        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        StatisticDao sdao = new StatisticDaoImpl();

        if (statistic == null)
        {
            statistic = new Statistic();
            statistic.setDate(new Date());
            statistic.setClicks(0);
            statistic.setPaid(0);
        }

        statistic.setAdd(ad);


        sdao.save(statistic, ad);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //-------------------------General statistic about advertisement--------------------------------------------------//
    @RequestMapping(value = "/advertisement/{id}/general", method = RequestMethod.GET)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public ResponseEntity<Statistic> generalStatistic(@PathVariable("id") int id, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to)
    {

        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<Statistic>(HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dateFrom;
        Date dateTo;

        try
        {
            dateFrom = sdf.parse(from);
        } catch (ParseException e) {
            dateFrom = new Date();
        }

        try {
            dateTo = sdf.parse(to);
        } catch (ParseException e) {
            dateTo = new Date();
        }

        StatisticDao sdao = new StatisticDaoImpl();

        Statistic statistic = sdao.generalStatisticInPeriod(ad, dateFrom, dateTo);

        return new ResponseEntity<Statistic>(statistic, HttpStatus.OK);
    }

    //------------------------Statistic about ads in category---------------------------------------------------------//
    @RequestMapping(value = "/category/{id}/statistic", method = RequestMethod.GET)
    public ResponseEntity<Statistic> generalStatisticAboutCategory(@PathVariable("id") int id, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to)
    {
        CategoryDao dao = new CategoryDaoImpl();
        Category category = dao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<Statistic>(HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dateFrom;
        Date dateTo;

        try
        {
            dateFrom = sdf.parse(from);
        } catch (ParseException e) {
            dateFrom = new Date();
        }
        try {
            dateTo = sdf.parse(to);
        } catch (ParseException e) {
            dateTo = new Date();
        }

        StatisticDao sdao = new StatisticDaoImpl();

        Statistic statistic = sdao.generalStatisticInPeriodFromCategory(category, dateFrom, dateTo);

        return new ResponseEntity<Statistic>(statistic, HttpStatus.OK);
    }


}
