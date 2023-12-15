package hello.basic;

import hello.basic.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class JpaBasicTest {

    // 모든 테스트에 Factory 공유
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("basic");

    @Test
    void testEntityCRUD() {
        defaultTestTemplate((em) -> {
            //핵심 로직
            Long id = 1L;
            Member member = new Member();
            member.setId(id);
            member.setUsername("오지환");
            member.setAge(19);

            // Create
            em.persist(member);

            // Update
            member.setAge(33);

            // Read - One
            Member findMember = em.find(Member.class, id);
            log.info("findMember = " + findMember.getUsername() + ", age=" + findMember.getAge());

            // Read - Many (JPQL)
            List<Member> members =
                    em.createQuery("select m from Member m", Member.class).getResultList();
            log.info("members size = {}", members.size());

            // Delete
            em.remove(member);
        });
    }

    @Test
    void testDetached() {
        defaultTestTemplate((em) -> {
            // 회원 엔티티 생성, 비영속 상태
            Member member = new Member();
            member.setId(1L);
            member.setUsername("memberA");

            // 회원 엔티티 영속 상태
            em.persist(member);

            // 회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
            em.detach(member);

            // detach()에 필요하지 않지만, 해주지 않으면 오류
            em.clear();
        });
    }

    // 모든 @Test 에서 아래와 같은 흐름으로 코드 진행.
    // 반복되는 코드를 없애기 위해서 콜백(testCallback)을 사용
    // 각 @Test 메소드에서는 핵심 로직만 작성하도록 함.
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