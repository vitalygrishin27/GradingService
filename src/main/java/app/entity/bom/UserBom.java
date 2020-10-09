package app.entity.bom;

import app.entity.AccessToken;
import app.entity.Contest;
import app.entity.Performance;
import app.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
@Data
public class UserBom {
    private Long id;
    private String login;
    private String password;
    private Role role;
    private String firstName;
    private String secondName;
    private String lastName;
    private String position;
    private List<ContestBom> contests;
    private List<PerformanceBom> performances;
}
