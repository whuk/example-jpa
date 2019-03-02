package lala.example.jpa.domain;

import lala.example.jpa.repository.MemberRepository;
import lala.example.jpa.repository.MembershipCardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneToOneTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipCardRepository membershipCardRepository;

    @Test
    public void 참조키를이용한일대일단방향연관() {

        // 1. 멤버 생성 및 가져오기
        Member user = Member.builder()
                .email("ryan@naver.com")
                .name("Ryan")
                .createdDate(new Date())
                .build();
        memberRepository.save(user);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(2020,12,31, 23,59,59,0, ZoneId.systemDefault());
        Date expiryDate = Date.from(zonedDateTime.toInstant());
        Optional<Member> owner = memberRepository.findById("ryan@naver.com");

        // 2-1. 멤버쉽 카드 생성
        MembershipCard membershipCard = MembershipCard.builder()
                .number("1234")
                .owner(owner.get())
                .expiryDate(expiryDate)
                .build();
        membershipCardRepository.save(membershipCard);
        // member_email 필드에 앞서 만든 member 의 email 이 저장됨

        // 2-2. 멤버쉽 카드를 생성하되, 연관 관계를 설정하지 않음
//        MembershipCard membershipCard = MembershipCard.builder()
//                .number("1234")
//                .expiryDate(expiryDate)
//                .build();
        membershipCardRepository.save(membershipCard);
        // member_email 필드에 null 이 저장됨
        
        // 3. 멤버쉽 카드를 조회하면, Member 와 아우터 조인이 걸려 함께 조회됨
        Optional<MembershipCard> find = membershipCardRepository.findById("1234");
        // 2-1, 2-2 모두 기본적으로 아우터 조인이 걸림
        
        // 4-1. 멤버쉽 카드에 할당된 멤버를 지우려고 하면?
//        memberRepository.deleteAll();
        // 익셉션이 발생한다. 멤버의 email 이 참조 관계가 생겼기 때문...

        // 4-2. 멤버쉽 카드를 지우려고 하면?
        membershipCardRepository.deleteAll();
        // 정상적으로 삭제가 됨

        // 5. 관계가 없어진 후 멤버를 삭제
        memberRepository.deleteAll();
        // 참조 관계의 레코드가 삭제 되었기 때문에 정상 삭제

        assertThat(membershipCardRepository.findById("1234")).isEmpty();
        assertThat(memberRepository.findById("ryan@naver.com")).isEmpty();
    }
}
