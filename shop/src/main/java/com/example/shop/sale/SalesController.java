package com.example.shop.sale;

import com.example.shop.user.CustomUser;
import com.example.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    // 주문 인터페이스 DI
    private final SalesRepository salesRepository;

    /**
     * 주문 기능 생성
     * @param prodNm    상품명
     * @param price     상품가격
     * @param orderCnt  상품수량
     * @param auth      유저정보
     * @return
     */
    @PostMapping("/order")
    String postOrder(String prodNm, Integer price, Integer orderCnt, Authentication auth) {

        CustomUser customUser = (CustomUser) auth.getPrincipal();

        User user = new User();
        user.setId(customUser.userRealId);

        Sales sales = new Sales();
        sales.setProdNm(prodNm);
        sales.setPrice(price);
        sales.setOrderCnt(orderCnt);
        sales.setUser(user);
        salesRepository.save(sales);

        return "product/productList.html";
    }

    /**
     * 주문정보 전체 보기
     * @param auth  유저정보
     * @return
     */
    @GetMapping("/order/all")
    String getOrder(Authentication auth) {
        List<Sales> result = salesRepository.findAll();
        System.out.println(result.get(0));
        return "product/productList.html";
    }
}
