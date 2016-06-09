package sptool.controller;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
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

        Category category = dao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_FOUND);
        }

        JSONObject categoryInfo = new JSONObject();
        categoryInfo.put("id", category.getId());
        categoryInfo.put("state", category.getState());
        categoryInfo.put("name", category.getName());
        categoryInfo.put("totalAds", category.getAds().size());

        return new ResponseEntity<JSONObject>(categoryInfo, HttpStatus.OK);
    }

    //-----------------------Retrieve all categories------------------------------------------------------------------//
    @RequestMapping(value = "/category/", method = RequestMethod.GET)
    public ResponseEntity<JSONArray> listAllCategories()
    {
        CategoryDao dao = new CategoryDaoImpl();

        List<Category> categories = dao.getAllCategories();

        if (categories.size() == 0)
            return new ResponseEntity<JSONArray>(HttpStatus.NOT_FOUND);



        JSONArray allCategories = new JSONArray();


        for (Category ct:
             categories) {
            JSONObject aux = new JSONObject();

            aux.put("id", ct.getId());
            aux.put("state", ct.getState());
            aux.put("name", ct.getName());
            aux.put("totalAds", ct.getAds().size());

            allCategories.add(aux);
        }

        return new ResponseEntity<JSONArray>(allCategories, HttpStatus.OK);
    }

    //-----------------------Create new category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<String> createCategory(@RequestBody Category category)
    {

        Validator validator = getValidator();
        Set<ConstraintViolation<Category>> validationErrors = validator.validate(category);

        if (!validationErrors.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Here was some error in Category object." +
                    "Please, check all fields.");

        }

        Session session = Util.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Category where name = :sameName");
        query.setParameter("sameName", category.getName());


        Category category1 = (Category) query.uniqueResult();

        tx.commit();

        if (category1 != null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category with this name already exists.");
        }

        CategoryDao dao = new CategoryDaoImpl();


        dao.save(category);

        return  ResponseEntity.ok("New category has beed created");


    }

    //---------------------------Update category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCategory(@PathVariable("id") int id, @RequestBody Category category)
    {

        CategoryDao dao = new CategoryDaoImpl();

        Category ct = dao.getCategoryById(id);

        if (ct != null)
        {
            Validator validator = getValidator();
            Set<ConstraintViolation<Category>> validationErrors = validator.validate(category);

            if (!validationErrors.isEmpty())
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);


            ct.setId(id);
            ct.setState(category.getState());
            ct.setName(category.getName());

            dao.updateCategory(ct);
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
    @RequestMapping(value = "/category/{id}/advertisement/", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<Void> createAd(@PathVariable("id") int id, @RequestBody Advertisement ad)
    {
        CategoryDao cdao = new CategoryDaoImpl();
        Category category = cdao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        Validator validator = getValidator();
        Set<ConstraintViolation<Advertisement>> validationErrors = validator.validate(ad);

        if (!validationErrors.isEmpty())
        {
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }

        ad.setCategory(category);
        AdvertisementDao adao = new AdvertisementDaoImpl();
        adao.save(ad);


        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    //-----------------------Retrieve advertisement with certain ID---------------------------------------------------//
    @RequestMapping(value = "/advertisement/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getAdById(@PathVariable("id") int id)
    {
        AdvertisementDao dao = new AdvertisementDaoImpl();

        Advertisement ad = dao.getAdvertisementById(id);

        if (ad == null)
        {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_FOUND);
        }


        JSONObject adInfo = new JSONObject();

        adInfo.put("id", ad.getId());
        adInfo.put("picture", ad.getPictureUrl());
        adInfo.put("url", ad.getLinkUrl());
        adInfo.put("state", ad.getState());
        adInfo.put("name", ad.getName());
        adInfo.put("category", ad.getCategory().getId());

        JSONArray statistics = new JSONArray();

        for (Statistic st:
             ad.getStatistics()) {
            JSONObject statistic = new JSONObject();

            statistic.put("id", st.getId());
            statistic.put("paid", st.getPaid());
            statistic.put("clicks", st.getClicks());
            statistic.put("date", st.getDate());

            statistics.add(statistic);
        }

        adInfo.put("statistics", statistics);

        return new ResponseEntity<JSONObject>(adInfo, HttpStatus.OK);
    }


    //-----------------------Retrieve advertisement from category-----------------------------------------------------//
    @RequestMapping(value = "/category/{id}/advertisement", method = RequestMethod.GET)
    public ResponseEntity<JSONArray> getAdsFromCategory(@PathVariable("id") int id, @RequestParam(value = "states") List<String> states)
    {

        CategoryDao dao = new CategoryDaoImpl();

        Category category = dao.getCategoryById(id);

        if (category == null)
        {
            return new ResponseEntity<JSONArray>(HttpStatus.NOT_FOUND);
        }

        AdvertisementDao adao = new AdvertisementDaoImpl();


        List<Advertisement> ads = adao.getListOfAdds(category, states);

        JSONArray adsFromCategory = new JSONArray();

        for (Advertisement advertisement:
             ads) {
            JSONObject aux = new JSONObject();

            aux.put("id", advertisement.getId());
            aux.put("name", advertisement.getName());
            aux.put("picture", advertisement.getPictureUrl());
            aux.put("url", advertisement.getLinkUrl());
            aux.put("state", advertisement.getState());

            adsFromCategory.add(aux);
        }

        return new ResponseEntity<JSONArray>(adsFromCategory, HttpStatus.OK);
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
    @RequestMapping(value = "/category/{id}/statistic", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> generalStatisticAboutCategory(@PathVariable("id") int id, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to)
    {
        CategoryDao dao = new CategoryDaoImpl();
        Category category = dao.getCategoryById(id);

        if (category == null)
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

        JSONObject statistic = sdao.generalStatisticInPeriodFromCategory(category, dateFrom, dateTo);

        return new ResponseEntity<JSONObject>(statistic, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/frequently", method = RequestMethod.GET)
    public ResponseEntity<JSONArray> theMostFrequentlyClicked(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to)
    {
        Date dateFrom;
        Date dateTo;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            dateFrom = sdf.parse(from);
        } catch (ParseException e) {
            return new ResponseEntity<JSONArray>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            dateTo = sdf.parse(to);

        } catch (ParseException e) {
            return new ResponseEntity<JSONArray>(HttpStatus.NOT_ACCEPTABLE);
        }

        StatisticDao dao = new StatisticDaoImpl();

        JSONArray jArray = dao.complicateQuery(dateFrom, dateTo);


        return new ResponseEntity<JSONArray>(jArray, HttpStatus.OK);
    }


}
