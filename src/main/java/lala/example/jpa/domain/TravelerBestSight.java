package lala.example.jpa.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class TravelerBestSight {

    @Id
    private String email;
    private String title;
    private String description;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Traveler traveler;
}
