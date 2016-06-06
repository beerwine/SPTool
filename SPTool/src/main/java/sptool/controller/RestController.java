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
    public ResponseEntity<Void> createCategory(@RequestBody Category category)
    {

        CategoryDao dao = new CategoryDaoImpl();

        Category category1 = dao.getCategoryById(category.getId());

        if (category1 == null)
        {
            dao.save(category);
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

}
