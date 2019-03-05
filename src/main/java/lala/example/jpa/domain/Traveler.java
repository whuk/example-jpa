package lala.example.jpa.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Traveler {

    @Id
    private String email;
    private String name;
}
