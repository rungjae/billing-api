package com.study.billing_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController   // 이 클래스의 메서드 반환값을 JSON으로 응답
public class BillingController {

    private static final int BASE_FEE = 15_000;   // 기본료
    private static final int FREE_USAGE = 100;    // 기본 제공량
    private static final int UNIT_PRICE = 50;     // 초과 단위당 요금

    // 예: GET /bill?usage=150
    // GET /bill?usage=150&discountRate=10  (할인율 생략 시 0%)
    @GetMapping("/bill")
    public Map<String, Object> calc(@RequestParam int usage,
                                    @RequestParam(defaultValue = "0") int discountRate) {
        int extra = Math.max(0, usage - FREE_USAGE) * UNIT_PRICE;
        int amount = BASE_FEE + extra;                 // 할인 전 금액
        int discount = amount * discountRate / 100;    // 할인액
        int supply = amount - discount;                // 공급가액 (할인 후)
        int vat = supply / 10;                         // 부가세는 할인 후 금액 기준
        return Map.of(
                "usage", usage,
                "amount", amount,
                "discount", discount,
                "vat", vat,
                "total", supply + vat
        );
    }

    // 배포 후 "살아있는지" 확인하는 용도 (나중에 K8s에서 씀)
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}