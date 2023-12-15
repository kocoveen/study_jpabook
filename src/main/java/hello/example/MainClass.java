package hello.example;

import hello.example.domain.Member;
import hello.example.domain.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class MainClass {
	public static void main(String[] args) {

		// [엔티티 매니저 팩토리] 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("example");
		// [엔티티 매니저] 생성
		EntityManager em = emf.createEntityManager();
		// [트랜잭션] 획득
		EntityTransaction tx = em.getTransaction();

		try (em) { // try(em) <- 엔티티 매니저는 DB 커넥션을 갖고 있기 때문에 엔티티 매니저를 닫아야 함.
            tx.begin();
			logic(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        }
		emf.close();
	}

	private static void logic(EntityManager em) {
		// team1 저장
		Team team1 = new Team("team1");
		em.persist(team1);

		// member1 저장
		Member member1 = new Member("member1");
		em.persist(member1);

		// member2 저장
		Member member2 = new Member("member2");
		em.persist(member2);
		team1.getMembers().add(member2); // 연관관계 설정 team1 -> member2
	}
}
