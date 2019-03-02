package lala.example.jpa.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Member {
    @Id
    private String email;
    private String name;

    @Temporal(TemporalType.DATE)
    private Date createdDate;
}
