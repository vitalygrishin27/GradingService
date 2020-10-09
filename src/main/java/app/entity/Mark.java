package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_jury")
    private User jury;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_performance")
    private Performance performance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_criterion")
    private Criterion criterion;
}
