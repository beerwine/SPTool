package sptool.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sptool.dao.CategoryDao;
import sptool.dao.CategoryDaoImpl;
import sptool.model.Category;

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
    @RequestMapping(value = "/category/", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> listAllCategories()
    {
        CategoryDao dao = new CategoryDaoImpl();

        List<Category> categories = dao.getAllCategories();

        if (categories.size() == 0)
            return new ResponseEntity<List<Category>>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    //-----------------------Create new category----------------------------------------------------------------------//
    @RequestMapping(value = "/category/", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<Void> createCategory(Category category)
    {
        //TODO
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~HERE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println(category.getName());

        CategoryDao dao = new CategoryDaoImpl();

        System.out.println(category);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
