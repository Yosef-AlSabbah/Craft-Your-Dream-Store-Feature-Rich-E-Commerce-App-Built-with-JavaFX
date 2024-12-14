package Models;

import javafx.scene.image.ImageView;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Store")
@NamedQueries({
        @NamedQuery(
                name = "Store.fetchAllStoresASC",
                query = "SELECT s FROM Store s ORDER BY s.id ASC"
        ),
        @NamedQuery(
                name = "Store.getNumberOfStores",
                query = "SELECT COUNT(s) FROM  Store s"
        ),
        @NamedQuery(
                name = "Store.getStoreInfo",
                query = "SELECT s, AVG(r.rating), COUNT(r), SUM(r.rating) FROM Store s LEFT OUTER JOIN Rating r ON r.store = s GROUP BY s"
        )
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Store.getStoresRatingTrend",
                query = "SELECT s.Store_name, FORMAT(IFNULL(AVG(outer_query.Rating), 0) - IFNULL((SELECT AVG(inner_query.Rating) FROM rating inner_query WHERE inner_query.Rating_date > DATE_SUB(now(), INTERVAL 1 MONTH ) AND inner_query.Store_id = outer_query.Store_id), 0), 2) FROM rating outer_query INNER JOIN store s USING(Store_id) WHERE outer_query.Rating_date <= DATE_SUB(now(), INTERVAL 1 MONTH ) GROUP BY outer_query.Rating_date, s.Store_name LIMIT 6;"
        )
})
public class Store implements Serializable {

    @Column(name = "Store_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "Store_name",
            nullable = false,
            unique = true
    )
    private String name;

    @Column(
            name = "Image",
            nullable = false
    )
    private String image;

    @Column(
            name = "Phone",
            unique = true,
            length = 15
    )
    private String phone;

    @Column(
            name = "Address",
            length = 50
    )
    private String address;

    @ManyToOne
    @JoinColumn(
            name = "Category_id", referencedColumnName = "Category_id",
            foreignKey = @ForeignKey(
                    name = "fk_store_category",
                    foreignKeyDefinition = "FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE NO ACTION"))
    private Category category;

    @Transient
    private ImageView imageView;

    public Store(String name, String image, String phone, String address, Category category) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.address = address;
        this.category = category;
    }

    public Store() {
    }

    public ImageView getIcon() {
        if (imageView == null) {
            this.imageView = new ImageView("file:C:/xampp/htdocs/HYPEX/Dashboard/" + image.toLowerCase());
            this.imageView.setFitWidth(40);
            this.imageView.setFitHeight(40);
            this.imageView.getStyleClass().add("icons");
        }
        return this.imageView;
    }

    public void updateIcon() {
        this.imageView = null;
        getIcon();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
