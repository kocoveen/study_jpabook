package hello.basic;

import jakarta.persistence.*;

public class MainClass {
	public static void main(String[] args) {

		// [엔티티 매니저 팩토리] 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("basic");
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
	}
}
