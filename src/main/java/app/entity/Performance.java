package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_contest")
    private Contest contest;

    @Override
    public String toString() {
        return "Performance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
