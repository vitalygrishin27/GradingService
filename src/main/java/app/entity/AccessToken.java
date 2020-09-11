package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    @ManyToOne (optional = false)
    @JoinColumn(name = "id_user")
    private User user;
    private LocalDateTime dateFrom;
    private LocalDateTime dateEnd;
}
