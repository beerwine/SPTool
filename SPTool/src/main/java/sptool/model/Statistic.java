package sptool.model;



import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import sptool.model.Advertisement;
/**
 * Created by sergey on 6/5/16.
 * Maps class Statistic on the table statistic
 */
@Entity
@Table(name = "statistic")
@NamedQueries({@NamedQuery(name = "@THE_MOST_FREQ_CLICKED", query = "from Statistic s inner join ")})
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statistic", nullable = false)
    private int id;

    @Column(name = "date")
    private Date date;

    @Column(name = "clicks")
    private int clicks;

    @Column(name = "paid")
    private int paid;


    @ManyToOne(optional = false)
    @JoinColumn(name = "id_advertisement", nullable = false)
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
