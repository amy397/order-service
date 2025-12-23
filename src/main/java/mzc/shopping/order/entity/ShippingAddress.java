package mzc.shopping.order.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddress {
    private String recipientName;   // 수령인 이름
    private String phone;           // 연락처
    private String zipCode;         // 우편번호
    private String address;         // 기본 주소
    private String addressDetail;   // 상세 주소
}