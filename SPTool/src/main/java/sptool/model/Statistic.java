package sptool.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by sergey on 6/5/16.
 * Maps class Statistic on the table statistic
 */
@Entity
@Table(name = "statistic")
public class Statistic {

    @Id
    @GeneratedValue(generator="gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "add")})
    @Column(name = "id_statistic", nullable = false)
    private int id;

    @Column(name = "date")
    private Date date;

    @Column(name = "clicks")
    private int clicks;

    @Column(name = "paid")
    private int paid;


    @OneToOne
    @PrimaryKeyJoinColumn
    private Advertisement add;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public Advertisement getAdd() {
        return add;
    }

    public void setAdd(Advertisement add) {
        this.add = add;
    }
}
