package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String EncryptedPassword;
    @Column(insertable = false, updatable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private Role role;
    @Column(nullable = false)
    private String firstName;
    private String secondName;
    private String lastName;
    private String position;
    @Lob
    private String photo;
}
