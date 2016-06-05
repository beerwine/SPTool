package sptool.model;

import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by sergey on 6/5/16.
 * Maps class Category on the table category
 */
@Entity
@Table(name = "category")
@NamedQueries({@NamedQuery(name = "@ALL_CATEGORIES", query = "from Category")
})
public class Category {

    @Id
    @Column(name = "id_category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private String state;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Advertisement> adds;


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


    public List<Advertisement> getAdds() {
        return adds;
    }

    public void setAdds(List<Advertisement> adds) {
        this.adds = adds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
