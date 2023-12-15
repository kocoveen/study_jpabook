package hello.example;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.example.domain.Member;
import hello.example.domain.Team;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.function.Consumer;

import static hello.example.domain.QMember.*;

@Slf4j
public class JpaExampleTest {

    // 모든 테스트에 Factory 공유
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("example");

    @BeforeEach
    void before() {
        defaultTestTemplate((em) -> {
            // team1 저장
            Team team1 = new Team("t1");
            em.persist(team1);

            // team2 저장
            Team team2 = new Team("t2");
            em.persist(team2);

            // member1 저장
            Member member1 = new Member("kim");
            member1.setTeam(team1);
            em.persist(member1);

            // member2 저장
            Member member2 = new Member("park");
            member2.setTeam(team1);
            em.persist(member2);

            // member3 저장
            Member member3 = new Member("bae");
            member3.setTeam(team2);
            em.persist(member3);

            // member4 저장
            Member member4 = new Member("jeong");
            member4.setTeam(team2);
            em.persist(member4);
        });
    }

    @Test
    void test1() {
        defaultTestTemplate((em) -> {
            //JPQL 조인 검색
            List<Member> resultList = em.createQuery("select m from Member m join m.team t", Member.class)
                    .getResultList();

            for (Member member : resultList) {
                log.info("member.username = {}", member.getUsername());
            }

            //연관관계 수정
            // team2 저장
            Team team2 = new Team("t2");
            em.persist(team2);
            // m2 -> t2
            Member m2 = em.find(Member.class, 2);
            m2.setTeam(team2);

            // 연관관계 제거
            Member m1 = em.find(Member.class, 1);
            m1.setTeam(null);

            // 연관 엔티티 삭제
            m2.setTeam(null);
            em.remove(team2); // 기존 연관관계를 먼저 전부 제거하고 엔티티 삭제하는 것이 나음
        });
    }

    @Test
    void test2() {
        defaultTestTemplate((em) -> {
            //일대다 조회
            Team team1 = em.find(Team.class, 1);
            List<Member> resultList = team1.getMembers(); // (팀 -> 회원) 객체그래프 탐색

            for (Member member : resultList) {
                log.info("member.username = {}", member.getUsername());
            }
        });
    }

    @Test
    void queryDslTest() throws Exception {
        defaultTestTemplate((em) -> {
            // query 작성할 JPAQuery 객체 생성
            JPAQueryFactory query = new JPAQueryFactory(em);
            // QType 객체 생성 (별칭 필수)
//            QMember m = new QMember("m");
//            QMember m = QMember.member;
            // query 작성
            List<Member> members = query
                    .select(member)
                    .from(member)
                    .where(member.username.contains("pa"))
                    .fetch();

            for (Member member : members) {
                log.info("name = {}", member.getUsername());
            }
        });
    }

    @Test
    void nativeQueryTest() throws Exception {
        defaultTestTemplate(em -> {
            String sql = "SELECT * FROM MEMBER";
            Query query = em.createNativeQuery(sql, Member.class);
            List<Member> members = query.getResultList();

            for (Member member : members) {
                log.info("name = {}", member.getUsername());
            }
        });
    }

    private void defaultTestTemplate(Consumer<EntityManager> testCallback) {
        final EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try (em) {
            tx.begin();

            // 테스트 소스 작성
            testCallback.accept(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }

    @AfterAll
    static void AfterAll() {
        emf.close();
    }
}