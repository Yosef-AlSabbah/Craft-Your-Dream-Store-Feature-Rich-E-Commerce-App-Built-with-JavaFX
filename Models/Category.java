package Models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Category")
@NamedQueries({
        @NamedQuery(
                name = "Category.fetchAllASC",
                query = "SELECT c FROM Category c ORDER BY c.id ASC"
        ),
        @NamedQuery(
                name = "Category.getNumberOfCategories",
                query = "SELECT COUNT(c) FROM  Category c"
        ),
        @NamedQuery(
                name = "Category.getCategoriesWithStoresCount",
                query = "SELECT c.name AS CategoryName, COUNT(s) AS NumberOfStores FROM Category c LEFT JOIN Store s ON s.category = c GROUP BY c"
        )
})
public class Category implements Serializable {

    @Column(name = "Category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "Category_name",
            nullable = false,
            unique = true,
            length = 50
    )
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Store> stores = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    public Category() {
    }

    public void addStores(Store... stores) {
        this.stores.addAll(Arrays.asList(stores));
    }

    public void removeStores(Store... stores) {
        Arrays.asList(stores).forEach(this.stores::remove);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Set<Store> getStores() {
        return stores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
