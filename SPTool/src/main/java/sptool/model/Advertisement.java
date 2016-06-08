package sptool.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by sergey on 6/5/16.
 * Maps class Advertisement on the table advertisement
 */
@Entity
@Table(name = "advertisement", schema = "public")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_advertisement")
    private int id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "picture_url")
    @NotNull
    @URL
    private String pictureUrl;

    @Column(name = "link_url")
    @NotNull
    @URL
    private String linkUrl;

    @Column(name = "state")
    @Pattern(regexp = "(Active|Stopped|Pending)")
    private String state = "Stopped";

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "add", cascade = CascadeType.ALL)
    @Valid
    private List<Statistic> statistics;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }
}
