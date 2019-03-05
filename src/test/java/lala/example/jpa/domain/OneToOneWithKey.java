package lala.example.jpa.domain;

import lala.example.jpa.repository.TravelerBestSightRepository;
import lala.example.jpa.repository.TravelerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneToOneWithKey {

    @Autowired
    private TravelerBestSightRepository travelerBestSightRepository;

    @Autowired
    private TravelerRepository travelerRepository;

    @Test
    public void 주요키를공유하는일대일단방향테스트() {
        String email = "ryan@naver.com";

        // Traveler 생성
        Traveler traveler = Traveler.builder()
                .email(email)
                .name("Ryan")
                .build();
        travelerRepository.save(traveler);

        // TravelerBestSight 생성
        TravelerBestSight travelerBestSight = TravelerBestSight.builder()
                .email(traveler.getEmail())
                .title("lake luise")
                .description("very very good")
                .traveler(traveler)
                .build();
        travelerBestSightRepository.save(travelerBestSight);

        // 아우터 조인이 걸려 조회된다.
        Optional<TravelerBestSight> byId = travelerBestSightRepository.findById(email);
        assertThat(byId.get().getTraveler().getEmail()).isEqualTo(email);
    }
}
