package Models;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "User")
@NamedQueries({
        @NamedQuery(
                name = "User.loginCheck",
                query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password"
        )
})
public class User implements Serializable {

    @Column(name = "User_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "User_name",
            nullable = false,
            length = 50
    )
    private String name;

    @Column(
            name = "Email",
            unique = true,
            nullable = false,
            length = 50
    )
    private String email;

    @Column(
            name = "Password",
            length = 65
    )
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }
}
