package mzc.shopping.order.repository;

import mzc.shopping.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 주문별 아이템 조회
    List<OrderItem> findByOrderId(Long orderId);

    // 상품별 주문 아이템 조회 (어떤 주문들에서 이 상품을 샀는지)
    List<OrderItem> findByProductId(Long productId);
}