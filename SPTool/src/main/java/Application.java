import org.hibernate.Session;
import org.hibernate.Transaction;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * Created by sergey on 6/6/16.
 */
public class Application {
    public static void main(String[] args) throws ParseException {


        Session session = Util.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();




        tx.commit();
        session.close();

        Util.getSessionFactory().close();


    }
}
