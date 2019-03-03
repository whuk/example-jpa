package lala.example.jpa.domain;

import lala.example.jpa.repository.MemberRepository;
import lala.example.jpa.repository.MembershipCardRepository;
import org.junit.Before;
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

    private final String number = "1234";
    private final String email = "ryan@naver.com";

    @Before
    public void init() {
        // 1. 멤버 생성
        Member user = Member.builder()
                .email(email)
                .name("Ryan")
                .createdDate(new Date())
                .build();
        memberRepository.save(user);
    }

    @Test
    public void 참조키를이용한일대일양방향연관() {
        // 1. 생성된 멤버는 아직 MembershipCard 가 존재하지 않음
        Optional<Member> member = memberRepository.findById(email);
        assertThat(member.get().getMembershipCard()).isNull();

        // 2. 멤버쉽 카드에 오너를 할당하여 생성 - 레이지 로딩
        MembershipCard membershipCard = createMembershipCardWithOwner();
        // 레이지 로딩이기 때문에 쿼리에 조인은 걸리지 않음
        Optional<MembershipCard> getCard = membershipCardRepository.findById(number);

        // 3. 멤버에 멤버십 카드 연결
        member.get().setMembershipCard(membershipCard);

        // 4. 멤버 재조회 - 즉시로딩, 멤버십 카드와 아우터 조인이 걸림
        Optional<Member> getMember = memberRepository.findById(email);
        assertThat(getMember.get().getMembershipCard().getNumber()).isEqualTo(number);
    }

//    @Test
    public void 참조키를이용한일대일단방향연관의레이지로딩() {
        // 1. 멤버쉽카드에 오너를 할당하여 생성
        MembershipCard membershipCard = createMembershipCardWithOwner();

        // 2. 멤버쉽 카드 조회
        Optional<MembershipCard> getCard = membershipCardRepository.findById(number);
        // 이번에는 아우터 조인이 걸리지 않는다.

        // 3. 멤버십 카드의 오너를 엑세스하면 이 시점에 select 쿼리가 실행되거나 프록시 객체를 활용. -> Lazy Loading
        Member owner = getCard.get().getOwner();

        assertThat(owner.getEmail()).isEqualTo(email);
    }

//    @Test
    public void 참조키를이용한일대일단방향연관() {
        // 2-2. 멤버쉽 카드를 생성하되, 연관 관계를 설정하지 않음
        MembershipCard membershipCard = createMembershipCardWithOwner();
        // 3. 멤버쉽 카드를 조회하면, Member 와 아우터 조인이 걸려 함께 조회됨
        Optional<MembershipCard> find = membershipCardRepository.findById(number);
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

        assertThat(membershipCardRepository.findById(number)).isEmpty();
        assertThat(memberRepository.findById(email)).isEmpty();
    }

    private MembershipCard ceateMembershipCardWithoutOwner() {

        MembershipCard membershipCard = MembershipCard.builder()
                .number(number)
                .expiryDate(getZoneTimeDate())
                .build();
        membershipCardRepository.save(membershipCard);
        // member_email 필드에 null 이 저장됨
        return membershipCard;
    }

    private MembershipCard createMembershipCardWithOwner() {
        Optional<Member> owner = memberRepository.findById(email);

        // 2-1. 멤버쉽 카드 생성
        MembershipCard membershipCard = MembershipCard.builder()
                .number(number)
                .owner(owner.get())
                .expiryDate(getZoneTimeDate())
                .build();
        membershipCardRepository.save(membershipCard);
        // member_email 필드에 앞서 만든 member 의 email 이 저장됨
        return membershipCard;
    }

    private Date getZoneTimeDate() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2020, 12, 31, 23, 59, 59, 0, ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}
