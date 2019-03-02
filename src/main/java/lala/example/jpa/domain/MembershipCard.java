package lala.example.jpa.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "membership_card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MembershipCard {

    @Id
    @Column(name = "card_number")
    private String number;

    @OneToOne
    @JoinColumn(name = "member_email")
    private Member owner;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date")
    private Date expiryDate;

    protected boolean enabled;
}
