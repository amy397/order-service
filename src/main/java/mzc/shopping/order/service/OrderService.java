package mzc.shopping.order.service;

import lombok.RequiredArgsConstructor;
import mzc.shopping.order.client.*;
import mzc.shopping.order.dto.*;
import mzc.shopping.order.entity.*;
import mzc.shopping.order.exception.*;
import mzc.shopping.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // 1. 회원 확인 (User Service 호출)
        try {
            userServiceClient.getUser(request.getUserId());
        } catch (Exception e) {
            throw new UserNotFoundException(request.getUserId());
        }

        // 2. 주문 생성
        Order order = Order.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .status(OrderStatus.PENDING)
                .build();

        // 3. 주문 아이템 추가 + 재고 감소 (Product Service 호출)
        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductResponse product;
            try {
                product = productServiceClient.getProduct(itemRequest.getProductId());
            } catch (Exception e) {
                throw new ProductNotFoundException(itemRequest.getProductId());
            }

            // 재고 감소
            productServiceClient.decreaseStock(
                    itemRequest.getProductId(),
                    new StockRequest(itemRequest.getQuantity())
            );

            // 주문 아이템 생성
            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();

            order.addOrderItem(orderItem);
        }

        // 4. 총 가격 계산
        order.calculateTotalPrice();

        // 5. 저장
        Order saved = orderRepository.save(order);
        return OrderResponse.from(saved);
    }

    // 주문 조회
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderResponse.from(order);
    }

    // 회원별 주문 조회
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    // 전체 주문 조회
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    // 주문 취소
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // 주문 취소
        order.cancel();

        // 재고 복구 (Product Service 호출)
        for (OrderItem item : order.getOrderItems()) {
            productServiceClient.increaseStock(
                    item.getProductId(),
                    new StockRequest(item.getQuantity())
            );
        }

        return OrderResponse.from(order);
    }

    // 주문 상태 변경
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(status);
        return OrderResponse.from(order);
    }
}