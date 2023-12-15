package hello.practice;

import hello.practice.domain.Member;
import hello.practice.domain.Order;
import hello.practice.domain.OrderItem;
import hello.practice.domain.OrderStatus;
import hello.practice.domain.item.Album;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {

        // [엔티티 매니저 팩토리] 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("practice");
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
        //Album 추가
        Album album = new Album();
        album.setName("The Black Parade");
        album.setArtist("My Chemical Romance");
        em.persist(album);

        //Member 추가
        Member member = new Member();
        member.setName("kim");
        em.persist(member);

        //주문
        Order order = new Order();
        order.setStatus(OrderStatus.ORDER);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(album);
        em.persist(orderItem);

        order.addOrderItem(orderItem);
        order.orderedBy(member);
        em.persist(order);
    }
}
