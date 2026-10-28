package com.study.billing_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController   // 이 클래스의 메서드 반환값을 JSON으로 응답
public class BillingController {

    private static final int BASE_FEE = 10_000;   // 기본료
    private static final int FREE_USAGE = 100;    // 기본 제공량
    private static final int UNIT_PRICE = 500000000;     // 초과 단위당 요금

    // 예: GET /bill?usage=150
    @GetMapping("/bill")
    public Map<String, Object> calc(@RequestParam int usage) {
        int extra = Math.max(0, usage - FREE_USAGE) * UNIT_PRICE;
        int amount = BASE_FEE + extra;   // 공급가액
        int vat = amount / 10;           // 부가세 10%
        return Map.of(
                "usage", usage,
                "amount", amount,
                "vat", vat,
                "total", amount + vat     // 최종 청구액
        );
    }

    // 배포 후 "살아있는지" 확인하는 용도 (나중에 K8s에서 씀)
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}