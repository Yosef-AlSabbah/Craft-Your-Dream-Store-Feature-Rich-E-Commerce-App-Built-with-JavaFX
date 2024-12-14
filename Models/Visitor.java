package Models;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(
        name = "Visitor",
        uniqueConstraints = @UniqueConstraint(columnNames={"Visit_ip", "Visit_date"}))
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Visitor.getVisitsStats",
                query = "SELECT Visit_date, COUNT(Visit_ip) FROM visitor WHERE Visit_date BETWEEN DATE_SUB(DATE(NOW()), INTERVAL 1 WEEK) AND DATE_SUB(DATE(NOW()), INTERVAL 1 DAY) GROUP BY Visit_date;"
        )
})
public class Visitor implements Serializable {

    @Column(name = "Visit_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Visit_ip")
    private String visit_ip;

    @Column(
            name = "Visit_date"
    )
    @ColumnDefault("CURRENT_DATE")
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public String getVisit_ip() {
        return visit_ip;
    }

    public void setVisit_ip(String visit_ip) {
        this.visit_ip = visit_ip;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
