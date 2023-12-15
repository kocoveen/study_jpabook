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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.StringTokenizer;
import java.util.function.Consumer;

@Slf4j
public class JpaPracticeTest {

    // 모든 테스트에 Factory 공유
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("practice");

    @BeforeEach
    void before() {
        defaultTestTemplate((em) -> {

        });
    }

    @Test
    void test() {
        defaultTestTemplate((em) -> {
            //핵심 로직
            //Album 추가
            Album album1 = new Album();
            album1.setName("The Black Parade");
            album1.setArtist("My Chemical Romance");
            em.persist(album1);

            Album album2 = new Album();
            album2.setName("The Reason");
            album2.setArtist("Hoobastank");
            em.persist(album2);

            //Member 추가
            Member member = new Member();
            member.setName("kim");
            em.persist(member);

            //주문
            Order order = new Order();
            order.setStatus(OrderStatus.ORDER);

            OrderItem orderItem1 = new OrderItem();
            orderItem1.setItem(album1);
            orderItem1.setCount(1);

            OrderItem orderItem2 = new OrderItem();
            orderItem2.setItem(album2);
            orderItem2.setCount(1);

            order.addOrderItem(orderItem1);
            order.addOrderItem(orderItem2);

            order.orderedBy(member);

            em.persist(order);
            em.flush();

            Order order1 = em.find(Order.class, 1);
            log.info("주문자명 = {}", order1.getMember().getName());
            for (OrderItem findOrderItem : order1.getOrderItems()) {
                log.info("주문 = {}, 개수 = {}", findOrderItem.getItem().getName(), findOrderItem.getCount());
            }


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