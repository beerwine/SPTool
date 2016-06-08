package sptool.controller;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptool.dao.*;
import sptool.model.Advertisement;
import sptool.model.Category;
import sptool.model.Statistic;
import sptool.util.Util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sergey on 6/5/16.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private Validator getValidator()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }


    //-----------------------Retrieve category with certain ID--------------------------------------------------------//
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject>  getCategory(@PathVariable("id") int id)
    {
        CategoryDao dao = new CategoryDaoImpl();

        JSONObject category = dao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<JSONObject>(category, HttpStatus.OK);
    }

    //-----------------------Retrieve all categories------------------------------------------------------------------//
    @RequestMapping(value = "/category/", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> listAllCategories()
    {
        CategoryDao dao = new CategoryDaoImpl();

        List<Category> categories = dao.getAllCategories();

        if (categories.size() == 0)
            return new ResponseEntity<List<Category>>(HttpStatus.NOT_FOUND);

        Iterator<Category> iter = categories.iterator();

        while (iter.hasNext())
            iter.next().setAds(new ArrayList<Advertisement>());

        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    //-----------------------Create new category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<Void> createCategory(@RequestBody Category category)
    {

        Validator validator = getValidator();
        Set<ConstraintViolation<Category>> validationErrors = validator.validate(category);

        if (!validationErrors.isEmpty())
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }

        Session session = Util.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Category.class)
                .add(Restrictions.eq("name", category.getName()));

        Category category1 = (Category) criteria.uniqueResult();

        tx.commit();

        if (category1 != null)
        {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        CategoryDao dao = new CategoryDaoImpl();


        dao.save(category);

        return new ResponseEntity<Void>(HttpStatus.OK);


    }

    //---------------------------Update category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCategory(@PathVariable("id") int id, @RequestBody Category category)
    {

        CategoryDao dao = new CategoryDaoImpl();

        JSONObject ct = dao.getCategoryById(id);

        if (ct != null)
        {
            Validator validator = getValidator();
            Set<ConstraintViolation<Category>> validationErrors = validator.validate(category);

            if (!validationErrors.isEmpty())
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);

            Category aux = new Category();

            aux.setId(id);
            aux.setState(category.getState());
            aux.setName(category.getName());

            dao.updateCategory(aux);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    //---------------------------Remove category----------------------------------------------------------------------//
//    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Void> removeCategory(@PathVariable("id") int id)
//    {
//        CategoryDao dao = new CategoryDaoImpl();
//
//
//        Category category = dao.getCategoryById(id);
//
//        if (category != null)
//        {
//            dao.deleteCategory(id);
//            return new ResponseEntity<Void>(HttpStatus.OK);
//        }
//
//        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
//    }

    //---------------------------------Create advertisement-----------------------------------------------------------//
//    @RequestMapping(value = "/category/{id}/advertisement/", method = RequestMethod.POST, headers="Accept=application/json")
//    public ResponseEntity<Void> createAd(@PathVariable("id") int id, @RequestBody Advertisement ad)
//    {
//        CategoryDao cdao = new CategoryDaoImpl();
//        Category category = cdao.getCategoryById(id);
//
//        if (category == null)
//        {
//            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
//        }
//
//        Validator validator = getValidator();
//        Set<ConstraintViolation<Advertisement>> validationErrors = validator.validate(ad);
//
//        if (!validationErrors.isEmpty())
//        {
//            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        ad.setCategory(category);
//        AdvertisementDao adao = new AdvertisementDaoImpl();
//        adao.save(ad);
//
//
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
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
        ad.setStatistics(new ArrayList<Statistic>());
        ad.getCategory().setAds(new ArrayList<Advertisement>());
        return new ResponseEntity<Advertisement>(ad, HttpStatus.OK);
    }


    //-----------------------Retrieve advertisement from category-----------------------------------------------------//
//    @RequestMapping(value = "/category/{id}/advertisement", method = RequestMethod.GET)
//    public ResponseEntity<List<Advertisement>> getAdsFromCategory(@PathVariable("id") int id, @RequestParam(value = "states") List<String> states)
//    {
//
//        CategoryDao dao = new CategoryDaoImpl();
//        Category category = dao.getCategoryById(id);
//        if (category == null)
//        {
//            return new ResponseEntity<List<Advertisement>>(HttpStatus.NOT_FOUND);
//        }
//
//        AdvertisementDao adao = new AdvertisementDaoImpl();
//
//        List<Advertisement> ads = adao.getListOfAdds(category, states);
//
//        return new ResponseEntity<List<Advertisement>>(ads, HttpStatus.OK);
//    }

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
    public ResponseEntity<Void> createStatistic(@PathVariable("id") int id, @RequestBody Statistic statistic)
    {
        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        Validator validator = getValidator();
        Set<ConstraintViolation<Statistic>> validationErrors = validator.validate(statistic);

        if (!validationErrors.isEmpty())
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }


        StatisticDao sdao = new StatisticDaoImpl();


        statistic.setAdd(ad);


        sdao.save(statistic, ad);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //-------------------------General statistic about advertisement--------------------------------------------------//
    @RequestMapping(value = "/advertisement/{id}/general", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> generalStatistic(@PathVariable("id") int id, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to)
    {

        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dateFrom;
        Date dateTo;

        try
        {
            dateFrom = sdf.parse(from);
        } catch (ParseException e) {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            dateTo = sdf.parse(to);
        } catch (ParseException e) {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_ACCEPTABLE);
        }

        StatisticDao sdao = new StatisticDaoImpl();

        JSONObject statistic = sdao.generalStatisticInPeriod(ad, dateFrom, dateTo);

        return new ResponseEntity<JSONObject>(statistic, HttpStatus.OK);
    }

    //------------------------Statistic about ads in category---------------------------------------------------------//
//    @RequestMapping(value = "/category/{id}/statistic", method = RequestMethod.GET)
//    public ResponseEntity<JSONObject> generalStatisticAboutCategory(@PathVariable("id") int id, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to)
//    {
//        CategoryDao dao = new CategoryDaoImpl();
//        Category category = dao.getCategoryById(id);
//
//        if (category == null)
//        {
//            return new ResponseEntity<JSONObject>(HttpStatus.NOT_FOUND);
//        }
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        Date dateFrom;
//        Date dateTo;
//
//        try
//        {
//            dateFrom = sdf.parse(from);
//        } catch (ParseException e) {
//            return new ResponseEntity<JSONObject>(HttpStatus.NOT_ACCEPTABLE);
//        }
//        try {
//            dateTo = sdf.parse(to);
//        } catch (ParseException e) {
//            return new ResponseEntity<JSONObject>(HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        StatisticDao sdao = new StatisticDaoImpl();
//
//        JSONObject statistic = sdao.generalStatisticInPeriodFromCategory(category, dateFrom, dateTo);
//
//        return new ResponseEntity<JSONObject>(statistic, HttpStatus.OK);
//    }


}
