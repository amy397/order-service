package mzc.shopping.order.repository;

import mzc.shopping.order.entity.Order;
import mzc.shopping.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 회원별 주문 조회
    List<Order> findByUserId(Long userId);

    // 회원별 + 상태별 주문 조회
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    // 상태별 주문 조회
    List<Order> findByStatus(OrderStatus status);

    // 회원별 주문 최신순 조회
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}