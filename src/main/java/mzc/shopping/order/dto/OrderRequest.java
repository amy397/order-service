package mzc.shopping.order.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "회원 ID는 필수입니다")
    private Long userId;

    @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다")
    private List<OrderItemRequest> items;

    private String email;  // 추가
}