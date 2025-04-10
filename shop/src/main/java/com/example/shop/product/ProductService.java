package com.example.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 비즈니스 로직 처리를 위한 Service 클래스 생성
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository prdRepo;                            // Product 엔티티 사용을 위한 인터페이스
    private final ProductCommentRepository productCommentRepository;    // 상품 댓글 작성용 인터페이스 DI 주입

    /**
     * 상품 추가 기능
     * @param prodNm 상품명
     * @param price  가격
     * @return  prodcutList page
     */
    public void saveProduct(String prodNm, String price){
        Product prd = new Product();
        prd.setProdNm(prodNm);
        // price 가 숫자만 있다고 체크 했으니 String to Integer 로 변환
        prd.setPrice(Integer.parseInt(price));
        prdRepo.save(prd);
        // 자원 할당 후 해제 처리
        prd = null;
    }

    /**
     * 상품 수정 기능
     * @param prodCd 상품코드
     * @param prodNm 상품명
     * @param price  가격
     * @return  prodcutList page
     */
    public void editProduct(Long prodCd, String prodNm, String price){
        Product prd = new Product();
        prd.setProdCd(prodCd);
        prd.setProdNm(prodNm);
        // price 가 숫자만 있다고 체크 했으니 String to Integer 로 변환
        prd.setPrice(Integer.parseInt(price));
        prdRepo.save(prd);
        // 자원 할당 후 해제 처리
        prd = null;
    }

    /**
     * 상품 댓글 작성
     * @param content   댓글내용
     * @param parentId  상픔코드
     * @param userId    로그인 사용자 아이디
     * @return  prodcutList page
     */
    public void saveComment(String content, @RequestParam Long parentId, String userId){
        ProductComment prdCmmt = new ProductComment();
        prdCmmt.setContent(content);
        prdCmmt.setParentId(parentId);
        prdCmmt.setUserId(userId);
        productCommentRepository.save(prdCmmt);
        // 자원 할당 후 해제 처리
        prdCmmt = null;
    }

}
