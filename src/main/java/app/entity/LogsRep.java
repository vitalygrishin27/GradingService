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
public class LogsRep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;
    private String token;
    private String caller;
    private String operation;
    private LocalDateTime requestTime;
    private String requestBody;
    private String responseCode;
    private LocalDateTime responseTime;
    private String responseBody;

    @Override
    public String toString() {
        return "SlaTimeDto{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", token='" + token + '\'' +
                ", caller='" + caller + '\'' +
                ", operation='" + operation + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseTime='" + responseTime + '\'' +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
