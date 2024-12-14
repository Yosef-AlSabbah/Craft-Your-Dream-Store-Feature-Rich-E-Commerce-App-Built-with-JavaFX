package Models;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(
        name = "Rating",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"User_ip", "Store_id"})})
@NamedNativeQueries({
        @NamedNativeQuery(
        name = "Rating.getTopRatingsASC",
        query = "SELECT s.Store_name, COUNT(*) AS 'Rates' FROM rating r INNER JOIN store s ON (r.Store_id = s.Store_id) WHERE r.Rating_date >= DATE_SUB(now(), INTERVAL 1 MONTH) GROUP BY s.Store_id ORDER BY Rates DESC LIMIT 3;"
)})
@NamedQueries({
        @NamedQuery(
                name = "Rating.getRatingAssociatedWithUserIP",
                query = "SELECT r FROM Rating r WHERE r.user_ip = ?1 AND r.store = ?2"
        )
})
public class Rating implements Serializable {

    @Column(name = "Rating_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "User_ip",
            nullable = false
    )
    private String user_ip;

    @ManyToOne
    @JoinColumn(
            name = "Store_id",
            nullable = false
    )
    private Store store;

    @Column(
            name = "Rating",
            columnDefinition = "TINYINT(1) DEFAULT 0",
            nullable = false
    )
    private byte rating;

    @Column(name = "Rating_date")
    @ColumnDefault("CURRENT_DATE")
    private LocalDate date;

    public Rating() {
    }

    public Rating(String user_ip, Store store, byte rating, LocalDate date) {
        this.user_ip = user_ip;
        this.store = store;
        this.rating = rating;
        this.date = date;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }
}
